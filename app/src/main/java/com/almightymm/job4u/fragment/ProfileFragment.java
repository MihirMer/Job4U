package com.almightymm.job4u.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.almightymm.job4u.R;
public class ProfileFragment extends Fragment {

    private Button button, educationButton, addSkillButton, workExp,projectWork, companyDetails;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setListeners(view);
        return view;
    }
    private void initViews(View view) {
        button = view.findViewById(R.id.btn);
        educationButton = view.findViewById(R.id.educational_details);
        addSkillButton = view.findViewById(R.id.addskill);
        workExp = view.findViewById(R.id.worl_experience);
        projectWork = view.findViewById(R.id.project_work);
        companyDetails = view.findViewById(R.id.company_details);
    }

    private void setListeners(final View view) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_personalDetailsFragment);
            }
        });
        educationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_educationDetailsFragment);

            }
        });
        addSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addSkillFragment);

            }
        });
        workExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_workExperienceFragment);

            }
        });
        projectWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_projectWorkFragment);

            }
        });
        companyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_companyDetailsFragment);

            }
        });
    }
}