package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.EducationAdapter;
import com.almightymm.job4u.Adapter.ExperienceAdapter;
import com.almightymm.job4u.Adapter.ProjectAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.EducationDetails;
import com.almightymm.job4u.model.PersonalDetails;
import com.almightymm.job4u.model.ProjectWork;
import com.almightymm.job4u.model.Skills;
import com.almightymm.job4u.model.WorkExperience;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    TextView name;
    TextView gmail, phone, city, dob, gender;

    //    edu
    TextView info1, college, streem, degree, cgpa, gyear_txt, gyear, achievements;
    RecyclerView edu_rec;
    ArrayList<EducationDetails> edu_list;
    EducationAdapter educationAdapter;
    LinearLayoutManager eduLinearLayoutManager;

    //    project
    TextView info2, project_name, project_description, start_year, end_year;
    RecyclerView proj_rec;
    ArrayList<ProjectWork> proj_list;
    ProjectAdapter projectAdapter;
    LinearLayoutManager projLinearLayoutManager;

//experience
    TextView info3, Exprience, duration;
    RadioButton freshers, experience;
    RadioGroup rg1;
    RecyclerView exp_rec;
    ArrayList<WorkExperience> exp_list;
    ExperienceAdapter experienceAdapter;
    LinearLayoutManager expLinearLayoutManager;


    Button add_education, add_projectWork, add_exp;
    LinearLayout l1, l2;
    TagContainerLayout skill;
    DatabaseReference databaseReference;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;
    private Button button, educationButton, addSkillButton, workExp, projectWork, companyDetails;

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
        initPreferences();
        userId = preferences.getString("userId", "");
        setValues(view);
        return view;
    }

    private void setValues(View view) {

//        personal details
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PersonalDetails personaldetails = dataSnapshot.getValue(PersonalDetails.class);
                name.setText(personaldetails.getFirstName() + " " + personaldetails.getLastName());
                gmail.setText(personaldetails.getEmailAddress());
                phone.setText(personaldetails.getPhone());
                city.setText(personaldetails.getAddress());
                dob.setText(personaldetails.getDOB());
                gender.setText(personaldetails.getGender());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        education details

        edu_list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("EDUCATION_DETAILS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edu_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EducationDetails details = dataSnapshot.getValue(EducationDetails.class);
                    Log.e(TAG, "onDataChange: " + details.getCollegeName());
                    edu_list.add(details);
                    educationAdapter = new EducationAdapter(getContext(), edu_list);
                    edu_rec.setAdapter(educationAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                EducationDetails educationn = dataSnapshot.getValue(EducationDetails.class);
//                college.setText(educationn.getCollegeName());
//                streem.setText(educationn.getStream());
//                degree.setText(educationn.getDegreeName() + " in ");
//                gyear.setText("Graduation Year" + " : " + educationn.getYear());
//                cgpa.setText("CGPA" + " : " + educationn.getGpa());
//                achievements.setText("Achievements" + " : " + educationn.getOtherAchievements());
//
//                info1.setVisibility(View.GONE);
//                college.setVisibility(View.VISIBLE);
//                l1.setVisibility(View.VISIBLE);
//                gyear.setVisibility(View.VISIBLE);
//                cgpa.setVisibility(View.VISIBLE);
//                achievements.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        //For Skills Display
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("ADDSKILLS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Skills add_skill = dataSnapshot.getValue(Skills.class);
                skill.setTags(add_skill.getSkills());
                skill.setVisibility(View.VISIBLE);
                skill.setBackgroundColor(Color.WHITE);
                skill.setBorderColor(Color.WHITE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//project work
        proj_list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("PROJECT_WORK");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proj_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProjectWork projectWork1 = dataSnapshot.getValue(ProjectWork.class);
                    proj_list.add(projectWork1);
                    projectAdapter = new ProjectAdapter(getContext(), proj_list);
                    proj_rec.setAdapter(projectAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ProjectWork projectwork = dataSnapshot.getValue(ProjectWork.class);
//                project_name.setText(projectwork.getProjectName());
//                project_description.setText(projectwork.getDescription());
//                start_year.setText("Duration " + " : " + projectwork.getStartYear() + " " + "to" + " " + projectwork.getEndYear());
//
//
//                info2.setVisibility(View.GONE);
//                project_name.setVisibility(View.VISIBLE);
//                project_description.setVisibility(View.VISIBLE);
//                start_year.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        exp_list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("WORK_EXPERIENCE");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exp_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WorkExperience workExperience = dataSnapshot.getValue(WorkExperience.class);
                    exp_list.add(workExperience);
                    experienceAdapter = new ExperienceAdapter(getContext(), exp_list);
                    exp_rec.setAdapter(experienceAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        experience.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add_exp.setVisibility(View.VISIBLE);
//                info3.setVisibility(View.VISIBLE);
//            }
//        });
//        freshers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                info3.setVisibility(View.GONE);
//                add_exp.setVisibility(View.GONE);
//            }
//        });
//
//        add_exp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("WORK_EXPERIENCE").child("-MXrk11ijXzpmt53Ed-0");
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        WorkExperience workexperience = dataSnapshot.getValue(WorkExperience.class);
//                        Exprience.setText("I have " + workexperience.getYearsOfExperience() + " year experience as a " + workexperience.getDesignation() + " in " +
//                                workexperience.getCompanyName() + " which is located in " + workexperience.getCity());
//                        duration.setText("Duration : " + workexperience.getFromYear() + " to " + workexperience.getToYear());
//
//                        info3.setVisibility(View.GONE);
//                        Exprience.setVisibility(View.VISIBLE);
//                        duration.setVisibility(View.VISIBLE);
//                        add_exp.setVisibility(View.GONE);
//                        rg1.setVisibility(View.GONE);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
    }

    private void initViews(View view) {
//        personal details
        name = view.findViewById(R.id.name);
        gmail = view.findViewById(R.id.txt_my_mail);
        phone = view.findViewById(R.id.txt_my_phone);
        city = view.findViewById(R.id.txt_my_city);
        dob = view.findViewById(R.id.txt_my_dob);
        gender = view.findViewById(R.id.txt_my_gender);

        //For Education Display
        college = view.findViewById(R.id.txt_my_college);
        streem = view.findViewById(R.id.txt_my_streem);
        degree = view.findViewById(R.id.txt_my_degree);
        gyear = view.findViewById(R.id.txt_my_gyear);
        cgpa = view.findViewById(R.id.txt_my_cgpa);
        achievements = view.findViewById(R.id.txt_my_achievements);
        info1 = view.findViewById(R.id.txt_1);
        l1 = view.findViewById(R.id.lyl1_21);
        add_education = view.findViewById(R.id.btn_addeducation);

        edu_rec = view.findViewById(R.id.education_list);
        eduLinearLayoutManager = new LinearLayoutManager(getContext());
        eduLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        edu_rec.setLayoutManager(eduLinearLayoutManager);

//        skill details
        skill = view.findViewById(R.id.tag_skill);

        // Project Work Display
        project_name = view.findViewById(R.id.txt_my_project_name);
        project_description = view.findViewById(R.id.txt_my_project_description);
        start_year = view.findViewById(R.id.txt_my_project_start_year);
        end_year = view.findViewById(R.id.txt_my_project_end_year);
        l2 = view.findViewById(R.id.lyl1_22);
        info2 = view.findViewById(R.id.txt_2);
        add_projectWork = view.findViewById(R.id.btn_add_projectWork);

        proj_rec = view.findViewById(R.id.project_list);
        projLinearLayoutManager = new LinearLayoutManager(getContext());
        projLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        proj_rec.setLayoutManager(projLinearLayoutManager);


        //For Experience display

        info3 = view.findViewById(R.id.txt_3);
        Exprience = view.findViewById(R.id.txt_my_experience);
        duration = view.findViewById(R.id.txt_my_duration);
        freshers = view.findViewById(R.id.rbtn_freshers);
        experience = view.findViewById(R.id.rbtn_experience);
        add_exp = view.findViewById(R.id.btn_add_workExperience);
        rg1 = view.findViewById(R.id.lyl1_23);

        exp_rec = view.findViewById(R.id.experience_list);
        expLinearLayoutManager = new LinearLayoutManager(getContext());
        expLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        exp_rec.setLayoutManager(expLinearLayoutManager);

//      forms
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

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }

}