package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class JobDetailsFragment extends Fragment {
    TextView description,vacancy,qualification;
    DatabaseReference databaseReference;
    RelativeLayout relativeLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String jobId;
    public JobDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_details, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
initPreferences();
        description = view.findViewById(R.id.txt_description);
        vacancy=view.findViewById(R.id.txt_Vacancy);
        qualification= view.findViewById(R.id.txt_Qualification);
        relativeLayout= view.findViewById(R.id.job_detail_layout);

        jobId = preferences.getString("jobId", "");
        databaseReference= FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB").child(jobId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Job addjob = snapshot.getValue(Job.class);

                description.setText(addjob.getDescription());
                vacancy.setText(addjob.getVacancy());
                qualification.setText(addjob.getQualification());

                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}