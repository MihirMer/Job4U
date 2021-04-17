package com.almightymm.job4u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    static View fragment;
    //    share pref
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    NavController navController;
    private BottomNavigationView navigation;
    private Toolbar toolbar;
String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragment = findViewById(R.id.nav_host_fragment);

        initPreferences();
        Log.d(TAG, "onCreate: " + preferences.getBoolean("roleAssigned", false));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigation = findViewById(R.id.navigation);
        role = preferences.getString("role", "");

        if (role.equals("HR")) {
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.hr_bottom_nav_menu);
        } else {
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.bottom_nav_menu);
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.savedJobFragment,
                R.id.notificationFragment,
                R.id.profileFragment
        )
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, navController);
        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });

    }



    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

}