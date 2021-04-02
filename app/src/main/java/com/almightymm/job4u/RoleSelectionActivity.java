package com.almightymm.job4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private DatabaseReference mDatabase;
    private static final String TAG = "RoleSelectionActivity";

    private Button jobseekerButton, hrButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        jobseekerButton = findViewById(R.id.btn_jobseeker);
        hrButton = findViewById(R.id.btn_hr);
        
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){
            userId = firebaseUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Users").child(userId).child("roleAssigned").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        boolean roleAssigned= (boolean) task.getResult().getValue();
                        if(roleAssigned){
                            Intent intent = new Intent(RoleSelectionActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            hrButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(RoleSelectionActivity.this, "Hr Clicked", Toast.LENGTH_SHORT).show();
                                    mDatabase.child("Users").child(userId).child("roleAssigned").setValue(true);
                                    mDatabase.child("Users").child(userId).child("role").setValue("HR");
                                    Intent intent = new Intent(RoleSelectionActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            jobseekerButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(RoleSelectionActivity.this, "job seeker clicked", Toast.LENGTH_SHORT).show();
                                    mDatabase.child("Users").child(userId).child("roleAssigned").setValue(true);
                                    mDatabase.child("Users").child(userId).child("role").setValue("Job Seeker");
                                    Intent intent = new Intent(RoleSelectionActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                }
            });
        } else {
            Intent intent = new Intent(RoleSelectionActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        
        
    }
}