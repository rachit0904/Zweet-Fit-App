package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.practise.zweet_fit_app.R;

public class InfoFragment extends Fragment {
    TextView stdate, endate, timeleft, eventtar, eventrew;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info, container, false);
        stdate = view.findViewById(R.id.startDate);
        endate = view.findViewById(R.id.endDate);
        timeleft = view.findViewById(R.id.dur);
        eventtar = view.findViewById(R.id.evtar);
        eventrew = view.findViewById(R.id.evrew);
        setData();
        return view;
    }
    private void setData() {
        String tempdate = getActivity().getIntent().getStringExtra("dur").split("-")[0];
        stdate.setText(tempdate);
        eventtar.setText(getActivity().getIntent().getStringExtra("target"));
        String tempdate2 = getActivity().getIntent().getStringExtra("dur").split("-")[1];
        endate.setText(tempdate2);
        String dur = String.valueOf(Integer.parseInt(tempdate2.substring(2, 3)) - Integer.parseInt(tempdate.substring(1, 2)));
        eventrew.setText(getActivity().getIntent().getStringExtra("coins"));
        timeleft.setText(dur);
    }
}