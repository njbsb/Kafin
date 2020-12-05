package com.fyp.kafin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SavingGoal {
    private String savingID;
    private float goalAmount;
    private String dateStart;
    private String dateEnd;
    private String dateCreated;
    private ArrayList<Commitment> commitments;
    private boolean activeStatus;

    public SavingGoal() {
    }

    public SavingGoal(String savingID, float goalAmount, String dateStart, String dateEnd, String dateCreated, ArrayList<Commitment> commitments, boolean activeStatus) {
        this.savingID = savingID;
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateCreated = dateCreated;
        this.commitments = commitments;
        this.activeStatus = activeStatus;
    }

    public SavingGoal(String savingID, float goalAmount, String dateStart, String dateEnd, String dateCreated, ArrayList<Commitment> commitments) {
        this.savingID = savingID;
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateCreated = dateCreated;
        this.commitments = commitments;
    }

    public SavingGoal(float goalAmount, String dateStart, String dateEnd, ArrayList<Commitment> commitments, String dateCreated) {
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.commitments = commitments;
        this.dateCreated = dateCreated;
    }
    public SavingGoal(float goalAmount, String dateStart, String dateEnd, ArrayList<Commitment> commitments, String dateCreated, boolean activeStatus) {
        this.goalAmount = goalAmount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.commitments = commitments;
        this.dateCreated = dateCreated;
        this.activeStatus = activeStatus;
    }

    public String getSavingID() {
        return savingID;
    }

    public void setSavingID(String savingID) {
        this.savingID = savingID;
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

    public void setGoalAmount(float goalAmount) {
        this.goalAmount = goalAmount;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }
}
