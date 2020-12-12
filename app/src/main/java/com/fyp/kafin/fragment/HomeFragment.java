package com.fyp.kafin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.activity.CommitmentActivity;
import com.fyp.kafin.activity.LoginActivity;
import com.fyp.kafin.activity.SavingGoalActivity;
import com.fyp.kafin.controller.SavingGoalController;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView welcomeText;
    ProgressBar progressBar;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();;
    User appUser;
    SavingGoal savingGoal;
    TextView summaryIncome, summaryDuration, incomeMessage, thisGoal, thisDailyExpenseLimit, thisCumulativeSpent, thisCumulativeSaved;
    CardView cardSaving, cardCommitment;
    Locale myLocale = new Locale("en", "MY");

    public HomeFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeText = view.findViewById(R.id.welcome_text);
        progressBar = view.findViewById(R.id.summary_progressbar);
        summaryDuration = view.findViewById(R.id.summary_duration);
        cardCommitment = view.findViewById(R.id.card_commitments);
        cardSaving = view.findViewById(R.id.card_savings);
        summaryIncome = view.findViewById(R.id.summary_income);
        incomeMessage = view.findViewById(R.id.summary_income_message);
        thisGoal = view.findViewById(R.id.summary_goalAmount);
        thisDailyExpenseLimit = view.findViewById(R.id.summary_dailyExpenseLimit);
        thisCumulativeSpent = view.findViewById(R.id.summary_spent);
        thisCumulativeSaved = view.findViewById(R.id.summary_savedDaily);
        progressBar.setVisibility(View.VISIBLE);
        if(user != null) {
            setWelcomeText(user);
            appUser = User.getInstance();
            loadUserData();
            cardCommitment.setOnClickListener(this);
            cardSaving.setOnClickListener(this);
            loadSavingData();

        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
        return view;
    }

    public void loadUserData() {
        DatabaseReference userRef = dbRef.child("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userDB = snapshot.getValue(User.class);
                assert userDB != null;
                appUser.setUsername(userDB.getUsername());
                appUser.setUserID(user.getUid());
                appUser.setUserEmail(userDB.getUserEmail());
                appUser.setMonthlyIncome(userDB.getMonthlyIncome());
                if(userDB.getMonthlyIncome() == 0) {
                    summaryIncome.setText("RM ?");
                    incomeMessage.setText("You have not set your monthly income yet. Set your income in profile page.");
                } else {
                    summaryIncome.setText(moneyFormat(userDB.getMonthlyIncome()));
                    incomeMessage.setText("You are all set!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void loadSavingData() {
        DatabaseReference savingRef = dbRef.child("savinggoals").child(user.getUid());

        savingRef.orderByChild("activeStatus").equalTo(true).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    savingGoal = dataSnapshot.getValue(SavingGoal.class);
                    Log.d("snapshot.getValue", String.valueOf(dataSnapshot.getValue()));
                }
                assert savingGoal != null;
                thisGoal.setText(moneyFormat(savingGoal.getGoalAmount()));

                final ArrayList<SavingProgress> progressList = new ArrayList<>();
                DatabaseReference savingProgRef = dbRef.child("progress").child(user.getUid()).child(savingGoal.getSavingID());
                savingProgRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                            progressList.add(progress);
                        }
                        SavingGoalController controller = new SavingGoalController(savingGoal, appUser, progressList);
                        controller.addMissingProgress();
                        summaryDuration.setText(String.format("%s days", controller.getSavingDuration()));
                        thisDailyExpenseLimit.setText(moneyFormat(controller.getAllowedDailyExpenses()));
                        thisCumulativeSaved.setText(moneyFormat(controller.getCumulativeSaved()));
                        thisCumulativeSpent.setText(moneyFormat(controller.getCumulativeSpent()));
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setWelcomeText(FirebaseUser user) {
        String username = "";
        if(user != null) {
            if(user.getDisplayName() != null || !user.getDisplayName().equals(""))
                username = user.getDisplayName();
            else
                username = user.getEmail();
        } else {
            username = "missing_name";
        }
        welcomeText.setText(String.format("Welcome, %s!", username));
    }

    public String moneyFormat(float value) {
        return NumberFormat.getCurrencyInstance(myLocale).format(value);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.card_commitments:
                intent = new Intent(getActivity(), CommitmentActivity.class);
                break;
            case R.id.card_savings:
                intent = new Intent(getActivity(), SavingGoalActivity.class);
                break;
        }
        startActivity(intent);
    }
}