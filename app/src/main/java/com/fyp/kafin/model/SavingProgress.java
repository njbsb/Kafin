package com.fyp.kafin.model;

public class SavingProgress {
    private String date;
    private float spentToday;

    public SavingProgress() {
    }

    public SavingProgress(String date, float spentToday) {
        this.date = date;
        this.spentToday = spentToday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getSpentToday() {
        return spentToday;
    }

    public void setSpentToday(float spentToday) {
        this.spentToday = spentToday;
    }
}
