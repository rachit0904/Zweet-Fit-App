package com.practise.zweet_fit_app.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.practise.zweet_fit_app.Adapters.StatViewCardAdapter;
import com.practise.zweet_fit_app.Modals.StatRecordModal;
import com.practise.zweet_fit_app.R;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeeklyStatView extends Fragment implements View.OnClickListener {
    List<StatRecordModal> statRecordModalList=new ArrayList<>();
    BarGraphSeries<DataPoint> barGraphSeries;
    TextView steps,week;
    ImageView prevWeek,nextWeek;
    int netSteps=0;
    LocalDate date=LocalDate.now();
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_weekly_stat_view, container, false);
        GraphView graph = view.findViewById(R.id.weeklyGraphView);
        RecyclerView recyclerView=view.findViewById(R.id.weekyStatRv);
        week=view.findViewById(R.id.week);
        prevWeek=view.findViewById(R.id.prevWeek);
        nextWeek=view.findViewById(R.id.nextWeek);
        steps=view.findViewById(R.id.weeklySteps);
        prevWeek.setOnClickListener(this);
        nextWeek.setOnClickListener(this);
        setWeek(date);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        StatViewCardAdapter statViewCardAdapter=new StatViewCardAdapter(getWeekyStats());
        recyclerView.setAdapter(statViewCardAdapter);
        recyclerView.setHasFixedSize(true);
        barGraphSeries = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(2, Integer.parseInt(statRecordModalList.get(0).getSteps())),
                new DataPoint(3, Integer.parseInt(statRecordModalList.get(1).getSteps())),
                new DataPoint(4, Integer.parseInt(statRecordModalList.get(2).getSteps())),
                new DataPoint(5, Integer.parseInt(statRecordModalList.get(3).getSteps())),
                new DataPoint(6, 0),
                new DataPoint(7, 0),
                new DataPoint(8, 0),
        });
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Mon","Tue", "Wed","Thu","Fri","Sat","Sun"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"500", "1000", "1500","2000","2200","4000"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.addSeries(barGraphSeries);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
        barGraphSeries.setSpacing(50);
        barGraphSeries.setColor(Color.parseColor("#1976D2"));
        barGraphSeries.setDataWidth(0.6);
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setAnimated(true);
        steps.setText(String.valueOf(netSteps));
        return view;
    }

    private List<StatRecordModal> getWeekyStats() {
        for (int i=0;i<3;i++){
            StatRecordModal modal=new StatRecordModal(
                    (28+i)+" Nov",
                    String.valueOf((i+1)*530),
                    true);
            statRecordModalList.add(modal);
            netSteps+=(i+1)*530;
        }
        for (int i=1;i<2;i++){
            StatRecordModal modal=new StatRecordModal(
                    i+" Dec",
                    String.valueOf(1*1800-20*i),
                    false);
            statRecordModalList.add(modal);
            netSteps+=1*1800-20*i;
        }
        return  statRecordModalList;
    }
    void setWeek(LocalDate date){
        this.date=date;
        try {
            LocalDate start = date;
            while (start.getDayOfWeek() != DayOfWeek.MONDAY) {
                start = start.minusDays(1);
            }
            LocalDate end = date;
            while (end.getDayOfWeek() != DayOfWeek.SUNDAY) {
                end = end.plusDays(1);
            }
            week.setText(
                    new StringBuilder()
                            .append(start.toString().split("-")[2]).append(" ")
                            .append(months[Integer.parseInt(start.toString().split("-")[1])-1])
                            .append(" - ").append(end.toString().split("-")[2])
                            .append(" ")
                            .append(months[Integer.parseInt(end.toString().split("-")[1])-1]).toString()
            );
        }catch(Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==prevWeek){
            setWeek(date.minusWeeks(1));
        }
        if(view==nextWeek){
            setWeek(date.plusWeeks(1));
        }
    }
}
