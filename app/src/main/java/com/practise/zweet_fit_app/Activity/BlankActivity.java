package com.practise.zweet_fit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.practise.zweet_fit_app.Fragments.CoinHistory;
import com.practise.zweet_fit_app.Fragments.EditProfile;
import com.practise.zweet_fit_app.Fragments.GroupEventsFragment;
import com.practise.zweet_fit_app.Fragments.ManageFriendsFragment;
import com.practise.zweet_fit_app.Fragments.ProfilePage;
import com.practise.zweet_fit_app.Fragments.SearchFragment;
import com.practise.zweet_fit_app.Fragments.Subs_Details_Page;
import com.practise.zweet_fit_app.Fragments.login;
import com.practise.zweet_fit_app.Fragments.otp;
import com.practise.zweet_fit_app.R;


public class BlankActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragment(getIntent().getStringExtra("activity"));
        setContentView(R.layout.activity_blank);
    }

    private void setFragment(String frag) {
        switch (frag) {
            case "login":{
                addFragment(new login());
                break;
            }
            case "otp":{
                addFragment(new otp());
                break;
            }
            case "grp_event":{
                addFragment(new GroupEventsFragment());
                break;
            }
            case "search user":{
                addFragment(new SearchFragment());
                break;
            }
            case "edit profile":{
                addFragment(new EditProfile());
                break;
            }
            case "premium" :{
                addFragment(new Subs_Details_Page());
                break;
            }
            case "friends":{
                addFragment(new ManageFriendsFragment());
                break;
            }
            case "profile":{
                addFragment(new ProfilePage());
                break;
            }
            case "coin transactions":{
                addFragment(new CoinHistory());
                break;
            }
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.blank_activity_frame, fragment);
        transaction.commit();
    }


}