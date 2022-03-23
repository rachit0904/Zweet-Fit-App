package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class friend_profile extends Fragment implements View.OnClickListener {
    CoordinatorLayout events;
    CardView coinTransactionsCard;
    ImageView add_user, remove_user;
    TextView editProfile, name, coins, steps, calories, distance, target_text, weight_text,username;
    CircleImageView userImg;
    ProgressBar progressBar3;
    LinearLayout add_remove;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);
        add_remove = view.findViewById(R.id.add_remove_user);
//        editProfile = view.findViewById(R.id.edit_profile);
//        coinTransactionsCard = view.findViewById(R.id.coinsTransactions);
        events=view.findViewById(R.id.events);
        userImg = view.findViewById(R.id.userImg);
        username = view.findViewById(R.id.usname);
        name = view.findViewById(R.id.textView);
        coins = view.findViewById(R.id.coins);
        steps = view.findViewById(R.id.steps);
        calories = view.findViewById(R.id.calories);
        distance = view.findViewById(R.id.distance);
        target_text = view.findViewById(R.id.target_text);
        weight_text = view.findViewById(R.id.weight_text);
        progressBar3 = view.findViewById(R.id.progressBar3);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
//        editProfile.setOnClickListener(this);
        events.setOnClickListener(this);
        add_remove.setOnClickListener(this);
//        coinTransactionsCard.setOnClickListener(this);
        setData();
        String coins = "0";
//        progressBar3.setProgress(Integer.parseInt(numSteps)/Integer.parseInt(target)*100);

        return view;
    }

    private void setData() {
        name.setText(pref.getString("name",""));
        username.setText(new StringBuilder().append("@").append(pref.getString("usname", "")).toString());
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
        if(view == events){
            Intent intent=new Intent(getContext(), BlankActivity.class);
            intent.putExtra("activity","events");
            startActivity(intent);
        }
//        if(view==editProfile){
//            Intent intent=new Intent(getContext(), BlankActivity.class);
//            intent.putExtra("activity","edit profile");
//            startActivity(intent);
//        }
//        if(view==coinTransactionsCard){
//            Intent intent=new Intent(getContext(), BlankActivity.class);
//            intent.putExtra("activity","coin transactions");
//            startActivity(intent);
//        }
        if(view==add_remove){
            if(add_user.getVisibility()==View.GONE)
            {
                add_user.setVisibility(View.VISIBLE);
                remove_user.setVisibility(View.GONE);
            }
            else
            {
                add_user.setVisibility(View.GONE);
                remove_user.setVisibility(View.VISIBLE);
            }
        }
    }
}