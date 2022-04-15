package com.practise.zweet_fit_app.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.auth.User;
import com.practise.zweet_fit_app.Adapters.FriendsCardAdapter;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
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

public class SearchFragment extends Fragment {
    SearchView searchView;
    String temp;
    RecyclerView searchedUsers;
    TextView userNotAvailable;
    FriendsCardAdapter adapter;
    SharedPreferences pref;
    List<UsersDataModal> usersDataModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search);
        searchedUsers=view.findViewById(R.id.searchedUsers);
        userNotAvailable=view.findViewById(R.id.notAvailable);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        searchedUsers.setHasFixedSize(true);
        searchedUsers.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapter=new FriendsCardAdapter(usersDataModalList,getContext());
        searchedUsers.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersDataModalList.clear();
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String user) {
                if (user.isEmpty())
                {
                    usersDataModalList.clear();
                    adapter.notifyDataSetChanged();
                    userNotAvailable.setVisibility(View.GONE);
                }
                else
                {
                    usersDataModalList.clear();
                    searchUser(user);
                }
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.clearFocus();
        return view;
    }

    private void searchUser(String user) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=users&query=name&value="+user)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            Log.i("response data",data);
            if(jsonArray.length()>0) {
                searchedUsers.setVisibility(View.VISIBLE);
                userNotAvailable.setVisibility(View.GONE);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    UsersDataModal dataModal = new UsersDataModal();
                    dataModal.setUid(obj.getString("uid"));
                    dataModal.setName(obj.getString("name"));
                    if(isFriend(obj.getString("uid"))) {
                        dataModal.setIsFriend("yes");
                    }else{
                        dataModal.setIsFriend("no");
                    }
                    dataModal.setCardType("user");
                    dataModal.setImagePath(obj.getString("dp_url"));
                    usersDataModalList.add(dataModal);
                }
                adapter.notifyDataSetChanged();
            }else{
                searchedUsers.setVisibility(View.GONE);
                userNotAvailable.setVisibility(View.VISIBLE);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isFriend(String pid) {
        String uid=pref.getString("id","");
        if(pid.equals(uid)){
            return false;
        }else {
            String status = "";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://35.207.233.155:3578/selectwQuery?table=friends&query=uid&value=" + pref.getString("id", "") + "&query=fid&value=" + pid)
                    .method("GET", null)
                    .addHeader("key", "MyApiKEy")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (pid.equals(obj.getString("fid")) || pid.equals(obj.getString("uid"))
                                && uid.equals(obj.getString("uid")) || uid.equals(obj.getString("fid"))) {
                            status = obj.getString("status");
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Log.i("status", status);
            if (status.equals("friend")) {
                return true;
            } else {
                return false;
            }
        }
    }

}