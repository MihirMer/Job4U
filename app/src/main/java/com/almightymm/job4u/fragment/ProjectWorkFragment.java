package com.almightymm.job4u.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.almightymm.job4u.R;
import com.almightymm.job4u.model.ProjectWork;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ProjectWorkFragment extends Fragment {
    EditText projectName, description, start_year, end_year;
    Button add_projectWork;
    DatabaseReference db_add_projectWork;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    public ProjectWorkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project_work, container, false);
        initViews(view);
        initPreferences();
        String userId = preferences.getString("userId", "");

        db_add_projectWork = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("PROJECT_WORK");
        addListeners(view);

        return view;
    }

    private void addListeners(View view) {
        add_projectWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_projectwork();
            }
        });
        start_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_start_year(v);
                hideKeyboard(getActivity());
            }
        });
        end_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_end_year(v);
                hideKeyboard(getActivity());
            }
        });
    }

    public void txt_start_year(View view) {

        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                start_year.setText(String.valueOf(selectedYear));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY).setMinYear(1990).
                setActivatedYear(today.get(Calendar.YEAR)).setMaxYear(2021)
                .setTitle("Select Year").showYearOnly()
                .build().show();
    }

    public void txt_end_year(View view) {

        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                end_year.setText(String.valueOf(selectedYear));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY).setMinYear(1990).
                setActivatedYear(today.get(Calendar.YEAR)).setMaxYear(2021)
                .setTitle("Select Year").showYearOnly()
                .build().show();
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


    private void add_projectwork() {
        String pn = projectName.getText().toString().trim();
        String desc = description.getText().toString().trim();
//        String lan = language.getText().toString().trim();
        String syear = start_year.getText().toString().trim();
        String eyear = end_year.getText().toString().trim();

        if (pn.isEmpty()) {
            projectName.requestFocus();
            Toast.makeText(getContext(), "Project name is required !", Toast.LENGTH_SHORT).show();
        } else if (desc.isEmpty()) {
            description.requestFocus();
            Toast.makeText(getContext(), "Description is required !", Toast.LENGTH_SHORT).show();
        } else if (syear.isEmpty()) {
            start_year.requestFocus();
            Toast.makeText(getContext(), "Start year is required !", Toast.LENGTH_SHORT).show();
        } else if (eyear.isEmpty()) {
            end_year.requestFocus();
            Toast.makeText(getContext(), "End year is required !", Toast.LENGTH_SHORT).show();
        } else {

            String id = db_add_projectWork.push().getKey();
            ProjectWork projectwork = new ProjectWork(id, pn, desc, syear, eyear);
            db_add_projectWork.child(id).setValue(projectwork).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Details Save", Toast.LENGTH_LONG).show();
                    clear();
                }
            });
        }
    }

    private void clear() {
        getActivity().onBackPressed();
    }

    private void initViews(View view) {

        projectName = (EditText) view.findViewById(R.id.txt_project);
        description = (EditText) view.findViewById(R.id.txt_desc);
//        language = (EditText) view.findViewById(R.id.txt_language);
        start_year = (EditText) view.findViewById(R.id.txt_start_year);
        end_year = (EditText) view.findViewById(R.id.txt_end_year);
        add_projectWork = (Button) view.findViewById(R.id.btn_add_projectWork);

    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}