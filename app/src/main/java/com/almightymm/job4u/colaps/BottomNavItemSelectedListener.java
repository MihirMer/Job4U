package com.almightymm.job4u.colaps;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.almightymm.job4u.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavItemSelectedListener
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final Toolbar toolbar;
    private final ViewPager viewPager;

    public BottomNavItemSelectedListener(ViewPager viewPager, Toolbar toolbar) {
        this.toolbar = toolbar;
        this.viewPager = viewPager;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        toolbar.setTitle(item.getTitle());
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.item_home:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.item_search:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.item_saved_job:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.item_notification:
                viewPager.setCurrentItem(3);
                return true;
            case R.id.item_account:
                viewPager.setCurrentItem(4);
                return true;
            default:
                return false;
        }
    }
}
