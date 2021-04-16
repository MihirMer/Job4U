package com.almightymm.job4u.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.CategoryAdapter;
import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.HomeActivity;
import com.almightymm.job4u.R;
import com.almightymm.job4u.SignInActivity;
import com.almightymm.job4u.model.Category;
import com.almightymm.job4u.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {


//  recycler

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    LinearLayoutManager linearLayoutManager;

    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    JobAdapter jobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    DatabaseReference firebaseDatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initPreferences();
        recyclerView = view.findViewById(R.id.category);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryArrayList = new ArrayList<>();
        userId = preferences.getString("userId", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("JOBCATEGORY");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Category category = dataSnapshot.getValue(Category.class);
                        categoryArrayList.add(category);
                    }
                    categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList);
                    recyclerView.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
        jobArrayList = new ArrayList<>();
//        jobArrayList.add(new Job("","Front-end Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Backend Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Full-stack Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Middle-Tier Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Web Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Desktop Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Mobile Developer","","","","","","","","",""));
//        jobArrayList.add(new Job("","Graphics Developer","","","","","","","","",""));

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    jobAdapter = new JobAdapter(getContext(), filter());
                    jobRecyclerView.setAdapter(jobAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    private ArrayList<Job> filter() {
        ArrayList<Job> filterList = new ArrayList<>();
        for (Job job : jobArrayList) {
            for (Category category : categoryArrayList) {
                //Log.e("errr", "filter: "+job.getJob_Name()+" "+category.getC_Job_name() );
                if (job.getName() != null && category.getC_Job_name() != null) {
                    if (job.getName().equals(category.getC_Job_name())) {
                        filterList.add(job);
                    }
                }
            }
        }
        return filterList;

    }


    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}