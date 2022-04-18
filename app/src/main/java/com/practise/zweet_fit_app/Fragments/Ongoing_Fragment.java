package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.practise.zweet_fit_app.Adapters.EventParentAdapter;
import com.practise.zweet_fit_app.Modals.EventCardModal;
import com.practise.zweet_fit_app.Modals.GrpEventsModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Ongoing_Fragment extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences pref;
    int cnt =0;
    TextView noData;
    List<EventCardModal> eventCardModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_ongoing_, container, false);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        recyclerView=view.findViewById(R.id.parentEventRv);
        noData=view.findViewById(R.id.noData);
        recyclerView.setHasFixedSize(true);
        eventCardModalList.clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        EventParentAdapter adapter=new EventParentAdapter(view.getContext(),getCardDetails());
        recyclerView.setAdapter(adapter);
        if(eventCardModalList.isEmpty()){
            noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    return view;
    }

    private List<EventCardModal> getCardDetails() {
        try {
            String userid = pref.getString("id","");
            String url = Constant.ServerUrl+"/select?table=events";
            String url2 = Constant.ServerUrl+"/select?table=group_event";
            String url3 = Constant.ServerUrl+"/select?table=group_event_holder";
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
                response3 = client.newCall(request3).execute();
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            HashMap<String, List<GrpEventsModal>> allevents = new HashMap<>();
            HashMap<String, List<GrpEventsModal>> allevents2 = new HashMap<>();
            JSONArray Jarray = Jobject.getJSONArray("data");
            String respo2 = response2.body().string();
            JSONObject Jobject2 = new JSONObject(respo2);
            JSONArray Jarray2 = Jobject2.getJSONArray("data");
            String respo3 = response3.body().string();
            JSONObject Jobject3 = new JSONObject(respo3);
            JSONArray Jarray3 = Jobject3.getJSONArray("data");
            String date[]=new String[Jarray.length() + Jarray2.length()];
            String stdate[]=new String[Jarray.length() + Jarray2.length()];
            for (int i = 0; i < Jarray.length(); i++) {
                List<GrpEventsModal> grpEventsModalList=new ArrayList<>();
                JSONObject object2 = Jarray.getJSONObject(i);
                String dur = object2.get("duration").toString();
                String fdate = null;
                date[i]=dur;
                date[i]=date[i].split("-")[0];
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(date[i]);
                date[i]=dt.toString();
                date[i]=date[i].substring(8, 10) + " " + date[i].substring(4, 7) + " " + date[i].substring(30, 34);
                for(int k = i; k<Jarray.length(); k++)
                {
                    int flag = 0;
                    JSONObject object = Jarray.getJSONObject(k);
                    String title = object.get("title").toString();
                    String st = object.get("status").toString();
                    String dur2 = object.get("duration").toString();
                    String target = object.get("target").toString();
                    String ent_coin = object.get("entry_coin").toString();
                    String eid = object.get("eid").toString();
                    String p1id = object.get("p1id").toString();
                    String p2id = object.get("p2id").toString();
                    String tempdate = dur2.split("-")[0];
                    Date dts = new SimpleDateFormat("dd/MM/yyyy").parse(tempdate);
                    tempdate=dts.toString();
                    tempdate=tempdate.substring(8, 10) + " " + tempdate.substring(4, 7) + " " + tempdate.substring(30, 34);
                    if(st.equals("2") && (p1id.equals(userid) || p2id.equals(userid)))
                    {
                        if(tempdate.equals(date[i]))
                        {
                            GrpEventsModal modal = new GrpEventsModal(
                                    eid, title, "2",
                                    "2", "1", ent_coin, dur, target, "", "ongoing", "p2p");
                            grpEventsModalList.add(modal);
                            fdate=tempdate;
                        }
                        else
                        {
                            i=k-1;
                            break;
                        }
                    }
                    if(k==(Jarray.length()-1))
                    {
                        i=k;
                        break;
                    }
                }
                if(fdate!=null)
                {
                    allevents.put(fdate, grpEventsModalList);
                }
            }
            for (int i = 0; i < Jarray2.length(); i++) {
                List<GrpEventsModal> grpEventsModalList=new ArrayList<>();
                JSONObject object2 = Jarray2.getJSONObject(i);
                String dur = object2.get("duration").toString();
                date[i]=dur;
                date[i]=date[i].split("-")[0];
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(date[i]);
                date[i]=dt.toString();
                date[i]=date[i].substring(8, 10) + " " + date[i].substring(4, 7) + " " + date[i].substring(30, 34);
                String fdate2 = null;
                for(int k = i; k<Jarray2.length(); k++)
                {
                    int flag=0;
                    JSONObject object = Jarray2.getJSONObject(k);
                    String title = object.get("title").toString();
                    String st = object.get("status").toString();
                    String dur2 = object.get("duration").toString();
                    String parti = object.get("participates").toString();
                    String ent_coin = object.get("EntryCoin").toString();
                    String minp = object.get("minP").toString();
                    String maxp = object.get("maxP").toString();
                    String target = object.get("target").toString();
                    String eid = object.get("id").toString();
                    String level = object.get("levelUp").toString();
                    String tempdate = dur2.split("-")[0];
                    Date dts = new SimpleDateFormat("dd/MM/yyyy").parse(tempdate);
                    tempdate=dts.toString();
                    tempdate=tempdate.substring(8, 10) + " " + tempdate.substring(4, 7) + " " + tempdate.substring(30, 34);
                    for(int p = 0; p<Jarray3.length();p++)
                    {
                        JSONObject object3 = Jarray3.getJSONObject(p);
                        String evid = object3.get("eid").toString();
                        String uid = object3.get("uid").toString();
                        Log.d("evid", evid + " " + uid);
                        if(evid.equals(eid))
                        {
                            if(userid.equals(uid))
                            {
                                flag=1;
                                break;
                            }
                        }
                    }
                    if(st.equals("2") && flag==1)
                    {
                        if(tempdate.equals(date[i]))
                        {
                            GrpEventsModal modal = new GrpEventsModal(
                                    eid, title, minp,
                                    maxp, level, ent_coin, dur, target, parti, "ongoing", "grpev");
                            grpEventsModalList.add(modal);
                            fdate2=tempdate;
                        }
                        else
                        {
                            Log.d("Dates" + k, String.valueOf(grpEventsModalList.size()));
                            i=(k-1);
                            break;
                        }
                    }
                    if(k==(Jarray2.length()-1))
                    {
                        i=k;
                        break;
                    }
                }
                if(fdate2!=null)
                {
                    allevents2.put(fdate2, grpEventsModalList);
                }
            }
            HashMap<String, List<GrpEventsModal>> finalevent = new HashMap<>();
            for(Map.Entry<String, List<GrpEventsModal>> m : allevents.entrySet())
            {
                finalevent.put(m.getKey(), m.getValue());
            }
            for(Map.Entry<String, List<GrpEventsModal>> m : allevents2.entrySet())
            {
                if(!finalevent.containsKey(m.getKey()))
                {
                    finalevent.put(m.getKey(), m.getValue());
                }
                else
                {
                    List<GrpEventsModal> grpEventsModalList=new ArrayList<>();
                    grpEventsModalList=finalevent.get(m.getKey());
                    grpEventsModalList.addAll(m.getValue());
                    finalevent.put(m.getKey(), grpEventsModalList);
                }
            }
            for(Map.Entry<String, List<GrpEventsModal>> m : finalevent.entrySet())
            {
                EventCardModal cardModal = new EventCardModal(m.getKey(), m.getValue());
                eventCardModalList.add(cardModal);
                Log.d("Sizesss", String.valueOf(m.getValue().size()) + m.getKey());
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            Log.d("Error2", e.toString());
        }
        return eventCardModalList;
    }
}