package com.fyp.kafin.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fyp.kafin.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DialogSavingProgress extends AppCompatDialogFragment {

    TextView todayDateText;
    EditText todaySpent;
    SavingProgressListener listener;

    public DialogSavingProgress() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder progressForm = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_saving_progress, null);
//        todayDate = view.findViewById(R.id.dialog_progressDate);
        todaySpent = view.findViewById(R.id.dialog_progressSpent);
        todayDateText = view.findViewById(R.id.dialog_progressDateText);
        Date today = new Date();
        SimpleDateFormat stringDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todayDateText.setText(stringDate.format(today));
        progressForm
                .setView(view)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String date = todayDate.getText().toString();
                        String date = todayDateText.getText().toString();
                        float spent = Float.parseFloat(todaySpent.getText().toString());
                        listener.saveTodaysProgress(date, spent);
                    }
                });
        return progressForm.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (SavingProgressListener) context;
    }

    public interface SavingProgressListener {
        void saveTodaysProgress(String date, float spent);
    }
}
