package com.fyp.kafin.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.fyp.kafin.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsFragment extends Fragment {

    private PieChart pieChart;

    public AnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart = view.findViewById(R.id.analytic_pieChart);
        setUpPieChart();
        return view;
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