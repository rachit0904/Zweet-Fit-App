package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.PagerAdapter.GroupEventsViewPagerAdapter;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GroupEventsFragment extends Fragment {
    ImageView bckBtn, img1, img2, img3, img4, img5;
    TextView eventName,levelUp,coins,status,target,joinbtn;
    TabLayout eventTabs;
    ViewPager eventViews;
    String uid="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group_events, container, false);
        bckBtn=view.findViewById(R.id.bckBtn);
        eventName=view.findViewById(R.id.eventName);
        levelUp=view.findViewById(R.id.eventLevelUp);
        coins=view.findViewById(R.id.eventEntryCoins);
        status=view.findViewById(R.id.eventStatus);
        target=view.findViewById(R.id.eventTarget);
        eventTabs=view.findViewById(R.id.eventTabs);
        eventViews=view.findViewById(R.id.eventViews);
        joinbtn = view.findViewById(R.id.joinEvent);
        img1 = view.findViewById(R.id.p1);
        img2 = view.findViewById(R.id.p2);
        img3 = view.findViewById(R.id.p3);
        img4 = view.findViewById(R.id.p4);
        img5 = view.findViewById(R.id.p5);
        SharedPreferences pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        uid=pref.getString("id","");
        checkifuserjoin();
        setData();
        eventTabs.selectTab(eventTabs.getTabAt(1));
        GroupEventsViewPagerAdapter adapter=new GroupEventsViewPagerAdapter(getChildFragmentManager(),eventTabs.getTabCount());
        eventViews.setAdapter(adapter);
        eventTabs.setupWithViewPager(eventViews);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(joinbtn.getText()=="Join")
                {
                        String grpid = getActivity().getIntent().getStringExtra("id");
                        String url = Constant.ServerUrl+"/addUserToGroupEvent";
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("uid",uid)
                                .addFormDataPart("eid", grpid)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://35.207.233.155:3578/addUserToGroupEvent")
                                .method("POST", body)
                                .addHeader("key", "MyApiKEy")
                                .build();
                        Response response = null;
                        try {
                            response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                else
                {
                    String grpid = getActivity().getIntent().getStringExtra("id");
                    String url = Constant.ServerUrl+"/removeUserToGroupEvent";
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    MediaType mediaType = MediaType.parse("text/plain");
                    MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("uid",uid)
                            .addFormDataPart("eid", grpid)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://35.207.233.155:3578/removeUserToGroupEvent")
                            .method("POST", body)
                            .addHeader("key", "MyApiKEy")
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                checkifuserjoin();
            }
        });
        return view;
    }
    public void checkifuserjoin()
    {
        try {
            String grpid = getActivity().getIntent().getStringExtra("id");
            String url = Constant.ServerUrl+"/select?table=users";
            String url2 = Constant.ServerUrl+"/select?table=group_event_holder";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            OkHttpClient client2 = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://35.207.233.155:3578/select?table=users")
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Request request2 = new Request.Builder()
                    .url("http://35.207.233.155:3578/select?table=group_event_holder")
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Response response = null;
            Response response2 = null;
            try {
                response = client.newCall(request).execute();
                response2 = client2.newCall(request2).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Leave2", e.toString());
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            String respo2 = response2.body().string();
            JSONObject Jobject2 = new JSONObject(respo2);
            JSONArray Jarray2 = Jobject2.getJSONArray("data");
            JSONObject object = Jarray.getJSONObject(1);
            String userid = object.get("uid").toString();
            String grp = "";
            Log.d("Id", userid);
            for(int i=0;i<Jarray2.length();i++)
            {
                JSONObject object2 = Jarray2.getJSONObject(i);
                grp = object2.get("eid").toString();
                String uid = object2.get("uid").toString();
                Log.d("Id", uid);
                if(grp.equals(grpid))
                {
                    if(userid.equals(uid))
                    {
                        userid="1";
                        Log.d("Leave", "Join");
                        joinbtn.setText("Leave");
                        break;
                    }
                }
            }
            if(!userid.equals("1"))
            {
                joinbtn.setText("Join");
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("Leave", e.toString());
        }
    }
    private void setData() {
        String tempdate = getActivity().getIntent().getStringExtra("dur").split("-")[0];
        eventName.setText(getActivity().getIntent().getStringExtra("title"));
        levelUp.setText(getActivity().getIntent().getStringExtra("lvlup"));
        coins.setText(getActivity().getIntent().getStringExtra("coins"));
        if(getActivity().getIntent().getStringExtra("status").equals("ongoing"))
            status.setText(getActivity().getIntent().getStringExtra("title"));
        else if(getActivity().getIntent().getStringExtra("status").equals("finished"))
            status.setText("Event Ended!");
        target.setText("Goal: "+getActivity().getIntent().getStringExtra("target")+" steps");
        String type = getActivity().getIntent().getStringExtra("type");
        if(type.equals("p2p"))
        {
            img1.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            img5.setVisibility(View.GONE);
        }
    }
}