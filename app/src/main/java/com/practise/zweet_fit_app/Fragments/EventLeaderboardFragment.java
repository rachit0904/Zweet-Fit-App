package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practise.zweet_fit_app.Adapters.LeaderboardAdapter;
import com.practise.zweet_fit_app.Modals.EventCardModal;
import com.practise.zweet_fit_app.Modals.GrpLeaderboardModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventLeaderboardFragment extends Fragment {
    RecyclerView rv;
    List<GrpLeaderboardModal> grpleaderboard=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_leaderboard, container, false);
        rv = view.findViewById(R.id.leaderboard_rv);
        rv.setHasFixedSize(true);
        grpleaderboard.clear();
        rv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        LeaderboardAdapter adapter = new LeaderboardAdapter(getCardDetails(),view.getContext());
        rv.setAdapter(adapter);
        return view;
    }

    private List<GrpLeaderboardModal> getCardDetails()  {
        try {
            String url = Constant.ServerUrl + "/select?table=leaderboard";
            String grpid = getActivity().getIntent().getStringExtra("id");
            String url2 = Constant.ServerUrl + "/select?table=users";
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
                response2 = client2.newCall(request2).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            String respo2 = response2.body().string();
            JSONObject Jobject2 = new JSONObject(respo2);
            JSONArray Jarray2 = Jobject2.getJSONArray("data");
            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject object = Jarray.getJSONObject(i);
                String eid = object.get("eid").toString();
                String w1 = object.get("w1").toString();
                String w2 = object.get("w2").toString();
                String w3 = object.get("w3").toString();
                if (eid.equals(grpid)){
                    if(w1!=null)
                    {
                        for (int j=0;j<Jarray2.length();j++)
                        {
                            JSONObject object2 = Jarray2.getJSONObject(j);
                            String uids = object2.get("uid").toString();
                            String dp=object2.get("dp_url").toString();
                            Log.d("Eid", uids + " Winner1: " + w1);
                            if(uids.equals(w1)){
                                Log.d("Eid", uids);
                                String name = object2.get("name").toString();
                                String steps = object2.get("steps").toString();
                                String coins = object2.get("coins").toString();
                                String lvl = object2.get("level").toString();
                                String rank = "1";
                                GrpLeaderboardModal modal = new GrpLeaderboardModal(grpid, name, rank, steps, coins, lvl,dp);
                                grpleaderboard.add(modal);
                            }
                        }
                    }
                    if(w2!=null)
                    {
                        for (int j=0;j<Jarray2.length();j++)
                        {
                            JSONObject object2 = Jarray2.getJSONObject(j);
                            String uids2 = object2.get("uid").toString();
                            String dp=object2.get("dp_url").toString();
                            if(uids2.equals(w2)) {
                                String name = object2.get("name").toString();
                                String steps = object2.get("steps").toString();
                                String coins = object2.get("coins").toString();
                                String lvl = object2.get("level").toString();
                                String rank = "2";
                                GrpLeaderboardModal modal = new GrpLeaderboardModal(grpid, name, rank, steps, coins, lvl,dp);
                                grpleaderboard.add(modal);
                            }
                        }
                    }
                    if(w3!=null)
                    {
                        for (int j=0;j<Jarray2.length();j++)
                        {
                            JSONObject object2 = Jarray2.getJSONObject(j);
                            String uids3 = object2.get("uid").toString();
                            String dp=object2.get("dp_url").toString();
                            if(uids3.equals(w3)) {
                                String name = object2.get("name").toString();
                                String steps = object2.get("steps").toString();
                                String coins = object2.get("coins").toString();
                                String lvl = object2.get("level").toString();
                                String rank = "3";
                                GrpLeaderboardModal modal = new GrpLeaderboardModal(grpid, name, rank, steps, coins, lvl,dp);
                                grpleaderboard.add(modal);
                            }
                        }
                    }
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("Errors", e.toString());
        }
        return grpleaderboard;
    }
}