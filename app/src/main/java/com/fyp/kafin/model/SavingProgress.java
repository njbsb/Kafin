package com.fyp.kafin.model;

public class SavingProgress {

    private String progressID;
    private String date;
    private float spentToday;

    public SavingProgress() {
    }

    public SavingProgress(String date, float spentToday) {
        this.date = date;
        this.spentToday = spentToday;
    }

    public SavingProgress(String progressID, String date, float spentToday) {
        this.progressID = progressID;
        this.date = date;
        this.spentToday = spentToday;
    }

    public String getProgressID() {
        return progressID;
    }

    public void setProgressID(String progressID) {
        this.progressID = progressID;
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
