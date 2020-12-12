package com.fyp.kafin.controller;

import android.util.Log;

import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SavingGoalController {
    private final SavingGoal savingGoal;
    private final User user;
    private static final int daysInMonth = 28;
    private static final int daysInWeek = 7;
    Locale myLocale = new Locale("en", "MY");
    SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", myLocale);
    SimpleDateFormat ddMM = new SimpleDateFormat("dd.MM", myLocale);
    private ArrayList<SavingProgress> progressList;
    HashMap<String, SavingProgress> progressMap;
    ArrayList<Commitment> commitments;

    public SavingGoalController(SavingGoal savingGoal, User user) {
        this.savingGoal = savingGoal;
        this.user = user;
    }

    public ArrayList<Commitment> getCommitments() {
        return commitments;
    }

    public void setCommitments(ArrayList<Commitment> commitments) {
        this.commitments = commitments;
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
            // ceil?
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getNoWeek() {
        float noWeek = (float) getSavingDuration()/daysInWeek;
        return Math.ceil(noWeek);
    }

    public double getNoMonth() {
        float noMonth = (float) getSavingDuration()/daysInMonth;
        return Math.ceil(noMonth);
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

    public HashMap<String, SavingProgress> getProgressMap() {
        progressMap = new HashMap<>();
        for(SavingProgress progress: progressList) {
            progressMap.put(progress.getDate(), progress);
        }
        return progressMap;
    }

    public String getFormattedMoney(float value) {
        return NumberFormat.getCurrencyInstance(myLocale).format(value);
    }

    public ArrayList<BarEntry> getWeeklyProgress() {
        ArrayList<BarEntry> weeklyBar = new ArrayList<>();
        int noWeek = (int) getNoWeek();
        int noDays = getSavingDuration();
        String dateStart = savingGoal.getDateStart();
        progressMap = getProgressMap();
        try {
            for(int i = 0; i<noWeek; i++) {
                Date startDate = simpleFormat.parse(dateStart);
                Calendar calendar = Calendar.getInstance();
                float sum = 0;
                for(int j = i*daysInWeek; j<(i+1)*daysInWeek && j<noDays; j++) {
                    calendar.setTime(Objects.requireNonNull(startDate));
                    calendar.add(Calendar.DAY_OF_MONTH, j);
                    Date thisDate = calendar.getTime();
                    String dateString = simpleFormat.format(thisDate);
                    SavingProgress todayProgress = progressMap.get(dateString);
                    if(todayProgress != null) {
                        sum += Objects.requireNonNull(todayProgress).getSpentToday();
                        Log.e("Loop " + i, "Loop j: "+ j);
                        Log.e(todayProgress.getDate(), String.valueOf(todayProgress.getSpentToday()));
                    }
                }
                Log.e("sum week "+ (i + 1), String.valueOf(sum));
                weeklyBar.add(new BarEntry(i, sum));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weeklyBar;
    }

    public ArrayList<String> getWeeklyLabel() {
        ArrayList<String> weeklyLabelList = new ArrayList<>();
        int noWeek = (int) getNoWeek();
        try {
            Date startDate = simpleFormat.parse(savingGoal.getDateStart());
            Calendar calendar = Calendar.getInstance();
            for(int i = 0; i<noWeek; i++) {
                calendar.setTime(Objects.requireNonNull(startDate));
                calendar.add(Calendar.DAY_OF_MONTH, i*daysInWeek);
                Date start = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, daysInWeek - 1);
                Date end = calendar.getTime();
//                weeklyLabelList.add(ddMM.format(start) + " - " + ddMM.format(end));
                weeklyLabelList.add("Week " + (i+1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weeklyLabelList;
    }

    public ArrayList<BarEntry> getMonthlyProgress() {
        ArrayList<BarEntry> monthlyBar = new ArrayList<>();
        int noMonth = (int) getNoMonth();
        int noDays = getSavingDuration();
        String dateStart = savingGoal.getDateStart();
        progressMap = getProgressMap();
        try {
            for(int i = 0; i < noMonth; i++) {
                Date startDate = simpleFormat.parse(dateStart);
                Calendar calendar = Calendar.getInstance();
                float sum = 0;
                for(int j = i*daysInMonth; j<(i+1)*daysInMonth && j<noDays; j++) {
                    calendar.setTime(Objects.requireNonNull(startDate));
                    calendar.add(Calendar.DAY_OF_MONTH, j);
                    Date thisDate = calendar.getTime();
                    String dateString = simpleFormat.format(thisDate);
                    SavingProgress todayProgress = progressMap.get(dateString);
                    if(todayProgress != null) {
                        sum += Objects.requireNonNull(todayProgress).getSpentToday();
                        Log.e("Loop " + i, "Loop j: "+ j);
                        Log.e(todayProgress.getDate(), String.valueOf(todayProgress.getSpentToday()));
                    }
                }
                Log.e("sum week "+ (i + 1), String.valueOf(sum));
                monthlyBar.add(new BarEntry(i, sum));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthlyBar;
    }

    public ArrayList<String> getMonthlyLabel() {
        ArrayList<String> monthlyLabelList = new ArrayList<>();
        int noMonth = (int) getNoMonth();
        try {
            Date startDate = simpleFormat.parse(savingGoal.getDateStart());
            Calendar calendar = Calendar.getInstance();
            for(int i = 0; i<noMonth; i++) {
                calendar.setTime(Objects.requireNonNull(startDate));
                calendar.add(Calendar.DAY_OF_MONTH, i*daysInMonth);
                Date start = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, daysInMonth - 1);
                Date end = calendar.getTime();
//                monthlyLabelList.add(ddMM.format(start) + " - " + ddMM.format(end));
                monthlyLabelList.add("Month " + (i+1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthlyLabelList;
    }

    public ArrayList<PieEntry> getCommitmentPie() {
        ArrayList<PieEntry> commitmentPieList = new ArrayList<>();
        for (int i = 0; i<commitments.size(); i++) {
            commitmentPieList.add(new PieEntry(commitments.get(i).getComAmount(), commitments.get(i).getComName()));
        }
        return commitmentPieList;
    }

    public Locale getMyLocale() {
        return myLocale;
    }

}
