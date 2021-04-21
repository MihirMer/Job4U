package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almightymm.job4u.Adapter.HRJobAdapter;
import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class JobListFragment extends Fragment {
    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    JobAdapter jobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    DatabaseReference firebaseDatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
String categoryName;
    String userId;
    public JobListFragment() {
        // Required empty public constructor
    }




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
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);

        initPreferences();

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
        jobArrayList = new ArrayList<>();

        categoryName =preferences.getString("categoryName", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    jobAdapter = new JobAdapter(getContext(), filter(),preferences,preferenceEditor,R.id.action_jobListFragment_to_jobPreviewFragment);
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
            if (job.getName().equals(categoryName)){

            filterList.add(job);
            }
        }
        return filterList;

    }


    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}