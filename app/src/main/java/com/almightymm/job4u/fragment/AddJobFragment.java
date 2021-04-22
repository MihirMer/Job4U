package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Job;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AddJobFragment extends Fragment {
    EditText desigation, description, salary, companyname, city, website, vacancy, pod;
    Spinner jobtype, jobQualification;

    String[] jobTitleArray;
    String[] jobQualificationArray;
    ArrayAdapter<String> jobTitleAdapter;
    ArrayAdapter<String> jobQualificationAdapter;
    Button add_job;
    DatabaseReference db_add_job;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;
     String jobId;

    public AddJobFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_job, container, false);

        initViews(view);
        initPreferences();

        setValues();
        addListeners(view);

        userId = preferences.getString("userId", "");
        db_add_job = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");

        if (!preferences.getBoolean("companyDetailsAdded", false)) {
            Toast.makeText(getContext(), "Please add Company details first !", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void setValues() {
//        companyname, city, website,
         jobId = preferences.getString("jobId", "");
        if (!jobId.equals("")) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB").child(jobId);
            databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Job job = dataSnapshot.getValue(Job.class);
//                    mare string compare karavi ne id set krvi cheok
                    jobtype.setSelection(jobTitleAdapter.getPosition(job.getName()));
                    jobQualification.setSelection(jobQualificationAdapter.getPosition(job.getQualification()));
                    desigation.setText(job.getQualification());
                    description.setText(job.getDescription());
                    salary.setText(job.getSalary());
                    companyname.setText(job.getCompanyName());
                    city.setText(job.getCity());
                    website.setText(job.getWebsite());
                    vacancy.setText(job.getVacancy());
                    pod.setText(job.getPost_date());
                }
            });

        }
        if (preferences.getBoolean("companyDetailsAdded", false)) {
            companyname.setText(preferences.getString("companyName", ""));
            city.setText(preferences.getString("companyLocation", ""));
            website.setText(preferences.getString("companyWebsite", ""));
        }
    }

    private void initViews(View view) {
        jobtype = view.findViewById(R.id.jobtitle);
        jobTitleArray =new String[] {"Job Title", "Web Developer", "Web Designer", "Android Developer", "UI/UX Designer", "Graphics Designer", "Backend Developer", "Frontend Developer"};
        jobTitleAdapter = new ArrayAdapter<String>(getContext(), R.layout.job_spinner_row, R.id.item, jobTitleArray);
        jobtype.setPrompt("Select Job");
        jobtype.setAdapter(jobTitleAdapter);
        jobQualification = view.findViewById(R.id.jobQualification);
        jobQualificationArray =new String[] {"Qualification", "B.E.", "M.E.", "Diploma in IT", "12th", "Graduate", "Post Graduate", "B.tech", "M.Sc"};
        jobQualificationAdapter = new ArrayAdapter<String>(getContext(), R.layout.job_spinner_row, R.id.item, jobQualificationArray);
        jobQualification.setPrompt("Select Qualification");
        jobQualification.setAdapter(jobQualificationAdapter);

        desigation = view.findViewById(R.id.edit_desig);
        description = view.findViewById(R.id.edit_desc);
        salary = view.findViewById(R.id.edit_salary);
        companyname = view.findViewById(R.id.edit_company_name);
        city = view.findViewById(R.id.edit_city);
        website = view.findViewById(R.id.edit_website);
        pod = view.findViewById(R.id.edit_post_date);
        Date date = new Date();
        CharSequence s = DateFormat.format("MMMM d,yyyy", date.getTime());
        pod.setText(s);
        pod.setEnabled(false);
        companyname.setEnabled(false);
        website.setEnabled(false);
        vacancy = view.findViewById(R.id.edit_vacancy);
        add_job = view.findViewById(R.id.btn_addjob);
    }

    private void addListeners(View view) {
        add_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addjob();
            }
        });
    }

    private void addjob() {
        String title = jobtype.getSelectedItem().toString().trim();
        String desi = desigation.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String sala = salary.getText().toString().trim();
        String company = companyname.getText().toString().trim();
        String cit = city.getText().toString().trim();
        String web = website.getText().toString().trim();
        String quali = jobQualification.getSelectedItem().toString().trim();
        String post_date = pod.getText().toString().trim();
        String vacan = vacancy.getText().toString().trim();
        if (!preferences.getBoolean("companyDetailsAdded", false)) {
            Toast.makeText(getContext(), "Please add Company details first !", Toast.LENGTH_SHORT).show();
        } else if (title.equals("Job Title")) {
            jobtype.requestFocus();
            Toast.makeText(getContext(), "Title is required !", Toast.LENGTH_SHORT).show();
        } else if (quali.equals("Qualification")) {
            jobQualification.requestFocus();
            Toast.makeText(getContext(), "Qualification is required !", Toast.LENGTH_SHORT).show();
        } else if (desi.isEmpty()) {
            desigation.requestFocus();
            Toast.makeText(getContext(), "Designation is required !", Toast.LENGTH_SHORT).show();
        } else if (sala.isEmpty()) {
            salary.requestFocus();
            Toast.makeText(getContext(), "Salary is required !", Toast.LENGTH_SHORT).show();
        } else if (company.isEmpty()) {
            companyname.requestFocus();
            Toast.makeText(getContext(), "Company name is required !", Toast.LENGTH_SHORT).show();
        } else if (cit.isEmpty()) {
            city.requestFocus();
            Toast.makeText(getContext(), "City is required !", Toast.LENGTH_SHORT).show();
        } else if (web.isEmpty()) {
            website.requestFocus();
            Toast.makeText(getContext(), "Website is required !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.WEB_URL.matcher(web).matches()) {
            website.requestFocus();
            Toast.makeText(getContext(), "Please enter valid Website !", Toast.LENGTH_SHORT).show();
        } else if (vacan.isEmpty()) {
            vacancy.requestFocus();
            Toast.makeText(getContext(), "No. of Vacancy is required !", Toast.LENGTH_SHORT).show();
        } else {
            if (jobId.equals("")) {
                jobId = db_add_job.push().getKey();
            }

            Job job = new Job(jobId, title, desi, desc, sala, company, cit, web, vacan, post_date, quali, userId);
            db_add_job.child(jobId).setValue(job).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(getContext(), "Task Successful", Toast.LENGTH_LONG).show();
                        jobId = db_add_job.push().getKey();

                    clear();
                }
            });
        }
    }

    private void clear() {
//        jobtype.setSelection(0);
//        jobQualification.setSelection(0);
//        desigation.setText("");
//        description.setText("");
//        salary.setText("");
//        companyname.setText("");
//        city.setText("");
//        website.setText("");
//        vacancy.setText("");
//        pod.setText("");

        preferenceEditor.putString("jobId","");
        preferenceEditor.apply();
        preferenceEditor.commit();
        getActivity().onBackPressed();

    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}