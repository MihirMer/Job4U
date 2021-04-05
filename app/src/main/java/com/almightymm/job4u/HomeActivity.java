package com.almightymm.job4u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private BottomNavigationView navigation;
    private Toolbar toolbar;


    //    share pref
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initPreferences();
        Log.d(TAG, "onCreate: "+ preferences.getBoolean("roleAssigned", false));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigation = findViewById(R.id.navigation);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
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


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            SharedPreferences pref = getSharedPreferences("User_Details", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();

        }
        return true;
    }

    private void initPreferences() {
        preferences = getSharedPreferences("User_Details", MODE_PRIVATE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}