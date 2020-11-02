package com.fyp.kafin.model;

import java.math.BigDecimal;
import java.util.List;

public class User {
    private static User userInstance;
    private String username;
    private String userEmail;
    private BigDecimal monthlyIncome;
    private List<Commitment> userCommitment;
    private SavingGoal userSavingGoal;

    public static User getInstance() {
        if(userInstance == null) {
            userInstance = new User();
        }
        return userInstance;
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

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public List<Commitment> getUserCommitment() {
        return userCommitment;
    }

    public void setUserCommitment(List<Commitment> userCommitment) {
        this.userCommitment = userCommitment;
    }

    public SavingGoal getUserSavingGoal() {
        return userSavingGoal;
    }

    public void setUserSavingGoal(SavingGoal userSavingGoal) {
        this.userSavingGoal = userSavingGoal;
    }
}
