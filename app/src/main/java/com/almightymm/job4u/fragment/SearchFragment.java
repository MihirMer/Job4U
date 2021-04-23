package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.CategoryAdapter;
import com.almightymm.job4u.Adapter.HRJobAdapter;
import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Category;
import com.almightymm.job4u.model.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends Fragment {
    EditText editText;
    private static final String TAG = "SearchFragment";
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    LinearLayoutManager linearLayoutManager;

    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    JobAdapter jobAdapter;
    HRJobAdapter hrJobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    RelativeLayout lay1, lay2, lay3, noData;

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initPreferences();
        lay1 = view.findViewById(R.id.lay1);
        lay2 = view.findViewById(R.id.lay2);
        lay3 = view.findViewById(R.id.lay3);
noData = view.findViewById(R.id.lay4);
        recyclerView = view.findViewById(R.id.category_filtered);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryArrayList = new ArrayList<>();
        categoryArrayList = getJobCategory();

        if (preferences.getString("role","").equals("HR")){

        categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList,preferences,preferenceEditor,R.id.action_searchFragment_to_jobListFragment22);
        }else{

        categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList,preferences,preferenceEditor,R.id.action_searchFragment_to_jobListFragment);
        }

        recyclerView.setAdapter(categoryAdapter);

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job_filtered);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);

        jobArrayList = new ArrayList<>();
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("HR").child("ADDJOB");
    final String cn = preferences.getString("companyName","");
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        Log.e(TAG, "onDataChange: "+cn );
                        jobArrayList.add(job);

                        Log.e(TAG, "onDataChange: "+job.getName());

                    }
                    if (preferences.getString("role", "").equals("HR")) {
                    jobArrayList = filter1();
                        hrJobAdapter = new HRJobAdapter(getContext(), jobArrayList, preferences, preferenceEditor,"search");
                        jobRecyclerView.setAdapter(hrJobAdapter);
                    } else {

                        jobAdapter = new JobAdapter(getContext(), jobArrayList, preferences, preferenceEditor, R.id.action_jobListFragment_to_jobPreviewFragment);
                        jobRecyclerView.setAdapter(jobAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editText = view.findViewById(R.id.search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return view;
    }
    private ArrayList<Job> filter1() {
        ArrayList<Job> filterList = new ArrayList<>();
        String cn = preferences.getString("companyName", "");
        for (Job job : jobArrayList) {
            if (job.getCompanyName().equals(cn))
                filterList.add(job);
        }
        if (!filterList.isEmpty()){
            noData.setVisibility(View.GONE);
            lay2.setVisibility(View.VISIBLE);
        } else{
            noData.setVisibility(View.VISIBLE);
            lay2.setVisibility(View.GONE);
        }
        return filterList;
    }
    private ArrayList<Category> getJobCategory() {
        ArrayList<Category> jobCategories = new ArrayList<>();
        Category jc = new Category(R.drawable.ic_frontend_developer, "Web Developer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_mobile_app_developer, "Android Developer");

        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_web_designer, "Web Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_ux_interface, "UI/UX Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_graphic_designer, "Graphics Designer");
        jobCategories.add(jc);

        jc = new Category(R.drawable.ic_backend, "Backend Developer");

        jobCategories.add(jc);
        return jobCategories;
    }

    private void filter(String text) {
        ArrayList<Category> filteredList = new ArrayList<>();
        ArrayList<Job> filteredList2 = new ArrayList<>();

        for (Category item : categoryArrayList) {
            if (item.getC_Job_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        for (Job item : jobArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ) {
                filteredList2.add(item);
            }
        }

        categoryAdapter.filterList(filteredList);
        if (preferences.getString("role", "").equals("HR")) {
        hrJobAdapter.filterList(filteredList2);
        } else {
        jobAdapter.filterList(filteredList2);

        }
        if (filteredList.isEmpty()){
            lay1.setVisibility(View.GONE);
        } else {
            lay1.setVisibility(View.VISIBLE);
            lay3.setVisibility(View.GONE);
        }
        if (filteredList2.isEmpty()){
            lay2.setVisibility(View.GONE);
        } else {
            lay2.setVisibility(View.VISIBLE);
            lay3.setVisibility(View.GONE);
        }
        if (filteredList.isEmpty()&&filteredList2.isEmpty()){
            lay1.setVisibility(View.GONE);
            lay2.setVisibility(View.GONE);
            lay3.setVisibility(View.VISIBLE);

        }
    }
    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}