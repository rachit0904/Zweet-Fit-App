package com.practise.zweet_fit_app.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practise.zweet_fit_app.Fragments.ActivityFragment;
import com.practise.zweet_fit_app.Fragments.SettingsFragment;
import com.practise.zweet_fit_app.Fragments.StatsFragment;
import com.practise.zweet_fit_app.Fragments.home_fragment;
import com.practise.zweet_fit_app.R;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private static final int MOVING_AVERAGE_WINDOW_SIZE = 30;
    public static final String TAG = "com.zweet.fitnessapplication";
    public static final String BROADCAST = "com.zweet.fitnessapplication.android.action.broadcast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        addFragment(new home_fragment());
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


}
