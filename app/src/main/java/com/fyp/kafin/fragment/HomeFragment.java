package com.fyp.kafin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.kafin.R;
import com.fyp.kafin.adapter.CommitmentAdapter;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.HomeCard;
import com.fyp.kafin.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private User user = User.getInstance();
    private TextView welcomeText;
    private RecyclerView commitmentRecycler;
    private CommitmentAdapter commitmentAdapter;
    private ArrayList<Commitment> commitments;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeText = (TextView)  view.findViewById(R.id.welcome_text);
        setWelcomeText(user.getUsername());
        commitments = new ArrayList<>();
        commitments.add(new Commitment("Makan" , 60.0));
        commitments.add(new Commitment("Shopping" , 100.0));
        commitments.add(new Commitment("Game" , 1400.6));
        commitmentRecycler = view.findViewById(R.id.commitment_recycler);
        commitmentAdapter = new CommitmentAdapter(getActivity(), commitments);
        commitmentRecycler.setAdapter(commitmentAdapter);
        commitmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void setWelcomeText(String username) {
        if(username == null) {
            username = "Khairul Anuar";
        }
        welcomeText.setText(String.format("Welcome, %s!", username));
    }
}