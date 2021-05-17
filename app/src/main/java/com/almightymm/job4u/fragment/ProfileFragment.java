package com.almightymm.job4u.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.almightymm.job4u.SignInActivity;
import com.almightymm.job4u.latex.Document;
import com.almightymm.job4u.latex.LatexCompiler;
import com.almightymm.job4u.latex.PreferenceHelper;
import com.almightymm.job4u.model.EducationDetails;
import com.almightymm.job4u.model.ProjectWork;
import com.almightymm.job4u.model.Skills;
import com.almightymm.job4u.model.WorkExperience;
import com.almightymm.job4u.utils.FilesUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private final LinkedList<Document> documents = new LinkedList<>();
    //    personal details
    TextView name, gmail, phone, city, dob, gender;
    ImageView personalEditImageView;
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
    //    skill
    TagContainerLayout skill;
    ImageView editSkillImageView;
    TextView skillinfo;
    //    database
    DatabaseReference databaseReference;
//    storage
StorageReference storageRef ;

    //    preferences
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    String userId;
    //    generate resume
    String Name;
    String outputPath;
    String imagesPath;
    String filename;
    LatexCompiler latexCompiler;
    StringBuilder texFilecontent;
    //    addjob
    public Permissions currentPermission;
    private Document document;


    public ProfileFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setListeners(view);
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
        personalEditImageView = view.findViewById(R.id.icon2);

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
        editSkillImageView = view.findViewById(R.id.icon1);
        skillinfo = view.findViewById(R.id.txt_0);

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
        add_exp.setVisibility(View.GONE);
        rg1 = view.findViewById(R.id.lyl1_23);

        exp_rec = view.findViewById(R.id.experience_list);
        expLinearLayoutManager = new LinearLayoutManager(getContext());
        expLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        exp_rec.setLayoutManager(expLinearLayoutManager);

