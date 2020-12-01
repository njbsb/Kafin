package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.fyp.kafin.R;
import com.fyp.kafin.fragment.AnalyticsFragment;
import com.fyp.kafin.fragment.HomeFragment;
import com.fyp.kafin.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
//    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null) {
//            Intent signIn = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(signIn);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavBar = findViewById(R.id.bottomNavBar);
        bottomNavBar.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new HomeFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    replace(R.id.frameContainer, Objects.requireNonNull(fragment)).commit();
            return true;
        }
    };

}