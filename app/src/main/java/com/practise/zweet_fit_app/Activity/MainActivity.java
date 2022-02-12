package com.practise.zweet_fit_app.Activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.Fragments.ActivityFragment;
import com.practise.zweet_fit_app.Fragments.SettingsFragment;
import com.practise.zweet_fit_app.Fragments.StatsFragment;
import com.practise.zweet_fit_app.Fragments.home_fragment;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Services.BrodcastReciver;

public class MainActivity extends AppCompatActivity {
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    TabLayout tabLayout;
    private static final int MOVING_AVERAGE_WINDOW_SIZE = 30;
    public static final String TAG = "com.zweet.fitnessapplication";
    public static final String BROADCAST = "com.zweet.fitnessapplication.android.action.broadcast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tabLayout = findViewById(R.id.tabLayout);
        try {
            if(getIntent().getStringExtra("frag").isEmpty()) {
                addFragment(new home_fragment());
            }else{
                addFragment(new StatsFragment());
                tabLayout.selectTab(tabLayout.getTabAt(2));
            }
        }catch (Exception e){
            addFragment(new home_fragment());
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        loadBroadcast();

    }

    private void setFragment(int position) {
        switch (position + 1) {
            case 1: {
                addFragment(new home_fragment());
                break;
            }
            case 2: {
                addFragment(new StatsFragment());
                break;
            }
            case 3: {
                addFragment(new ActivityFragment());
                break;
            }
            case 4: {
                addFragment(new SettingsFragment());
                break;
            }
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    void loadBroadcast() {
        String BROADCAST = "com.practise.zweet_fit_app.android.action.broadcast";
        IntentFilter intentFilter = new IntentFilter(BROADCAST);
        BrodcastReciver reciver = new BrodcastReciver();
        registerReceiver(reciver, intentFilter);
        Intent intent = new Intent(BROADCAST);
        sendBroadcast(intent);
    }

}
