package com.practise.zweet_fit_app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Adapters.InvitationRvAdapter;
import com.practise.zweet_fit_app.Modals.InviteCardModal;
import com.practise.zweet_fit_app.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class invitation_Fragment extends Fragment implements View.OnClickListener {
    FloatingActionButton addEvent;
    List<InviteCardModal> inviteCardModalList=new ArrayList<>();
    RecyclerView invititationsRv;
    SharedPreferences pref;
    ArrayList<JSONObject> f=new ArrayList<JSONObject>();
    ArrayList<String> friends=new ArrayList<>();
    String rid="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_invitation_, container, false);
        invititationsRv=view.findViewById(R.id.invitations);
        addEvent=view.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(this);
        inviteCardModalList.clear();
        invititationsRv.setHasFixedSize(true);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        invititationsRv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        InvitationRvAdapter adapter=new InvitationRvAdapter(getInvitations(),view.getContext());
        invititationsRv.setAdapter(adapter);
        return view;
    }

    private List<InviteCardModal> getInvitations() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/select?table=invitation")
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            Log.i("invitations",data);
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            if(jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(pref.getString("id","").equals(object.getString("rid"))){
                        inviteCardModalList.add(getEventDetails(object));
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return inviteCardModalList;
    }

    private InviteCardModal getEventDetails(JSONObject obj) {
        InviteCardModal modal=new InviteCardModal();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        try {
            modal.setRid(obj.getString("rid"));
            String eid=obj.getString("eid");
            String sid=obj.getString("sid");
            modal.setSid(obj.getString("id"));
            modal.seteId(eid);
            Request request = new Request.Builder()
                    .url("http://35.207.233.155:3578/selectwQuery?table=events&query=eid&value="+eid)
                    .method("GET", null)
                    .addHeader("key", "MyApiKEy")
                    .build();
            Request request2 = new Request.Builder()
                    .url("http://35.207.233.155:3578/selectwQuery?table=users&query=uid&value="+sid)
                    .method("GET", null)
                    .addHeader("key", "MyApiKEy")
                    .build();
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            Log.i("event details",data);
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            if(jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    modal.setEventName(object.getString("title"));
                }
            }
            Response response2 = client.newCall(request2).execute();
            String data2=response2.body().string();
            Log.i("user details",data2);
            JSONObject jsonObject2=new JSONObject(data2);
            JSONArray jsonArray2=jsonObject2.getJSONArray("data");
            if(jsonArray2.length()>0) {
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject object = jsonArray2.getJSONObject(i);
                    modal.setInviteFrom(object.getString("name"));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return modal;
    }

    @Override
    public void onClick(View view) {
        if(view==addEvent){
            try {
                friends.clear();
                f.clear();
                AlertDialog.Builder addEventDialog = new AlertDialog.Builder(getContext());
                View eventDialog = getLayoutInflater().inflate(R.layout.add_events_dialog, null);
                getFriends(pref.getString("id", ""));
                String event[] = {"Peer To Peer Event"};
//                Spinner eventType = eventDialog.findViewById(R.id.eventType);
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, event);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                eventType.setAdapter(adapter);
                Spinner selectFriend = eventDialog.findViewById(R.id.selectFriend);
                ArrayAdapter adapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, friends);
                addEventDialog.setView(eventDialog);
                selectFriend.setAdapter(adapter2);
                TextInputEditText title = eventDialog.findViewById(R.id.edit_name);
                TextInputEditText target = eventDialog.findViewById(R.id.edit_Target);
                TextInputEditText coins = eventDialog.findViewById(R.id.edit_Coin);
                TextInputEditText sDate = eventDialog.findViewById(R.id.edit_sDate);
                TextInputEditText eDate = eventDialog.findViewById(R.id.edit_eDate);
                Button host = eventDialog.findViewById(R.id.host);
                AlertDialog dialog = addEventDialog.create();
                dialog.setCancelable(true);
                dialog.show();
                selectFriend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        String friend = friends.get(position);
                        Log.i("friend", friend);
                        Log.i("pos", String.valueOf(position));
                        try {
                            if(position>=1) {
                                Log.i("friend data", position+"\n"+f.get(position - 1).getString("name") + "\n" + f.get(position-1).getString("uid"));
                                rid=f.get(position-1).getString("uid");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Snackbar.make(view,"No Friend Selected!",Snackbar.LENGTH_SHORT).show();
                    }
                });
                host.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //take data of target dur pts coins and friend for creating event;
                        //Post addPeerEvent Request
                        {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("text/plain");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("p1id", pref.getString("id", ""))
                                    .addFormDataPart("p2id", "0")
                                    .addFormDataPart("title", title.getText().toString())
                                    .addFormDataPart("duration", sDate.getText().toString() + " - " + eDate.getText().toString())
                                    .addFormDataPart("target", target.getText().toString())
                                    .addFormDataPart("entry_coin", coins.getText().toString())
                                    .addFormDataPart("status", "0")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://35.207.233.155:3578/AddPeerEvent")
                                    .method("POST", body)
                                    .addHeader("key", "MyApiKEy")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String data = response.body().string();
                                Log.i("response",data);
                                JSONObject object=new JSONObject(data);
                                JSONObject obj=object.getJSONObject("data");
                                String eid=obj.getString("eid");
                                        RequestBody body2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                .addFormDataPart("rid", rid)
                                                .addFormDataPart("sid", pref.getString("id", ""))
                                                .addFormDataPart("eid", eid)
                                                .build();
                                        Request request2 = new Request.Builder()
                                                .url("http://35.207.233.155:3578/addInvitation")
                                                .method("POST", body2)
                                                .addHeader("key", "MyApiKEy")
                                                .build();
                                        Response response2 = client.newCall(request2).execute();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getFriends(String id) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/select?table=friends")
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            JSONArray jsonArray=object.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj=jsonArray.getJSONObject(i);
                if(id.equals(obj.getString("fid")) && obj.getString("status").equals("friend")){
                    getUserData(obj.getString("uid"));
                }else {
                    if (id.equals(obj.getString("uid")) && obj.getString("status").equals("friend")) {
                        getUserData(obj.getString("fid"));
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserData(String uid) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://35.207.233.155:3578/selectwQuery?table=users&query=uid&value=" + uid)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject object = new JSONObject(data);
        JSONArray jsonArray = object.getJSONArray("data");
        friends.add("Select Friends");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if(obj.getString("subscription").equals("true")){
                f.add(obj);
                friends.add(obj.getString("name"));
            }
        }
    }
    }
