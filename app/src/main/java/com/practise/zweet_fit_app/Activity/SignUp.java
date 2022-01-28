package com.practise.zweet_fit_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.practise.zweet_fit_app.Fragments.Signup_PersonalDetails;
import com.practise.zweet_fit_app.Fragments.signup_getstarted;
import com.practise.zweet_fit_app.Fragments.signup_userstats;
import com.practise.zweet_fit_app.R;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setFragment(getIntent().getStringExtra("fragment"));
    }

    private void setFragment(String fragment) {
        switch(fragment){
            case "personal details":{
                addFragment(new Signup_PersonalDetails());
                break;
            }
            case "stats":{
                addFragment(new signup_userstats());
                break;
            }
            case "get started":{
                addFragment(new signup_getstarted());
                break;
            }
        }
    }


    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.signupFrame, fragment);
        transaction.commit();
    }

}