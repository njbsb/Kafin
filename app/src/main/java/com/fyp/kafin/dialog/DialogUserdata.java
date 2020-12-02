package com.fyp.kafin.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fyp.kafin.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class DialogUserdata extends AppCompatDialogFragment{
    EditText username, income;
    MaterialButton saveBtn;
    TextView cancelText;
    FormDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_userdata, container, false);
        username = view.findViewById(R.id.et_user_name);
        income = view.findViewById(R.id.et_user_salary);
        saveBtn = view.findViewById(R.id.userdialog_save);
        cancelText = view.findViewById(R.id.userdialog_cancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameString = username.getText().toString();
                float incomeFloat = Float.parseFloat(income.getText().toString());
                if(!usernameString.equals("") && !income.getText().toString().equals("")) {
                    listener.saveUserdata(usernameString, incomeFloat);
                }
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FormDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FormDialogListener");
        }
    }

    public interface FormDialogListener {
        void saveUserdata(String username, float income);
    }
}
