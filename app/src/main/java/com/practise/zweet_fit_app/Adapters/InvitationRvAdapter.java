package com.practise.zweet_fit_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.InviteCardModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InvitationRvAdapter extends RecyclerView.Adapter<InvitationRvAdapter.ViewHolder> {
    List<InviteCardModal> inviteCardModalList;
    Context contextl;

    public InvitationRvAdapter(List<InviteCardModal> inviteCardModalList, Context contextl) {
        this.inviteCardModalList = inviteCardModalList;
        this.contextl = contextl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InviteCardModal  modal=inviteCardModalList.get(position);
        holder.eventName.setText(modal.getEventName());
        holder.sender.setText("from " + modal.getInviteFrom());
    }

    @Override
    public int getItemCount() {
        return inviteCardModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName,sender;
        ImageView accept,reject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName=itemView.findViewById(R.id.eventTitle);
            sender=itemView.findViewById(R.id.inviteSender);
            accept=itemView.findViewById(R.id.acceptInvite);
            reject=itemView.findViewById(R.id.declineInvite);
            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View view) {
            if(view==accept){
                requestToDb(view, Constant.ServerUrl,"/acceptinvite",getAdapterPosition());
                notifyDataSetChanged();
            }
            if(view==reject){
                requestToDb(view, Constant.ServerUrl,"/removeInvitation",getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }

    private void requestToDb(View view, String serverUrl, String endpt,int pos) {
        String eid=inviteCardModalList.get(pos).geteId();
        String rid=inviteCardModalList.get(pos).getRid();
        String id=inviteCardModalList.get(pos).getSid();
        try {
            switch (endpt) {
                case "/acceptinvite": {
                    {
                        Log.i("id",id);
                        if(checkCoins(rid,eid)){
                        acceptInvite(rid, eid, serverUrl);
//                            {
//                                JSONObject object=getEventData(eid);
//                                InviteCardModal modal=inviteCardModalList.get(pos);
//                                OkHttpClient client = new OkHttpClient().newBuilder()
//                                        .build();
//                                com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("text/plain");
//                                MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                                        .addFormDataPart("amount","-"+object.getString("entry_coin"))
//                                        .addFormDataPart("eid",eid)
//                                        .addFormDataPart("source","joined "+object.getString("title"))
//                                        .addFormDataPart("uid",rid)
//                                        .build();
//                                Request request = new Request.Builder()
//                                        .url(Constant.ServerUrl+"/addCoin")
//                                        .method("POST", body)
//                                        .addHeader("key", "MyApiKEy")
//                                        .build();
//                                try {
//                                    Response response = client.newCall(request).execute();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        rejectInvite(eid, serverUrl);
                        }
                        else{
                            Toast.makeText(contextl, "Not Sufficient Coins", Toast.LENGTH_SHORT).show();
                        }
                    }
                     break;
                }
                case "/removeInvitation": {
                    rejectInvite(eid,serverUrl);
                    {
                        JSONObject object=getEventData(eid);
                        InviteCardModal modal=inviteCardModalList.get(pos);
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("text/plain");
                        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("amount","-"+object.getString("entry_coin"))
                                .addFormDataPart("eid",eid)
                                .addFormDataPart("source","refund for "+object.getString("title"))
                                .addFormDataPart("uid",rid)
                                .build();
                        Request request = new Request.Builder()
                                .url(Constant.ServerUrl+"/addCoin")
                                .method("POST", body)
                                .addHeader("key", "MyApiKEy")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        inviteCardModalList.remove(pos);
    }

    private boolean checkCoins(String id,String eid){
        int uCoins = 0,eCoins=0;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Constant.ServerUrl+"/selectwQuery?table=users&query=uid&value="+id)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        Request request2 = new Request.Builder()
                .url(Constant.ServerUrl+"/selectwQuery?table=events&query=eid&value="+eid)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            JSONArray array=object.getJSONArray("data");
            if(array.length()>0){
                for(int i=0;i<array.length();i++) {
                    JSONObject obj = array.getJSONObject(i);
                    uCoins= Integer.parseInt(obj.getString("coins"));
                }
            }
            Response response2 = client.newCall(request2).execute();
            String data2=response2.body().string();
            JSONObject object2=new JSONObject(data2);
            JSONArray array2=object2.getJSONArray("data");
            String eventname="";
            if(array2.length()>0){
                for(int i=0;i<array2.length();i++) {
                    JSONObject obj = array2.getJSONObject(i);
                    eventname=obj.getString("title");
                    eCoins= Integer.parseInt(obj.getString("entry_coin"));
                }
            }
            Log.i("data",data);
            Log.i("ucoins : ecoins",uCoins+":"+eCoins);
            if(uCoins>=eCoins){
                deductCoins(eventname,eCoins,id,eid);
                return true;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deductCoins(String eventName,int eCoins,String id,String eid) {
        Log.i("id",id);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("amount","-"+eCoins)
                .addFormDataPart("eid",eid)
                .addFormDataPart("source",eventName)
                .addFormDataPart("uid",id)
                .build();
        Request request = new Request.Builder()
                .url(Constant.ServerUrl+"/addCoin")
                .method("POST", body)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rejectInvite(String eid,String serverUrl) {
        {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("eid", eid)
                    .build();
            Request request = new Request.Builder()
                    .url(serverUrl + "/removeInvitation")
                    .method("POST", body)
                    .addHeader("key", "MyApiKEy")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String data = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptInvite(String rid,String eid,String serverUrl) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("rid", rid)
                .addFormDataPart("eid", eid)
                .build();
        Request request = new Request.Builder()
                .url(serverUrl + "/acceptinvite")
                .method("POST", body)
                .addHeader("key", "MyApiKEy")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String data = response.body().string();
            Log.i("accept invite",data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getEventData(String eid) {
        JSONObject obj=null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Constant.ServerUrl+"/selectwQuery?table=events&query=eid&value="+eid)
                .method("GET", null)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            obj=new JSONObject(data);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
