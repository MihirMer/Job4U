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
    HRJobAdapter hrJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;
    RelativeLayout noData;
    DatabaseReference firebaseDatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String categoryName;
    String userId;

    TextView lay2;

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
        lay2 = view.findViewById(R.id.hr_job_title);
        noData = view.findViewById(R.id.lay4);
        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
        jobArrayList = new ArrayList<>();

        categoryName = preferences.getString("categoryName", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        jobArrayList.add(job);


                    }
                    if (preferences.getString("role", "").equals("HR")) {
                        jobArrayList = filter1();
                        hrJobAdapter = new HRJobAdapter(getContext(), filter(), preferences, preferenceEditor, "joblist");
                        jobRecyclerView.setAdapter(hrJobAdapter);
                    } else {

                        jobAdapter = new JobAdapter(getContext(), filter(), preferences, preferenceEditor, R.id.action_jobListFragment_to_jobPreviewFragment);
                        jobRecyclerView.setAdapter(jobAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    private ArrayList<Job> filter1() {
        ArrayList<Job> filterList = new ArrayList<>();
        String cn = preferences.getString("companyName", "");
        for (Job job : jobArrayList) {
            if (job.getCompanyName().equals(cn))
                filterList.add(job);
        }
        if (!filterList.isEmpty()) {
            noData.setVisibility(View.GONE);
            lay2.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.VISIBLE);
            lay2.setVisibility(View.GONE);
        }
        return filterList;
    }

    private ArrayList<Job> filter() {
        ArrayList<Job> filterList = new ArrayList<>();
        for (Job job : jobArrayList) {
            if (job.getName().equals(categoryName)) {
                noData.setVisibility(View.GONE);
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