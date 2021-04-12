package com.almightymm.job4u.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.EducationAdapter;
import com.almightymm.job4u.Adapter.ExperienceAdapter;
import com.almightymm.job4u.Adapter.ProjectAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.latex.Document;
import com.almightymm.job4u.latex.LatexCompiler;
import com.almightymm.job4u.latex.PreferenceHelper;
import com.almightymm.job4u.model.EducationDetails;
import com.almightymm.job4u.model.PersonalDetails;
import com.almightymm.job4u.model.ProjectWork;
import com.almightymm.job4u.model.Skills;
import com.almightymm.job4u.model.WorkExperience;
import com.almightymm.job4u.utils.FilesUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import co.lujun.androidtagview.TagContainerLayout;
import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
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
    //    generate resume
    Button generateResume;
    String Name = "Mihir Mer";
    String email = "mihirmer@gmail.com";
    String outputPath;
    String imagesPath;
    String filename;
    LatexCompiler latexCompiler;
    String texFilecontent = "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\documentclass{article}\n" +
            "\n" +
            "\\usepackage[top=0.5in, bottom=0.5in, left=0.5in, right=0.5in]{geometry}\n" +
            "\\usepackage{enumitem}\n" +
            "\n" +
            "\\begin{document}\n" +
            "\\begin{center}\n" +
            "\\thispagestyle{empty}\n" +
            "\\large \\textbf{" + Name + " \\\\}\n" +
            "\\normalsize " + email + " $\\mid$ 000-000-0000 $\\mid$ www.mywebsite.com    \\\\\n" +
            "\\hrulefill\n" +
            "\\end{center}\n" +
            "\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% OBJECTIVE\n" +
            "% Who you are, what domain, what are you looking for and when?\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\noindent \\textbf{\\underline{OBJECTIVE}} \\\\\n" +
            "\\noindent Graduate student looking for domain positions starting month and year. \\\\\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% SKILLS: Important and relevant to the job you are applying for\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\n" +
            "\\noindent \\textbf{\\underline{CORE SKILLS}} \\\\\n" +
            "Skill 1 (years of experience), Skill 2 (years of experience), Skill 3 (years of experience) \\\\\n" +
            "% Skill 1 (level of expertise), Skill 2 (level of expertise), Skill 3 (level of expertise) \\\\\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% EDUCATION\n" +
            "% University name, degree, year of graduation, GPA (optional)\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\noindent \\textbf{\\underline{EDUCATION}} \\\\\n" +
            "\\textbf{University name} \\hfill City, State \\\\\n" +
            "\\textit{Degree name + Specialization} \\hfill GPA: x.x/x.x \\hfill month-year \\\\ \\\\\n" +
            "\\textbf{University name} \\hfill City, State \\\\\n" +
            "\\textit{Degree name + Specialization} \\hfill GPA: x.x/x.x \\hfill month-year \\\\\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% WORK EXPERIENCE\n" +
            "% What did you do? -> Project goals OR what problem did you solve?\n" +
            "% How did you do it? -> Skills and technologies\n" +
            "% What impact did you create? -> Numbers and percentages.\n" +
            "% Example: \n" +
            "% + Developed an app for matching mentor and mentees for Android and iOS platform.\n" +
            "% + Successfully matched 85% of the applications and randomized the rest.\n" +
            "% \n" +
            "% Talk about team work, initiative, soft skills.\n" +
            "%\n" +
            "% Can also include personal projects, competitions, contribution to Open source.\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\noindent \\textbf{\\underline{WORK EXPERIENCE}} \\\\\n" +
            "\\noindent \\textbf{Company name} \\hfill City Name, State \\\\\n" +
            "\\textit{Role name, Deparment Name} \\hfill Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {Developed XYZ using XYZ that led to X\\% improvement.}\n" +
            "\\item {Led an initiative XYZ to identify the root cause.}\n" +
            "\\item {Collaborated with XYZ team to work on XYZ feature. \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "\\noindent \\textbf{Company name} \\hfill City Name, State \\\\\n" +
            "\\textit{Role name} \\hfill Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {Developed XYZ using XYZ that led to X\\% improvement.}\n" +
            "\\item {... \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "\\noindent \\textbf{Competition Name} \\hfill City Name, State \\\\\n" +
            "\\textit{Role name, Team name} \\hfill Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {Developed XYZ using XYZ that led to X\\% improvement.}\n" +
            "\\item{Came in the top 10 OR received the most innovative award.}\n" +
            "\\item {... \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% PROJECT\n" +
            "% What did you do?\n" +
            "% How did you do it? -> Skills and technologies\n" +
            "% What impact did you create? -> Numbers and percentages.\n" +
            "%\n" +
            "% Talk about team work, initiative, soft skills.\n" +
            "%\n" +
            "% Can also include personal projects, competitions, contribution to Open source.\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\noindent \\textbf{\\underline{PROJECT WORK}} \\\\\n" +
            "\\noindent \\textbf{Project Name} \\textit{Course Name} \\hfill  Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {Developed XYZ using XYZ that led to X\\% improvement.}\n" +
            "\\item {Led an initiative XYZ to identify the root cause.}\n" +
            "\\item {Collaborated with XYZ team to work on XYZ feature. \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "\\noindent \\textbf{Project Name} \\textit{Course Name} \\hfill  Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {Developed XYZ using XYZ that led to X\\% improvement.}\n" +
            "\\item {Led an initiative XYZ to identify the root cause.}\n" +
            "\\item {Collaborated with XYZ team to work on XYZ feature. \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "\\noindent \\textbf{Project Name} \\textit{Course Name} \\hfill  Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {Developed XYZ using XYZ that led to X\\% improvement.}\n" +
            "\\item {... \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% Extra Curricular Activities, Leadership, etc \n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\noindent \\textbf{\\underline{EXTRA SECTION}} \\\\\n" +
            "\\noindent \\textbf{Activity/ Role} \\hfill Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {What did you do, how did you do it and what did you achieve? \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "\\noindent \\textbf{Activity/ Role} \\hfill Month, Year $-$ Month, Year\n" +
            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
            "\\item {What did you do, how did you do it and what did you achieve? \\\\}\n" +
            "\\end{itemize}\n" +
            "\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "% Other Skills: you can add all your other skills here.\n" +
            "% Continue to keep only relevant skills\n" +
            "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
            "\\noindent \\textbf{\\underline{OTHER SKILLS}} \\\\\n" +
            "\\noindent \\textbf{Skill Group 1:} Skill 1, Skill 2, Skill 3 \\\\\n" +
            "\\noindent \\textbf{Skill Group 2: } Skill 1, Skill 2, Skill 3, Skill 4\n" +
            "\n" +
            "\n" +
            "\\end{document}\n" +
            "\n";
    private Button button, educationButton, addSkillButton, workExp, projectWork, companyDetails;
    private Permissions currentPermission;
    private Document document;
    private LinkedList<Document> documents = new LinkedList<>();

