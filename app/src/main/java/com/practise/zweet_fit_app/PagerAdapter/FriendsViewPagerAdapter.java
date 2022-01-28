package com.practise.zweet_fit_app.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.practise.zweet_fit_app.Fragments.FriendRequests;
import com.practise.zweet_fit_app.Fragments.FriendsList;

public class FriendsViewPagerAdapter extends FragmentPagerAdapter {
    final private int noOfTabs;

    public FriendsViewPagerAdapter(@NonNull FragmentManager fm,  int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:{
                return "Requests";
            }
            case 1: {
                return "Friends";
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
                return new FriendRequests();
            } case 1:{
                return new FriendsList();
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