package com.fyp.kafin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static User userInstance;
    private String username;
    private String userEmail;
    private float monthlyIncome;
    private ArrayList<Commitment> userCommitment;
    private String userID;

    public static User getInstance() {
        if(userInstance == null) {
            userInstance = new User();
        }
        return userInstance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public float getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(float monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public ArrayList<Commitment> getUserCommitment() {
        return userCommitment;
    }

    public void setUserCommitment(ArrayList<Commitment> userCommitment) {
        this.userCommitment = userCommitment;
    }
}
