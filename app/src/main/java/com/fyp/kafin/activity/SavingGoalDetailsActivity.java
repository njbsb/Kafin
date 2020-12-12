package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.SavingProgressAdapter;
import com.fyp.kafin.controller.SavingGoalController;
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

import org.joda.time.DateTimeComparator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SavingGoalDetailsActivity extends AppCompatActivity implements View.OnClickListener, DialogSavingProgress.SavingProgressListener {

    TextView savingID, dateCreatedText, dateStartText, dateEndText, totalDaysText, goalText, dailyLimitText, spentText, savedText, dueText;
    ImageButton btnDelete;
    MaterialButton btnAddProgress;
    RecyclerView progressRecycler;
    SavingProgressAdapter progressAdapter;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    User appUser = User.getInstance();
    SavingGoal savingGoal;
    ArrayList<SavingProgress> progressList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Locale MY = new Locale("en", "MY");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", MY);
    Date today = new Date();
    final static String NO_DATA = "No data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_goal_details);
        savingID = findViewById(R.id.details_savingID);
        dateCreatedText = findViewById(R.id.details_createdAt);
        dateStartText = findViewById(R.id.details_dateStart);
        dateEndText = findViewById(R.id.details_dateEnd);
        totalDaysText = findViewById(R.id.details_totalDays);
        goalText = findViewById(R.id.savingDetailsGoal);
        dailyLimitText = findViewById(R.id.details_dailyLimit);
        spentText = findViewById(R.id.details_totalSpent);
        savedText = findViewById(R.id.details_totalSaved);
        dueText = findViewById(R.id.details_totalDue);
        btnAddProgress = findViewById(R.id.btn_addProgress);
        btnDelete = findViewById(R.id.btn_delete_saving);
        progressRecycler = findViewById(R.id.savingProgressRecycler);
        progressList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        progressRecycler.setLayoutManager(layoutManager);
        progressRecycler.setHasFixedSize(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String savingGoalID = extras.getString("savingID");;
            loadSavingGoalData(savingGoalID);
            loadProgressData(savingGoalID);
        }
        btnAddProgress.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void loadSavingGoalData(String savingGoalID) {
        DatabaseReference savingRef = dbRef.child("savinggoals").child(appUser.getUserID()).child(savingGoalID);
        savingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savingGoal = snapshot.getValue(SavingGoal.class);
                assert savingGoal != null;
                savingGoal.setSavingID(snapshot.getKey());
                savingID.setText(user.getDisplayName());
                dateCreatedText.setText(savingGoal.getDateCreated());
                dateStartText.setText(savingGoal.getDateStart());
                dateEndText.setText(savingGoal.getDateEnd());
                goalText.setText(moneyFormat(savingGoal.getGoalAmount()));
                SavingGoalController savingController = new SavingGoalController(savingGoal, appUser);
                dailyLimitText.setText(savingController.getFormattedMoney(savingController.getAllowedDailyExpenses()));
                totalDaysText.setText(String.format("%s days", savingController.getSavingDuration()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                savingGoal = new SavingGoal();
            }
        });
    }

    private void loadProgressData(String savingID) {
        final DatabaseReference progressRef = dbRef.child("progress").child(appUser.getUserID()).child(savingID);
        progressRef.orderByChild("savingID").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearProgresses();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                    progressList.add(progress);
                }
                if(progressList.size() > 0) {
                    Collections.reverse(progressList);
                    try {
                        Date topDate = simpleDateFormat.parse(progressList.get(0).getDate());
                        assert topDate != null;
                        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
                        int compareValue = comparator.compare(today, topDate);
                        if(compareValue == 0) {
                            btnAddProgress.setText("Edit progress");
                        } else if(compareValue > 0) {
                            btnAddProgress.setText("Add progress");
                        }
                        Log.d("today", today.toString());
                        Log.d("topDate", topDate.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SavingGoalController controller = new SavingGoalController(savingGoal, appUser, progressList);
                    spentText.setText(controller.getFormattedMoney(controller.getCumulativeSpent()));
                    savedText.setText(controller.getFormattedMoney(controller.getCumulativeSaved()));
                    dueText.setText(controller.getFormattedMoney(controller.getCumulativeSaved() + savingGoal.getGoalAmount()));

                    progressAdapter = new SavingProgressAdapter(savingGoal, progressList, getApplicationContext());
                    progressRecycler.setAdapter(progressAdapter);
                    progressAdapter.notifyDataSetChanged();
                } else {
                    spentText.setText(NO_DATA);
                    savedText.setText(NO_DATA);
                    dueText.setText(NO_DATA);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clearProgresses() {
        if(progressList != null) {
            progressList.clear();
            if(progressAdapter != null) {
                progressAdapter.notifyDataSetChanged();
            }
        }
        progressList = new ArrayList<>();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addProgress:
                DialogSavingProgress dialogSavingProgress = new DialogSavingProgress();
                dialogSavingProgress.show(getSupportFragmentManager(), "Progress_dialog");
                break;
            case R.id.btn_delete_saving:
                showConfirmDelete();
                break;
        }
    }

    private void showConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete this saving goal?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSavingGoal(savingGoal.getSavingID());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteSavingGoal(String savingID) {
        DatabaseReference savingRef = dbRef.child("savinggoals").child(user.getUid()).child(savingID);
        DatabaseReference progressRef = dbRef.child("progress").child(user.getUid()).child(savingID);
        savingRef.removeValue();
        progressRef.removeValue();
        finish();
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

    private String moneyFormat(float value) {
        return NumberFormat.getCurrencyInstance(MY).format(value);
    }
}