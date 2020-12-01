package com.fyp.kafin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.kafin.R;
import com.fyp.kafin.activity.LoginActivity;
import com.fyp.kafin.dialog.DialogUserdata;
import com.fyp.kafin.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment implements DialogUserdata.FormDialogListener {

    private TextView name, email;
    private ImageView icon;
    private MaterialButton signOut;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User appUser = User.getInstance();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        icon = view.findViewById(R.id.profile_icon);
        signOut = view.findViewById(R.id.btn_signout);
        email.setText(user.getEmail());
        name.setText(user.getDisplayName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUserdata dialogUserdata = new DialogUserdata();
                dialogUserdata.setTargetFragment(ProfileFragment.this, 1);
                dialogUserdata.show(getParentFragmentManager(), "Userdata Form");
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
            }
        });
        return view;
    }

    @Override
    public void saveUserdata(String username, float income) {
        // save to db here
        DatabaseReference userRef = dbRef.child("users").child(user.getUid());
        appUser.setUsername(username);
        appUser.setMonthlyIncome(income);
        userRef.setValue(appUser);
//        userRef.child("income").setValue(income);
//        userRef.child("username").setValue(username);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Update Name", "User profile updated.");
                        }
                    }
                });
        name.setText(user.getDisplayName());
    }
}