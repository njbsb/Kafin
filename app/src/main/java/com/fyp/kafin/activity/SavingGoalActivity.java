package com.fyp.kafin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.SavingGoalAdapter;
import com.fyp.kafin.model.SavingGoal;

import java.util.ArrayList;
import java.util.List;

public class SavingGoalActivity extends AppCompatActivity {

    ViewPager savingPager;
    SavingGoalAdapter savingAdapter;
    ArrayList<SavingGoal> savingGoals;
    Button btnAddGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_goal);
        savingGoals = new ArrayList<>();
//        savingGoals.add(new SavingGoal(300));
//        savingGoals.add(new SavingGoal(400));
//        savingGoals.add(new SavingGoal(500));

        savingAdapter = new SavingGoalAdapter(savingGoals, this);
        savingPager = findViewById(R.id.saving_viewpager);
        savingPager.setAdapter(savingAdapter);
        savingPager.setPadding(100,0,100,0);
        savingPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnAddGoal = findViewById(R.id.btn_new_saving);
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SavingForm.class);
                startActivity(i);
//                Toast.makeText(getApplicationContext(), "This", Toast.LENGTH_SHORT).show();
            }
        });
    }

}