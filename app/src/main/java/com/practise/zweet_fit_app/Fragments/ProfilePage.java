package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilePage extends Fragment implements View.OnClickListener {
    CoordinatorLayout logout;
    CardView coinTransactionsCard;

    ImageView manageFriends;
    TextView editProfile, name, coins, steps, calories, distance, target_text, weight_text;
    CircleImageView userImg;
    ProgressBar progressBar3;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        logout = view.findViewById(R.id.logout);
        manageFriends = view.findViewById(R.id.manageFriends);
        editProfile = view.findViewById(R.id.edit_profile);
        coinTransactionsCard = view.findViewById(R.id.coinsTransactions);
        userImg = view.findViewById(R.id.userImg);
        name = view.findViewById(R.id.textView);
        coins = view.findViewById(R.id.coins);
        steps = view.findViewById(R.id.steps);
        calories = view.findViewById(R.id.calories);
        distance = view.findViewById(R.id.distance);
        target_text = view.findViewById(R.id.target_text);
        weight_text = view.findViewById(R.id.weight_text);
        progressBar3 = view.findViewById(R.id.progressBar3);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        logout.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        manageFriends.setOnClickListener(this);
        coinTransactionsCard.setOnClickListener(this);
        setData();
        String coins = "0";
//        progressBar3.setProgress(Integer.parseInt(numSteps)/Integer.parseInt(target)*100);

        return view;
    }

    private void setData() {
        name.setText(pref.getString("name",""));
        target_text.setText(pref.getString("target","")+" steps");
        weight_text.setText(pref.getString("wt","")+" Kg");
        progressBar3.setProgress(Integer.parseInt(pref.getString("progress","")),true);
        steps.setText(pref.getString("steps",""));
        calories.setText(pref.getString("cal",""));
        distance.setText(pref.getString("dist",""));
        coins.setText(pref.getString("coins","0"));
        if(!pref.getString("dp","").isEmpty()){
            Picasso.get().load(pref.getString("dp","")).into(userImg);
        }
//        Toast.makeText(getActivity(), pref.getString("name",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("dob",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("gender",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("wt",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("ht",""), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), pref.getString("target",""), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        if(view==logout){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity", "login");
            startActivity(intent);
        }
        if(view==editProfile){
            Intent intent=new Intent(getContext(), BlankActivity.class);
            intent.putExtra("activity","edit profile");
            startActivity(intent);
            getActivity().finish();
        }
        if(view==coinTransactionsCard){
            Intent intent=new Intent(getContext(), BlankActivity.class);
            intent.putExtra("activity","coin transactions");
            startActivity(intent);
        }
        if(view==manageFriends){
            Intent intent=new Intent(getActivity(), BlankActivity.class);
            intent.putExtra("activity","friends");
            startActivity(intent);
        }
    }
}