package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.SavingGoalAdapter;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavingGoalActivity extends AppCompatActivity {

    ViewPager savingPager;
    SavingGoalAdapter savingAdapter;
    ArrayList<SavingGoal> savingGoals;
    Button btnAddGoal;
    DatabaseReference myRef;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_goal);
        savingGoals = new ArrayList<>();
        user = User.getInstance();
        savingPager = findViewById(R.id.saving_viewpager);
        btnAddGoal = findViewById(R.id.btn_new_saving);
        getSavingGoals(user);
        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SavingForm.class);
                startActivity(i);
            }
        });
    }

    private void getSavingGoals(User user) {
        myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child("savinggoals").child(user.getUserID());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearData();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    SavingGoal savingGoal = dataSnapshot.getValue(SavingGoal.class);
                    savingGoals.add(savingGoal);
                    savingAdapter = new SavingGoalAdapter(savingGoals, SavingGoalActivity.this);
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clearData() {
        if(savingGoals != null) {
            savingGoals.clear();
            if(savingAdapter != null) {
                savingAdapter.notifyDataSetChanged();
            }
        }
        savingGoals = new ArrayList<>();
    }

}