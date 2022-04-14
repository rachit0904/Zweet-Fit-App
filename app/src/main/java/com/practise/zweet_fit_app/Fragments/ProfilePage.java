package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.practise.zweet_fit_app.Util.Constant;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilePage extends Fragment implements View.OnClickListener {
    CoordinatorLayout settings;
    CardView coinTransactionsCard;
    ImageView manageFriends;
    TextView editProfile, name, coins, steps, calories, distance, target_text, weight_text,username;
    CircleImageView userImg;
    ProgressBar progressBar3;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);
        manageFriends = view.findViewById(R.id.manageFriends);
        editProfile = view.findViewById(R.id.edit_profile);
        coinTransactionsCard = view.findViewById(R.id.coinsTransactions);
        settings=view.findViewById(R.id.events);
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
        editProfile.setOnClickListener(this);
        settings.setOnClickListener(this);
        manageFriends.setOnClickListener(this);
        coinTransactionsCard.setOnClickListener(this);
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
        getData();
    }

    private void getData() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://35.207.233.155:3578/selectwQuery?table=users&query=uid&value=" + pref.getString("id",""))
                    .method("GET", null)
                    .addHeader("key", "MyApiKEy")
                    .build();
            Response response=null;
            try {
               response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data=response.body().string();
            Log.i("response",data);
            JSONObject object=new JSONObject(data);
            JSONArray userData=object.getJSONArray("data");
            for(int i=0;i<userData.length();i++){
                JSONObject obj=userData.getJSONObject(i);
                Log.i("name",obj.getString("name"));
            }

        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == settings){
            Intent intent=new Intent(getContext(), BlankActivity.class);
            intent.putExtra("activity","settings");
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