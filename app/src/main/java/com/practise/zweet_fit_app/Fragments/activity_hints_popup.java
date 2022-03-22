package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.practise.zweet_fit_app.PagerAdapter.ActivityHintsViewPagerAdapter;
import com.practise.zweet_fit_app.PagerAdapter.ActivityViewPagerAdapter;
import com.practise.zweet_fit_app.R;


public class activity_hints_popup extends Fragment {
    ViewPager hintpopup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_activity_hints_popup, container, false);
        hintpopup=view.findViewById(R.id.hintpopup);
        ActivityHintsViewPagerAdapter adapter=new ActivityHintsViewPagerAdapter(getChildFragmentManager());
        hintpopup.setAdapter(adapter);
        return view;
    }
}