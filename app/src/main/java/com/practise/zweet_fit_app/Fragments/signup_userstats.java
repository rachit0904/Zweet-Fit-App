package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.practise.zweet_fit_app.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
                    try {
                        pref = getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
                        preferences = pref.edit();
                        String username = edit_usname.getText().toString();
                        if (!username.isEmpty()) {
                            validateUsername(username);
                            preferences.putString("usname", username);
                        } else {
                            Toast.makeText(getContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                        }
                        if (!edit_Height.getText().toString().isEmpty()) {
                            dataModal.setHeight(edit_Height.getText().toString());
                        }
                        if (!edit_Weight.getText().toString().isEmpty()) {
                            dataModal.setWeight(edit_Weight.getText().toString());
                        }
                        if (!edit_Target.getText().toString().isEmpty()) {
                            if (Integer.parseInt(edit_Target.getText().toString()) < 750) {
                                Toast.makeText(getContext(), "Minimum Target is 750 Steps!", Toast.LENGTH_SHORT).show();
                            } else {
                                dataModal.setTarget(edit_Target.getText().toString());
                                preferences.putString("target", dataModal.getTarget());
                            }
                        }
                        preferences.putString("wt", dataModal.getWeight());
                        preferences.putString("ht", dataModal.getHeight());
                        preferences.apply();
                        createUser(pref);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getActivity(), SignUp.class);
                    intent.putExtra("fragment", "get started");
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return view;
    }

    private void createUser(SharedPreferences pref) {
        String url = Constant.ServerUrl+"/updateUsers";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid",pref.getString("id","1"))
                .addFormDataPart("username",pref.getString("usname",""))
                .addFormDataPart("name",pref.getString("name","test user"))
                .addFormDataPart("dob",pref.getString("dob","09-04-2001"))
                .addFormDataPart("weight",pref.getString("wt","0"))
                .addFormDataPart("height",pref.getString("ht","0"))
                .addFormDataPart("target",pref.getString("target","1000"))
                .addFormDataPart("streak","0")
                .addFormDataPart("steps","0")
                .addFormDataPart("subscription","true")
                .addFormDataPart("coins","0")
                .addFormDataPart("points","0")
                .addFormDataPart("level","1")
                .addFormDataPart("win_rate","0")
                .addFormDataPart("mobile","0")
                .addFormDataPart("dp_url","")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            Log.i("response",data);
            if(object.getString("status").equals("200")){

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void validateUsername(String username) {

    }
}