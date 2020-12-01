package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.User;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SavingForm extends AppCompatActivity {

    CardView dateCard, commitmentCard;
    EditText goalAmount;
    TextView savingPeriodText, commitment_list;
    Date dateStart, dateEnd;
    Button btn_submit;
    ArrayList<Commitment> commitments;
    boolean[] checkedItems;
    ArrayList<Integer> userComInt = new ArrayList<>();
    ArrayList<Commitment> selectedCom = new ArrayList<>();
    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    User appUser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_form);
        // bind with xml
        goalAmount = findViewById(R.id.form_goal_amount);
        dateCard = findViewById(R.id.card_startDate);
        commitmentCard = findViewById(R.id.card_commitment);
        savingPeriodText = findViewById(R.id.form_startDateText);
        commitment_list = findViewById(R.id.form_commitmentText);
        btn_submit = findViewById(R.id.btn_submit_savinggoal);
        // initialize variables
        appUser = User.getInstance();
        commitments = appUser.getUserCommitment();
        myRef = FirebaseDatabase.getInstance().getReference();

        // date picker
        final MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Saving Period");
        final MaterialDatePicker materialDatePicker = builder.build();
        dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "date_picker");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                savingPeriodText.setText(materialDatePicker.getHeaderText());
                Long startdatelong = selection.first;
                Long enddatelong = selection.second;
                TimeZone timeZoneUTC = TimeZone.getDefault(); // Get the offset from our timezone and UTC.
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1; // It will be negative, so that's the -1
                 // Create a date format, then a date object with our offset
                dateStart = new Date(startdatelong + offsetFromUTC);
                dateEnd = new Date(enddatelong + offsetFromUTC);
                long diff = dateEnd.getTime() - dateStart.getTime();
                String duration = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
                if((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1) < 28) {
                    Toast.makeText(getApplicationContext(), "Please select a bigger period", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Duration satisfies a month period", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getApplicationContext(), duration, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), simpleFormat.format(dateStart), Toast.LENGTH_SHORT).show();
            }
        });
        final CharSequence[] comString = new String[commitments.size()];
        final String[] comID = new String[commitments.size()];
        final float[] comAmount = new float[commitments.size()];
        checkedItems = new boolean[commitments.size()];
        for(int i = 0; i<commitments.size(); i++) {
            comString[i] = commitments.get(i).getComName();
            comAmount[i] = commitments.get(i).getComAmount();
            comID[i] = commitments.get(i).getComID();
            checkedItems[i] = false;
        }

        // select commitments
        commitmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder comBuilder = new AlertDialog.Builder(SavingForm.this);
                comBuilder.setTitle("Select commitments");
                comBuilder.setMultiChoiceItems(comString, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                         if(isChecked) { // this is if user click on check (True)
                             checkedItems[position] = isChecked;
                             if(!userComInt.contains(position)) {
                                 userComInt.add(position);
                                 Toast.makeText(getApplicationContext(),"added " + position + ", left: " + userComInt.toString(), Toast.LENGTH_SHORT).show();
                             } else {
                                 if(userComInt.contains(position)) {
                                    userComInt.remove(userComInt.indexOf(position));
                                    Toast.makeText(getApplicationContext(),"removed " + position + ", left: " + userComInt.toString(), Toast.LENGTH_SHORT).show();
                                 }
                             }
                         } else { // if user uncheck a checkbox
                             if(userComInt.contains(position)) {
                                 userComInt.remove(userComInt.indexOf(position));
                                 Toast.makeText(getApplicationContext(),"removed " + position + ", left: " + userComInt.toString(), Toast.LENGTH_SHORT).show();
                             }
                         }
                    }
                });
                comBuilder.setCancelable(true);
                comBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder listed = new StringBuilder();
                        for(int i = 0; i<userComInt.size(); i++) {
                            listed.append(comString[userComInt.get(i)]);
                            if(i != userComInt.size() - 1) {
                                listed.append(", ");
                            }
                            Commitment com = new Commitment((String) comString[userComInt.get(i)], comAmount[userComInt.get(i)]);
                            com.setComID(comID[userComInt.get(i)]);
                            selectedCom.add(com);
                        }
                        commitment_list.setText(listed);
//                        Toast.makeText(getApplicationContext(), selectedCom.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                comBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        for(int i = 0; i<checkedItems.length; i++) {
                            checkedItems[i] = false;
                            selectedCom.clear();
                            commitment_list.setText("Commitments");
                        }
                    }
                });
                AlertDialog mdialog = comBuilder.create();
                mdialog.show();
            }
        });

        // submit form
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goalAmount.getText().toString().equals("") || dateStart == null || dateEnd == null || selectedCom.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    SavingGoal savingGoal = new SavingGoal(
                            Float.parseFloat(goalAmount.getText().toString()),
                            simpleFormat.format(dateStart),
                            simpleFormat.format(dateEnd),
                            selectedCom,
                            simpleFormat.format(new Date()));
//                    DatabaseReference postsRef = myRef.child("posts");
//                    DatabaseReference newPostRef = postsRef.push();
//                    newPostRef.setValue(savingGoal);
                    DatabaseReference userGoalCom = myRef.child("savinggoals").child(appUser.getUserID());
                    userGoalCom.push().setValue(savingGoal);
                    Toast.makeText(getApplicationContext(), "save success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}