package com.practise.zweet_fit_app.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Activity.SignUp;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Signup_PersonalDetails extends Fragment implements View.OnClickListener {
    Spinner gender;
    TextInputEditText edit_Name;
    Button next,edit_dob;
    String[] genders = {"Gender", "Male", "Female", "Other"};
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
        pref= getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        edit_dob.setOnClickListener(this);
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
        if(view == edit_dob){
            Calendar cal = Calendar.getInstance();
            int year = LocalDate.now().getYear();
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    if(y>=LocalDate.now().getYear() || (LocalDate.now().getYear()-y)<=3){
                        Toast.makeText(getContext(), "Enter a valid DOB!", Toast.LENGTH_SHORT).show();
                    }else{
                        m++;
                        edit_dob.setText(d+"/"+ m+"/"+y);
                        preferences=pref.edit();
                        preferences.putString("dob",d+"/"+m +"/"+y);
                        preferences.apply();
                    }
                }
            },year,month,day);

            datePickerDialog.show();
        }
        if(view==next){
            boolean f1=false,f2=false,f3=false;
            String d=edit_dob.getText().toString();
            if(d.split("/")[2] != "YYYY"){
                f1=true;
            }else{
                Toast.makeText(getContext(), "Enter a valid DOB!", Toast.LENGTH_SHORT).show();
            }
            if(!edit_Name.getText().toString().isEmpty()){
                dataModal.setName(edit_Name.getText().toString());
                f2=true;
            }else{
                Toast.makeText(getContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
            }
            if(!edit_dob.getText().toString().isEmpty()){
                dataModal.setDob(edit_dob.getText().toString());
                f3=true;
            }
            if(f1 && f2 && f3) {
                userData.add(dataModal);
                Intent intent = new Intent(getActivity(), SignUp.class);
                intent.putExtra("fragment", "stats");
                pref = getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
                preferences = pref.edit();
                preferences.putString("name", dataModal.getName());
                preferences.putString("gender", dataModal.getGender());
                preferences.apply();
                Log.d("result","pd saved");
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}