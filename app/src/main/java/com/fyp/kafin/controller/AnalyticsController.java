package com.fyp.kafin.controller;

import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

public class AnalyticsController {

    SavingGoal savingGoal;
    ArrayList<SavingProgress> progressList;

    public AnalyticsController() {
    }

    public AnalyticsController(SavingGoal savingGoal, ArrayList<SavingProgress> progressList) {
        this.savingGoal = savingGoal;
        this.progressList = progressList;
    }

}
