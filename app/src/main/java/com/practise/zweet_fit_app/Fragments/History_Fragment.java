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
import java.util.ArrayList;
import java.util.List;

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
            List<GrpEventsModal> grpEventsModalList=new ArrayList<>();
            String url = Constant.ServerUrl+"/select?table=events";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://35.207.233.155:3578/select?table=events")
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            String date[]=new String[Jarray.length()];
            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject object = Jarray.getJSONObject(i);
                String title = object.get("title").toString();
                String st = object.get("status").toString();
                String dur = object.get("duration").toString();
                String target = object.get("target").toString();
                String ent_coin = object.get("entry_coin").toString();
                if(st.equals("0"))
                {
                    date[cnt]=dur;
                    GrpEventsModal modal = new GrpEventsModal(
                            "1", title + (i + 1), "2",
                            "5", "3", ent_coin, dur, target, "", "ongoing");
                    grpEventsModalList.add(modal);
                    cnt++;
                }
            }

            for (int i=0;i< cnt;i++) {
                {
                    List<GrpEventsModal> gl=new ArrayList<>();
                    gl.add(grpEventsModalList.get(i));
                    EventCardModal cardModal = new EventCardModal(date[i], gl);
                    eventCardModalList.add(cardModal);
                    Log.d("Tag", String.valueOf(cnt));
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("Error2", e.toString());
        }
        return eventCardModalList;
    }

}