package com.fyp.kafin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.kafin.R;
import com.fyp.kafin.controller.SavingGoalController;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SavingProgressAdapter extends RecyclerView.Adapter<SavingProgressAdapter.ViewHolder> {

    SavingGoal savingGoal;
    ArrayList<SavingProgress> savingProgresses;
    Context context;
    static DecimalFormat df2 = new DecimalFormat("#.##");

    public SavingProgressAdapter(SavingGoal savingGoal, ArrayList<SavingProgress> savingProgresses, Context context) {
        this.savingGoal = savingGoal;
        this.savingProgresses = savingProgresses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_saving_progress, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavingProgress progress = savingProgresses.get(position);
        SavingGoalController savingGoalController = new SavingGoalController(savingGoal, User.getInstance());
        holder.progressDate.setText(progress.getDate());
        holder.dailySpent.setText(String.format("RM %s", df2.format(progress.getSpentToday())));
        holder.dailySaved.setText(String.format("RM %s",df2.format(savingGoalController.getAllowedDailyExpenses() - progress.getSpentToday())));
    }

    @Override
    public int getItemCount() {
        return savingProgresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView progressDate, dailySpent, dailySaved;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressDate = itemView.findViewById(R.id.progress_date);
            dailySpent = itemView.findViewById(R.id.progress_spentToday);
            dailySaved= itemView.findViewById(R.id.progress_savedToday);
        }
    }
}
