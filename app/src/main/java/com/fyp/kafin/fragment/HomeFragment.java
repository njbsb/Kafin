package com.fyp.kafin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.fyp.kafin.activity.LoginActivity;
import com.fyp.kafin.activity.SavingGoalActivity;
import com.fyp.kafin.adapter.CommitmentAdapter;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.HomeCard;
import com.fyp.kafin.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView welcomeText;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();;
    ArrayList<Commitment> commitments;
    User appUser;

    public HomeFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeText = view.findViewById(R.id.welcome_text);
        if(user != null) {
            setWelcomeText(user);
            appUser = User.getInstance();
            loadUserData();
            CardView cardCommitment = view.findViewById(R.id.card_commitments);
            CardView cardSaving = view.findViewById(R.id.card_savings);
            cardCommitment.setOnClickListener(this);
            cardSaving.setOnClickListener(this);
//            loadData();
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        return view;
    }

    public void loadUserData() {
        DatabaseReference userRef = dbRef.child("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userDB = snapshot.getValue(User.class);
                assert userDB != null;
                appUser.setUsername(userDB.getUsername());
                appUser.setUserID(user.getUid());
                appUser.setUserEmail(userDB.getUserEmail());
                appUser.setMonthlyIncome(userDB.getMonthlyIncome());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



//    public void loadData() {
////        databaseReference = FirebaseDatabase.getInstance().getReference();
//        Query query = dbRef.child("commitments").child(user.getUid());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                clearAll();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String comName = Objects.requireNonNull(snapshot.child("comName").getValue()).toString();
//                    long comAmount = Long.parseLong(Objects.requireNonNull(snapshot.child("comAmount").getValue()).toString());
//                    Commitment commitment = new Commitment(comName, comAmount);
//                    commitment.setComID(snapshot.getKey());
//                    commitments.add(commitment);
//                }
//                appUser.setUserCommitment(commitments);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

    private void clearAll() {
        if(commitments != null) {
            commitments.clear();
        }
        commitments = new ArrayList<>();
    }

    public void setWelcomeText(FirebaseUser user) {
        String username = "";
        if(user != null) {
            if(user.getDisplayName() != null || !user.getDisplayName().equals(""))
                username = user.getDisplayName();
            else
                username = user.getEmail();
        } else {
            username = "missing_name";
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