package com.fyp.kafin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.kafin.R;
import com.fyp.kafin.activity.EditProgressActivity;
import com.fyp.kafin.controller.SavingGoalController;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;

import java.util.ArrayList;

public class SavingProgressAdapter extends RecyclerView.Adapter<SavingProgressAdapter.ViewHolder> {

    SavingGoal savingGoal;
    ArrayList<SavingProgress> savingProgresses;
    private final Context context;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SavingProgress progress = savingProgresses.get(position);
        SavingGoalController controller = new SavingGoalController(savingGoal, User.getInstance());
        holder.progressDate.setText(progress.getDate());
        holder.dailySpent.setText(controller.getFormattedMoney(progress.getSpentToday()));
        holder.dailySaved.setText(controller.getFormattedMoney(controller.getAllowedDailyExpenses() - progress.getSpentToday()));

        if(progress.getSpentToday() > controller.getAllowedDailyExpenses()) {
            holder.dailySpent.setTextColor(ContextCompat.getColor(context, R.color.colorDanger));
        }
        float dailySaved = controller.getAllowedDailyExpenses() - progress.getSpentToday();
        if(dailySaved < 0) {
            holder.dailySaved.setTextColor(ContextCompat.getColor(context, R.color.colorWarning));
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditProgressActivity.class);
                i.putExtra(EditProgressActivity.PROGRESS_ID, progress.getProgressID());
                i.putExtra(EditProgressActivity.SAVING_ID, savingGoal.getSavingID());
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return savingProgresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView progressDate, dailySpent, dailySaved;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_progress);
            progressDate = itemView.findViewById(R.id.progress_date);
            dailySpent = itemView.findViewById(R.id.progress_spentToday);
            dailySaved= itemView.findViewById(R.id.progress_savedToday);
        }
    }

}
