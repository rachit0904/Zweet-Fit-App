package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.practise.zweet_fit_app.Activity.BlankActivity;
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
    int fl = 0;
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
        if(checkeventst())
        {
            joinbtn.setVisibility(View.GONE);
        }
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
                String grpid = getActivity().getIntent().getStringExtra("id");
                String event_coin = getActivity().getIntent().getStringExtra("coins");
                if(joinbtn.getText()=="Join") {
                    if(checkcoins(event_coin)) {
                        {
                            String url = Constant.ServerUrl + "/addUserToGroupEvent";
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("text/plain");
                            MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("uid", uid)
                                    .addFormDataPart("eid", grpid)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(url)
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
                        {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("text/plain");
                            MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("amount", "-" + getActivity().getIntent().getStringExtra("coins"))
                                    .addFormDataPart("eid", grpid)
                                    .addFormDataPart("source", "joined " + getActivity().getIntent().getStringExtra("title"))
                                    .addFormDataPart("uid", uid)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(Constant.ServerUrl + "/addCoin")
                                    .method("POST", body)
                                    .addHeader("key", "MyApiKEy")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Snackbar.make(view, "Event Joined!", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                         fl=1;
                         Toast.makeText(getContext(), "Not Enough Coins!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                        {
                        String url2 = Constant.ServerUrl + "/removeUserToGroupEvent";
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("uid", uid)
                                .addFormDataPart("eid", grpid)
                                .build();
                        Request request = new Request.Builder()
                                .url(url2)
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
                    {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("amount",getActivity().getIntent().getStringExtra("coins"))
                                .addFormDataPart("eid",grpid)
                                .addFormDataPart("source","refund for "+getActivity().getIntent().getStringExtra("title"))
                                .addFormDataPart("uid",uid)
                                .build();
                        Request request = new Request.Builder()
                                .url(Constant.ServerUrl+"/addCoin")
                                .method("POST", body)
                                .addHeader("key", "MyApiKEy")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Snackbar.make(view,"Event Left!",Snackbar.LENGTH_SHORT).show();
                }
                checkifuserjoin();
                if(fl==0)
                {
                    Intent intent=new Intent(getContext(), BlankActivity.class);
                    intent.putExtra("activity","grp_event");
                    intent.putExtra("title",getActivity().getIntent().getStringExtra("title"));
                    intent.putExtra("lvlup",getActivity().getIntent().getStringExtra("lvl"));
                    intent.putExtra("coins",getActivity().getIntent().getStringExtra("coins"));
                    intent.putExtra("target",getActivity().getIntent().getStringExtra("target"));
                    intent.putExtra("status",getActivity().getIntent().getStringExtra("status"));
                    intent.putExtra("participants",getActivity().getIntent().getStringExtra("participants"));
                    intent.putExtra("maxP",getActivity().getIntent().getStringExtra("maxp"));
                    intent.putExtra("minP",getActivity().getIntent().getStringExtra("minp"));
                    intent.putExtra("id",getActivity().getIntent().getStringExtra("id"));
                    intent.putExtra("dur",getActivity().getIntent().getStringExtra("dur"));
                    intent.putExtra("type",getActivity().getIntent().getStringExtra("type"));
                    getContext().startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    public boolean checkeventst()
    {
        try {
            String url = Constant.ServerUrl+"/select?table=events";
            String url2 = Constant.ServerUrl+"/select?table=group_event";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            OkHttpClient client2 = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Request request2 = new Request.Builder()
                    .url(url2)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Response response = null;
            Response response2 = null;
            try {
                response = client.newCall(request).execute();
                response2 = client.newCall(request2).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("L11", e.toString());
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            String respo2 = response2.body().string();
            JSONObject Jobject2 = new JSONObject(respo2);
            JSONArray Jarray2 = Jobject2.getJSONArray("data");
            for(int i=0;i<Jarray.length();i++)
            {
                JSONObject object = Jarray.getJSONObject(i);
                String eid = object.get("eid").toString();
                String st = object.get("status").toString();
                Log.d("eid1", eid);
                if(eid.equals(getActivity().getIntent().getStringExtra("id")))
                {
                    if(st.equals("3"))
                    {
                       return true;
                    }
                    else
                    {
                        break;
                    }
                }
            }
            for(int i=0;i<Jarray2.length();i++)
            {
                JSONObject object2 = Jarray2.getJSONObject(i);
                String eid = object2.get("id").toString();
                String st = object2.get("status").toString();
                Log.d("eid2", eid);
                if(eid.equals(getActivity().getIntent().getStringExtra("id")))
                {
                    if(st.equals("3"))
                    {
                        return true;
                    }
                    else
                    {
                        break;
                    }
                }
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("Lll2", e.toString());
        }
        return false;
    }

    public boolean checkcoins(String event_coin)
    {
       try {
            String url = Constant.ServerUrl+"/select?table=users";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("L11", e.toString());
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            for(int i=0;i<Jarray.length();i++)
            {
                JSONObject object = Jarray.getJSONObject(i);
                String user = object.getString("uid");
                int coins = Integer.parseInt(object.getString("coins"));
                int ec = Integer.parseInt(event_coin);
                if(user.equals(uid))
                {
                    if(coins>=ec)
                    {
                        return true;
                    }
                }
            }
       } catch (JSONException | IOException e) {
           e.printStackTrace();
           Log.d("L2", e.toString());
       }
       return false;
    }
    public void checkifuserjoin()
    {
        try {
            String grpid = getActivity().getIntent().getStringExtra("id");
            String url4 = Constant.ServerUrl+"/select?table=users";
            String url5 = Constant.ServerUrl+"/select?table=group_event_holder";
            String url3 = Constant.ServerUrl+"/select?table=events";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            OkHttpClient client2 = new OkHttpClient().newBuilder()
                    .build();
            OkHttpClient client3 = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url4)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Request request2 = new Request.Builder()
                    .url(url5)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Request request3 = new Request.Builder()
                    .url(url3)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Response response = null;
            Response response2 = null;
            Response response3 = null;
            try {
                response = client.newCall(request).execute();
                response2 = client2.newCall(request2).execute();
                response3 = client2.newCall(request3).execute();
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
            String respo3 = response3.body().string();
            JSONObject Jobject3 = new JSONObject(respo3);
            JSONArray Jarray3 = Jobject3.getJSONArray("data");
            JSONObject object = Jarray.getJSONObject(1);
            String userid = uid;
            String grp = "";
            Log.d("Id1", userid);
            for(int i=0;i<Jarray2.length();i++)
            {
                JSONObject object2 = Jarray2.getJSONObject(i);
                grp = object2.get("eid").toString();
                String uid2 = object2.get("uid").toString();
                Log.d("Id", uid2);
                if(grp.equals(grpid))
                {
                    if(userid.equals(uid2))
                    {
                        userid="1";
                        joinbtn.setText("Leave");
                        break;
                    }
                }
            }
            if(!userid.equals("1"))
            {
                for(int k=0;k<Jarray3.length();k++)
                {
                    JSONObject object3 = Jarray3.getJSONObject(k);
                    String pid1 = object3.getString("p1id");
                    String pid2 = object3.getString("p2id");
                    String eid = object3.get("eid").toString();
                    if(eid.equals(grpid))
                    {
                        if(userid.equals(pid1) || userid.equals(pid2))
                        {
                            userid="2";
                            joinbtn.setText("Leave");
                            break;
                        }
                    }
                }
                if(!userid.equals("2"))
                {
                    joinbtn.setText("Join");
                }
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