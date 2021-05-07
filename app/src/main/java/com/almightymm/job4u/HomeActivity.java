package com.almightymm.job4u;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    static View fragment;
    //    share pref
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    NavController navController;
    String role;
    private BottomNavigationView navigation;
    private Toolbar toolbar;



@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fragment = findViewById(R.id.nav_host_fragment);

//    firebase messaging subscribe


        initPreferences();
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
            Log.e(TAG, "onCreate: hr1");
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.hr_bottom_nav_menu);
        } else {
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.bottom_nav_menu);
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (role.equals("HR")) {
            Log.e(TAG, "onCreate: hr2");
            navController.setGraph(R.navigation.h_r_mobile_navigation);
        } else {
            navController.setGraph(R.navigation.mobile_navigation);
        }
        final AppBarConfiguration appBarConfiguration;
        if (role.equals("HR")) {
            Log.e(TAG, "onCreate: hr3");
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.HRHomeFragment,
                    R.id.searchFragment,
                    R.id.addJobFragment,
                    R.id.notificationFragment,
                    R.id.HRProfileFragment
            ).build();
        } else {
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment,
                    R.id.searchFragment,
                    R.id.savedJobFragment,
                    R.id.notificationFragment,
                    R.id.profileFragment
            ).build();
        }

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigation, navController);
//        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
//            @Override
//            public void onNavigationItemReselected(@NonNull MenuItem item) {
//
//            }
//        });

    }




    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

}