package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.almightymm.job4u.Adapter.JobFragmentPagerAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.Job;
import com.almightymm.job4u.model.SavedJob;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Context.MODE_PRIVATE;

public class JobPreviewFragment extends Fragment {
    TextView job_name, company, dop, salary, designation, website, city;
    DatabaseReference databaseReference;
    DatabaseReference save_job_ref;
    DatabaseReference applied_job_ref;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    Button savedJob;
    Button appliedJob;
    String jobId;
    String userId;
    private static final String TAG = "JobPreviewFragment";

    public JobPreviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_job_preview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreferences();

        job_name = view.findViewById(R.id.txt_job_name);
        company = view.findViewById(R.id.txt_company_name);
        dop = view.findViewById(R.id.txt_post_date);
        designation = view.findViewById(R.id.txt_job_designation);
        website = view.findViewById(R.id.txt_job_website);
        salary = view.findViewById(R.id.txt_job_salary);
        city = view.findViewById(R.id.txt_job_city);
        savedJob = view.findViewById(R.id.job_saved);
        appliedJob= view.findViewById(R.id.job_apply_now);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        userId = preferences.getString("userId", "");
        jobId = preferences.getString("jobId", "");
        save_job_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("SAVED_JOB").child(jobId);
        setListeners(view);
        applied_job_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("APPLIED_JOB").child(jobId);
        setListeners1(view);


        databaseReference = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB").child(jobId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Job addjob = snapshot.getValue(Job.class);

                job_name.setText(addjob.getName());
                dop.setText(" Post Date : " + addjob.getPost_date());
                company.setText(addjob.getCompanyName());
                designation.setText(" Designation : " + addjob.getDesignation());
                salary.setText(" Salary : " + addjob.getSalary());
                //           Log.e(TAG, "onDataChange: "+addjob.getJob_City() );
                city.setText(" City : " + addjob.getCity());
                website.setText(" Website : " + addjob.getWebsite());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Job Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Company Details"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        JobFragmentPagerAdapter adapter = new JobFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
//saved job
    private void setListeners(View view) {
        savedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedJob s = new SavedJob(jobId);
                save_job_ref.setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Job Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    //applied job
    private void setListeners1(View view)
    {
        appliedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getBoolean("areaOfInterestSelected", false) && preferences.getBoolean("personalDetailsAdded", false) && preferences.getBoolean("keySkillAdded", false) && preferences.getBoolean("educationAdded", false) && preferences.getBoolean("projectWorkAdded", false)) {
//                Toast.makeText(context, "Position " + positionPlusOne, Toast.LENGTH_SHORT).show();
                    final String userId = preferences.getString("userId", "");
                    String userName = preferences.getString("firstName", "") + " " + preferences.getString("lastName", "");

                    Candidate candidate = new Candidate(userId, userName, "Applied");
                    DatabaseReference applyNowDbRef = FirebaseDatabase.getInstance().getReference().child("HR").child("RESPONSES").child(jobId);
                    applyNowDbRef.child(userId).setValue(candidate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SavedJob savedJob = new SavedJob(jobId);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("APPLIED_JOB").child(jobId).setValue(savedJob).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    here to subscribe
                                    Log.e(TAG, "onSuccess:  "+jobId+" "+userId  );
                                    FirebaseMessaging.getInstance().subscribeToTopic(jobId+userId)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.e(TAG, "onComplete: Subscribed to topic " +jobId+ " "+userId);
                                                    } else {
                                                        Log.e(TAG, "onComplete: Subscribed to topic failed ");
                                                    }
                                                }
                                            });
                                    Toast.makeText(getContext(), "Resume sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Please complete your profile details first !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}