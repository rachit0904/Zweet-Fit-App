package com.practise.zweet_fit_app.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.practise.zweet_fit_app.Fragments.EventLeaderboardFragment;
import com.practise.zweet_fit_app.Fragments.EventStatsFragment;
import com.practise.zweet_fit_app.Fragments.InfoFragment;

public class GroupEventsViewPagerAdapter extends FriendsViewPagerAdapter{
    final private int noOfTabs;

    public GroupEventsViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm, noOfTabs);
        this.noOfTabs = noOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:{
                return "Info";
            }
            case 1: {
                return "Stats";
            }case 2: {
                return "Leaderboard";
            } default:{
                return null;
            }
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                return new InfoFragment();
            } case 1:{
                return new EventStatsFragment();
            } case 2:{
                return new EventLeaderboardFragment();
            } default:{
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }

}
