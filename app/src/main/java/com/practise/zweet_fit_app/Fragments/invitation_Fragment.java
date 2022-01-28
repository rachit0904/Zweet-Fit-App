package com.practise.zweet_fit_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Adapters.InvitationRvAdapter;
import com.practise.zweet_fit_app.Modals.InviteCardModal;
import com.practise.zweet_fit_app.R;


import java.util.ArrayList;
import java.util.List;

public class invitation_Fragment extends Fragment implements View.OnClickListener {
    FloatingActionButton addEvent;
    List<InviteCardModal> inviteCardModalList=new ArrayList<>();
    RecyclerView invititationsRv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_invitation_, container, false);
        invititationsRv=view.findViewById(R.id.invitations);
        addEvent=view.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(this);
        inviteCardModalList.clear();
        invititationsRv.setHasFixedSize(true);
        invititationsRv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        InvitationRvAdapter adapter=new InvitationRvAdapter(getInvitations(),view.getContext());
        invititationsRv.setAdapter(adapter);
        return view;
    }

    private List<InviteCardModal> getInvitations() {
        String names[]={"Rachit","Sushil","Shiv"};
        for(int i=0;i<3;i++) {
            InviteCardModal cardModal = new InviteCardModal(String.valueOf(i+1),"Fit Freek's Tour","from "+names[i]);
            inviteCardModalList.add(cardModal);
        }
        return inviteCardModalList;
    }

    @Override
    public void onClick(View view) {
        if(view==addEvent){
            AlertDialog.Builder addEventDialog=new AlertDialog.Builder(getContext());
            View eventDialog=getLayoutInflater().inflate(R.layout.add_events_dialog,null);
            String friends[]={"Select Friend"};
            String event[]={"Peer To Peer"};
            Spinner eventType=eventDialog.findViewById(R.id.eventType);
            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, event);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eventType.setAdapter(adapter);
            Spinner selectFriend=eventDialog.findViewById(R.id.selectFriend);
            ArrayAdapter adapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, friends);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            addEventDialog.setView(eventDialog);
            selectFriend.setAdapter(adapter2);
            TextInputEditText target=eventDialog.findViewById(R.id.edit_Target);
            TextInputEditText dur=eventDialog.findViewById(R.id.edit_Duration);
            TextInputEditText pts=eventDialog.findViewById(R.id.edit_Points);
            TextInputEditText coins=eventDialog.findViewById(R.id.edit_Coin);
            Button host=eventDialog.findViewById(R.id.host);
            AlertDialog dialog=addEventDialog.create();
            dialog.setCancelable(false);
            dialog.show();
            host.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //select friend
                    selectFriend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            String friend=friends[position];
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    //TODO- take data of target dur pts coins and friend for creating event;
                    dialog.dismiss();
                }
            });
        }
    }
}