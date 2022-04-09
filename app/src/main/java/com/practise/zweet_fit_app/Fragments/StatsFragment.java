package com.practise.zweet_fit_app.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.practise.zweet_fit_app.R;


public class StatsFragment extends Fragment implements View.OnClickListener {
    Button daily,weekly,monthly,yearly;
    FrameLayout statView;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_stats, container, false);
        daily=view.findViewById(R.id.dailyView);
        weekly=view.findViewById(R.id.weeklyView);
        monthly=view.findViewById(R.id.monthlyView);
        yearly=view.findViewById(R.id.yearlyView);
        statView=view.findViewById(R.id.statView);
        daily.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
        setFragment(1);
        daily.setOnClickListener(this);monthly.setOnClickListener(this);
        weekly.setOnClickListener(this);yearly.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==daily){
            setFragment(1);
            daily.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
            weekly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            monthly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            yearly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
        }else if(v==weekly){
            setFragment(2);
            daily.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            weekly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
            monthly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            yearly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
        }else if(v==monthly){
            setFragment(3);
            daily.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            weekly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            monthly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
            yearly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
        }else if(v==yearly){
            setFragment(4);
            daily.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            weekly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            monthly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A9101010")));
            yearly.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
        }
    }

    private void setFragment(int position) {
        switch (position ) {
            case 1: {
                addFragment(new DailyStatView());
                break;
            }
            case 2: {
                addFragment(new WeeklyStatView());
                break;
            }
            case 3: {
                addFragment(new MonthlyFragment());
                break;
            }
            case 4: {
                addFragment(new YearlyFragment());
                break;
            }
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.statView, fragment);
        transaction.commit();
    }
}