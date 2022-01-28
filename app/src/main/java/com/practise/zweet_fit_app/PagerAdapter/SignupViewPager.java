package com.practise.zweet_fit_app.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.practise.zweet_fit_app.Fragments.Signup_PersonalDetails;
import com.practise.zweet_fit_app.Fragments.signup_getstarted;
import com.practise.zweet_fit_app.Fragments.signup_userstats;

public class SignupViewPager extends FragmentPagerAdapter {
    final private int noOfTabs;

    public SignupViewPager(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:{
                return "personal details";
            }
            case 1:{
                return "stats";
            } case 2:{
                return "get started";
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
                return new Signup_PersonalDetails();
            }
            case 1:{
                return new signup_userstats();
            }
            case 2:{
                return new signup_getstarted();
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
