package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almightymm.job4u.Adapter.HRJobAdapter;
import com.almightymm.job4u.Adapter.HRResponsesAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.latex.PreferenceHelper;
import com.almightymm.job4u.model.Candidate;
import com.almightymm.job4u.model.Job;
import com.almightymm.job4u.utils.FilesUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.almightymm.job4u.fragment.ProfileFragment.DEFAULT_IMAGES_FOLDER;
import static com.almightymm.job4u.fragment.ProfileFragment.DEFAULT_OUTPUT_FOLDER;
import static com.almightymm.job4u.fragment.ProfileFragment.IMAGES_FOLDER;
import static com.almightymm.job4u.fragment.ProfileFragment.OUTPUT_FOLDER;

public class HrViewResponsesFragment extends Fragment {
    RecyclerView jobRecyclerView;
    ArrayList<Candidate> candidateArrayList;
    HRResponsesAdapter hrJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    DatabaseReference firebaseDatabase;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    String userId;
    String jobId;
    private static final String TAG = "HrViewResponsesFragment";
    public HrViewResponsesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hr_view_responses, container, false);

        initPreferences();

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);
         candidateArrayList= new ArrayList<>();
        jobId = preferences.getString("jobId", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("HR").child("ADDJOB").child(jobId).child("CANDIDATES");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Candidate candidate = dataSnapshot.getValue(Candidate.class);
                        candidateArrayList.add(candidate);
                    }

                    hrJobAdapter = new HRResponsesAdapter(getContext(),getActivity(), candidateArrayList,preferences,preferenceEditor);
                    jobRecyclerView.setAdapter(hrJobAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;

    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
        String outputPath = DEFAULT_OUTPUT_FOLDER;
        String imagesPath = DEFAULT_IMAGES_FOLDER;

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
}