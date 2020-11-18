package com.fyp.kafin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fyp.kafin.R;
import com.fyp.kafin.fragment.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class SavingForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private CardView startCard, endCard;
    private TextView startDateText, endDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_form);
        startCard = findViewById(R.id.card_startDate);
        endCard = findViewById(R.id.card_endDate);
        startDateText = findViewById(R.id.form_startDateText);
        endDateText = findViewById(R.id.form_endDateText);
        startCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerStart = new DatePickerFragment();
                datePickerStart.show(getSupportFragmentManager(), "datePicker");
            }
        });
        endCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerEnd = new DatePickerFragment();
                datePickerEnd.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dateString = DateFormat.getDateInstance().format(calendar.getTime());
        startDateText.setText(dateString);
    }
}