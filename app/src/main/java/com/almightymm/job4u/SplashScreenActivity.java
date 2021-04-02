package com.almightymm.job4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView logo;
    int timeOut = 3000;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        logo = findViewById(R.id.logo);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                //  user logged in?
                initPreferences();
                String userId = preferences.getString("userId", "");
                if (!userId.isEmpty()) {
                    boolean roleAssigned = preferences.getBoolean("roleAssigned", false);
                    if (roleAssigned) {
                        intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    } else {
                        intent = new Intent(SplashScreenActivity.this, RoleSelectionActivity.class);
                    }
                } else {
                    intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
                }


                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finishAfterTransition();
            }
        }, timeOut);
        Animation animation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.push_down_in);
        logo.startAnimation(animation);
    }

    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);

    }
}
