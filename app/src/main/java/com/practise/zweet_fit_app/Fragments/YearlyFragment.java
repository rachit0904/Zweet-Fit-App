package com.practise.zweet_fit_app.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.practise.zweet_fit_app.Adapters.StatViewCardAdapter;
import com.practise.zweet_fit_app.Modals.StatRecordModal;
import com.practise.zweet_fit_app.R;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class YearlyFragment extends Fragment implements View.OnClickListener {
    TextView steps,year;
    ImageView prevYear,nextYear;
    LocalDate date=LocalDate.now();
    BarGraphSeries<DataPoint> barGraphSeries;
    int netSteps=0;
    String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
    List<StatRecordModal> statRecordModalList=new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yearly,container,false);
        year=view.findViewById(R.id.year);
        prevYear=view.findViewById(R.id.prevYear);
        nextYear=view.findViewById(R.id.nextYear);
        prevYear.setOnClickListener(this);
        nextYear.setOnClickListener(this);
        RecyclerView recyclerView=view.findViewById(R.id.yearlyStatRv);
        GraphView graph = view.findViewById(R.id.yearlyGraphView);
        steps=view.findViewById(R.id.yearlySteps);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), GridLayoutManager.VERTICAL,false));
        StatViewCardAdapter statViewCardAdapter=new StatViewCardAdapter(getYearlyStats());
        recyclerView.setAdapter(statViewCardAdapter);
        recyclerView.setHasFixedSize(true);
        steps.setText(String.valueOf(netSteps));
        barGraphSeries = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, Integer.parseInt(statRecordModalList.get(0).getSteps())),
                new DataPoint(2, Integer.parseInt(statRecordModalList.get(1).getSteps())),
                new DataPoint(3, Integer.parseInt(statRecordModalList.get(2).getSteps())),
                new DataPoint(4, Integer.parseInt(statRecordModalList.get(3).getSteps())),
                new DataPoint(5, Integer.parseInt(statRecordModalList.get(4).getSteps())),
                new DataPoint(6, Integer.parseInt(statRecordModalList.get(5).getSteps())),
                new DataPoint(7, Integer.parseInt(statRecordModalList.get(6).getSteps())),
                new DataPoint(8, Integer.parseInt(statRecordModalList.get(7).getSteps())),
                new DataPoint(9, Integer.parseInt(statRecordModalList.get(8).getSteps())),
                new DataPoint(10, Integer.parseInt(statRecordModalList.get(9).getSteps())),
                new DataPoint(11, Integer.parseInt(statRecordModalList.get(10).getSteps())),
                new DataPoint(12, Integer.parseInt(statRecordModalList.get(11).getSteps())),
        });
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"J","F", "M","A","M","J","J","A","S","O","N","D"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"500", "1000", "1500","2000","2200","4000"});
        setDate(date);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.addSeries(barGraphSeries);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
        barGraphSeries.setSpacing(20);
        barGraphSeries.setColor(Color.parseColor("#1976D2"));
        barGraphSeries.setDataWidth(0.15);
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setAnimated(true);
        return view;
    }

    private List<StatRecordModal> getYearlyStats    () {
        for (int i=0;i<4;i++){
            StatRecordModal modal=new StatRecordModal(
                    months[i],
                    String.valueOf((i+1)*25350),
                    false);
            statRecordModalList.add(modal);
            netSteps+=(i+1)*25350;
        }
        for (int i=4;i<8;i++){
            StatRecordModal modal=new StatRecordModal(
                    months[i],
                    String.valueOf((i+1)*5350),
                    false);
            statRecordModalList.add(modal);
            netSteps+=(i+1)*50;
        }
        for (int i=8;i<12;i++){
            StatRecordModal modal=new StatRecordModal(
                    months[i],
                    String.valueOf((i+1)*32150),
                    false);
            statRecordModalList.add(modal);
            netSteps+=(i+1)*150;
        }
        return  statRecordModalList;
    }

    public void setDate(LocalDate date) {
        this.date=date;
        year.setText(date.toString().split("-")[0]);
    }

    @Override
    public void onClick(View view) {
        if(view==prevYear){
            setDate(date.minusYears(1));
        }
        if(view==nextYear){
            setDate(date.plusYears(1));
        }
    }
}