//    addjob
    Button addJobButton;


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
        initPreferences();
        userId = preferences.getString("userId", "");
        setValues(view);
        return view;
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

//        generate resume
        generateResume = view.findViewById(R.id.gen_resume);
        latexCompiler = new LatexCompiler(getContext());

//        add job
        addJobButton = view.findViewById(R.id.add_job_button);

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

        generateResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateResume(view);
            }
        });

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addJobFragment);

            }
        });
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();

//        latex

        outputPath = DEFAULT_OUTPUT_FOLDER;
        imagesPath = DEFAULT_IMAGES_FOLDER;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        String currentOutputFolder = prefs.getString(OUTPUT_FOLDER, null);
        if (currentOutputFolder == null || currentOutputFolder.equals("")) {
            prefsEdit.putString(OUTPUT_FOLDER, outputPath);
        }
        String currentImageFolder = prefs.getString(IMAGES_FOLDER, null);
        if (currentImageFolder == null || currentImageFolder.equals("")) {
            prefsEdit.putString(IMAGES_FOLDER, imagesPath);
        }
        prefsEdit.apply();
        // Creates the folders if they not exists
        final String imagesFolderPath = PreferenceHelper.getImageFolder(getContext());
        final String outputFolderPath = PreferenceHelper.getOutputFolder(getContext());
        Log.e(TAG, "initPreferences: i'm here");
        File imagesFolder = new File(imagesFolderPath);
        if (!imagesFolder.exists()) {
            FilesUtils.newDirectory(imagesFolderPath);
            Log.e(TAG, "initPreferences: image folder created");
        }
        final File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            FilesUtils.newDirectory(outputFolderPath);
            Log.e(TAG, "initPreferences: output folder created");
        }

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

    public void generateResume(View view) {
        onNewFileClick();
        saveFile();
        generatePDF();
        removeDocument(document.getPath());
    }

    /**
     * On new file click, it opens an empty file in the editor
     */
    public void onNewFileClick() {
        document = new Document(FilesUtils.newUntitledFile());
    }

    /**
     * Routine to save the current document
     *
     * @return True if the file existed before this method call.
     */
    private boolean saveFile() {
        boolean exists = false;
        if (checkStoragePermissions(Permissions.SAVE)) {
            if (!document.exists()) {
                filename = "Resume of " + Name;
                filename = filename.replaceAll(" ", "_");
                renameFile(document.getPath(), filename);
            } else {
                FilesUtils.writeFile(document, texFilecontent);
                exists = true;
                document.setSaved(true);
            }
        }
        return exists;
    }

    private boolean checkStoragePermissions(Permissions type) {
        currentPermission = type;
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    /**
     * Renames a document
     *
     * @param oldPath     Old path
     * @param newFilename New filename
     */
    private void renameFile(String oldPath, String newFilename) {
        String name = ensureTexExtension(newFilename);
        Document oldDocument = new Document(oldPath);
        Document newDocument = new Document(FilesUtils.saveFileRenaming(oldPath, name));
        // Remove the old document from the dataset and insert the new one in the same position
        if (documents.contains(oldDocument)) {
            int oldDocumentIndex = documents.indexOf(oldDocument);
            documents.remove(oldDocument);
            documents.add(oldDocumentIndex, newDocument);
            FilesUtils.deleteFile(oldDocument);
        } else {
            documents.add(newDocument);
        }
        if (document.getPath().equals(oldDocument.getPath())) {
            document = newDocument;
        }
    }

    /**
     * Returns the filename having a .tex suffix.
     *
     * @param name Filename
     * @return .tex filename
     */
    private String ensureTexExtension(String name) {
        if (!name.endsWith(".tex")) {
            int lastIndex;
            if (name.contains(".")) {
                lastIndex = name.lastIndexOf(".");
            } else {
                lastIndex = name.length();
            }
            name = name.substring(0, lastIndex) + ".tex";
        }
        return name;
    }

    /**
     * Routine that searches for the images used in the current document, zips them with the file
     * and sends it to the server.
     * It will show the response pdf or log.
     */
    private void generatePDF() {
        boolean fileNeedsToBeSaved = !saveFile();
        if (!fileNeedsToBeSaved) {
            if (!texFilecontent.equals("")) {
                final File imagesFolder = new File(imagesPath);
                final File outputFolder = new File(outputPath);
                final ProgressDialog asyncDialog = new ProgressDialog(getContext());
                asyncDialog.setMessage("Compressing and sending files...");
                asyncDialog.show();
                Log.e(TAG, "generatePDF: " + imagesPath + "\n" + outputPath);
                latexCompiler.generatePDF(texFilecontent, imagesFolder, outputFolder, document, new FileAsyncHttpResponseHandler(getContext()) {
                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                        asyncDialog.dismiss();
                        // On failure shows an error toast
                        Toast.makeText(getContext(), "Server Error.",
                                Toast.LENGTH_LONG).show();
                        Log.e("LATEX_NET", throwable.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                        asyncDialog.dismiss();
                        Toast.makeText(getContext(), "Your file is saved at Document folder\n namely: " + filename+".pdf", Toast.LENGTH_LONG).show();
                        Header header = null;
                        // Retrieves the content-type header
                        for (Header h : headers) {
                            if (h.getName().equals("Content-Type")) {
                                header = h;
                                break;
                            }
                        }
                        openResultingFile(header, file);
                    }
                });
                asyncDialog.setMessage("Waiting for the server to compile...");
            } else {
                // Empty file
                Toast.makeText(getContext(), "Can't compile an empty file!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openResultingFile(Header header, File file) {
        final String outputFolderPath = PreferenceHelper.getOutputFolder(getContext());
        final String headerType = header.getValue();
        // If it's a PDF, the compile succeeded
        if (headerType.equals("application/pdf") || headerType.equals("application/x-dvi")) {
            String ext = headerType.substring(headerType.length() - 3, headerType.length());
            // Saves the file in the output directory and tries to open it
            byte[] bytes = FilesUtils.readBinaryFile(file);
            String pdfName = document.getName().substring(0, document.getName().lastIndexOf(".") + 1) + ext;
            File pdf = new File(outputFolderPath, pdfName);
            FilesUtils.writeBinaryFile(pdf, bytes);
            Intent pdfIntent = new Intent();
            pdfIntent.setAction(Intent.ACTION_VIEW);

            pdfIntent.setDataAndType(Uri.fromFile(pdf), headerType);

            Uri apkURI = FileProvider.getUriForFile(
                    getContext(),
                    this.getContext()
                            .getPackageName() + ".provider", pdf);
            pdfIntent.setDataAndType(apkURI, headerType);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (pdfIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(pdfIntent);
            } else {
                Toast.makeText(getContext(), "You don't have any " + ext + " reader!",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            final Document receivedDocument = new Document(file);
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeDocument(String path) {
        Document document = new Document(path);
        documents.remove(document);
        FilesUtils.deleteFile(document);
    }

    private enum Permissions {
        SAVE, OPEN
    }
}