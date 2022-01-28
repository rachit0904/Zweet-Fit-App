package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Ongoing_Fragment extends Fragment {
    RecyclerView recyclerView;
    List<EventCardModal> eventCardModalList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_ongoing_, container, false);
        recyclerView=view.findViewById(R.id.parentEventRv);
        recyclerView.setHasFixedSize(true);
        eventCardModalList.clear();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        EventParentAdapter adapter=new EventParentAdapter(view.getContext(),getCardDetails());
        recyclerView.setAdapter(adapter);
    return view;
    }

    private List<EventCardModal> getCardDetails() {
        String date[]={"9th Dec 2021","10th Dec 2021","11th Dec 2021"};
        List<GrpEventsModal> grpEventsModalList=new ArrayList<>();
        GrpEventsModal grpEventsModal = new GrpEventsModal(
                "101", "P2P    events", "2",
                "5", "15", "10",
                "24h", "8000", "", "ongoing"
        );
        grpEventsModalList.add(grpEventsModal);
        for (int i=0;i<3;i++) {
            {
                EventCardModal cardModal = new EventCardModal(date[i], grpEventsModalList);
                eventCardModalList.add(cardModal);
            }
        }
        return eventCardModalList;
    }
}