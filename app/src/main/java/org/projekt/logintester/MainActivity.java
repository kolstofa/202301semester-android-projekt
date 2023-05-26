package org.projekt.logintester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //Parameters for google signin
    private SignInButton mBtnGglSignin; //Google signin button
    private FirebaseAuth mFirebaseAuth; //Firebase authentication
    private GoogleApiClient googleApiClient; //Google API client object
    private static final int REQ_SIGN_GOOGLE = 100; //Login output

    //Parameteres for kakao signin
    private View kakaoSigninBtn;
    private TextView nickName;
    private ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kakaoSigninBtn=findViewById(R.id.btn_kakaoSignin);
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken!=null){ //토큰이 넘어옴, 로그인 성공
                    Toast.makeText(MainActivity.this, "Signin Succeed.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, UsermodeActivity.class); //To
                    startActivity(intent);
                }
                if(throwable!=null){ //결과에 오류가 있을때
                    Toast.makeText(MainActivity.this, "Signin Failed.", Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        };

        kakaoSigninBtn.setOnClickListener(v->{
            if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){ //카카오톡 설치 되어있을 때
                UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
            }
            else{ //카카오톡 설치 안되어있을 때
                UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,callback);
            }
        });


        //Google Sign Options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();

        mBtnGglSignin = findViewById(R.id.btn_googleSignin);
        mBtnGglSignin.setOnClickListener(v -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, REQ_SIGN_GOOGLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // When request google auth, will get result.
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) { //Succeed
                GoogleSignInAccount account = result.getSignInAccount();
                resultLogin(account); //Output signin result
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Succeed.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), UsermodeActivity.class);
                            //Send user info to UsermodeActivity
                            intent.putExtra("nickName", account.getDisplayName());
                            intent.putExtra("photoUrl", String.valueOf(account.getPhotoUrl()));
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}