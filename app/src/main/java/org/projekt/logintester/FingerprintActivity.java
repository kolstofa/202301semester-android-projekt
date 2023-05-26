package org.projekt.logintester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricPrompt;

import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class FingerprintActivity extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        executor = ContextCompat.getMainExecutor(this);

        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "지문인식이 불가능한 장치입니다.", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "지문인식이 작동하지 않습니다.", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "등록된 지문이 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }

        executor=ContextCompat.getMainExecutor(this);


        biometricPrompt=new BiometricPrompt(FingerprintActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "지문인식이 성공했습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "지문인식이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문 인증")
                .setSubtitle("기기에 등록 된 지문을 이용하여 지문을 인증해주세요.")
                .setNegativeButtonText("취소")
                .setDeviceCredentialAllowed(false)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
}