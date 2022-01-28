package com.practise.zweet_fit_app.Fragments;

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

import java.util.ArrayList;
import java.util.List;

public class FriendsList extends Fragment {
    RecyclerView friends;
    List<UsersDataModal> friendsList =new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_friends_list, container, false);
        friends =view.findViewById(R.id.friends);
        friends.setHasFixedSize(true);
        friends.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        FriendsCardAdapter adapter=new FriendsCardAdapter(getFriends());
        friends.setAdapter(adapter);
        return view;
    }

    private List<UsersDataModal> getFriends() {

        for(int i=0;i<2;i++){
            UsersDataModal modal=new UsersDataModal();
            String name[]={"Rachit","Shiv"};
            modal.setIsFriend("yes");
            modal.setCardType("friends");
            modal.setName(name[i]);
            friendsList.add(modal);
        }
        return friendsList;
    }

}