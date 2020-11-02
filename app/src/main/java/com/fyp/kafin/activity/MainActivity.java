package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.fyp.kafin.R;
import com.fyp.kafin.fragment.AnalyticsFragment;
import com.fyp.kafin.fragment.HomeFragment;
import com.fyp.kafin.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavBar = findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            if(item.getItemId() == R.id.home) {
                fragment = new HomeFragment();
            } else if(item.getItemId() == R.id.charts) {
                fragment = new AnalyticsFragment();
            } else if(item.getItemId() == R.id.profile) {
                fragment = new ProfileFragment();
            }
            getSupportFragmentManager().
                    beginTransaction().
//                    setCustomAnimations(
//                            R.anim.slide_in,  // enter
//                            R.anim.fade_out,  // exit
//                            R.anim.fade_in   // popEnter
//                            R.anim.slide_out  // popExit
                    replace(R.id.frameContainer, fragment).commit();
            return true;
        }
    };

}