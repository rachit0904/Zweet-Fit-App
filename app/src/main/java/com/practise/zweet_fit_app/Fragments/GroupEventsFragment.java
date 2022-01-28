package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.PagerAdapter.GroupEventsViewPagerAdapter;
import com.practise.zweet_fit_app.R;

public class GroupEventsFragment extends Fragment {
    ImageView bckBtn;
    TextView eventName,levelUp,coins,status,target;
    TabLayout eventTabs;
    ViewPager eventViews;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group_events, container, false);
        bckBtn=view.findViewById(R.id.bckBtn);
        eventName=view.findViewById(R.id.eventName);
        levelUp=view.findViewById(R.id.eventLevelUp);
        coins=view.findViewById(R.id.eventEntryCoins);
        status=view.findViewById(R.id.eventStatus);
        target=view.findViewById(R.id.eventTarget);
        eventTabs=view.findViewById(R.id.eventTabs);
        eventViews=view.findViewById(R.id.eventViews);
        setData();
        eventTabs.selectTab(eventTabs.getTabAt(1));
        GroupEventsViewPagerAdapter adapter=new GroupEventsViewPagerAdapter(getChildFragmentManager(),eventTabs.getTabCount());
        eventViews.setAdapter(adapter);
        eventTabs.setupWithViewPager(eventViews);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    private void setData() {
        //TODO - set card data
        eventName.setText(getActivity().getIntent().getStringExtra("title"));
        levelUp.setText(getActivity().getIntent().getStringExtra("lvlup"));
        coins.setText(getActivity().getIntent().getStringExtra("coins"));
        if(getActivity().getIntent().getStringExtra("status").equals("ongoing"))
            status.setText(getActivity().getIntent().getStringExtra("dur")+" Event");
        if(getActivity().getIntent().getStringExtra("status").equals("finished"))
            status.setText("Event Ended!");
        target.setText("Goal: "+getActivity().getIntent().getStringExtra("target")+" steps");

    }
}