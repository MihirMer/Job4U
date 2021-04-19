package com.almightymm.job4u.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.service.autofill.Sanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almightymm.job4u.R;
import com.almightymm.job4u.SignInActivity;
import com.almightymm.job4u.latex.Document;
import com.almightymm.job4u.latex.PreferenceHelper;
import com.almightymm.job4u.model.CompanyDetails;
import com.almightymm.job4u.utils.FilesUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;


public class HRProfileFragment extends Fragment {
    public final static int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    //setting
    public static final String FONT_SIZE = "font_size";
    public static final String IMAGES_FOLDER = "images_folder";
    public static final String OUTPUT_FOLDER = "output_folder";
    public static final String EXE = "executable";
    public static final String SERVER_ADDRESS = "server_address";
    public static final String TAB_SIZE = "tab_size";
    public static final String DEFAULT_IMAGES_FOLDER =
            FilesUtils.getDocumentsDir().getPath();
    public static final String DEFAULT_OUTPUT_FOLDER =
            FilesUtils.getDocumentsDir().getPath();
    private static final String TAG = "ProfileFragment";
    private final LinkedList<Document> documents = new LinkedList<>();

    //    personal details
    TextView name, gmail, phone, city, dob, gender;
    ImageView personalEditImageView;

    //Company deatils
    TextView companyInfo ,companyName, about, companyCity, companyPhone, companyWebsite;
    ImageView companyEditImageView;
    private Button companyDetails;
    //private HRProfileFragment.Permissions currentPermission;
    //private Document document;
    //    database
    DatabaseReference databaseReference;
    //    preferences
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;

    public HRProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_h_r_profile, container, false);
        initViews(view);
        setListeners(view);
        initPreferences();
        setValues(view);
        return view;
    }

    private void initViews(View view) {
        //Personal Details
        name = view.findViewById(R.id.name);
        gmail = view.findViewById(R.id.txt_my_mail);
        phone = view.findViewById(R.id.txt_my_phone);
        city = view.findViewById(R.id.txt_my_city);
        dob = view.findViewById(R.id.txt_my_dob);
        gender = view.findViewById(R.id.txt_my_gender);
        personalEditImageView = view.findViewById(R.id.hr_icon1);


        //Company Details
        companyName = view.findViewById(R.id.txt_my_company_name);
        about = view.findViewById(R.id.txt_my_company_about);
        companyPhone = view.findViewById(R.id.txt_my_company_phone);
        companyWebsite = view.findViewById(R.id.txt_my_company_website);
        companyCity = view.findViewById(R.id.txt_my_company_city);
        companyDetails= view.findViewById(R.id.btn_company);
        companyEditImageView= view.findViewById(R.id.icon2);
        companyInfo = view.findViewById(R.id.txt_1);
    }

    private void setListeners(final View view) {

        //        personal details

        personalEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_HRProfileFragment_to_personalDetailsFragment2);
            }
        });
        companyEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_HRProfileFragment_to_companyDetailsFragment2);
            }
        });

        companyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_HRProfileFragment_to_companyDetailsFragment2);
            }
        });
    }


    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();

    }

    private void setValues(View view) {

//        personal details
        name.setText(preferences.getString("firstName", "") + " " + preferences.getString("lastName", ""));
        gmail.setText(preferences.getString("emailAddress", ""));
        if (preferences.getString("phone", null) != null && preferences.getString("DOB", null) != null && preferences.getString("address", null) != null && preferences.getString("gender", null) != null) {
            phone.setText(preferences.getString("phone", null));
            city.setText(preferences.getString("DOB", null));
            dob.setText(preferences.getString("address", null));
            gender.setText(preferences.getString("gender", null));
            phone.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);
            dob.setVisibility(View.VISIBLE);
            gender.setVisibility(View.VISIBLE);
        } else {
            phone.setVisibility(View.GONE);
            city.setVisibility(View.GONE);
            dob.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
        }
        //For company Display
//        userId = preferences.getString("userId", "");
//        databaseReference = FirebaseDatabase.getInstance().getReference("HR").child("COMPANY_DETAILS").child(userId);
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                CompanyDetails companyDetails1 = snapshot.getValue(CompanyDetails.class);
//                companyName.setText(companyDetails1.getCompanyName());
//                about.setText(companyDetails1.getAbout());
//                companyPhone.setText(companyDetails1.getPhone());
//                companyWebsite.setText(companyDetails1.getWebsite());
//                companyCity.setText(companyDetails1.getLocation());
//                companyDetails.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        if (preferences.getBoolean("companyDetailsAdded", false)){
            Log.e(TAG, "setValues: "+preferences.getString("companyName", ""));
            companyName.setText(preferences.getString("companyName",""));
            about.setText(preferences.getString("about",""));
            companyCity.setText(preferences.getString("companyLocation",""));
            companyPhone.setText(preferences.getString("companyPhone",""));
            companyWebsite.setText(preferences.getString("companyWebsite",""));
            companyDetails.setVisibility(View.GONE);
            companyInfo.setVisibility(View.GONE);
            companyEditImageView.setVisibility(View.VISIBLE);
            companyName.setVisibility(View.VISIBLE);
            about.setVisibility(View.VISIBLE);
            companyPhone.setVisibility(View.VISIBLE);
            companyWebsite.setVisibility(View.VISIBLE);
            companyCity.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.hr_profile_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hr_app_bar_logout:
                SharedPreferences pref = getActivity().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().finishAfterTransition();
                return true;
            default:
                return false;
        }
    }
}