//      forms
//        generate resume
        latexCompiler = new LatexCompiler(getContext());
    }

    private void setListeners(final View view) {


        //        personal details

        personalEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_personalDetailsFragment);
            }
        });
        add_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_educationDetailsFragment);
            }
        });
        editSkillImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addSkillFragment);

            }
        });
        add_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_workExperienceFragment);

            }
        });
        add_projectWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_projectWorkFragment);

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
                    educationAdapter = new EducationAdapter(getContext(), edu_list,preferences, preferenceEditor);
                    edu_rec.setAdapter(educationAdapter);
                    info1.setVisibility(View.GONE);
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
                if (add_skill != null) {
                    skill.setTags(add_skill.getSkills());
                    skill.setVisibility(View.VISIBLE);
                    skill.setBackgroundColor(Color.WHITE);
                    skill.setBorderColor(Color.WHITE);
                    skillinfo.setVisibility(View.GONE);
                } else {
                    skillinfo.setVisibility(View.VISIBLE);
                    skill.removeAllTags();
                }

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
                    if (projectWork1 != null) {
                        proj_list.add(projectWork1);
                        projectAdapter = new ProjectAdapter(getContext(), proj_list, preferences, preferenceEditor);
                        proj_rec.setAdapter(projectAdapter);
                        info2.setVisibility(View.GONE);
                    }
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
                    if (workExperience != null) {
                        exp_list.add(workExperience);
                        experienceAdapter = new ExperienceAdapter(getContext(), exp_list,preferences, preferenceEditor);
                        exp_rec.setAdapter(experienceAdapter);
                        add_exp.setVisibility(View.VISIBLE);
                        info3.setVisibility(View.GONE);
                        rg1.setVisibility(View.GONE);
                    } else {
                        freshers.setSelected(true);
                        add_exp.setVisibility(View.GONE);
                        info3.setVisibility(View.VISIBLE);
                        rg1.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_exp.setVisibility(View.VISIBLE);
                info3.setVisibility(View.VISIBLE);
            }
        });
        freshers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info3.setVisibility(View.GONE);
                add_exp.setVisibility(View.GONE);
            }
        });

    }

    public void generateResume() {
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
                FilesUtils.writeFile(document, texFilecontent.toString());
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
                latexCompiler.generatePDF(texFilecontent.toString(), imagesFolder, outputFolder, document, new FileAsyncHttpResponseHandler(getContext()) {
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
                        Toast.makeText(getContext(), "Your file is saved at Document folder\n namely: " + filename + ".pdf", Toast.LENGTH_LONG).show();
                        Header header = null;
                        // Retrieves the content-type header
                        for (Header h : headers) {
                            if (h.getName().equals("Content-Type")) {
                                header = h;
                                break;
                            }
                        }
//                        code to upload file here

                        InputStream stream = null;
                        try {
                            stream = new FileInputStream(file);
                            storageRef = FirebaseStorage.getInstance().getReference().child("Resumes").child(userId);
                            UploadTask uploadTask = storageRef.putStream(stream);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getContext(), "Resume upload failed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(), "Resume upload successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
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
            String ext = headerType.substring(headerType.length() - 3);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }


//option menu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_download_resume:
//                Toast.makeText(getContext(), "Download resume", Toast.LENGTH_SHORT).show();
                if (!preferences.getBoolean("personalDetailsAdded", false)) {
                    Toast.makeText(getContext(), "Please add Personal Details first.", Toast.LENGTH_SHORT).show();
                } else if (!preferences.getBoolean("educationAdded", false)) {
                    Toast.makeText(getContext(), "Please add Educational Details first.", Toast.LENGTH_SHORT).show();
                } else if (!preferences.getBoolean("projectWorkAdded", false)) {
                    Toast.makeText(getContext(), "Please add Project Work first.", Toast.LENGTH_SHORT).show();
                } else if (!preferences.getBoolean("keySkillAdded", false)) {
                    Toast.makeText(getContext(), "Please add Key Skills first.", Toast.LENGTH_SHORT).show();
                } else {
                    generateString();
                    generateResume();

                }
                return true;
            case R.id.app_bar_logout:
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

    private void generateString() {

        Name = preferences.getString("firstName", "") + " " + preferences.getString("lastName", "");
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        String emailAddress = preferences.getString("emailAddress", "");
        String phone = preferences.getString("phone", "");
        phone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        String address = preferences.getString("address", "");
        List<String> skillTags = skill.getTags();


        Log.e(TAG, "generateString: ");
        texFilecontent = new StringBuilder();
        texFilecontent.append("%% start of file `new.tex'.\n" +
                "%% Copyright 2006-2012 Xavier Danaux (xdanaux@gmail.com).\n" +
                "%\n" +
                "% This work may be distributed and/or modified under the\n" +
                "% conditions of the LaTeX Project Public License version 1.3c,\n" +
                "% available at http://www.latex-project.org/lppl/.\n" +
                "\n" +
                "\n" +
                "\\documentclass[11pt,a4paper,roman]{moderncv}   % possible options include font size ('10pt', '11pt' and '12pt'), paper size ('a4paper', 'letterpaper', 'a5paper', 'legalpaper', 'executivepaper' and 'landscape') and font family ('sans' and 'roman')\n" +
                "\n" +
                "% moderncv themes\n" +
                "\\moderncvstyle{classic}                        % style options are 'casual' (default), 'classic', 'oldstyle' and 'banking'\n" +
                "\\moderncvcolor{blue}                          % color options 'blue' (default), 'orange', 'green', 'red', 'purple', 'grey' and 'black'\n" +
                "%\\renewcommand{\\familydefault}{\\rmdefault}    % to set the default font; use '\\sfdefault' for the default sans serif font, '\\rmdefault' for the default roman one, or any tex font name\n" +
                "%\\nopagenumbers{}                             % uncomment to suppress automatic page numbering for CVs longer than one page\n" +
                "\n" +
                "% character encoding\n" +
                "%\\usepackage[utf8]{inputenc}                  % if you are not using xelatex ou lualatex, replace by the encoding you are using\n" +
                "%\\usepackage{CJKutf8}                         % if you need to use CJK to typeset your resume in Chinese, Japanese or Korean\n" +
                "\n" +
                "% adjust the page margins\n" +
                "\\usepackage[scale=0.75]{geometry}\n" +
                "%\\setlength{\\hintscolumnwidth}{3cm}           % if you want to change the width of the column with the dates\n" +
                "%\\setlength{\\makecvtitlenamewidth}{10cm}      % for the 'classic' style, if you want to force the width allocated to your name and avoid line breaks. be careful though, the length is normally calculated to avoid any overlap with your personal info; use this at your own typographical risks...\n" +
                "\n" +
                "% personal data\n" +
                "\\firstname{" + firstName + "}\n" +
                "\\familyname{" + lastName + "}\n");
        if (preferences.getBoolean("experienceAdded", false)) {
            texFilecontent.append("\\title{Curriculum Vitae}               % optional, remove the line if not wanted\n");
        } else {
            texFilecontent.append("\\title{Resume}               % optional, remove the line if not wanted\n");
        }
        texFilecontent.append("\\address{" + address + "}    % optional, remove the line if not wanted\n" +
                "\\mobile{" + phone + "}                     % optional, remove the line if not wanted\n" +
                "\\email{" + emailAddress + "}                          % optional, remove the line if not wanted\n" +
                "\n" +
                "% to show numerical labels in the bibliography (default is to show no labels); only useful if you make citations in your resume\n" +
                "%\\makeatletter\n" +
                "%\\renewcommand*{\\bibliographyitemlabel}{\\@biblabel{\\arabic{enumiv}}}\n" +
                "%\\makeatother\n" +
                "\n" +
                "% bibliography with mutiple entries\n" +
                "%\\usepackage{multibib}\n" +
                "%\\newcites{book,misc}{{Books},{Others}}\n" +
                "%----------------------------------------------------------------------------------\n" +
                "%            content\n" +
                "%----------------------------------------------------------------------------------\n" +
                "\\begin{document}\n" +
                "%\\begin{CJK*}{UTF8}{gbsn}                     % to typeset your resume in Chinese using CJK\n" +
                "%-----       resume       ---------------------------------------------------------\n" +
                "\\makecvtitle\n" +
                "\n" +
                "\\section{Objective}\n" +
                "\\cvlistitem {To be a successful professional in a globally respected company and to continuously upgrade my knowledge and skill as well.}\n" +
                "\\section{Education}\n");
        for (EducationDetails edu : edu_list) {
            texFilecontent.append("\\cventry{" + edu.getAdmissionYear() + "--" + edu.getYear() + "}{" + edu.getDegreeName() + " in " + edu.getStream() + "}{}{GPA: " + edu.getGpa() + ", " + edu.getCollegeName() + "}{}{}{}\n");
//                    "\\cvlistitem {\\textbf{Ph.D. Thesis:} \"\\textit{Title of Ph.D. Thesis},\" under supervision of \\textbf{Prof. SupervisorName} }\n");
        }
        texFilecontent.append(
                "\n" +
                        "\n" +
                        "\n" +
                        "\\section{Project Works}\n" +
                        "\n");
        for (ProjectWork proj : proj_list) {
            texFilecontent.append("\\cventry{" + proj.getStartYear() + "--" + proj.getEndYear() + "}{" + proj.getProjectName() + "}{}{}{}{}{}\n" +
                    "\\cvlistitem {\\textit{" + proj.getDescription() + "}}\n");
        }
        if (preferences.getBoolean("experienceAdded", false)) {
            texFilecontent.append("\\section{Work Experiences}\n");
            for (WorkExperience exp : exp_list) {
                texFilecontent.append(
                        "\n" +
                                "\\cventry{" + exp.getFromYear() + "--" + exp.getToYear() + "}{" + exp.getDesignation() + "}{}{}{}{}\n" +
                                "\\cvlistitem {\\textit{" + exp.getCompanyName() + ", " + exp.getCity() + "}\n" +
                                "}\n");
            }
        }

        texFilecontent.append(
                "\n" +
                        "\\section{Skills}\n" +
                        "\\cvlistitem {\\textbf{Key Skills} \\\\ ");
        texFilecontent.append(skillTags.get(0));
        for (int i = 1; i < skillTags.size(); i++) {
            texFilecontent.append(", " + skillTags.get(i));
        }
        texFilecontent.append("}\n" +
                "\\end{document}\n" +
                "%% end of file `template.tex'.\n");


//        texFilecontent.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\documentclass{article}\n" +
//                "\n" +
//                "\\usepackage[top=0.5in, bottom=0.5in, left=0.5in, right=0.5in]{geometry}\n" +
//                "\\usepackage{enumitem}\n" +
//                "\n" +
//                "\\begin{document}\n" +
//                "\\begin{center}\n" +
//                "\\thispagestyle{empty}\n" +
//                "\\large \\textbf{" + Name + "\\\\}\n" +
//                "\\normalsize " + emailAddress + " $\\mid$ " + phone + " $\\mid$ "+address+" \\\\\n" +
//                "\\hrulefill\n" +
//                "\\end{center}\n" +
//                "\n" +
//                "\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% OBJECTIVE\n" +
//                "% Who you are, what domain, what are you looking for and when?\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\noindent \\textbf{\\underline{OBJECTIVE}} \\\\\n" +
//                "\\noindent Graduate student looking for domain positions starting month and year. \\\\\n" +
//                "\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% SKILLS: Important and relevant to the job you are applying for\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\n" +
//                "\\noindent \\textbf{\\underline{CORE SKILLS}} \\\\\n");
//        texFilecontent.append(skillTags.get(0));
//        for (int i = 1; i < skillTags.size(); i++) {
//            texFilecontent.append(", " + skillTags.get(i));
//        }
//        texFilecontent.append(" \\\\\n");
//        texFilecontent.append("% Skill 1 (level of expertise), Skill 2 (level of expertise), Skill 3 (level of expertise) \\\\\n" +
//                "\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% EDUCATION\n" +
//                "% University name, degree, year of graduation, GPA (optional)\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\noindent \\textbf{\\underline{EDUCATION}} \\\\\n");
//
//        for (EducationDetails edu : edu_list) {
//            texFilecontent.append("\\textbf{" + edu.getCollegeName() + "}  \\\\\n" +
//                    "\\textit{" + edu.getDegreeName() + " in " + edu.getStream() + "} \\hfill GPA: " + edu.getGpa() + " \\hfill " + edu.getYear() + " \\\\ \\\\\n");
//        }
//        texFilecontent.append("\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% WORK EXPERIENCE\n" +
//                "% What did you do? -> Project goals OR what problem did you solve?\n" +
//                "% How did you do it? -> Skills and technologies\n" +
//                "% What impact did you create? -> Numbers and percentages.\n" +
//                "% Example: \n" +
//                "% + Developed an app for matching mentor and mentees for Android and iOS platform.\n" +
//                "% + Successfully matched 85% of the applications and randomized the rest.\n" +
//                "% \n" +
//                "% Talk about team work, initiative, soft skills.\n" +
//                "%\n" +
//                "% Can also include personal projects, competitions, contribution to Open source.\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\noindent \\textbf{\\underline{WORK EXPERIENCE}} \\\\");
//        for (WorkExperience exp: exp_list) {
//            texFilecontent.append("\\noindent \\textbf{"+exp.getCompanyName()+"} \\hfill "+exp.getCity()+" \\\\\n" +
//                    "\\textit{"+exp.getDesignation()+"} \\hfill "+exp.getFromYear()+" $-$ "+exp.getToYear()+" \\\\\n" +
//                    "\n");
//        }
//        texFilecontent.append(
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% PROJECT\n" +
//                "% What did you do?\n" +
//                "% How did you do it? -> Skills and technologies\n" +
//                "% What impact did you create? -> Numbers and percentages.\n" +
//                "%\n" +
//                "% Talk about team work, initiative, soft skills.\n" +
//                "%\n" +
//                "% Can also include personal projects, competitions, contribution to Open source.\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\noindent \\textbf{\\underline{PROJECT WORK}} \\\\\n" );
//        for(ProjectWork proj: proj_list) {
//            texFilecontent.append(
//                    "\\noindent \\textbf{"+proj.getProjectName()+"} \\hfill  "+proj.getStartYear()+" $-$ "+proj.getEndYear()+"\n" +
//                            "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
//                            "\\item {"+proj.getDescription()+" \\\\}\n" +
//                            "\\end{itemize}\n");
//        }
//        texFilecontent.append(
//                "\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% Extra Curricular Activities, Leadership, etc \n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\noindent \\textbf{\\underline{EXTRA SECTION}} \\\\\n" +
//                "\\noindent \\textbf{Activity/ Role} \\hfill Month, Year $-$ Month, Year\n" +
//                "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
//                "\\item {What did you do, how did you do it and what did you achieve? \\\\}\n" +
//                "\\end{itemize}\n" +
//                "\n" +
//                "\\noindent \\textbf{Activity/ Role} \\hfill Month, Year $-$ Month, Year\n" +
//                "\\begin{itemize}[noitemsep,nolistsep,leftmargin=*]\n" +
//                "\\item {What did you do, how did you do it and what did you achieve? \\\\}\n" +
//                "\\end{itemize}\n" +
//                "\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "% Other Skills: you can add all your other skills here.\n" +
//                "% Continue to keep only relevant skills\n" +
//                "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
//                "\\noindent \\textbf{\\underline{OTHER SKILLS}} \\\\\n" +
//                "\\noindent \\textbf{Skill Group 1:} Skill 1, Skill 2, Skill 3 \\\\\n" +
//                "\\noindent \\textbf{Skill Group 2: } Skill 1, Skill 2, Skill 3, Skill 4\n" +
//                "\n" +
//                "\n" +
//                "\\end{document}\n");


    }

    public enum Permissions {
        SAVE, OPEN
    }
}