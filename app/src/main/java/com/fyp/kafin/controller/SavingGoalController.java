package com.fyp.kafin.controller;

import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SavingGoalController {
    private final SavingGoal savingGoal;
    private final User user;
    private static final int daysInMonth = 28;
    Locale myLocale = new Locale("en", "MY");
    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", myLocale);
    private ArrayList<SavingProgress> progressList;

    public SavingGoalController(SavingGoal savingGoal, User user) {
        this.savingGoal = savingGoal;
        this.user = user;
    }

    public SavingGoalController(SavingGoal savingGoal, User user, ArrayList<SavingProgress> progressList) {
        this.savingGoal = savingGoal;
        this.user = user;
        this.progressList = progressList;
    }

    public ArrayList<SavingProgress> getProgressList() {
        return progressList;
    }

    public void setProgressList(ArrayList<SavingProgress> progressList) {
        this.progressList = progressList;
    }

    public String get_formattedDateDay(String stringDate) {
        try {
            Date date = simpleFormat.parse(stringDate);
            SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM yyyy (EEE)", myLocale);
            return fullDateFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String get_formattedDate(String stringDate) {
        try {
            Date date = simpleFormat.parse(stringDate);
            SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM yyyy", myLocale);
            return fullDateFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getSavingDuration() {
        // duration in days between two dates
        String dateStartString = savingGoal.getDateStart();
        String dateEndString = savingGoal.getDateEnd();
        try {
            Date dateStart = simpleFormat.parse(dateStartString);
            Date dateEnd = simpleFormat.parse(dateEndString);
            long diff = Objects.requireNonNull(dateEnd).getTime() - Objects.requireNonNull(dateStart).getTime();
            return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getComAsString() {
        ArrayList<Commitment> commitments = savingGoal.getCommitments();
        StringBuilder c = new StringBuilder();
        for(int i = 0; i<commitments.size(); i++) {
            c.append(commitments.get(i).getComName());
        }
        return c.toString();
    }

    public float getTotalCommitmentAmount() {
        // user's monthly commitment amount
        float monthlyComAmount = 0;
        for(int i = 0; i<savingGoal.getCommitments().size(); i++) {
            monthlyComAmount += savingGoal.getCommitments().get(i).getComAmount();
        }
        return monthlyComAmount;
    }

    public float getMonthlySavingTarget() {
        // how much user has to save in each month
        float savingTargetAmount = savingGoal.getGoalAmount();
        int savingDuration = getSavingDuration();
        int month = savingDuration/daysInMonth;
        if(month > 0) {
            return savingTargetAmount / month;
        } else {
            return savingTargetAmount;
        }
    }

    public float getAllowedMonthlyExpenses() {
        // how much user can spend after substracting commitment and saving
        float monthlyIncome = user.getMonthlyIncome();
        float monthlyCommitmentAmount = getTotalCommitmentAmount();
        float monthlySavingTarget = getMonthlySavingTarget();
        return monthlyIncome - monthlyCommitmentAmount - monthlySavingTarget;
    }

    public float getAllowedDailyExpenses() {
        // how much user can spend in a day
        float allowedMonthlyExpenses = getAllowedMonthlyExpenses();
        return allowedMonthlyExpenses/daysInMonth;
    }

    public float getCumulativeSaved() {
        float cumulativeSaved = 0;
        float dailyExpenseLimit = getAllowedDailyExpenses();
        if(progressList != null) {
            for(int i = 0; i<progressList.size(); i++) {
                float savedToday = dailyExpenseLimit - progressList.get(i).getSpentToday();
                cumulativeSaved += savedToday;
            }
        }
        return cumulativeSaved;
    }

    public float getCumulativeSpent() {
        float cumulativeSpent = 0;
        if(progressList != null) {
            for(int i = 0; i<progressList.size(); i++) {
                cumulativeSpent += progressList.get(i).getSpentToday();
            }
        }
        return cumulativeSpent;
    }
}
