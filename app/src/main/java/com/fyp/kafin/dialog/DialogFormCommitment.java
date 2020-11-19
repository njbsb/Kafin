package com.fyp.kafin.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fyp.kafin.R;

public class DialogFormCommitment extends AppCompatDialogFragment {
    EditText commitmentName, commitmentAmount;
    FormDialogListener listener;
    public DialogFormCommitment() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder comform = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_commitment_form, null);
        commitmentName = view.findViewById(R.id.form_commitmentName);
        commitmentAmount = view.findViewById(R.id.form_commitmentAmount);
        comform.setView(view).setTitle("New Commitment").setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comname = commitmentName.getText().toString();
                String comamount = commitmentAmount.getText().toString();
                listener.saveCommitment(comname, comamount);
            }
        });

        return comform.create();
//        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FormDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FormDialogListener");
        }
    }

    public interface FormDialogListener {
        void saveCommitment(String comname, String comamount);
    }

//    @Override
//    public void setupDialog(@NonNull Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//    }
}
