package com.almightymm.job4u.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.CategoryAdapter;
import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.model.Category;
import com.almightymm.job4u.model.Job;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    EditText editText;

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    LinearLayoutManager linearLayoutManager;

    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    JobAdapter jobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    RelativeLayout lay1, lay2, lay3;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        lay1 = view.findViewById(R.id.lay1);
        lay2 = view.findViewById(R.id.lay2);
        lay3 = view.findViewById(R.id.lay3);

        recyclerView = view.findViewById(R.id.category_filtered);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryArrayList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList);
        recyclerView.setAdapter(categoryAdapter);

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job_filtered);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);

        jobArrayList = new ArrayList<>();

        jobRecyclerView.setAdapter(jobAdapter);

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

    private void filter(String text) {
        ArrayList<Category> filteredList = new ArrayList<>();
        ArrayList<Job> filteredList2 = new ArrayList<>();

        for (Category item : categoryArrayList) {
            if (item.getC_Job_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        for (Job item : jobArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList2.add(item);
            }
        }

        categoryAdapter.filterList(filteredList);
        jobAdapter.filterList(filteredList2);

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
}