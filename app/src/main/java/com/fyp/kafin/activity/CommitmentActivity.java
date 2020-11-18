package com.fyp.kafin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.CommitmentAdapter;
import com.fyp.kafin.model.Commitment;

import java.util.ArrayList;

public class CommitmentActivity extends AppCompatActivity {

    private RecyclerView commitmentRecycler;
//    private CommitmentAdapter commitmentAdapter;
//    private ArrayList<Commitment> commitments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitment);
        commitmentRecycler = findViewById(R.id.commitmentRecycler);
        setUpRecycler();
    }

    void setUpRecycler() {
        ArrayList<Commitment> commitments = new ArrayList<>();
        commitments.add(new Commitment("Makan" , 60.0));
        commitments.add(new Commitment("Shopping" , 100.0));
        commitments.add(new Commitment("Game" , 1400.6));

        CommitmentAdapter commitmentAdapter = new CommitmentAdapter(getApplicationContext(), commitments);
        commitmentRecycler.setAdapter(commitmentAdapter);
        commitmentRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}