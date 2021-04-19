package com.almightymm.job4u.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.almightymm.job4u.fragment.CompanyDetailsFragment;
import com.almightymm.job4u.fragment.JobDetailsFragment;
import com.almightymm.job4u.fragment.appliedJobFragment;
import com.almightymm.job4u.fragment.savedJobFragment;

public class SavedJobFragmentPagerAdapter extends FragmentStatePagerAdapter {
    public SavedJobFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new savedJobFragment();
            case 1:
                return new appliedJobFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Saved Jobs";
        else if (position == 1)
            return "Applied Jobs";
        else
            return super.getPageTitle(position);
    }

}
