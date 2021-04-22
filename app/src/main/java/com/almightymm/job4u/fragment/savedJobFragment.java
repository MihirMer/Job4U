package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.SavedJobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.almightymm.job4u.model.SavedJob;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class savedJobFragment extends Fragment {
    private static final String TAG = "savedJobFragment";
    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    ArrayList<SavedJob> savedJobArrayList;
    SavedJobAdapter savedJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;
    RelativeLayout noData;
    DatabaseReference saved_job_ref;
    DatabaseReference job_ref;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;

    public savedJobFragment() {
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
        View view = inflater.inflate(R.layout.fragment_saved_job, container, false);

        initPreferences();
        noData= view.findViewById(R.id.lay3);
        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.saved_job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
        jobArrayList = new ArrayList<>();
        savedJobArrayList = new ArrayList<>();
        userId = preferences.getString("userId", "");
        saved_job_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("SAVED_JOB");
        job_ref = FirebaseDatabase.getInstance().getReference().child("HR").child("ADDJOB");
        saved_job_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SavedJob savedJob = dataSnapshot.getValue(SavedJob.class);
                        savedJobArrayList.add(savedJob);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        job_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        for (SavedJob savedJob: savedJobArrayList){
                            if (job.getId().equals(savedJob.getId())) {
                                jobArrayList.add(job);
                        Log.e(TAG, "onDataChange: "+job.getId() );
                            }
                        }
                    }
                    if (jobArrayList.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                    } else{
                        noData.setVisibility(View.GONE);
                    }
                    savedJobAdapter = new SavedJobAdapter(getContext(), jobArrayList, preferences, preferenceEditor,"savedJob");
                    jobRecyclerView.setAdapter(savedJobAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}
