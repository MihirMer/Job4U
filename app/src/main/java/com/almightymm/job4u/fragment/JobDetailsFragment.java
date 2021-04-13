package com.almightymm.job4u.fragment;

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

public class JobDetailsFragment extends Fragment {
    TextView job_name,company,dop,description,salary,designation,website,city,vacancy,qualification;
    DatabaseReference databaseReference;
    RelativeLayout relativeLayout;
    public JobDetailsFragment() {
        // Required empty public constructor
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

        job_name = view.findViewById(R.id.txt_job_name);
        company = view.findViewById(R.id.txt_company_name);
        dop = view.findViewById(R.id.txt_post_date);
        description = view.findViewById(R.id.txt_description);
        designation = view.findViewById(R.id.txt_job_designation);
        website =view.findViewById(R.id.txt_job_website);
        salary = view.findViewById(R.id.txt_job_salary);
        city = view.findViewById(R.id.txt_job_city);
        vacancy=view.findViewById(R.id.txt_Vacancy);
        qualification= view.findViewById(R.id.txt_Qualification);
        relativeLayout= view.findViewById(R.id.job_detail_layout);

        databaseReference= FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB").child("-MY6JZObLOQm2asNhvVS");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Job addjob = snapshot.getValue(Job.class);

                job_name.setText(addjob.getName());
                dop.setText(" Post Date : "+addjob.getPost_date());
                company.setText(addjob.getCompanyName());
                description.setText(addjob.getDescription());
                designation.setText(" Designation : "+addjob.getDesignation());
                salary.setText(" Salary : "+addjob.getSalary());
                //           Log.e(TAG, "onDataChange: "+addjob.getJob_City() );
                city.setText(" City : "+addjob.getCity());
                website.setText(" Website : "+addjob.getWebsite());
                vacancy.setText(addjob.getVacancy());
                qualification.setText(addjob.getQualification());

                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}