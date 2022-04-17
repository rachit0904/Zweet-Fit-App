package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.practise.zweet_fit_app.R;

public class EventStatsFragment extends Fragment {
    ProgressBar progressBar;
    TextView prg,steps,cal,dist;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_event_stats, container, false);
        progressBar=view.findViewById(R.id.userPrg);
        prg=view.findViewById(R.id.progress);
        steps=view.findViewById(R.id.steps);
        cal=view.findViewById(R.id.calorie);
        dist=view.findViewById(R.id.dist);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        steps.setText(pref.getString("steps",""));
        cal.setText(pref.getString("cal","")+ "KCal");
        dist.setText(pref.getString("dist","")+" Km");
        Float s=Float.valueOf(pref.getString("steps",""));
        Float t= Float.valueOf(pref.getString("target",""));
        prg.setText(pref.getString("progress","")+" %");
        progressBar.setProgress(Integer.parseInt(pref.getString("progress","")));
        return view;
    }
}