package com.almightymm.job4u.fragment;

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

public class CompanyDetailsFragment extends Fragment {
    EditText company_name, about_company, location, phone_no, website;
    Button btn_conti;
    DatabaseReference db_addcompany_details;


    public CompanyDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_details, container, false);

        initViews(view);
        db_addcompany_details = FirebaseDatabase.getInstance().getReference().child("Users").child("COMPANY_DETAILS");
        setListeners(view);
        return view;
    }

    private void setListeners(View view) {

        btn_conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCompanyProfile();
                clear();
            }
        });
    }


    public void addCompanyProfile() {
        String cname = company_name.getText().toString().trim();
        String cabout = about_company.getText().toString().trim();
        String clocation = location.getText().toString().trim();
        String cphone = phone_no.getText().toString().trim();
        String cwebsite = website.getText().toString().trim();
        String id = db_addcompany_details.push().getKey();
        CompanyDetails addcompanyDetails = new CompanyDetails(cname, cabout, clocation, cphone, cwebsite);
       
        db_addcompany_details.child(id).setValue(addcompanyDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();     
            }
        });

    }

    public void clear() {
        company_name.setText("");
        about_company.setText("");
        location.setText("");
        phone_no.setText("");
        website.setText("");
    }

    private void initViews(View view) {
        company_name = (EditText) view.findViewById(R.id.edit_company_name);
        about_company = (EditText) view.findViewById(R.id.edit_about);
        location = (EditText) view.findViewById(R.id.edit_location);
        phone_no = (EditText) view.findViewById(R.id.edit_phone);
        website = (EditText) view.findViewById(R.id.edit_website);
        btn_conti = (Button) view.findViewById(R.id.btn_continue);
    }
}