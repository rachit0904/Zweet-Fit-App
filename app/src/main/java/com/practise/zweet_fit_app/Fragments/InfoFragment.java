package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.practise.zweet_fit_app.Adapters.FriendsCardAdapter;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InfoFragment extends Fragment {
    TextView stdate, endate, timeleft, eventtar, eventrew;
    RecyclerView partirv;
    List<UsersDataModal> friendsList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info, container, false);
        friendsList.clear();
        stdate = view.findViewById(R.id.startDate);
        endate = view.findViewById(R.id.endDate);
        timeleft = view.findViewById(R.id.dur);
        eventtar = view.findViewById(R.id.evtar);
        eventrew = view.findViewById(R.id.evrew);
        partirv = view.findViewById(R.id.participantsRv);
        partirv.setHasFixedSize(true);
        partirv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FriendsCardAdapter adapter = new FriendsCardAdapter(getFriends(), requireContext());
        partirv.setAdapter(adapter);
        setData();
        return view;
    }

    private List<UsersDataModal> getFriends()
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
            for(int i=0;i<Jarray2.length();i++)
            {
                JSONObject obj = Jarray2.getJSONObject(i);
                String eid = obj.get("eid").toString();
                String uid = obj.get("uid").toString();
                if(eid.equals(grpid))
                {
                    for(int k =0;k<Jarray.length();k++)
                    {
                        JSONObject obj2 = Jarray.getJSONObject(k);
                        String uid2 = obj2.get("uid").toString();
                        if(uid.equals(uid2))
                        {
                            UsersDataModal modal = new UsersDataModal();
                            modal.setUid(uid);
                            modal.setCardType("friend");
                            modal.setName(obj2.getString("name"));
                            modal.setImagePath(obj2.get("dp_url").toString());
                            friendsList.add(modal);
                            break;
                        }
                    }
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("Leave", e.toString());
        }
        return friendsList;
    }

    private void setData() {
        String tempdate = getActivity().getIntent().getStringExtra("dur").split("-")[0];
        stdate.setText(tempdate);
        Log.d("Durat", getActivity().getIntent().getStringExtra("dur"));
        eventtar.setText("TARGET : " + getActivity().getIntent().getStringExtra("target") + " Steps");
        String tempdate2 = getActivity().getIntent().getStringExtra("dur").split("-")[1];
        endate.setText(tempdate2);
        String val = LocalDateTime.now().toString();
        Log.d("Dates", tempdate2.substring(1, 3));
        String dur = String.valueOf(Integer.parseInt(tempdate2.substring(1, 3)) - Integer.parseInt(val.substring(8,10)));
        eventrew.setText("REWARD : " + getActivity().getIntent().getStringExtra("coins") + " Coins");
        timeleft.setText(dur + " days");
    }
}