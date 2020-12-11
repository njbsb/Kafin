package com.fyp.kafin.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AnalyticsFragment extends Fragment {

    private PieChart pieChart;
    private BarChart comBarChart;
    private ProgressBar progressBar;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User appUser = User.getInstance();
    ArrayList<Commitment> commitmentList;
    ArrayList<String> labelName;
    ArrayList<SavingProgress> progressList;
    ArrayList<BarEntry> weeklyBarEntry;
    SavingGoal savingGoal;
    String savingID;

    public AnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart = view.findViewById(R.id.analytic_pieChart);
        comBarChart = view.findViewById(R.id.analytic_commitmentbar);
        progressBar = view.findViewById(R.id.analytic_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        loadSavingGoal();
//        loadProgress(savingID);
//        setUpPieChart();
//        setUpBarChart();
//        setUpWeeklyBarChart();
        return view;
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
                Toast.makeText(getContext(), "Data loaded successfully", Toast.LENGTH_SHORT).show();

                progressList = new ArrayList<>();

                progressList = new ArrayList<>();
                weeklyBarEntry = new ArrayList<>();
                DatabaseReference progressRef = dbRef.child("progress").child(user.getUid()).child(savingGoal.getSavingID());
                progressRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                            progressList.add(progress);
                        }
                        SavingGoalController controller = new SavingGoalController(savingGoal, appUser, progressList);
                        labelName = controller.getWeeklyLabel();
                        weeklyBarEntry = controller.getWeeklyProgress();
                        BarDataSet barDataSet = new BarDataSet(weeklyBarEntry, "Weekly");
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);
                        BarData barData = new BarData(barDataSet);
                        comBarChart.setFitBars(true);
                        comBarChart.setData(barData);
                        XAxis xAxis = comBarChart.getXAxis();
//                xAxis.setValueFormatter(new );
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
                        xAxis.setDrawGridLines(false);
                        xAxis.setDrawAxisLine(false);
                        xAxis.setGranularity(1f);
                        xAxis.setLabelCount(labelName.size());
                        xAxis.setLabelRotationAngle(270);
                        comBarChart.getDescription().setText("....");
                        comBarChart.animateY(2000);
                        comBarChart.invalidate();

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

    private void loadProgress(String savingID) {
        progressList = new ArrayList<>();
        DatabaseReference progressRef = dbRef.child("progress").child(user.getUid()).child(savingID);
        progressRef.orderByChild("savingID").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                    progressList.add(progress);
                }
                Collections.reverse(progressList);
                SavingGoalController controller = new SavingGoalController(savingGoal, appUser, progressList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setUpBarChart() {
        commitmentList = new ArrayList<>();
        final ArrayList<BarEntry> commitmentBar = new ArrayList<>();
        DatabaseReference comRef = dbRef.child("commitments").child(user.getUid());
        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    commitmentList.add(commitment);
//                    assert commitment != null;
//                    commitmentBar.add(new BarEntry(commitment.getComAmount(), commitment.getComAmount()));
                }
                labelName = new ArrayList<>();
                for(int i = 0; i<commitmentList.size(); i++) {
                    labelName.add(commitmentList.get(i).getComName());
                    commitmentBar.add(new BarEntry(i, commitmentList.get(i).getComAmount()));
                }
//                Description description = new Description();
//                description.setText("Months");
//                comBarChart.setDescription(description);

                BarDataSet barDataSet = new BarDataSet(commitmentBar, "Commitments");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                BarData barData = new BarData(barDataSet);
                comBarChart.setFitBars(true);
                comBarChart.setData(barData);
                XAxis xAxis = comBarChart.getXAxis();
//                xAxis.setValueFormatter(new );
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelName.size());
                xAxis.setLabelRotationAngle(270);
                comBarChart.getDescription().setText("....");
                comBarChart.animateY(2000);
                comBarChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void setUpWeeklyBarChart() {
        progressList = new ArrayList<>();
        weeklyBarEntry = new ArrayList<>();
        DatabaseReference progRef = dbRef.child("progress").child(user.getUid()).child(savingGoal.getSavingID());
        progRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    SavingProgress progress = dataSnapshot.getValue(SavingProgress.class);
                    progressList.add(progress);
                }
                SavingGoalController controller = new SavingGoalController(savingGoal, appUser, progressList);
                labelName = controller.getWeeklyLabel();
                weeklyBarEntry = controller.getWeeklyProgress();

                BarDataSet barDataSet = new BarDataSet(weeklyBarEntry, "Weekly");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                BarData barData = new BarData(barDataSet);
                comBarChart.setFitBars(true);
                comBarChart.setData(barData);
                XAxis xAxis = comBarChart.getXAxis();
//                xAxis.setValueFormatter(new );
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelName.size());
                xAxis.setLabelRotationAngle(270);
                comBarChart.getDescription().setText("....");
                comBarChart.animateY(2000);
                comBarChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUpPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(34f, "A"));
        values.add(new PieEntry(34f, "B"));
        values.add(new PieEntry(34f, "C"));

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(values, "Alpha");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Hello");
//        description.setTextAlign(Paint.Align.LEFT);
        description.setTextSize(12f);
        pieChart.setDescription(description);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);
        pieChart.animate();
        pieChart.setUsePercentValues(true);
    }
}