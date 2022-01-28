package com.practise.zweet_fit_app.Fragments;

import android.content.res.ColorStateList;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.practise.zweet_fit_app.Adapters.StatViewCardAdapter;
import com.practise.zweet_fit_app.Modals.StatRecordModal;
import com.practise.zweet_fit_app.R;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthlyFragment extends Fragment implements View.OnClickListener {
    final String curDayColorCode="#06417C";
    final String defaultDayColorCode="#3C1C1C1C";
    int monthlyStats=0;
    TextView monthlySteps,month;
    ImageView prevMonth,nextMonth;
    ChipGroup days;
    View view;
    RecyclerView monthlyRv;
    Chip d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13,d14,d15,d16,d17,d18,d19,d20,d21,d22,d23,d24,d25,d26,d27,d28,d29,d30,d31;
    String[] months ={"January","February","March","April","May","June","July","August","September","October","November","December"};
    LocalDate date=LocalDate.now();
    String mm="";
    List<StatRecordModal> statRecordModalList=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_monthly, container, false);
        initialize();
        initializeMonthChips();
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        setMonth(date);
        monthlyRv.setHasFixedSize(true);
        monthlyRv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        StatViewCardAdapter adapter=new StatViewCardAdapter(getMonthlyStats());
        monthlyRv.setAdapter(adapter);
        return view;
    }

    private void setDayChipColor(int day,String color){
        switch(day){
            case 1: {
                d1.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 2:{
                d2.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 3:{
                d3.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 4:{
                d4.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 5: {
                d5.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 6:{
                d6.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 7:{
                d7.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 8:{
                d8.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 9: {
                d9.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 10:{
                d10.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 11: {
                d11.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 12:{
                d12.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 13:{
                d13.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 14:{
                d14.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 15:{
                d15.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 16:{
                d16.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 17: {
                d17.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 18:{
                d18.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 19:{
                d19.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 20:{
                d20.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 21:{
                d21.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 22:{
                d22.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 23: {
                d23.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 24:{
                d24.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 25:{
                d25.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 26:{
                d26.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 27: {
                d27.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 28: {
                d28.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }case 29:{
                d29.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            } case 30:{
                d30.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }
            case 31:{
                d31.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(color)));
                break;
            }

        }
    }

    private void initializeMonthChips() {
        d1=view.findViewById(R.id.day1);d2=view.findViewById(R.id.day2);d3=view.findViewById(R.id.day3);d4=view.findViewById(R.id.day4);d5=view.findViewById(R.id.day5);
        d6=view.findViewById(R.id.day6);d7=view.findViewById(R.id.day7);d8=view.findViewById(R.id.day8);d9=view.findViewById(R.id.day9);d10=view.findViewById(R.id.day10);
        d11=view.findViewById(R.id.day11);d12=view.findViewById(R.id.day12);d13=view.findViewById(R.id.day13);d14=view.findViewById(R.id.day14);d15=view.findViewById(R.id.day15);
        d16=view.findViewById(R.id.day16);d17=view.findViewById(R.id.day17);d18=view.findViewById(R.id.day18);d19=view.findViewById(R.id.day19);d20=view.findViewById(R.id.day20);
        d21=view.findViewById(R.id.day21);d22=view.findViewById(R.id.day22);d23=view.findViewById(R.id.day23);d24=view.findViewById(R.id.day24);d25=view.findViewById(R.id.day25);
        d26=view.findViewById(R.id.day26);d27=view.findViewById(R.id.day27);d28=view.findViewById(R.id.day28);d29=view.findViewById(R.id.day29);d30=view.findViewById(R.id.day30);
        d31=view.findViewById(R.id.day31);
    }

    private void initialize() {
        days=view.findViewById(R.id.monlthDays);
        month=view.findViewById(R.id.month);
        prevMonth=view.findViewById(R.id.prevMonth);
        nextMonth=view.findViewById(R.id.nextMonth);
        monthlyRv=view.findViewById(R.id.monthlyStatRv);
    }

    private List<StatRecordModal> getMonthlyStats() {
            StatRecordModal modal1=new StatRecordModal(
              "1 Dec - 7 Dec",
              "2400",
              false
            );
            statRecordModalList.add(modal1);
        return statRecordModalList;
    }

    void setDays(int days) {
        if(days==31){
            d29.setVisibility(View.VISIBLE);
            d30.setVisibility(View.VISIBLE);
            d31.setVisibility(View.VISIBLE);
            d31.setVisibility(View.VISIBLE);
        }
        else if(days==28){
            d29.setVisibility(View.GONE);
            d30.setVisibility(View.GONE);
            d31.setVisibility(View.GONE);
        }else if(days==29){
            d30.setVisibility(View.GONE);
            d31.setVisibility(View.GONE);
        }
        else{
            d29.setVisibility(View.VISIBLE);
            d30.setVisibility(View.VISIBLE);
            d31.setVisibility(View.GONE);
        }
    }

    void setMonth(LocalDate date){
        if(Integer.parseInt(LocalDate.now().toString().split("-")[0])==Integer.parseInt(date.toString().split("-")[0])
             && Integer.parseInt(LocalDate.now().toString().split("-")[1])==Integer.parseInt(date.toString().split("-")[1])
            && Integer.parseInt(LocalDate.now().toString().split("-")[2])==Integer.parseInt(date.toString().split("-")[2])){
            setDayChipColor(Integer.parseInt(this.date.toString().split("-")[2]),curDayColorCode);
        }else{
            setDayChipColor(Integer.parseInt(this.date.toString().split("-")[2]),defaultDayColorCode);
        }
        this.date=date;
        setDays(date.lengthOfMonth());
        mm=months[Integer.parseInt(date.toString().split("-")[1])-1];
        month.setText(mm);
    }

    @Override
    public void onClick(View view) {
        if(view==prevMonth){
            setMonth(date.minusMonths(1));
        }
        if(view==nextMonth){
            setMonth(date.plusMonths(1));
        }
    }
}