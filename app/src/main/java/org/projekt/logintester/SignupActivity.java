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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //Firebase Auth
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd;

    private Button mBtnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("LoginTester");

        mEtEmail = findViewById(R.id.et_sumail);
        mEtPwd = findViewById(R.id.et_supwd);
        mBtnSignup = findViewById(R.id.btn_actSignup);

        mBtnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            String strEmail = mEtEmail.getText().toString();
            String strPwd = mEtPwd.getText().toString();

            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                        UserAccount account = new UserAccount();
                        account.setIdToken(firebaseUser.getUid());
                        account.setEmailId(firebaseUser.getEmail());
                        account.setPassword(strPwd);

                        //SetValue() -> Set Database
                        mDatabaseRef.child("UserAcoount").child(firebaseUser.getUid()).setValue(account);

                        Toast.makeText(SignupActivity.this, "Signup Completed.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "Signup Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }
}