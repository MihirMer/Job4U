package com.almightymm.job4u.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almightymm.job4u.Adapter.JobFragmentPagerAdapter;
import com.almightymm.job4u.Adapter.SavedJobFragmentPagerAdapter;
import com.almightymm.job4u.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import static android.content.Context.MODE_PRIVATE;

public class SavedAppliedPreviewFragment extends Fragment {

    DatabaseReference databaseReference;
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;


    public SavedAppliedPreviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_saved_applied_preview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreferences();
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout1);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager1);

        tabLayout.addTab(tabLayout.newTab().setText("Saved Jobs"));
        tabLayout.addTab(tabLayout.newTab().setText("Applied Jobs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        SavedJobFragmentPagerAdapter adapter = new SavedJobFragmentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initPreferences() {
        preferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
        preferenceEditor = preferences.edit();
    }
}