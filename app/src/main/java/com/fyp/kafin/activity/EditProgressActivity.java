package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProgressActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SAVING_ID = "SAVING_ID";
    public static final String PROGRESS_ID = "PROGRESS_ID";
    public static final String PROGRESS_DATE = "PROGRESS_DATE";
//    public static final String PROGRESS = getR;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private SavingProgress progress;
    private TextView progressIDText, progressDateText;
    private EditText progressSpentET;
    private MaterialButton btn_submitProgress;
    private String savingID, progressID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_progress);
        progressIDText = findViewById(R.id.editprogress_id);
        progressDateText = findViewById(R.id.editprogress_date);
        progressSpentET = findViewById(R.id.editprogress_spent);
        btn_submitProgress = findViewById(R.id.editprogress_submitbutton);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            progressID = extras.getString(PROGRESS_ID);
            savingID = extras.getString(SAVING_ID);
            loadProgressData(savingID, progressID);
            btn_submitProgress.setOnClickListener(this);
        }
    }

    private void loadProgressData(String savingID, final String progressID) {
        final DatabaseReference progRef = dbRef.child(getString(R.string.PROGRESS)).child(user.getUid()).child(savingID).child(progressID);
        progRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progress = snapshot.getValue(SavingProgress.class);
                Log.d("progress", progress.toString());
                progressIDText.setText(progress.getProgressID());
                progressDateText.setText(progress.getDate());
                progressSpentET.setText(String.valueOf(progress.getSpentToday()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        float expense = Float.parseFloat(progressSpentET.getText().toString());
        DatabaseReference expenseRef = dbRef.child("progress").child(user.getUid()).child(savingID).child(progressID).child("spentToday");
        expenseRef.setValue(expense);
        Toast.makeText(getApplicationContext(), "Progress updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}