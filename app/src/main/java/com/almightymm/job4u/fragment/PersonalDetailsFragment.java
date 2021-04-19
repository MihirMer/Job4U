package com.almightymm.job4u.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.PersonalDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class PersonalDetailsFragment extends Fragment {
    private static final String TAG = "AddPersonalDetails";
    EditText firstname, lastname, email, phone, dob, address;
    DatePickerDialog datePickerDialog;
    Spinner gendertxt;
    Button save;
    DatabaseReference db_add_personal_detail;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    PersonalDetails personalDetails1;

    public PersonalDetailsFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);
        initPreferences();

        firstname = view.findViewById(R.id.txt_firstname);
        lastname = view.findViewById(R.id.txt_lastname);
        email = view.findViewById(R.id.txt_email);
        email.setEnabled(false);
        phone = view.findViewById(R.id.txt_phone);
        dob = view.findViewById(R.id.txt_dob);
        gendertxt = view.findViewById(R.id.gender_layout);

        String userId = preferences.getString("userId", "");
        Log.d(TAG, "onCreateView: " + userId);
        db_add_personal_detail = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getValues();

        String[] genderArray = {"Gender", "Male", "Female"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getContext(), R.layout.gender_spinner_row, R.id.gender, genderArray);
        gendertxt.setAdapter(genderAdapter);
        gendertxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = String.valueOf(gendertxt.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(getActivity());
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        address = view.findViewById(R.id.txt_address);


        save = view.findViewById(R.id.btn_save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddetails();
            }
        });
        return view;
    }

    private void adddetails() {
        String fn = firstname.getText().toString().trim();
        String ln = lastname.getText().toString().trim();
        String mail = email.getText().toString().trim();
        final String phoneno = phone.getText().toString().trim();
        final String dateofbirth = dob.getText().toString().trim();
        final String adre = address.getText().toString().trim();
        final String gender = gendertxt.getSelectedItem().toString().trim();


        if (fn.isEmpty()) {
//            firstName.setError("First Name is Required");
            firstname.requestFocus();
            Toast.makeText(getContext(), "First name is required !", Toast.LENGTH_SHORT).show();
        } else if (ln.isEmpty()) {
//            lastname.setError("Last Name is Required");
            lastname.requestFocus();
            Toast.makeText(getContext(), "Last name is required !", Toast.LENGTH_SHORT).show();
        } else if (mail.isEmpty()) {
//            email.setError("Email is Required");
            email.requestFocus();
            Toast.makeText(getContext(), "Email id is required !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
//            email.setError("Please Provide Valid Email");
            email.requestFocus();
            Toast.makeText(getContext(), "Please provide valid Email id !", Toast.LENGTH_SHORT).show();
        } else if (phoneno.isEmpty()) {
            phone.requestFocus();
            Toast.makeText(getContext(), "Phone number is required !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(phoneno).matches()) {
            phone.requestFocus();
            Toast.makeText(getContext(), "Please provide valid Phone number !", Toast.LENGTH_SHORT).show();
        } else if (dateofbirth.isEmpty()) {
            dob.requestFocus();
            Toast.makeText(getContext(), "Date of Birth is required !", Toast.LENGTH_SHORT).show();
        } else if (gender.equals("Gender")) {
            gendertxt.requestFocus();
            Toast.makeText(getContext(), "Gender is required !", Toast.LENGTH_SHORT).show();
        } else if (adre.isEmpty()) {
            address.requestFocus();
            Toast.makeText(getContext(), "Address is required !", Toast.LENGTH_SHORT).show();
        } else {
//            PersonalDetails personalDetails = new PersonalDetails(fn, ln, mail, phoneno, dateofbirth, adre, gender);
            final PersonalDetails personalDetails = new PersonalDetails(
                    fn,
                    ln,
                    mail,
                    phoneno,
                    dateofbirth,
                    adre,
                    gender,
                    preferences.getString("role", ""),
                    preferences.getBoolean("roleAssigned", false),
                    personalDetails1.isAreaOfInterestSelected(),
                    true,
                    personalDetails1.isKeySkillAdded(),
                    personalDetails1.isEducationAdded(),
                    personalDetails1.isProjectWorkAdded(),
                    personalDetails1.isExperienceAdded(),
                    personalDetails1.isCompanyDetailsAdded()
            );
            db_add_personal_detail.setValue(personalDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    preferenceEditor.putString("phone", phoneno);
                    preferenceEditor.putString("DOB", dateofbirth);
                    preferenceEditor.putString("address", adre);
                    preferenceEditor.putString("gender", gender);
                    preferenceEditor.apply();
                    preferenceEditor.commit();

                    Toast.makeText(getContext(), "Details Saved", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
            });
        }
    }

    private void getValues() {
//        firstname = view.findViewById(R.id.txt_firstname);
//        lastname = view.findViewById(R.id.txt_lastname);
//        email = view.findViewById(R.id.txt_email);
//        phone = view.findViewById(R.id.txt_phone);
//        dob = view.findViewById(R.id.txt_dob);
//        gendertxt = view.findViewById(R.id.gender_layout);

        final String fname = preferences.getString("firstName", "");
        final String lname = preferences.getString("lastName", "");
        final String emailid = preferences.getString("emailAddress", "");


        db_add_personal_detail.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                personalDetails1 = dataSnapshot.getValue(PersonalDetails.class);
                if (personalDetails1 != null) {

                    firstname.setText(fname);
                    lastname.setText(lname);
                    email.setText(emailid);
                    phone.setText(personalDetails1.getPhone());
                    dob.setText(personalDetails1.getDOB());
                    if (personalDetails1.getGender() != null) {
                        if (personalDetails1.getGender().equals("Male")) {
                            gendertxt.setSelection(1);
                        } else {
                            gendertxt.setSelection(2);
                        }
                    }
                    address.setText(personalDetails1.getAddress());
                }

            }
        });
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}