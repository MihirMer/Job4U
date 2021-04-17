package com.almightymm.job4u.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Skills;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

import static android.content.Context.MODE_PRIVATE;

public class AddSkillFragment extends Fragment {
    private static final String TAG = "AddSkillFragment";
    TagContainerLayout tagContainerLayout;
    String[] skill = {"PHP", "Python", ".NET", "PL/SQL", "C#", "HTML", "CSS",
            "JavaScript", "SQL", "Android", "C", "JAVA", "WordPress", "Jquery", "Coraldraw", "PhotoShop"};
    Button add, addall;
    AutoCompleteTextView addskills;
    DatabaseReference db_add_skill;
    DatabaseReference personalDetails;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    public AddSkillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_skill, container, false);
        initViews(view);
        initPreferences();
        String userId = preferences.getString("userId", "");
        db_add_skill = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("ADDSKILLS");
        personalDetails = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("keySkillAdded");
        setValues(view);
        setListeners(view);

        return view;
    }

    private void setValues(View view) {
        db_add_skill.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Skills skills = dataSnapshot.getValue(Skills.class);
                if (skills != null) {
                    tagContainerLayout.setTags(skills.getSkills());
                }
            }
        });
    }

    private void setListeners(View view) {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addskill();
                clear();
                addskills.requestFocus();
            }
        });
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                tagContainerLayout.removeTag(0);
            }
        });


        addall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addskills();
            }
        });
    }

    private void initViews(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, skill);
        addskills = view.findViewById(R.id.txt_addskill);
        addskills.setThreshold(1);
        addskills.setAdapter(adapter);
        add = view.findViewById(R.id.btn_add);
        addall = view.findViewById(R.id.btn_addskill);
        tagContainerLayout = view.findViewById(R.id.skill);
    }

    public void addskill() {
        tagContainerLayout.addTag(addskills.getText().toString());
    }

    public void clear() {
        addskills.setText("");
    }

    public void addskills() {
        final Skills add_skill = new Skills((ArrayList<String>) tagContainerLayout.getTags());
        db_add_skill.setValue(add_skill).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideKeyboard(getActivity());
                if(add_skill.getSkills().isEmpty()) {
//                    Toast.makeText(getContext(), "empty", Toast.LENGTH_SHORT).show();
                    personalDetails.setValue(false);
                    preferenceEditor.putBoolean("keySkillAdded", false);
                } else {
//                    Toast.makeText(getContext(), "not empty", Toast.LENGTH_SHORT).show();
                    personalDetails.setValue(true);
                    preferenceEditor.putBoolean("keySkillAdded", true);
                }
                preferenceEditor.apply();
                preferenceEditor.commit();
                Toast.makeText(getContext(), "Skills added", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }

}