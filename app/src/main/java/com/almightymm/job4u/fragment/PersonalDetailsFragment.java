package com.almightymm.job4u.fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.PersonalDetails;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class PersonalDetailsFragment extends Fragment {
    EditText firstname, lastname, email, phone, dob, address;
    DatePickerDialog datePickerDialog;
    RadioButton radioButton;
    Button save;
    RadioGroup rg;
    RadioButton male, female;
    DatabaseReference db_add_personal_detail;
    AwesomeValidation awesomeValidation;
    SharedPreferences preferences;

    private static final String TAG = "PersonalDetailsFragment";
    public PersonalDetailsFragment() {
        // Required empty public constructor
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
        phone = view.findViewById(R.id.txt_phone);
        dob = view.findViewById(R.id.txt_dob);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int i, int i1, int i2) {
                        dob.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        address = view.findViewById(R.id.txt_address);
        female = view.findViewById(R.id.rb_female);
        male = view.findViewById(R.id.rb_male);

        rg = view.findViewById(R.id.gender_layout);
        save = view.findViewById(R.id.btn_save);

        String userId = preferences.getString("userId", "");
        Log.d(TAG, "onCreateView: "+userId);
        db_add_personal_detail = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PERSONAL_DETAILS");


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //FOR first name
        awesomeValidation.addValidation(getActivity(), R.id.txt_firstname, RegexTemplate.NOT_EMPTY,
                R.string.invalid_fname);
        //FOR last name
        awesomeValidation.addValidation(getActivity(), R.id.txt_lastname, RegexTemplate.NOT_EMPTY,
                R.string.invalid_lname);
        //FOR date of birth
        awesomeValidation.addValidation(getActivity(), R.id.txt_dob, RegexTemplate.NOT_EMPTY,
                R.string.invalid_dob);
        //FOR email
        awesomeValidation.addValidation(getActivity(), R.id.txt_email, Patterns.EMAIL_ADDRESS,
                R.string.invalid_email);
        //FOR phone
        awesomeValidation.addValidation(getActivity(), R.id.txt_phone, "[5-9]{1}[0-9]{9}$",
                R.string.invalid_phone);
        //FOR address
        awesomeValidation.addValidation(getActivity(), R.id.txt_firstname, RegexTemplate.NOT_EMPTY,
                R.string.invalid_address);
        //FOR gender
        awesomeValidation.addValidation(getActivity(), R.id.gender_layout, RegexTemplate.NOT_EMPTY,
                R.string.invalid_rg);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    adddetails();
                    clear();
                } else {
                    Toast.makeText(getContext(), "Please Enter Valid Details", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void adddetails() {
        String fn = firstname.getText().toString().trim();
        String ln = lastname.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String phoneno = phone.getText().toString().trim();
        String dateofbirth = dob.getText().toString().trim();
        String adre = address.getText().toString().trim();

        String gender = "";
        if (female.isChecked()) {
            gender = "Female";

        }
        if (male.isChecked()) {
            gender = "male";
        }

        if (!TextUtils.isEmpty(fn)) {
            String id = db_add_personal_detail.push().getKey();

            PersonalDetails personalDetails = new PersonalDetails(id, fn, ln, mail, phoneno,
                    dateofbirth, adre, gender);
            db_add_personal_detail.setValue(personalDetails);

            Toast.makeText(getContext(), "Details Saved", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "Enter Data", Toast.LENGTH_LONG).show();
        }


    }

    private void clear() {
        firstname.setText("");
        lastname.setText("");
        email.setText("");
        phone.setText("");
        dob.setText("");
        address.setText("");
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
    }
}