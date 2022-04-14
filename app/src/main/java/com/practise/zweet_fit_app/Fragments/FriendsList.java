package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class FriendsList extends Fragment {
    RecyclerView friends;
    List<UsersDataModal> friendsList = new ArrayList<>();
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        pref = getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        friends = view.findViewById(R.id.friends);
        friends.setHasFixedSize(true);
        friends.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FriendsCardAdapter adapter = new FriendsCardAdapter(getFriends(), requireContext());
        friends.setAdapter(adapter);
        return view;
    }

    private List<UsersDataModal> getFriends() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/select?table=friends")
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            String uid = pref.getString("id", "");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if ((uid.equals(obj.getString("fid")) || uid.equals(obj.getString("uid"))) && obj.getString("status").equals("friend")) {
                        UsersDataModal friend = new UsersDataModal();
                        if (uid.equals(obj.getString("fid"))) {
                            friend.setUid(obj.getString("uid"));
                        } else {
                            friend.setUid(obj.getString("fid"));
                        }
                        friendsList.add(getFriendData(friend));
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return friendsList;
    }

    private UsersDataModal getFriendData(UsersDataModal user) {
        user.setIsFriend("yes");
        user.setCardType("friend");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=users&query=uid&value=" + user.getUid())
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
                    user.setName(obj.getString("name"));
                    user.setImagePath(obj.getString("dp_url"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}