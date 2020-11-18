package com.fyp.kafin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.activity.CommitmentActivity;
import com.fyp.kafin.activity.SavingGoalActivity;
import com.fyp.kafin.adapter.CommitmentAdapter;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.HomeCard;
import com.fyp.kafin.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private User user = User.getInstance();
    private TextView welcomeText;
    private RecyclerView commitmentRecycler;
    private CommitmentAdapter commitmentAdapter;
    private ArrayList<Commitment> commitments;
//    private CardView cardCommitment;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeText = (TextView)  view.findViewById(R.id.welcome_text);
        setWelcomeText(user.getUsername());
        CardView cardCommitment = view.findViewById(R.id.card_commitments);
        CardView cardSaving = view.findViewById(R.id.card_savings);
        cardCommitment.setOnClickListener(this);
        cardSaving.setOnClickListener(this);
        return view;
    }

    public void setWelcomeText(String username) {
        if(username == null) {
            username = "Khairul Anuar";
        }
        welcomeText.setText(String.format("Welcome, %s!", username));
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