package com.practise.zweet_fit_app.Fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.practise.zweet_fit_app.Adapters.InvitationRvAdapter;
import com.practise.zweet_fit_app.Modals.InviteCardModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
    int pos = 0;
    LottieAnimationView animationView;
    TextView noData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_invitation_, container, false);
        invititationsRv=view.findViewById(R.id.invitations);
        addEvent=view.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(this);
        animationView=view.findViewById(R.id.invitationAnim);
        noData=view.findViewById(R.id.noInvitationData);
        inviteCardModalList.clear();
        invititationsRv.setHasFixedSize(true);
        pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        invititationsRv.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        InvitationRvAdapter adapter=new InvitationRvAdapter(getInvitations(),view.getContext());
        invititationsRv.setAdapter(adapter);
        if(inviteCardModalList.isEmpty()){
            invititationsRv.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
        }else{
            invititationsRv.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        }
        return view;
    }

    private List<InviteCardModal> getInvitations() {
        String url = Constant.ServerUrl+"/select?table=invitation";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            Log.d("invitations",data);
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
            String url = Constant.ServerUrl+"/selectwQuery?table=events&query=eid&value="+eid;
            String url2 = Constant.ServerUrl+"/selectwQuery?table=users&query=uid&value="+sid;
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("key", "MyApiKEy")
                    .build();
            Request request2 = new Request.Builder()
                    .url(url2)
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
                friends.add("Select Friends");
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
                Button sDate = eventDialog.findViewById(R.id.edit_sDate);
                Button eDate = eventDialog.findViewById(R.id.edit_eDate);
                Button host = eventDialog.findViewById(R.id.host);
                AlertDialog dialog = addEventDialog.create();
                dialog.setCancelable(true);
                dialog.show();
                final int[] sd = {0};
                sDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = LocalDate.now().getYear();
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                Log.i("day",day+" "+d);
                                if(d-day<=0 || d-day>=3 || year!=y || month!=m ){
                                    Toast.makeText(getContext(), "Enter a valid Date!", Toast.LENGTH_SHORT).show();
                                }else{
                                    m++;
                                    sd[0] =d;
                                    sDate.setText(d+"-"+m+"-"+y);
                                }
                            }
                        },year,month,day);
                        datePickerDialog.show();
                    }
                });
                eDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = LocalDate.now().getYear();
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                Log.i("day",day+" "+d);
                                if(d-sd[0]<=0 || d-sd[0]>=3 || year!=y || month!=m ){
                                    Toast.makeText(getContext(), "Enter a valid Date!", Toast.LENGTH_SHORT).show();
                                }else{
                                    m++;
                                    eDate.setText(d+"-"+m+"-"+y);
                                }
                            }
                        },year,month,day);
                        datePickerDialog.show();
                    }
                });
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
                        pos = 1;
                    }
                });
                host.setOnClickListener(new View.OnClickListener() {
                    int fl1=0,fl2=0,fl3=0;
                    @Override
                    public void onClick(View view) {
                        {
                            if(!checkcoins(coins.getText().toString()))
                            {
                                Toast.makeText(getContext(), "Not Enough Coins !!", Toast.LENGTH_SHORT).show();
                                fl3=1;
                                dialog.dismiss();
                            }
                            if(title.getText().toString().isEmpty())
                            {
                                fl1=1;
                                Log.d("Every", coins.getText() + " " + target.getText() + " " + title.getText());
                                Toast.makeText(getContext(), "Title Cannot be Empty !!", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
                            }
                            if(target.getText().toString().isEmpty())
                            {
                                Toast.makeText(getContext(), "Target Cannot be Empty !!", Toast.LENGTH_SHORT).show();
                                fl2=1;
//                                dialog.dismiss();
                            }
                            if(coins.getText().toString().isEmpty())
                            {
                                Toast.makeText(getContext(), "Coins Cannot be Empty !!", Toast.LENGTH_SHORT).show();
                                fl3=1;
//                                dialog.dismiss();
                            }
                            if(fl1==0 && fl2==0 && fl3==0){
                            String url = Constant.ServerUrl + "/AddPeerEvent";
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
                                    .url(url)
                                    .method("POST", body)
                                    .addHeader("key", "MyApiKEy")
                                    .build();
                            try {
                                String url2 = Constant.ServerUrl + "/addInvitation";
                                Response response = client.newCall(request).execute();
                                String data = response.body().string();
                                Log.i("response", data);
                                JSONObject object = new JSONObject(data);
                                JSONObject obj = object.getJSONObject("data");
                                String eid = obj.getString("eid");
                                RequestBody body2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                        .addFormDataPart("rid", rid)
                                        .addFormDataPart("sid", pref.getString("id", ""))
                                        .addFormDataPart("eid", eid)
                                        .build();
                                Request request2 = new Request.Builder()
                                        .url(url2)
                                        .method("POST", body2)
                                        .addHeader("key", "MyApiKEy")
                                        .build();
                                Response response2 = client.newCall(request2).execute();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            }
                        }
                        if(fl1==0 && fl2==0 && fl3==0)
                        {
                            Log.d("Every", coins.getText() + " " + target.getText() + " " + title.getText());
                            dialog.dismiss();
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean checkcoins(String event_coin)
    {
        SharedPreferences pref=getActivity().getSharedPreferences("user data", Context.MODE_PRIVATE);
        String uid=pref.getString("id","");
        try {
            String url = Constant.ServerUrl+"/select?table=users";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("Key", "MyApiKEy")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("L11", e.toString());
            }
            String respo = response.body().string();
            JSONObject Jobject = new JSONObject(respo);
            JSONArray Jarray = Jobject.getJSONArray("data");
            for(int i=0;i<Jarray.length();i++)
            {
                JSONObject object = Jarray.getJSONObject(i);
                String user = object.getString("uid");
                int coins = Integer.parseInt(object.getString("coins"));
                int ec = Integer.parseInt(event_coin);
                if(user.equals(uid))
                {
                    if(coins>=ec)
                    {
                        return true;
                    }
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("L2", e.toString());
        }
        return false;
    }

    private void getFriends(String id) {
        String url = Constant.ServerUrl+"/select?table=friends";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
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
        String url = Constant.ServerUrl+"/selectwQuery?table=users&query=uid&value="+uid;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        Response response = client.newCall(request).execute();
        String data = response.body().string();
        JSONObject object = new JSONObject(data);
        JSONArray jsonArray = object.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if(obj.getString("subscription").equals("true")){
                f.add(obj);
                friends.add(obj.getString("name"));
            }
        }
    }
    }
