package com.almightymm.job4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextInputEditText forgotPasswordEmail;
    private Button resetPasswordButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();
        addListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
        resetPasswordButton = findViewById(R.id.forgot_password_button);
        forgotPasswordEmail = findViewById(R.id.forgot_password_email_id);
        firebaseAuth = firebaseAuth.getInstance();
    }

    private void addListeners() {
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = forgotPasswordEmail.getText().toString();

        if (email.isEmpty()) {
//            forgotpass.setError("Email is required");
//            forgotpass.requestFocus();
            Toast.makeText(this, "Email is required !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            forgotpass.setError("Please Provide Valid Email");
//            forgotpass.requestFocus();
            Toast.makeText(this, "Please enter valid Email id !", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Please check your email to reset your password.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Please recheck the email that you have entered.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}