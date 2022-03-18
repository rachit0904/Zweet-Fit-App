package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Activity.SignUp;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Server.ServerRequests;


public class signup_userstats extends Fragment {

    Button submit;
    TextInputEditText edit_Weight, edit_Height, edit_Target,edit_usname;
    SharedPreferences.Editor preferences;
    TextView status;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_userstats, container, false);
        edit_Weight = view.findViewById(R.id.edit_Weight);
        edit_Height = view.findViewById(R.id.edit_Height);
        edit_Target = view.findViewById(R.id.edit_Target);
        edit_usname = view.findViewById(R.id.edit_usname);
        status = view.findViewById(R.id.status);
        submit=view.findViewById(R.id.next);
        UsersDataModal dataModal=new UsersDataModal();

        edit_usname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!view.hasFocus()){
                    status.setVisibility(View.VISIBLE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==submit) {
                    pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
                    preferences=pref.edit();
                    String username=edit_usname.getText().toString();
                    if(!username.isEmpty()){
                        validateUsername(username);
                        preferences.putString("usname",username);
                    }else{
                        Toast.makeText(getContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                    }
                    if (!edit_Height.getText().toString().isEmpty()) {
                        dataModal.setHeight(edit_Height.getText().toString());
                    }
                    if(!edit_Weight.getText().toString().isEmpty()){
                        dataModal.setWeight(edit_Weight.getText().toString());
                    }
                    if(!edit_Target.getText().toString().isEmpty()){
                        if(Integer.parseInt(edit_Target.getText().toString())<750){
                            Toast.makeText(getContext(), "Minimum Target is 750 Steps!", Toast.LENGTH_SHORT).show();
                        }else {
                            dataModal.setTarget(edit_Target.getText().toString());
                        }
                    }
                    Intent intent=new Intent(getActivity(), SignUp.class);
                    intent.putExtra("fragment","get started");
                    preferences.putString("wt",dataModal.getWeight());
                    preferences.putString("ht",dataModal.getHeight());
                    preferences.putString("target",dataModal.getTarget());
                    preferences.apply();
//                    createUser(pref);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return view;
    }

    private void createUser(SharedPreferences pref) {
        ServerRequests request=new ServerRequests();
        request.addUsers(
                pref.getString("id",""),pref.getString("name",""),pref.getString("dob",""),
                pref.getString("wt",""),pref.getString("ht",""),pref.getString("target",""),
                "0",
                "true",pref.getString("coins",""),"1",
                "80","",pref.getString("dp","")
        );
    }

    private void validateUsername(String username) {

    }
}