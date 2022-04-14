package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class friend_profile extends Fragment implements View.OnClickListener {
    CoordinatorLayout events;
    CardView coinTransactionsCard;
    ImageView add_friend, remove_friend;
    TextView editProfile, name, coins, steps, calories, distance, target_text, weight_text,username;
    CircleImageView userImg;
    ProgressBar progressBar3;
    LinearLayout add_remove;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    String uid="",pid="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);
        events=view.findViewById(R.id.events);
        userImg = view.findViewById(R.id.userImg);
        username = view.findViewById(R.id.usname);
        add_friend =view.findViewById(R.id.addFriend);
        remove_friend =view.findViewById(R.id.removeFriend);
        name = view.findViewById(R.id.textView);
        coins = view.findViewById(R.id.coins);
        steps = view.findViewById(R.id.steps);
        calories = view.findViewById(R.id.calories);
        distance = view.findViewById(R.id.distance);
        target_text = view.findViewById(R.id.target_text);
        weight_text = view.findViewById(R.id.weight_text);
        progressBar3 = view.findViewById(R.id.progressBar3);
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        uid=pref.getString("id","");
        setData();
        events.setOnClickListener(this);
        add_friend.setOnClickListener(this);
        remove_friend.setOnClickListener(this);
        return view;
    }

    private void setData() {
        pid=getActivity().getIntent().getStringExtra("uid");
        try{
        if(getActivity().getIntent().getStringExtra("type").equals("yes")){
            add_friend.setVisibility(View.GONE);
            remove_friend.setVisibility(View.VISIBLE);
        }}catch (Exception e){
            add_friend.setVisibility(View.VISIBLE);
            remove_friend.setVisibility(View.GONE);
        }
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=users&query=uid&value="+pid)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if(jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    coins.setText(obj.getString("coins"));
                    Picasso.get().load(obj.getString("dp_url")).placeholder(R.drawable.avatar_1).into(userImg);
                    name.setText(obj.getString("name"));
                    username.setText(obj.getString("username"));
                    target_text.setText(obj.getString("target")+" Steps");
                    weight_text.setText(obj.getString("weight"));
                    obj.getString("subscription");
                    String s=obj.getString("steps");
                    steps.setText(s);
                    int cal= (int) Math.ceil((Integer.parseInt(s)*0.04258));
                    calories.setText(String.valueOf(cal)+ " KCal");
                    int dist=  (int) Math.ceil(Integer.parseInt(s)/1312.33595801);
                    distance.setText(String.valueOf(dist)+ " Kms");
                    progressBar3.setProgress((Integer.parseInt(s)/Integer.parseInt(obj.getString("target")))*100 );
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isFriend(String pid) {
        String status="";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=friends&query=uid&value="+pref.getString("id","")+"&query=fid&value="+pid)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if(jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if(pid.equals(obj.getString("fid")) || pid.equals(obj.getString("uid"))
                            && uid.equals(obj.getString("uid")) || uid.equals(obj.getString("fid"))) {
                        status = obj.getString("status");
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        Log.i("status",status);
        if(status.equals("friend")){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        if(view == events){
            Intent intent=new Intent(getContext(), BlankActivity.class);
            intent.putExtra("uid",pid);
            intent.putExtra("activity","events");
            startActivity(intent);
        }
        if(view==add_friend){
            requestDb(Constant.ServerUrl,"/addFriend",uid,pid);
        }
        if(view==remove_friend){
            requestDb(Constant.ServerUrl,"/removeFriend",uid,pid);
        }
    }

    private void requestDb(String serverUrl, String endpt, String uid, String p2id) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fid",p2id)
                .addFormDataPart("uid",uid)
                .build();
        Request request = new Request.Builder()
                .url(serverUrl+endpt)
                .method("POST", body)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            if(endpt.contains("add")){
                Snackbar.make(getView(),"Friend Request Sent!",Snackbar.LENGTH_SHORT).show();
            }else if(endpt.contains("remove")){
                Snackbar.make(getView(),"Removed Friend!",Snackbar.LENGTH_SHORT).show();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Snackbar.make(getView(),"User not added as friend!",Snackbar.LENGTH_SHORT).show();
        }

    }


}