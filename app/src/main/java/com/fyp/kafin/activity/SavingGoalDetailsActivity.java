package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.SavingProgressAdapter;
import com.fyp.kafin.dialog.DialogSavingProgress;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SavingGoalDetailsActivity extends AppCompatActivity implements View.OnClickListener, DialogSavingProgress.SavingProgressListener {

    TextView savingID, dateCreatedText, dateStartText, dateEndText, goalText, spentText, savedText, dueText;
    MaterialButton btnAddProgress;
    RecyclerView progressRecycler;
    SavingProgressAdapter progressAdapter;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    User appUser = User.getInstance();
    SavingGoal savingGoal;
    ArrayList<SavingProgress> savingProgresses;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_goal_details);
        savingID = findViewById(R.id.details_savingID);
        dateCreatedText = findViewById(R.id.details_createdAt);
        dateStartText = findViewById(R.id.details_dateStart);
        dateEndText = findViewById(R.id.details_dateEnd);
        goalText = findViewById(R.id.savingDetailsGoal);
        spentText = findViewById(R.id.details_totalSpent);
        savedText = findViewById(R.id.details_totalSaved);
        dueText = findViewById(R.id.details_totalDue);
        btnAddProgress = findViewById(R.id.btn_addProgress);
        progressRecycler = findViewById(R.id.savingProgressRecycler);
        savingProgresses = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        progressRecycler.setLayoutManager(layoutManager);
        progressRecycler.setHasFixedSize(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String savingGoalID = extras.getString("savingID");
//            Toast.makeText(getApplicationContext(), savingGoalID, Toast.LENGTH_SHORT).show();
            loadSavingGoalData(savingGoalID);
            loadProgressData(savingGoalID);
        }
        btnAddProgress.setOnClickListener(this);
    }

    private void loadSavingGoalData(String savingGoalID) {
        DatabaseReference savingRef = dbRef.child("savinggoals").child(appUser.getUserID()).child(savingGoalID);
        savingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savingGoal = snapshot.getValue(SavingGoal.class);
                assert savingGoal != null;
                savingGoal.setSavingID(snapshot.getKey());

                savingID.setText(user.getDisplayName());
                dateCreatedText.setText(savingGoal.getDateCreated());
                dateStartText.setText(savingGoal.getDateStart());
                dateEndText.setText(savingGoal.getDateEnd());
                goalText.setText(String.format("RM %s", savingGoal.getGoalAmount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                savingGoal = new SavingGoal();
            }
        });
    }

    private void loadProgressData(String savingID) {
        DatabaseReference progressRef = dbRef.child("progress").child(appUser.getUserID()).child(savingID);
        progressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearProgresses();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                    savingProgresses.add(progress);
                }
//                Toast.makeText(getApplicationContext(), savingProgresses.toString() + savingProgresses.size(), Toast.LENGTH_SHORT).show();
                progressAdapter = new SavingProgressAdapter(savingGoal, savingProgresses, getApplicationContext());
                progressRecycler.setAdapter(progressAdapter);
                progressAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clearProgresses() {
        if(savingProgresses != null) {
            savingProgresses.clear();
            if(progressAdapter != null) {
                progressAdapter.notifyDataSetChanged();
            }
        }
        savingProgresses = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_addProgress) {
//            Toast.makeText(getApplicationContext(), "Add Progress", Toast.LENGTH_SHORT).show();
            DialogSavingProgress dialogSavingProgress = new DialogSavingProgress();
            dialogSavingProgress.show(getSupportFragmentManager(), "Progress_dialog");
        }
    }

    @Override
    public void saveTodaysProgress(String date, float spent) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        SimpleDateFormat dateKeyFormat = new SimpleDateFormat("ddMMyyyy", Locale.UK);
        try {
            Date properDate = simpleFormat.parse(date);
            String dateKey = dateKeyFormat.format(Objects.requireNonNull(properDate));
            SavingProgress progress = new SavingProgress(date, spent);
            DatabaseReference progressRef = dbRef.child("progress").child(appUser.getUserID()).child(savingGoal.getSavingID());
            progressRef.child(dateKey).setValue(progress);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}