package com.fyp.kafin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.kafin.R;
public class ProfileFragment extends Fragment {

    private TextView name, email;
    private ImageView icon;

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
        RecyclerView archivegoal_recycler = view.findViewById(R.id.archive_goals);
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        icon = view.findViewById(R.id.profile_icon);
        return  view;
    }
}