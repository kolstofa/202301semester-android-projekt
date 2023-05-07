package org.projekt.logintester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //Firebase Auth
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button mBtnSignin = findViewById(R.id.btn_actSignin);
        Button mBtnToSignup = findViewById(R.id.btn_signup);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("LoginTester");

        mEtEmail = findViewById(R.id.et_sumail);
        mEtPwd = findViewById(R.id.et_supwd);


        mBtnSignin.setOnClickListener(v -> {
            String strEmail = mEtEmail.getText().toString();
            String strPwd = mEtPwd.getText().toString();

            mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Signin Succeed
                        Intent intent = new Intent(SigninActivity.this, UsermodeActivity.class); //To
                        startActivity(intent);
                    } else {
                        Toast.makeText(SigninActivity.this, "Signin Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        mBtnToSignup.setOnClickListener(v -> {
            //Move to SignupActivity
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}