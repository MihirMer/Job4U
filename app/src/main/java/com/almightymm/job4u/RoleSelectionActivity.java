package com.almightymm.job4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoleSelectionActivity extends AppCompatActivity {

    //    firebase
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    String userId;

    private static final String TAG = "RoleSelectionActivity";

    private Button jobseekerButton, hrButton;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        initPreferences();
        jobseekerButton = findViewById(R.id.btn_jobseeker);
        hrButton = findViewById(R.id.btn_hr);
        boolean roleAssigned = preferences.getBoolean("roleAssigned", false);

        if (!roleAssigned) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userId = firebaseUser.getUid();
            hrButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PERSONAL_DETAILS").child("roleAssigned").setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PERSONAL_DETAILS").child("role").setValue("HR");
                    preferenceEditor = preferences.edit();
                    preferenceEditor.putBoolean("roleAssigned", true);
                    preferenceEditor.putString("role", "HR");
                    preferenceEditor.apply();
                    Intent intent = new Intent(RoleSelectionActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            jobseekerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PERSONAL_DETAILS").child("roleAssigned").setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PERSONAL_DETAILS").child("role").setValue("Job Seeker");
                    preferenceEditor = preferences.edit();
                    preferenceEditor.putBoolean("roleAssigned", true);
                    preferenceEditor.putString("role", "Job Seeker");
                    preferenceEditor.apply();
                    Intent intent = new Intent(RoleSelectionActivity.this, JobCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Intent intent = new Intent(RoleSelectionActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);
    }
}