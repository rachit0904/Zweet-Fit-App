package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.PagerAdapter.FriendsViewPagerAdapter;
import com.practise.zweet_fit_app.R;

public class ManageFriendsFragment extends Fragment {
    TabLayout friendsTabs;
    ViewPager friendsViewPager;
    ImageView back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_manage_friends, container, false);
        friendsTabs=view.findViewById(R.id.tabLayout2);
        friendsViewPager=view.findViewById(R.id.friendsViewPager);
        back=view.findViewById(R.id.bckBtn);
        FriendsViewPagerAdapter adapter=new FriendsViewPagerAdapter(getChildFragmentManager(),friendsTabs.getTabCount());
        friendsViewPager.setAdapter(adapter);
        friendsTabs.setupWithViewPager(friendsViewPager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }
}