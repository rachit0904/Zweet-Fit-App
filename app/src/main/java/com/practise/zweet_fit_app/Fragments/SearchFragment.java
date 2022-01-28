package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.practise.zweet_fit_app.Adapters.FriendsCardAdapter;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    SearchView searchView;
    RecyclerView searchedUsers;
    List<UsersDataModal> usersDataModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search);
        searchedUsers=view.findViewById(R.id.searchedUsers);
        searchedUsers.setHasFixedSize(true);
        searchedUsers.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        FriendsCardAdapter adapter=new FriendsCardAdapter(usersDataModalList);
        searchedUsers.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String user) {
                searchUser(user);
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.clearFocus();
        return view;
    }

    private void searchUser(String user) {
        //TODO - search user from db and add to RV list.

    }
}