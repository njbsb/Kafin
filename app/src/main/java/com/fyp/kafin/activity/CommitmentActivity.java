package com.fyp.kafin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.CommitmentAdapter;
import com.fyp.kafin.dialog.DialogFormCommitment;
import com.fyp.kafin.model.Commitment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CommitmentActivity extends AppCompatActivity implements DialogFormCommitment.FormDialogListener {

    private RecyclerView commitmentRecycler;
    MaterialButton btn_addCommitment;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef;
    CommitmentAdapter commitmentAdapter;
    ArrayList<Commitment> commitments;
    Context context;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitment);
        commitmentRecycler = findViewById(R.id.commitmentRecycler);
        btn_addCommitment = findViewById(R.id.add_commitment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commitmentRecycler.setLayoutManager(layoutManager);
        commitmentRecycler.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();
        commitments = new ArrayList<>();
//        clearAll();
        getDataFromDB();
        btn_addCommitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFormDialog();
            }
        });
    }

    private void getDataFromDB() {
        Query query = myRef.child("commitments").child(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Commitment commitment = snapshot.getValue(Commitment.class);
                    commitments.add(commitment);
                }
                commitmentAdapter = new CommitmentAdapter(context, commitments);
                commitmentRecycler.setAdapter(commitmentAdapter);
                commitmentAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clearAll() {
        if(commitments != null) {
            commitments.clear();
            if(commitmentAdapter != null) {
                commitmentAdapter.notifyDataSetChanged();
            }
        }
        commitments = new ArrayList<>();
    }

    private void openFormDialog() {
        DialogFormCommitment dialogCom = new DialogFormCommitment();
        dialogCom.show(getSupportFragmentManager(), "Form_dialog");
    }


    @Override
    public void saveCommitment(String comname, float comamount) {
        DatabaseReference com_ref = myRef.child("commitments").child(user.getUid());
        String key = com_ref.push().getKey();
        Commitment commitment = new Commitment(key, comname, comamount);
        assert key != null;
        com_ref.child(key).setValue(commitment);
//        com_ref.push().setValue(commitment);
    }
}