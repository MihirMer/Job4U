package com.almightymm.job4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.almightymm.job4u.model.PersonalDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class SignInActivity extends AppCompatActivity {
    //    views
    private TextView create_account, forgot_password;
    private Button signInButton;
    private TextInputEditText signInEmailId, signInPassword;

    //    shared prefs
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    //    firebase auth
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userId;

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
        initPreferences();
        addListeners();
    }


    @Override
    protected void onStart() {
        super.onStart();
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

    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
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

                Intent i = new Intent(SignInActivity.this, ForgotPassword.class);
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
                        firebaseUser = mAuth.getCurrentUser();
                        userId = firebaseUser.getUid();
                        if (firebaseUser.isEmailVerified()) {
                            initUserInDatabase("EmailPassword");
                        } else {
                            firebaseUser.sendEmailVerification();
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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = mAuth.getCurrentUser();
                            userId = firebaseUser.getUid();
                            initUserInDatabase("Google");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void initUserInDatabase(final String method) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.getResult().getValue(PersonalDetails.class) == null) {
                    if (method.equals("Google")) {
                        String fullName = firebaseUser.getDisplayName();
                        String firstName = fullName.substring(0, fullName.lastIndexOf(" "));
                        String lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
                        String email = firebaseUser.getEmail();

//                        final User user = new User(firstName, lastName, email, false, "");
                        final PersonalDetails user = new PersonalDetails(
                                firstName,
                                lastName,
                                email,
                                null,
                                null,
                                null,
                                null,
                                null,
                                false
                        );

                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                setPreferences(userId, user);
                                Intent intent;
                                intent = new Intent(SignInActivity.this, RoleSelectionActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    PersonalDetails user = task.getResult().getValue(PersonalDetails.class);
                    setPreferences(userId, user);
                    Intent intent;
                    if (!user.isRoleAssigned()) {
                        intent = new Intent(SignInActivity.this, RoleSelectionActivity.class);
                    } else {
                        intent = new Intent(SignInActivity.this, HomeActivity.class);
                    }
                    startActivity(intent);
                    finishAfterTransition();
                }
            }
        });
    }


    private void setPreferences(String userId, PersonalDetails user) {
        Log.d(TAG, "setPreferences: " + userId);
        preferenceEditor.putString("userId", userId);
        preferenceEditor.putString("firstName", user.getFirstName());
        preferenceEditor.putString("lastName", user.getLastName());
        preferenceEditor.putString("emailAddress", user.getEmailAddress());
        preferenceEditor.putString("userId", userId);
        preferenceEditor.putBoolean("roleAssigned", user.isRoleAssigned());
        preferenceEditor.putString("role", user.getRole());
        preferenceEditor.apply();
    }

}
