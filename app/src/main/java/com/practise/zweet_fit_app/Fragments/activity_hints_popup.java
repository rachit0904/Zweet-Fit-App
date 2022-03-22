package com.practise.zweet_fit_app.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.PagerAdapter.ActivityHintsViewPagerAdapter;
import com.practise.zweet_fit_app.PagerAdapter.ActivityViewPagerAdapter;
import com.practise.zweet_fit_app.R;


public class activity_hints_popup extends Fragment{
    ViewPager hintpopup;
    TabLayout activityTabs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_activity_hints_popup, container, false);
        hintpopup=view.findViewById(R.id.hintpopup);
        activityTabs=view.findViewById(R.id.eventTabs);
        ActivityHintsViewPagerAdapter adapter=new ActivityHintsViewPagerAdapter(getChildFragmentManager(),activityTabs.getTabCount());
        hintpopup.setAdapter(adapter);
        activityTabs.setupWithViewPager(hintpopup);
        return view;
    }
}