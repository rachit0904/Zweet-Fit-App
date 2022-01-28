package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Activity.SignUp;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;


public class signup_userstats extends Fragment {

    Button submit;
    TextInputEditText edit_Weight, edit_Height, edit_Target;
    SharedPreferences.Editor preferences;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_userstats, container, false);
        edit_Weight = view.findViewById(R.id.edit_Weight);
        edit_Height = view.findViewById(R.id.edit_Height);
        edit_Target = view.findViewById(R.id.edit_Target);
        submit=view.findViewById(R.id.next);
        UsersDataModal dataModal=new UsersDataModal();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==submit) {
                    if (!edit_Height.getText().toString().isEmpty()) {
                        dataModal.setHeight(edit_Height.getText().toString());
                    }
                    if(!edit_Weight.getText().toString().isEmpty()){
                        dataModal.setWeight(edit_Weight.getText().toString());
                    }
                    if(!edit_Target.getText().toString().isEmpty()){
                        dataModal.setTarget(edit_Target.getText().toString());
                    }
                    Intent intent=new Intent(getActivity(), SignUp.class);
                    intent.putExtra("fragment","get started");
                    pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
                    preferences=pref.edit();
                    preferences.putString("wt",dataModal.getWeight());
                    preferences.putString("ht",dataModal.getHeight());
                    preferences.putString("target",dataModal.getTarget());
                    preferences.apply();

                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return view;
    }
}