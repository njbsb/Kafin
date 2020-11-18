package com.fyp.kafin.model;

import java.math.BigDecimal;
import java.util.Date;

public class SavingGoal {
    private float goalAmount;
    private Date startingDate;
    private Date endingDate;

    public SavingGoal(float goalAmount) {
        this.goalAmount = goalAmount;
    }

    public float getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(float goalAmount) {
        this.goalAmount = goalAmount;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public int getSavingDuration() {
        return 0;
    }

    public float get_dailyExpense() {
        return 1;
    }

    public float get_monthlyExpense() {
        return 30;
    }

    public float get_totalSaved() {
        return 200;
    }
}
