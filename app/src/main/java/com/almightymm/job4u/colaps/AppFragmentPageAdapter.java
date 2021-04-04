package com.almightymm.job4u.colaps;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AppFragmentPageAdapter extends FragmentPagerAdapter {
    private static final String TAG = "AppFragmentPageAdapter";

    public AppFragmentPageAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance(1);
            case 1:
                return SearchFragment.newInstance("param1", "param2");
            case 2:
                return SavedJobFragment.newInstance(" ", " ");
            case 3:
                return NotificationFragment.newInstance(" ", " ");
            case 4:
                return ProfileFragment.newInstance(" ", " ");
            default:
                throw new RuntimeException("Not supported");
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
