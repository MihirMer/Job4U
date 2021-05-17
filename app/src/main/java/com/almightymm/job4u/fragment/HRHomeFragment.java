package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.HRJobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HRHomeFragment extends Fragment {
    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    HRJobAdapter hrJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;
    RelativeLayout noData;
    DatabaseReference firebaseDatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    TextView layjob;

    String userId;

    public HRHomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_h_r_home, container, false);

        initPreferences();
        noData = view.findViewById(R.id.lay3);
        layjob = view.findViewById(R.id.hr_job_title);
        preferenceEditor.putString("jobId", "");
        preferenceEditor.apply();
        preferenceEditor.commit();
        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
        jobArrayList = new ArrayList<>();
        hrJobAdapter = new HRJobAdapter(getContext(), filter(), preferences, preferenceEditor, "home");
        jobRecyclerView.setAdapter(hrJobAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
        firebaseDatabase.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                jobArrayList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    if (jobArrayList.get(0) == null) {
                        noData.setVisibility(View.GONE);
                        layjob.setVisibility(View.GONE);
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        layjob.setVisibility(View.VISIBLE);
                    }

                    hrJobAdapter.filterList(filter());
                }
            }
        });
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobArrayList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    hrJobAdapter.filterList(filter());

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
        String cn = preferences.getString("companyName", "");
        for (Job job : jobArrayList) {
            if (job.getCompanyName().equals(cn))
                filterList.add(job);
        }
        if (!filterList.isEmpty()) {
            noData.setVisibility(View.GONE);
            layjob.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.VISIBLE);
            layjob.setVisibility(View.GONE);
        }
        return filterList;

    }


    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}