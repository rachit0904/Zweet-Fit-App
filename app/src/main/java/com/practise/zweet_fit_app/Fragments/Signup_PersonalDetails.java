package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Activity.SignUp;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;

import java.util.ArrayList;
import java.util.List;


public class Signup_PersonalDetails extends Fragment implements View.OnClickListener {
    Spinner gender;
    TextInputEditText edit_Name, edit_dob;
    Button next;
    String[] genders = {"Gender", "Male", "Female"};
    List<UsersDataModal> userData=new ArrayList<>();
    SharedPreferences.Editor preferences;
    SharedPreferences pref;
    UsersDataModal dataModal=new UsersDataModal();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup__personal_details, container, false);
        gender = view.findViewById(R.id.gender);
        edit_dob = view.findViewById(R.id.edit_dob);
        edit_Name = view.findViewById(R.id.edit_Name);
        next=view.findViewById(R.id.next);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        next.setOnClickListener(this);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataModal.setGender(genders[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    @Override
    public void onClick(View view) {
        if(view==next){
            if(!edit_Name.getText().toString().isEmpty()){
                dataModal.setName(edit_Name.getText().toString());
            }
            if(!edit_dob.getText().toString().isEmpty()){
                dataModal.setDob(edit_dob.getText().toString());
            }
            userData.add(dataModal);
            Intent intent=new Intent(getActivity(),SignUp.class);
            intent.putExtra("fragment","stats");
            pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
            preferences=pref.edit();
            preferences.putString("name",dataModal.getName());
            preferences.putString("dob",dataModal.getDob());
            preferences.putString("gender",dataModal.getGender());
            preferences.apply();
            startActivity(intent);
            getActivity().finish();
        }
    }
}