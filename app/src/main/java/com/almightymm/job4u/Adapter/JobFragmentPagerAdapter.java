package com.almightymm.job4u.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.almightymm.job4u.fragment.CompanyDetailsFragment;
import com.almightymm.job4u.fragment.JobDetailsFragment;

public class JobFragmentPagerAdapter extends FragmentPagerAdapter {
    public JobFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new JobDetailsFragment();
            case 1:
                return new CompanyDetailsFragment();
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
            return "Job Details";
        else if (position == 1)
            return "Company Details";
        else
            return super.getPageTitle(position);
    }
}
