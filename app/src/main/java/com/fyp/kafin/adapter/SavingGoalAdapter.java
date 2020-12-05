package com.fyp.kafin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.fyp.kafin.R;
import com.fyp.kafin.activity.SavingGoalDetailsActivity;
import com.fyp.kafin.controller.SavingGoalController;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.User;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SavingGoalAdapter extends PagerAdapter {

    private final ArrayList<SavingGoal> savingGoals;
    private LayoutInflater inflater;
    private final Context context;
    private final User user = User.getInstance();
    Locale MY = new Locale("en", "MY");

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
        final SavingGoal savingGoal = savingGoals.get(position);
        final SavingGoalController savingGoalController = new SavingGoalController(savingGoal, user);
        TextView title, period, duration, dailyExpenseLimit, totalSaved, totalDue, createdAt;

        CardView card = view.findViewById(R.id.card_saving);
        title = view.findViewById(R.id.saving_card_title);
        period = view.findViewById(R.id.saving_period);
        duration = view.findViewById(R.id.saving_duration);
        dailyExpenseLimit = view.findViewById(R.id.analytic_dailyExpenseLimit);
        totalSaved = view.findViewById(R.id.saving_totalSaved);
        totalDue = view.findViewById(R.id.saving_totalDue);
        createdAt = view.findViewById(R.id.saving_createdAt);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SavingGoalDetailsActivity.class);
                i.putExtra("savingID", savingGoal.getSavingID());
                context.startActivity(i);
//                Toast.makeText(context, String.valueOf(savingGoalController.getAllowedDailyExpenses()), Toast.LENGTH_SHORT).show();
            }
        });

        String savingTitle = moneyFormat(savingGoal.getGoalAmount());
        String startDate = savingGoalController.get_formattedDate(savingGoal.getDateStart());
        String endDate = savingGoalController.get_formattedDate(savingGoal.getDateEnd());
        String created_at = savingGoalController.get_formattedDateDay(savingGoal.getDateCreated());
        String savingDuration = String.valueOf(savingGoalController.getSavingDuration());
        String dailyLimit = moneyFormat(savingGoalController.getAllowedDailyExpenses());
//        String savingDaily = "RM " + df.format(savingGoals.get(position).getMaxDailyExpense());
//        String savingMonthly = "RM " + df.format(savingGoals.get(position).getMonthlyExpense());
//        String savingTotal = "RM " + df.format(savingGoals.get(position).getTotalSaved());
//        Toast.makeText(context, "duration: "+savingGoalController.getSavingDuration(), Toast.LENGTH_SHORT).show();
        title.setText(savingTitle);
        period.setText(String.format("%s to %s", startDate, endDate));
        duration.setText(String.format("(%s days)", savingDuration));
        dailyExpenseLimit.setText(dailyLimit);
        totalSaved.setText("No data");
        totalDue.setText("No data");
        createdAt.setText("Created at: " + created_at);
        
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private String moneyFormat(float value) {
        return NumberFormat.getCurrencyInstance(MY).format(value);
    }
}
