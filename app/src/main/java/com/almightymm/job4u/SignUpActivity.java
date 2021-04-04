package com.almightymm.job4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.almightymm.job4u.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private TextView signIn;
    private Button signUp;
    private TextInputEditText signUpFirstName, signUpLastName, signUpEmailId, signUpPassword, signUpConfirmPassword;
    private FirebaseAuth mAuth;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        addListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void initViews() {
        signIn = findViewById(R.id.sign);
        signUp = findViewById(R.id.sign_up);
        signUpFirstName = findViewById(R.id.sign_up_first_name);
        signUpLastName = findViewById(R.id.sign_up_last_name);
        signUpEmailId = findViewById(R.id.sing_up_email_id);
        signUpPassword = findViewById(R.id.sign_up_password);
        signUpConfirmPassword = findViewById(R.id.sign_up_confirm_password);
        mAuth = FirebaseAuth.getInstance();
    }

    private void addListeners() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(nextActivity);
//                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finishAfterTransition();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent nextActivity = new Intent(SignUpActivity.this, HomeActivity.class);
//                startActivity(nextActivity);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String firstName = signUpFirstName.getText().toString();
        final String lastName = signUpLastName.getText().toString();
        final String emailId = signUpEmailId.getText().toString();
        final String password = signUpPassword.getText().toString();
        final String confirmPassword = signUpConfirmPassword.getText().toString();

        if (firstName.isEmpty()) {
//            firstName.setError("First Name is Required");
//            firstName.requestFocus();
            Toast.makeText(this, "First name is required !", Toast.LENGTH_SHORT).show();
        } else if (lastName.isEmpty()) {
//            lastname.setError("Last Name is Required");
//            lastname.requestFocus();
            Toast.makeText(this, "Last name is required !", Toast.LENGTH_SHORT).show();
        } else if (emailId.isEmpty()) {
//            email.setError("Email is Required");
//            email.requestFocus();
            Toast.makeText(this, "Email id is required !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
//            email.setError("Please Provide Valid Email");
//            email.requestFocus();
            Toast.makeText(this, "Please provide valid Email id !", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
//            pass.setError("Password is Required");
//            pass.requestFocus();
            Toast.makeText(this, "Password is required !", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
//            pass.setError("Password must be atleast 6 characters");
//            pass.requestFocus();
            Toast.makeText(this, "Password must be of at least of 6 characters !", Toast.LENGTH_SHORT).show();
        } else if (confirmPassword.isEmpty()) {
//            confpass.setError("Please Re-enter Password ");
//            confpass.requestFocus();
            Toast.makeText(this, "Confirmation password is required !", Toast.LENGTH_SHORT).show();
        } else if (!password.matches(confirmPassword)) {
            Toast.makeText(this, "Password doesn't match !", Toast.LENGTH_SHORT).show();

        } else {
            Log.e(TAG, "onComplete: "+firstName + "  "+lastName + "  "+emailId + "  "+password + "  "+confirmPassword + "  "+firstName + "  " );

            mAuth.createUserWithEmailAndPassword(emailId, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(firstName, lastName, emailId, false, "");
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "User has been registered successfully !", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                                            startActivity(i);
                                            finishAfterTransition();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "User registration failed !", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }

                        }
                    });

        }
    }
}

