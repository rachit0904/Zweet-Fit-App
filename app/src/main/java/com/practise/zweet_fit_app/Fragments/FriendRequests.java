package com.practise.zweet_fit_app.Fragments;import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import androidx.fragment.app.Fragment;import androidx.recyclerview.widget.LinearLayoutManager;import androidx.recyclerview.widget.RecyclerView;import com.practise.zweet_fit_app.Adapters.FriendsCardAdapter;import com.practise.zweet_fit_app.Modals.UsersDataModal;import com.practise.zweet_fit_app.R;import java.util.ArrayList;import java.util.List;public class FriendRequests extends Fragment {    RecyclerView requests;    List<UsersDataModal> requestsList=new ArrayList<>();    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container,                             Bundle savedInstanceState) {        View view= inflater.inflate(R.layout.fragment_friend_requests, container, false);        requests=view.findViewById(R.id.friendRequests);        requests.setHasFixedSize(true);        requests.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));        FriendsCardAdapter adapter=new FriendsCardAdapter(getRequests());        requests.setAdapter(adapter);        return view;    }    private List<UsersDataModal> getRequests() {        for(int i=0;i<2;i++){            UsersDataModal modal=new UsersDataModal();            String name[]={"Rachit","Shiv"};            modal.setIsFriend("no");            modal.setCardType("request");            modal.setName(name[i]);            requestsList.add(modal);        }        return requestsList;    }}