package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.CompanyDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class CompanyDetailsFragment extends Fragment {
    private static final String TAG = "CompanyDetailsFragment";
    TextView company_name, about, location, website;
    RelativeLayout relativeLayout;
    DatabaseReference databaseReference;
    String companyId;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    public CompanyDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_company_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreferences();
        company_name = view.findViewById(R.id.txt_company_name);
        location = view.findViewById(R.id.txt_company_location);
        about = view.findViewById(R.id.txt_company_about);
        website = view.findViewById(R.id.txt_company_website);
        relativeLayout = view.findViewById(R.id.company_detail_layout);

        companyId = preferences.getString("companyId", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("HR").child("COMPANY_DETAILS").child(companyId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CompanyDetails addcompanyDetails = snapshot.getValue(CompanyDetails.class);
                if (addcompanyDetails != null) {
                    company_name.setText(addcompanyDetails.getCompanyName());
                    about.setText(addcompanyDetails.getAbout());
                    location.setText(addcompanyDetails.getLocation());
                    website.setText(addcompanyDetails.getWebsite());
                    relativeLayout.setVisibility(View.VISIBLE);
                }
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
