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

import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.Adapter.SavedJobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class savedJobFragment extends Fragment {
    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    SavedJobAdapter savedJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    DatabaseReference firebaseDatabase;

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
        View view = inflater.inflate(R.layout.fragment_saved_job2, container, false);

        initPreferences();

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.saved_job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
        jobArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    savedJobAdapter = new SavedJobAdapter(getContext(), filter(), preferences, preferenceEditor);
                    jobRecyclerView.setAdapter(savedJobAdapter);

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
            //Log.e("errr", "filter: "+job.getJob_Name()+" "+category.getC_Job_name() );
            filterList.add(job);
        }
        return filterList;

    }
    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}
