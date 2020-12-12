package com.fyp.kafin.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.fyp.kafin.R;
import com.fyp.kafin.adapter.SavingProgressAdapter;
import com.fyp.kafin.controller.SavingGoalController;
import com.fyp.kafin.model.Commitment;
import com.fyp.kafin.model.SavingGoal;
import com.fyp.kafin.model.SavingProgress;
import com.fyp.kafin.model.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTimeComparator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AnalyticsFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart pieChart;
    private BarChart weeklyBar, monthlyBar;
    private ProgressBar progressBar;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User appUser = User.getInstance();
    ArrayList<Commitment> commitmentList;
    ArrayList<String> weeklyLabelName, monthlyLabelName;
    ArrayList<BarEntry> weeklyBarEntry, monthlyBarEntry;
    ArrayList<SavingProgress> progressList;
    SavingGoal savingGoal;
    SavingGoalController controller;
    String savingID;

    public AnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart = view.findViewById(R.id.analytic_pieChart);
        weeklyBar = view.findViewById(R.id.analytic_weeklyBar);
        monthlyBar = view.findViewById(R.id.analytic_monthlyBar);
        progressBar = view.findViewById(R.id.analytic_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        loadSavingGoal();
        loadCommitment();
        return view;
    }

    private void loadCommitment() {
        commitmentList = new ArrayList<>();
        DatabaseReference comRef = dbRef.child("commitments").child(user.getUid());
        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    commitmentList.add(commitment);
                }
                finishPieChart(commitmentList, pieChart);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void finishPieChart(ArrayList<Commitment> commitments, PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setOnChartValueSelectedListener(this);

        ArrayList<PieEntry> values = getCommitmentPie(commitments);

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        Description d = new Description();
        d.setText("Commitments je");
//        d.setTextAlign(Paint.Align.CENTER);
        pieChart.setDescription(d);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        data.setValueFormatter(new PercentFormatter(pieChart));

//        Legend l = pieChart.getLegend();
//
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(true);
//        l.setEnabled(true);

        pieChart.setData(data);
        pieChart.animate();
        pieChart.setUsePercentValues(true);
        pieChart.animateY(1000, Easing.EaseInOutCubic);
    }

    private void loadSavingGoal() {
        DatabaseReference savingRef = dbRef.child("savinggoals").child(user.getUid());
        savingRef.orderByChild("activeStatus").equalTo(true).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    savingGoal = dataSnapshot.getValue(SavingGoal.class);
                    Log.d("snapshot.getValue", String.valueOf(dataSnapshot.getValue()));
                }
                savingID = savingGoal.getSavingID();
                Toast.makeText(getActivity(), "Data loaded successfully", Toast.LENGTH_SHORT).show();

                progressList = new ArrayList<>();
                DatabaseReference progressRef = dbRef.child("progress").child(user.getUid()).child(savingGoal.getSavingID());
                progressRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                            progressList.add(progress);
                        }
                        controller = new SavingGoalController(savingGoal, appUser, progressList);
                        weeklyLabelName = controller.getWeeklyLabel();
                        weeklyBarEntry = controller.getWeeklyProgress();
                        monthlyLabelName = controller.getMonthlyLabel();
                        monthlyBarEntry = controller.getMonthlyProgress();
                        finishBarChart(weeklyBarEntry, weeklyLabelName, weeklyBar);
                        finishBarChart(monthlyBarEntry, monthlyLabelName, monthlyBar);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void finishBarChart(ArrayList<BarEntry> barEntries, ArrayList<String> labelName, BarChart barChart) {
        BarDataSet barDataSet = new BarDataSet(barEntries, "Weekly");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setSpaceMin(3f);
        xAxis.setLabelCount(labelName.size());
        xAxis.setLabelRotationAngle(0);
        barChart.getDescription().setText("....");
        barChart.animateY(2000);
        barChart.invalidate();
    }

    private ArrayList<PieEntry> getCommitmentPie(ArrayList<Commitment> commitments) {
        ArrayList<PieEntry> commitmentPieList = new ArrayList<>();
        for (int i = 0; i<commitments.size(); i++) {
            Commitment c = commitments.get(i);
            commitmentPieList.add(new PieEntry(c.getComAmount(), c.getComName()));
        }
        return commitmentPieList;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(getActivity(), NumberFormat.getCurrencyInstance(controller.getMyLocale()).format(e.getY()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
}