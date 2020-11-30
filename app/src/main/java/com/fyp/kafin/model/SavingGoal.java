package com.fyp.kafin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SavingGoal {
    private long goalAmount;
    private Date dateStart;
    private Date dateEnd;
    private ArrayList<Commitment> commitments;

    public SavingGoal(long goalAmount, Date dateStart, Date dateEnd, ArrayList<Commitment> commitments) {
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.commitments = commitments;
    }

    public ArrayList<Commitment> getCommitments() {
        return commitments;
    }

    public void setCommitments(ArrayList<Commitment> commitments) {
        this.commitments = commitments;
    }

    public float getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(long goalAmount) {
        this.goalAmount = goalAmount;
    }

    public Date getStartingDate() {
        return dateStart;
    }

    public void setStartingDate(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getEndingDate() {
        return dateEnd;
    }

    public void setEndingDate(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getSavingDuration() {
        long diff = dateEnd.getTime() - dateStart.getTime();
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
    }

    public long getMaxDailyExpense() {
        long diff = dateEnd.getTime() - dateStart.getTime();
        int duration = (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
        return goalAmount/duration;
    }

    public float getMonthlyExpense() {
        return 30;
    }

    public float getTotalSaved() {
        return 200;
    }
}
