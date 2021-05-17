package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.WorkExperience;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class WorkExperienceFragment extends Fragment {

    EditText designation, yearOfExperience, from_year, to_year, companName, city;
    Button add_workExperience;
    DatabaseReference db_add_workExperience;
    DatabaseReference personalDetails;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String experienceId;

    public WorkExperienceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_work_experience, container, false);
        initViews(view);
        initPreferences();
        String userId = preferences.getString("userId", "");
        experienceId = preferences.getString("experienceId", "");
        db_add_workExperience = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("WORK_EXPERIENCE");
        personalDetails = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PERSONAL_DETAILS").child("experienceAdded");
        setValues(view);
        setListeners(view);
        return view;
    }

    private void setValues(View view) {
        db_add_workExperience.child(experienceId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                WorkExperience workExperience = dataSnapshot.getValue(WorkExperience.class);
                if (workExperience!=null){
                    designation.setText(workExperience.getDesignation());
                    yearOfExperience.setText(workExperience.getYearsOfExperience());
                    from_year.setText(workExperience.getFromYear());
                    to_year.setText(workExperience.getToYear());
                    companName.setText(workExperience.getCompanyName());
                    city.setText(workExperience.getCity());
                }
            }
        });
    }

    private void setListeners(View view) {
        add_workExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_workexperience();
            }
        });
        from_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_start_year(v);
            }
        });
        to_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_end_year(v);
            }
        });
    }

    private void initViews(View view) {
        designation = view.findViewById(R.id.txt_designation);
        yearOfExperience = view.findViewById(R.id.txt_eyear);
        from_year = view.findViewById(R.id.txt_start_year);
        to_year = view.findViewById(R.id.txt_end_year);
        companName = view.findViewById(R.id.txt_companyname);
        city = view.findViewById(R.id.txt_city);
        add_workExperience = view.findViewById(R.id.btn_add_workExperience);
    }

    public void txt_start_year(View view) {

        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                from_year.setText(String.valueOf(selectedYear));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY).setMinYear(1990).
                setActivatedYear(today.get(Calendar.YEAR)).setMaxYear(2021)
                .setTitle("Select Year").showYearOnly()
                .build().show();
    }

    public void txt_end_year(View view) {

        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                to_year.setText(String.valueOf(selectedYear));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY).setMinYear(1990).
                setActivatedYear(today.get(Calendar.YEAR)).setMaxYear(2021)
                .setTitle("Select Year").showYearOnly()
                .build().show();
    }

    private void add_workexperience() {
        String design = designation.getText().toString().trim();
        String eyears = yearOfExperience.getText().toString().trim();
        String fyear = from_year.getText().toString().trim();
        String tyear = to_year.getText().toString().trim();
        String cp = companName.getText().toString().trim();
        String cit = city.getText().toString().trim();


        if (design.isEmpty()) {
            designation.requestFocus();
            Toast.makeText(getContext(), "Designation is required !", Toast.LENGTH_SHORT).show();
        } else if (eyears.isEmpty()) {
            yearOfExperience.requestFocus();
            Toast.makeText(getContext(), "Years of experience is required !", Toast.LENGTH_SHORT).show();
        } else if (fyear.isEmpty()) {
            from_year.requestFocus();
            Toast.makeText(getContext(), "From year is required !", Toast.LENGTH_SHORT).show();
        } else if (tyear.isEmpty()) {
            to_year.requestFocus();
            Toast.makeText(getContext(), "To year is required !", Toast.LENGTH_SHORT).show();
        } else {
            int f = Integer.parseInt(fyear);
            int t = Integer.parseInt(tyear);
            int e = Integer.parseInt(eyears);
            if ((t - f) != e) {
                yearOfExperience.requestFocus();
                Toast.makeText(getContext(), "Please enter valid Years of experience is required !", Toast.LENGTH_SHORT).show();
            } else if (cp.isEmpty()) {
                companName.requestFocus();
                Toast.makeText(getContext(), "Company Name is required !", Toast.LENGTH_SHORT).show();
            } else if (cit.isEmpty()) {
                city.requestFocus();
                Toast.makeText(getContext(), "City is required !", Toast.LENGTH_SHORT).show();
            } else {
                String id = db_add_workExperience.push().getKey();
                WorkExperience workexperience = new WorkExperience(id, design, eyears, fyear, tyear, cp, cit);
                db_add_workExperience.child(id).setValue(workexperience).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        personalDetails.setValue(true);
                        preferenceEditor.putBoolean("experienceAdded", true);
                        preferenceEditor.putString("experienceId", "");
                        preferenceEditor.apply();
                        preferenceEditor.commit();
                        Toast.makeText(getContext(), "Details Save", Toast.LENGTH_LONG).show();
                        clear();
                    }
                });
            }
        }
    }

    private void clear() {
        getActivity().onBackPressed();
//        designation.setText("");
//        yearOfExperience.setText("");
//        from_year.setText("");
//        to_year.setText("");
//        companName.setText("");
//        city.setText("");
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}