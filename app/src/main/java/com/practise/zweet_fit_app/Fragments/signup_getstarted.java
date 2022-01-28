package com.practise.zweet_fit_app.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practise.zweet_fit_app.Activity.MainActivity;
import com.practise.zweet_fit_app.R;


public class signup_getstarted extends Fragment {
    LottieAnimationView creationAnimation;
    TextView status;
    Button getStarted;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_signup_getstarted, container, false);
        status=view.findViewById(R.id.creationStatus);
        getStarted=view.findViewById(R.id.getStarted);
        creationAnimation=view.findViewById(R.id.successAnim);
        getStarted.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                creationAnimation.playAnimation();
                status.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.VISIBLE);
                getStarted.setEnabled(true);
            }
        },1600);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}