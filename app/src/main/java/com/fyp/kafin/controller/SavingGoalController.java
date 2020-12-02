package com.fyp.kafin.controller;

import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
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
    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

    public SavingGoalController(SavingGoal savingGoal, User user) {
        this.savingGoal = savingGoal;
        this.user = user;
    }

    public String get_formattedDateDay(String stringDate) {
        try {
            Date date = simpleFormat.parse(stringDate);
            SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM yyyy (EEE)", Locale.UK);
            return fullDateFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String get_formattedDate(String stringDate) {
        try {
            Date date = simpleFormat.parse(stringDate);
            SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
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
            long diff = dateEnd.getTime() - dateStart.getTime();
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


}
