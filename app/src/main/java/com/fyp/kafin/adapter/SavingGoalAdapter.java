package com.fyp.kafin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.fyp.kafin.R;
import com.fyp.kafin.activity.MainActivity;
import com.fyp.kafin.model.SavingGoal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SavingGoalAdapter extends PagerAdapter {

    private final ArrayList<SavingGoal> savingGoals;
    private LayoutInflater inflater;
    private final Context context;

    public SavingGoalAdapter(ArrayList<SavingGoal> savingGoals, Context context) {
        this.savingGoals = savingGoals;
        this.context = context;
    }

    @Override
    public int getCount() {
        return savingGoals.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_saving, container, false);
        TextView title, period, duration, daily, monthly, totalSaved;
        CardView card = view.findViewById(R.id.card_saving);
        title = view.findViewById(R.id.saving_card_title);
        period = view.findViewById(R.id.saving_period);
        duration = view.findViewById(R.id.saving_duration);
        daily = view.findViewById(R.id.analytic_daily);
        monthly = view.findViewById(R.id.analytic_monthly);
        totalSaved = view.findViewById(R.id.analytic_totalSaved);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
            }
        });

        DecimalFormat df = new DecimalFormat("#.##");

        String savingTitle = "RM " + df.format(savingGoals.get(position).getGoalAmount());
        String savingPeriod = "20/11 to 30/11";
        String savingDuration = savingGoals.get(position).getSavingDuration() + " months";
        String savingDaily = "RM " + df.format(savingGoals.get(position).get_dailyExpense());
        String savingMonthly = "RM " + df.format(savingGoals.get(position).get_monthlyExpense());
        String savingTotal = "RM " + df.format(savingGoals.get(position).get_totalSaved());

        title.setText(savingTitle);
        period.setText(savingPeriod);
        duration.setText(savingDuration);
        daily.setText(savingDaily);
        monthly.setText(savingMonthly);
        totalSaved.setText(savingTotal);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
