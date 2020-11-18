package com.fyp.kafin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.kafin.R;
import com.fyp.kafin.model.SavingGoal;

import java.util.ArrayList;

public class ArchivedGoalAdapter extends RecyclerView.Adapter<ArchivedGoalAdapter.ViewHolder> {
    ArrayList<SavingGoal> savingGoals;
    private LayoutInflater inflater;
    private Context context;

    @NonNull
    @Override
    public ArchivedGoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public ArchivedGoalAdapter(ArrayList<SavingGoal> savingGoals, Context context) {
        this.savingGoals = savingGoals;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivedGoalAdapter.ViewHolder holder, int position) {
        SavingGoal goal = savingGoals.get(position);
//        holder.duration.setText(goal.getStartingDate().toString());
    }

    @Override
    public int getItemCount() {
        return savingGoals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration, saving, achieved;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.archive_duration);
            saving = itemView.findViewById(R.id.archive_saving);
            achieved = itemView.findViewById(R.id.archive_achieved);
            icon = itemView.findViewById(R.id.archive_icon);
        }
    }
}
