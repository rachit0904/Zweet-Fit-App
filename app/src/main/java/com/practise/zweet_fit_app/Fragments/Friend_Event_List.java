package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.practise.zweet_fit_app.Adapters.EventParentAdapter;
import com.practise.zweet_fit_app.Modals.EventCardModal;
import com.practise.zweet_fit_app.Modals.GrpEventsModal;
import com.practise.zweet_fit_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Friend_Event_List extends Fragment {
    TextView friendname;
    RecyclerView recyclerView;
    String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    SharedPreferences pref;
    String uid="";
    ImageView back;
    List<EventCardModal> eventCardModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_friend__event__list, container, false);
        recyclerView=view.findViewById(R.id.friend_event_rv);
        friendname = view.findViewById(R.id.FriendsName);
        uid=getActivity().getIntent().getStringExtra("uid");
        back=view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        eventCardModalList.clear();
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        EventParentAdapter adapter=new EventParentAdapter(view.getContext(),getCardDetails());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<EventCardModal> getCardDetails() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/select?table=events")
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        Request request2 = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=group_event_holder&query=uid&value="+uid)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("data");
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                if(obj.getString("p1id").equals(pref.getString("id","")) ||
                        obj.getString("p2id").equals(pref.getString("id",""))){
                    EventCardModal modal=new EventCardModal();
                    List<GrpEventsModal> GrpEventsModalList = new ArrayList<>();
                    String startDate=getDate(obj.getString("duration").split("-")[0]);
                    modal.setDate(startDate);
                    for(int k=i;k<array.length();k++) {
                        JSONObject object2 = array.getJSONObject(k);
                        String tempdate = getDate(object2.getString("duration").split("-")[0]);
                        if(tempdate.equals(startDate)){
                            GrpEventsModal grpEventsModal=new GrpEventsModal();
                            grpEventsModal.setDur(obj.getString("duration"));
                            grpEventsModal.setEntryCoins(obj.getString("entry_coin"));
                            grpEventsModal.setgId(obj.getString("id"));
                            grpEventsModal.setTarget(obj.getString("target"));
                            grpEventsModal.setParticipants(obj.getString("participates"));
                            grpEventsModal.setTitle(obj.getString("title"));
                            grpEventsModal.setMaxP(obj.getString("maxP"));
                            grpEventsModal.setMinP(obj.getString("minP"));
                            grpEventsModal.setLevelUp(obj.getString("levelUp"));
                            GrpEventsModalList.add(grpEventsModal);
                        }
                    }
                    if(!GrpEventsModalList.isEmpty())
                    {
                        Log.d("List", GrpEventsModalList.toString());
                        modal.setGrpEventsModalList(GrpEventsModalList);
                        eventCardModalList.add(modal);
                    }
                }
            }
            Response response2 = client.newCall(request2).execute();
            String data2=response2.body().string();
            JSONObject object2=new JSONObject(data2);
            JSONArray array2=object2.getJSONArray("data");
            for(int i=0;i<array2.length();i++){
                JSONObject obj = array2.getJSONObject(i);
                Request request3 = new Request.Builder()
                        .url("http://35.207.233.155:3578/selectwQuery?table=group_event&query=id&value="+obj.getString("eid"))
                        .method("GET", null)
                        .addHeader("key", "MyApiKEy")
                        .build();
                Response response3 = client.newCall(request3).execute();
                String data3=response3.body().string();
                JSONObject object3=new JSONObject(data3);
                JSONArray array3=object3.getJSONArray("data");
                for(int j=0;j<array2.length();j++) {
                    JSONObject obj2 = array.getJSONObject(j);
                    if(obj2.getString("p1id").equals(pref.getString("id","")) ||
                            obj2.getString("p2id").equals(pref.getString("id",""))){
                        EventCardModal modal=new EventCardModal();
                        List<GrpEventsModal> GrpEventsModalList = new ArrayList<>();
                        String startDate=getDate(obj2.getString("duration").split("-")[0]);
                        modal.setDate(startDate);
                        for(int k=i;k<array.length();k++) {
                            JSONObject object4 = array.getJSONObject(k);
                            String tempdate = getDate(object4.getString("duration").split("-")[0]);
                            if(tempdate.equals(startDate)){
                                GrpEventsModal grpEventsModal=new GrpEventsModal();
                                grpEventsModal.setDur(obj2.getString("duration"));
                                grpEventsModal.setEntryCoins(obj2.getString("entry_coin"));
                                grpEventsModal.setgId(obj2.getString("id"));
                                grpEventsModal.setTarget(obj2.getString("target"));
                                grpEventsModal.setParticipants(obj2.getString("participates"));
                                grpEventsModal.setTitle(obj2.getString("title"));
                                grpEventsModal.setMaxP(obj2.getString("maxP"));
                                grpEventsModal.setMinP(obj2.getString("minP"));
                                grpEventsModal.setLevelUp(obj2.getString("levelUp"));
                                GrpEventsModalList.add(grpEventsModal);
                            }
                        }
                        if(!GrpEventsModalList.isEmpty())
                        {
                            Log.d("List", GrpEventsModalList.toString());
                            modal.setGrpEventsModalList(GrpEventsModalList);
                            eventCardModalList.add(modal);
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return eventCardModalList;
    }

    private String getDate(String date) {
        String d=date.split(" ")[0];
        String dd=d.split("-")[2];
        String mm=months[Integer.parseInt(d.split("-")[1])-1];
        String yy=d.split("-")[0];
        return dd+" "+mm+" "+yy;
    }
}