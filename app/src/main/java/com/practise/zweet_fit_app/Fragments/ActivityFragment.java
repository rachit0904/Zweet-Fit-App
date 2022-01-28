package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.PagerAdapter.ActivityViewPagerAdapter;
import com.practise.zweet_fit_app.R;


public class ActivityFragment extends Fragment {
    TabLayout activityTabs;
    ViewPager activityViewPager;
    ImageView manageFriends;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_activity, container, false);
        manageFriends=view.findViewById(R.id.manageFriends);
        activityTabs=view.findViewById(R.id.eventTabs);
        activityViewPager=view.findViewById(R.id.activityViews);
        ActivityViewPagerAdapter adapter=new ActivityViewPagerAdapter(getChildFragmentManager(),activityTabs.getTabCount());
        activityViewPager.setAdapter(adapter);
        activityTabs.setupWithViewPager(activityViewPager);
        return view;
    }
}