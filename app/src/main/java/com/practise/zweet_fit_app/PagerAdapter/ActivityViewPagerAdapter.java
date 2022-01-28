package com.practise.zweet_fit_app.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.practise.zweet_fit_app.Fragments.History_Fragment;
import com.practise.zweet_fit_app.Fragments.Ongoing_Fragment;
import com.practise.zweet_fit_app.Fragments.invitation_Fragment;


public class ActivityViewPagerAdapter  extends FragmentPagerAdapter {
    final private int noOfTabs;
    public ActivityViewPagerAdapter(@NonNull FragmentManager fm, int tabs) {
        super(fm);
        this.noOfTabs=tabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:{
                return "Invitation";
            }
            case 1:{
                return "On-Going";
            } case 2:{
                return "History";
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
                return new invitation_Fragment();
            } case 1:{
                return new Ongoing_Fragment();
            }  case 2:{
                return new History_Fragment();
            }
            default:{
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}