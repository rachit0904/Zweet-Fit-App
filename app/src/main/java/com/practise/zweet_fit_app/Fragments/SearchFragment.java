package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.firestore.auth.User;
import com.practise.zweet_fit_app.Adapters.FriendsCardAdapter;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    SearchView searchView;
    String temp;
    RecyclerView searchedUsers;
    List<UsersDataModal> usersDataModalList=new ArrayList<>();
    List<UsersDataModal> usersDataModalList2=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search);
        searchedUsers=view.findViewById(R.id.searchedUsers);
        searchedUsers.setHasFixedSize(true);
  UsersDataModal obj = new UsersDataModal("123455", "345", "Rachit", "true", "12", "5000", "200", "50", "9000", "2", "100", "100", "Male", "3", "50", "2", "Premium", "29-11-2002", "12");
  UsersDataModal obj1 = new UsersDataModal("123455", "345", "Jay", "true", "12", "5000", "200", "50", "9000", "2", "100", "100", "Male", "3", "50", "2", "Premium", "29-11-2002", "12");
  UsersDataModal obj2 = new UsersDataModal("123455", "345", "Jay", "true", "12", "5000", "200", "50", "9000", "2", "100", "100", "Male", "3", "50", "2", "Premium", "29-11-2002", "12");
        usersDataModalList2.add(obj);
        usersDataModalList2.add(obj1);
        usersDataModalList2.add(obj2);
        searchedUsers.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        FriendsCardAdapter adapter=new FriendsCardAdapter(usersDataModalList, requireContext());
        searchedUsers.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersDataModalList.clear();
//                if (!query.isEmpty())
//                {
//                    searchUser(query);
//                    adapter.notifyDataSetChanged();
//                }
                searchUser(query);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String user) {
                if (user.isEmpty())
                {
                    usersDataModalList.clear();
                }
                else
                {
                    usersDataModalList.clear();
                    searchUser(user);
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
//        if(usersDataModalList!=null)
//        {
//            usersDataModalList.
//        }
        searchView.setIconified(false);
        searchView.clearFocus();
        usersDataModalList.clear();
        return view;
    }

    private void searchUser(String user) {
        //TODO - search user from db and add to RV list.
        for(UsersDataModal d : usersDataModalList2)
        {
            if(d.getName() != null && d.getName().contains(user))
            {
                usersDataModalList.add(d);
            }
        }
    }
}