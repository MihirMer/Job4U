package com.almightymm.job4u;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class SignInActivity extends AppCompatActivity {
    private TextView create_account, forgot_password;
    private Button signInButton;

    private TextInputEditText signInEmailId, signInPassword;


    //    firebase auth
    private FirebaseAuth mAuth;

    //    google sign in
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInButton googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initViews();
        addListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void initViews() {
        create_account = findViewById(R.id.create_account);
        signInButton = findViewById(R.id.sign_in_button);
        googleSignInButton = findViewById(R.id.google_sign_in);
        mAuth = FirebaseAuth.getInstance();
        signInEmailId = findViewById(R.id.sign_in_email_id);
        signInPassword = findViewById(R.id.sign_in_password);
        forgot_password = findViewById(R.id.forgot_pass);
    }

    private void addListeners() {

        // don't have account, go to sign up
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(nextActivity);
            }
        });

        // sign in wit email and password
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailIdAndPassword();
            }
        });

        // google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignInActivity.this,ForgotPassword.class);
                startActivity(i);

            }
        });
    }

    private void signInWithEmailIdAndPassword() {
        String emailId = signInEmailId.getText().toString();
        String password = signInPassword.getText().toString();
        if (emailId.isEmpty()) {
//            signInEmailId.setError("Email Id is required !");
            Toast.makeText(this, "Email Id is required !", Toast.LENGTH_SHORT).show();
            signInEmailId.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
//            signInEmailId.setError("Please enter a valid Email Id");
            Toast.makeText(this, "Please enter a valid Email Id !", Toast.LENGTH_SHORT).show();
            signInEmailId.requestFocus();
        } else if (password.isEmpty()) {
//            signInPassword.setError("Password is required !");
            Toast.makeText(this, "Password is required !", Toast.LENGTH_SHORT).show();
            signInPassword.requestFocus();
        } else if (password.length() < 6) {
//            signInEmailId.setError("Password must be of at least of 6 characters !");
            Toast.makeText(this, "Password must be of at least of 6 characters !", Toast.LENGTH_SHORT).show();
            signInEmailId.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {
                            Intent i = new Intent(SignInActivity.this, RoleSelectionActivity.class);
                            startActivity(i);
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(SignInActivity.this, "Please check your email & verify to login", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Failed to Login! Please Check Your Credentials", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithCredential:success");
                                    Intent intent = new Intent(SignInActivity.this, RoleSelectionActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                }
                            }
                        });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

}
