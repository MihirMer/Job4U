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
import com.almightymm.job4u.Adapter.HRResponsesAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HrViewResponsesFragment extends Fragment {
    RecyclerView jobRecyclerView;
    ArrayList<Candidate> candidateArrayList;
    HRResponsesAdapter hrJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    DatabaseReference firebaseDatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    String userId;
    String jobId;
    public HrViewResponsesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_hr_view_responses, container, false);

        initPreferences();

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
         candidateArrayList= new ArrayList<>();
        jobId = preferences.getString("jobId", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("HR").child("RESPONSES").child(jobId);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Candidate candidate = dataSnapshot.getValue(Candidate.class);
                        candidateArrayList.add(candidate);
                    }

                    hrJobAdapter = new HRResponsesAdapter(getContext(), candidateArrayList,preferences,preferenceEditor);
                    jobRecyclerView.setAdapter(hrJobAdapter);


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