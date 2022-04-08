package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class History_Fragment extends Fragment {
    RecyclerView parentEventRv;
    int cnt=0;
    List<EventCardModal> eventCardModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history_, container, false);
        parentEventRv=view.findViewById(R.id.parentRv);
        parentEventRv.setHasFixedSize(true);
        eventCardModalList.clear();
        parentEventRv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        EventParentAdapter adapter=new EventParentAdapter(view.getContext(),getCardDetails());
        parentEventRv.setAdapter(adapter);
        return view;
    }

    private List<EventCardModal> getCardDetails() {
        try {
            String url = Constant.ServerUrl+"/select?table=events";
            String url2 = Constant.ServerUrl+"/select?table=group_event";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            OkHttpClient client2 = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://35.207.233.155:3578/select?table=events")
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Request request2 = new Request.Builder()
                    .url("http://35.207.233.155:3578/select?table=group_event")
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
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            HashMap<String, List<GrpEventsModal>> allevents = new HashMap<>();
            HashMap<String, List<GrpEventsModal>> allevents2 = new HashMap<>();
            JSONArray Jarray = Jobject.getJSONArray("data");
            String respo2 = response2.body().string();
            JSONObject Jobject2 = new JSONObject(respo2);
            JSONArray Jarray2 = Jobject2.getJSONArray("data");
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
                    JSONObject object = Jarray.getJSONObject(k);
                    String title = object.get("title").toString();
                    String st = object.get("status").toString();
                    String dur2 = object.get("duration").toString();
                    String target = object.get("target").toString();
                    String ent_coin = object.get("entry_coin").toString();
                    String tempdate = dur2.split("-")[0];
                    Date dts = new SimpleDateFormat("dd/MM/yyyy").parse(tempdate);
                    tempdate=dts.toString();
                    tempdate=tempdate.substring(8, 10) + " " + tempdate.substring(4, 7) + " " + tempdate.substring(30, 34);
                    if(st.equals("0"))
                    {
                        if(tempdate.equals(date[i]))
                        {
                            GrpEventsModal modal = new GrpEventsModal(
                                    "1", title + (i + 1), "2",
                                    "5", "3", ent_coin, dur, target, "", "finished");
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
                    JSONObject object = Jarray2.getJSONObject(k);
                    String title = object.get("title").toString();
                    String st = object.get("status").toString();
                    String dur2 = object.get("duration").toString();
                    String parti = object.get("participates").toString();
                    String ent_coin = object.get("EntryCoin").toString();
                    String minp = object.get("minP").toString();
                    String maxp = object.get("maxP").toString();
                    String target = object.get("target").toString();
                    String tempdate = dur2.split("-")[0];
                    Date dts = new SimpleDateFormat("dd/MM/yyyy").parse(tempdate);
                    tempdate=dts.toString();
                    tempdate=tempdate.substring(8, 10) + " " + tempdate.substring(4, 7) + " " + tempdate.substring(30, 34);
                    if(st.equals("0"))
                    {
                        if(tempdate.equals(date[i]))
                        {
                            GrpEventsModal modal = new GrpEventsModal(
                                    "1", title + (i + 1), minp,
                                    maxp, "3", ent_coin, dur, target, parti, "finished");
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
        } catch (JSONException | IOException | ParseException e) {
            e.printStackTrace();
            Log.d("Error2", e.toString());
        }
        return eventCardModalList;
    }
}