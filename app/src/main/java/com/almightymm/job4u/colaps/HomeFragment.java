package com.almightymm.job4u.colaps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almightymm.job4u.Adapter.CategoryAdapter;
import com.almightymm.job4u.Adapter.JobAdapter;
import com.almightymm.job4u.R;
import com.almightymm.job4u.SignInActivity;
import com.almightymm.job4u.model.Category;
import com.almightymm.job4u.model.Job;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment {

    private static final String ARG_ID = "arg_id";

//  recycler

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categoryArrayList;
    LinearLayoutManager linearLayoutManager;

    RecyclerView jobRecyclerView;
    ArrayList<Job> jobArrayList;
    JobAdapter jobAdapter;
    LinearLayoutManager jobLinearLayoutManager;

    public static HomeFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID, id);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.category);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category(1, "Front-end Developer"));
        categoryArrayList.add(new Category(2, "Backend Developer"));
        categoryArrayList.add(new Category(3, "Full-stack Developer"));
        categoryArrayList.add(new Category(4, "Middle-Tier Developer"));
        categoryArrayList.add(new Category(5, "Web Developer"));
        categoryArrayList.add(new Category(6, "Desktop Developer"));
        categoryArrayList.add(new Category(7, "Mobile Developer"));
        categoryArrayList.add(new Category(8, "Graphics Developer"));
        categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList);
        recyclerView.setAdapter(categoryAdapter);

        //        for job recycler view
        jobRecyclerView = view.findViewById(R.id.job);
        jobLinearLayoutManager = new LinearLayoutManager(getContext());
        jobLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        jobRecyclerView.setLayoutManager(jobLinearLayoutManager);

        jobArrayList = new ArrayList<>();
        jobArrayList.add(new Job("Front-end Developer"));
        jobArrayList.add(new Job("Backend Developer"));
        jobArrayList.add(new Job("Full-stack Developer"));
        jobArrayList.add(new Job("Middle-Tier Developer"));
        jobArrayList.add(new Job("Web Developer"));
        jobArrayList.add(new Job("Desktop Developer"));
        jobArrayList.add(new Job("Mobile Developer"));
        jobArrayList.add(new Job("Graphics Developer"));
        jobAdapter = new JobAdapter(getContext(), jobArrayList);
        jobRecyclerView.setAdapter(jobAdapter);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int id = requireArguments().getInt(ARG_ID);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);
    }


}
