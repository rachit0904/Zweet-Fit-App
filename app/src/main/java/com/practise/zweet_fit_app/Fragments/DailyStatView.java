package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.practise.zweet_fit_app.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DailyStatView extends Fragment implements View.OnClickListener {
    TextView statDate;
    ImageView prevDate,nextDate;
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    SimpleDateFormat dateFormat=new SimpleDateFormat("DD-MM-YYYY");
    LocalDate date=LocalDate.now();
    String dd,mm,yy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_daily_stat_view, container, false);
        prevDate=view.findViewById(R.id.prevDate);
        statDate=view.findViewById(R.id.date);
        nextDate=view.findViewById(R.id.nextDate);
        setDateFormat(LocalDate.now());
        prevDate.setOnClickListener(this);
        nextDate.setOnClickListener(this);
        return view;
    }

    private void setDateFormat(LocalDate date) {
        this.date=date;
        dd=date.toString().split("-")[2];
        mm=date.toString().split("-")[1];
        yy=date.toString().split("-")[0];
        statDate.setText(dd+" "+months[Integer.parseInt(mm)-1]+" "+yy);
    }

    @Override
    public void onClick(View view) {
        if(view==prevDate){
            setDateFormat(date.minusDays(1));
        }
        if(view==nextDate){
            setDateFormat(date.plusDays(1));
        }
    }
}