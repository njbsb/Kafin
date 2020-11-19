package com.fyp.kafin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.fragment.DatePickerFragment;
import com.fyp.kafin.model.Commitment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class SavingForm extends AppCompatActivity {

    CardView startCard;
    MaterialCardView commitmentCard;
    private TextView savingPeriodText;
    TextView commitment_list;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_form);
        startCard = findViewById(R.id.card_startDate);
        commitmentCard = findViewById(R.id.card_commitment);

        savingPeriodText = findViewById(R.id.form_startDateText);
        commitment_list = findViewById(R.id.form_commitmentText);

        btn_submit = findViewById(R.id.btn_submit_savinggoal);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Saving Period");
        final MaterialDatePicker materialDatePicker = builder.build();
        startCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "date_picker");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                savingPeriodText.setText(materialDatePicker.getHeaderText());
            }
        });


        commitmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder comBuilder = new AlertDialog.Builder(SavingForm.this);
//                comBuilder.setTitle("Select commitments");
////                comBuilder.setMultiChoiceItems();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}