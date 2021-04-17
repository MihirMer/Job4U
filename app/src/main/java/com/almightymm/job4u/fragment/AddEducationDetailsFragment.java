package com.almightymm.job4u.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.EducationDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class AddEducationDetailsFragment extends Fragment {

    EditText degreename, collegename, stream, edu_year, cgpa, ayear;
    Button add_education;
    DatabaseReference db_add_education_details;
    DatabaseReference personalDetails;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    private static final String TAG = "EducationDetailsFragmen";

    public AddEducationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_education_details, container, false);
        initPreferences();
        String userId = preferences.getString("userId", "");
        db_add_education_details = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("EDUCATION_DETAILS");
        personalDetails = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("educationAdded");
        initViews(view);
        setValues(view);
        setListeners(view);
        return view;
    }

    private void setValues(View view) {
        db_add_education_details.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                EducationDetails educationDetails = dataSnapshot.getValue(EducationDetails.class);
                if (educationDetails!=null){
                    degreename.setText(educationDetails.getDegreeName());
                    collegename.setText(educationDetails.getCollegeName());
                    stream.setText(educationDetails.getStream());
                    edu_year.setText(educationDetails.getYear());
                    cgpa.setText(educationDetails.getGpa());
                    ayear.setText(educationDetails.getAdmissionYear());

                }
                else
                    Log.e(TAG, "onSuccess: null" );
            }
        });
    }

    private void initViews(View view) {
        degreename = view.findViewById(R.id.txt_degree);
        collegename = view.findViewById(R.id.txt_college);
        stream = view.findViewById(R.id.txt_streem);
        edu_year = view.findViewById(R.id.txt_gyear);
        cgpa = view.findViewById(R.id.txt_cgpa);
        ayear = view.findViewById(R.id.txt_ayear);
        add_education = view.findViewById(R.id.btn_addeducation);
    }

    private void setListeners(View view) {
        add_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_education();
            }
        });
        edu_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                txt_gyear(v);

            }
        });
        ayear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                txt_ayear(v);
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void add_education() {
        String dn = degreename.getText().toString().trim();
        String cn = collegename.getText().toString().trim();
        String stre = stream.getText().toString().trim();
        String gyear = edu_year.getText().toString().trim();
        String CGPA = cgpa.getText().toString().trim();
        String aYear = ayear.getText().toString().trim();

        if (dn.isEmpty()) {
//            signInEmailId.setError("Email Id is required !");
            Toast.makeText(getContext(), "Degree name is required !", Toast.LENGTH_SHORT).show();
            degreename.requestFocus();
        } else if (cn.isEmpty()) {
            Toast.makeText(getContext(), "College name is required !", Toast.LENGTH_SHORT).show();
            collegename.requestFocus();
        } else if (stre.isEmpty()) {
            Toast.makeText(getContext(), "Stream is required !", Toast.LENGTH_SHORT).show();
            stream.requestFocus();
        } else if (aYear.isEmpty()) {
            Toast.makeText(getContext(), "Admission year is required !", Toast.LENGTH_SHORT).show();
            ayear.requestFocus();
        } else if (gyear.isEmpty()) {
            Toast.makeText(getContext(), "Graduation year is required !", Toast.LENGTH_SHORT).show();
            edu_year.requestFocus();
        } else if (CGPA.isEmpty()) {
            Toast.makeText(getContext(), "CGPA is required !", Toast.LENGTH_SHORT).show();
            cgpa.requestFocus();
        } else {

//        do after validation
            String id = db_add_education_details.push().getKey();
            EducationDetails educationDetails = new EducationDetails(
                    id,
                    dn,
                    cn,
                    stre,
                    aYear,
                    gyear,
                    CGPA
            );

            db_add_education_details.child(id).setValue(educationDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    personalDetails.setValue(true);
                    preferenceEditor.putBoolean("educationAdded", true);
                    preferenceEditor.apply();
                    preferenceEditor.commit();
                    Toast.makeText(getContext(), "Details Save", Toast.LENGTH_LONG).show();
                    clear();
                }
            });
        }

    }

    private void clear() {
        getActivity().onBackPressed();
    }


    public void txt_gyear(View view) {

        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                edu_year.setText(String.valueOf(selectedYear));
            }
        },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY).setMinYear(1990).
                setActivatedYear(today.get(Calendar.YEAR)).setMaxYear(2990)
                .setTitle("Select Year").showYearOnly()
                .build().show();

    }
    public void txt_ayear(View view) {

        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                ayear.setText(String.valueOf(selectedYear));
            }
        },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY).setMinYear(1990).
                setActivatedYear(today.get(Calendar.YEAR)).setMaxYear(2990)
                .setTitle("Select Year").showYearOnly()
                .build().show();

    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}