package com.practise.zweet_fit_app.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.practise.zweet_fit_app.Fragments.Coinhint;
import com.practise.zweet_fit_app.Fragments.LevelhintFragment;
import com.practise.zweet_fit_app.Fragments.stephint;

public class ActivityHintsViewPagerAdapter extends FragmentPagerAdapter {
    private final int noftabs;
    public ActivityHintsViewPagerAdapter(@NonNull FragmentManager childFragmentManager, int tabs) {
        super(childFragmentManager);
        this.noftabs = tabs;
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                return new stephint();
            } case 1:{
                return new Coinhint();
            }  case 2:{
                return new LevelhintFragment();
            }
            default:{
                return null;
            }
        }
//        return null;
    }

    @Override
    public int getCount() {
        return noftabs;
    }
}
