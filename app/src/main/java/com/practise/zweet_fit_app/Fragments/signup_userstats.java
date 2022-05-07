package com.practise.zweet_fit_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import org.json.JSONArray;
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
    boolean validUser=false;
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
                    if(!edit_usname.getText().toString().isEmpty()) {
                        validateUsername(edit_usname.getText().toString());
                        if(validUser){
                            status.setText("User name valid!");
                        }else{
                            status.setText("User name taken!");
                        }
                        status.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==submit) {
                    int fl1=0, fl2=0, fl3=0, fl4=0;
                    try {
                        pref = getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
                        preferences = pref.edit();
                        String username = edit_usname.getText().toString();
                        if (!username.isEmpty()) {
                            fl1=1;
                            if(validUser) {
                                preferences.putString("usname", username);
                            }
                        } else {
                            Toast.makeText(getContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                        }
                        if (!edit_Height.getText().toString().isEmpty()) {
                            fl4=1;
                            dataModal.setHeight(edit_Height.getText().toString());
                            preferences.putString("ht", dataModal.getHeight());
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Height Cannot Be Empty !!", Toast.LENGTH_SHORT).show();
                        }
                        if (!edit_Weight.getText().toString().isEmpty()) {
                            fl3=1;
                            dataModal.setWeight(edit_Weight.getText().toString());
                            preferences.putString("wt", dataModal.getWeight());
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Weight Cannot Be Empty !!", Toast.LENGTH_SHORT).show();
                        }
                        if (!edit_Target.getText().toString().isEmpty()) {
                            if (Integer.parseInt(edit_Target.getText().toString()) < 750) {
                                Toast.makeText(getContext(), "Minimum Target is 750 Steps!", Toast.LENGTH_SHORT).show();
                            } else {
                                fl2=1;
                                dataModal.setTarget(edit_Target.getText().toString());
                                preferences.putString("target", dataModal.getTarget());
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Target Cannot Be Empty !!", Toast.LENGTH_SHORT).show();
                        }
                        if(fl1==1 && fl2==1 && fl3==1 && fl4==1)
                        {
                            preferences.apply();
                            createUser(pref);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(fl1==1 && fl2==1 && fl3==1 && fl4==1)
                    {
                        Log.d("EveryThing", edit_Target.getText().toString());
                        Intent intent = new Intent(getActivity(), SignUp.class);
                        intent.putExtra("fragment", "get started");
                        startActivity(intent);
                        getActivity().finish();
                    }
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
                .addFormDataPart("creation_date","2050-06-30 2:00:00")
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
       Usernames usernames=new Usernames();
       usernames.username=username;
       usernames.execute();
       validUser=usernames.rslt;
    }
}

class Usernames extends AsyncTask<String,Void,Boolean> {

    String username;
    boolean rslt;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean rslt=false;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Constant.ServerUrl+"/selectwQuery?table=users&query=username&value="+username)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("data");
            if(array.length()>0){
                rslt=false;
            }else{
                rslt=true;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return rslt;
    }

    protected void onPostExecute(Boolean result) {
        rslt=result;
    }

}
