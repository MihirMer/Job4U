package com.almightymm.job4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    private TextView create_account;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        create_account=findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(nextActivity);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//                finish();
            }
        });
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(SignInActivity.this,HomeActivity.class);
                startActivity(nextActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                finish();
            }
        });
    }
}
