package com.almightymm.job4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.JobCategoryAdapter;
import com.almightymm.job4u.fragment.HomeFragment;
import com.almightymm.job4u.model.Category;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JobCategoryActivity extends AppCompatActivity {

    StringBuffer sb = null;
    Button addJobCategoty;
    JobCategoryAdapter jobCategoryAdapter;
    RecyclerView recyclerView;
    ArrayList<Category> jobArrayList;
    DatabaseReference firebaseDatabase;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_category);

        initViews();
        initPreferences();
        userId = preferences.getString("userId", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("JOBCATEGORY");
        setListeners();
    }

    private void setListeners() {
        addJobCategoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Category> jb = jobCategoryAdapter.getList();
                firebaseDatabase.setValue(jb).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(JobCategoryActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                        preferenceEditor.putBoolean("areaOfInterestSelected", true);
                        FirebaseDatabase.getInstance().getReference("Users").child(userId).child("PERSONAL_DETAILS").child("areaOfInterestSelected").setValue(true);
                        preferenceEditor.apply();
                        preferenceEditor.commit();

                        Intent intent = new Intent(JobCategoryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finishAfterTransition();

                    }
                });
            }
        });
    }

    private void initViews() {
        addJobCategoty = findViewById(R.id.btn_add_categoryJob);
        recyclerView = findViewById(R.id.rview_jobcategory);
        jobCategoryAdapter = new JobCategoryAdapter(this, getJobCategory());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(jobCategoryAdapter);
    }

    private ArrayList<Category> getJobCategory() {

        ArrayList<Category> jobCategories = new ArrayList<>();
        Category jc = new Category(R.drawable.ic_frontend_developer, "Web Developer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_mobile_app_developer, "Android Developer");

        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_web_designer, "Web Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_ux_interface, "UI/UX Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_graphic_designer, "Graphics Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_backend, "Backend Developer");

        jobCategories.add(jc);
        return jobCategories;
    }

    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}
