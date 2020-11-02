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

import java.util.ArrayList;
import java.util.List;

public class AnalyticsFragment extends Fragment {

    public AnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
//        anyChartView.setBackgroundColor(Color.TRANSPARENT);
//        anyChartView.background().fill("gold");
        Pie pie = AnyChart.pie();
//        pie.setOnClickListener(new ListenersInterface.OnClickListener() {
//            @Override
//            public void onClick(Event event) {
//
//            }
//        });
        pie.background().fill("transparent");
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
//                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Tidur", 6371664));
        data.add(new ValueDataEntry("Makan", 789622));
        data.add(new ValueDataEntry("Youtube", 7216301));
        data.add(new ValueDataEntry("Game", 1486621));
        data.add(new ValueDataEntry("Buat FYP", 1200000));

        pie.data(data);

        pie.title("Total time spent");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Activity Type")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
        return view;
    }
}