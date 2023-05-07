package org.projekt.logintester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button mBtnToSignin; //Normal signin button
    private SignInButton mBtnGglSignin; //Google signin button
    private FirebaseAuth mFirebaseAuth; //Firebase authentication
    private GoogleApiClient googleApiClient; //Google API Client Object
    private static final int REQ_SIGN_GOOGLE = 100; //Login Output

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        //Normal signin
        mBtnToSignin = findViewById(R.id.btn_normalSignin);
        mBtnToSignin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
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