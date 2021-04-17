package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.CompanyDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class AddCompanyDetailsFragment extends Fragment {
    EditText company_name, about_company, location, phone_no, website;
    Button btn_conti;
    DatabaseReference db_addcompany_details;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;

    public AddCompanyDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_company_details, container, false);

        initViews(view);
        initPreferences();
        userId = preferences.getString("userId", "");
        db_addcompany_details = FirebaseDatabase.getInstance().getReference().child("HR").child("COMPANY_DETAILS").child(userId);
        setListeners(view);
        return view;
    }

    private void setListeners(View view) {

        btn_conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCompanyProfile();

            }
        });
    }


    public void addCompanyProfile() {
        String cname = company_name.getText().toString().trim();
        String cabout = about_company.getText().toString().trim();
        String clocation = location.getText().toString().trim();
        String cphone = phone_no.getText().toString().trim();
        String cwebsite = website.getText().toString().trim();

        if (cname.isEmpty()) {
            company_name.requestFocus();
            Toast.makeText(getContext(), "Company name is required !", Toast.LENGTH_SHORT).show();
        } else if (cabout.isEmpty()) {
            about_company.requestFocus();
            Toast.makeText(getContext(), "Company description is required !", Toast.LENGTH_SHORT).show();
        } else if (clocation.isEmpty()) {
            location.requestFocus();
            Toast.makeText(getContext(), "Company location is required !", Toast.LENGTH_SHORT).show();
        } else if (cphone.isEmpty()) {
            phone_no.requestFocus();
            Toast.makeText(getContext(), "Phone no is required !", Toast.LENGTH_SHORT).show();
        } else if (cwebsite.isEmpty()) {
            website.requestFocus();
            Toast.makeText(getContext(), "Website is required !", Toast.LENGTH_SHORT).show();
        } else {
            CompanyDetails addcompanyDetails = new CompanyDetails(userId, cname, cabout, clocation, cphone, cwebsite);

            db_addcompany_details.setValue(addcompanyDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
                    clear();
                }
            });
        }
    }

    public void clear() {
        getActivity().onBackPressed();
    }

    private void initViews(View view) {
        company_name = (EditText) view.findViewById(R.id.edit_company_name);
        about_company = (EditText) view.findViewById(R.id.edit_about);
        location = (EditText) view.findViewById(R.id.edit_location);
        phone_no = (EditText) view.findViewById(R.id.edit_phone);
        website = (EditText) view.findViewById(R.id.edit_website);
        btn_conti = (Button) view.findViewById(R.id.btn_continue);
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}