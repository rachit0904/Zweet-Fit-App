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

import com.google.android.material.snackbar.Snackbar;
import com.practise.zweet_fit_app.Modals.InviteCardModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;


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
        String id=inviteCardModalList.get(pos   ).getSid();
        try {
            switch (endpt) {
                case "/acceptinvite": {
                    {
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
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                        Log.i("accept invite",data);
                    }
                    {
                        JSONObject object=getEventData(eid);
                        InviteCardModal modal=inviteCardModalList.get(pos);
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("text/plain");
                        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("amount","-"+object.getString("entry_coin"))
                                .addFormDataPart("eid",eid)
                                .addFormDataPart("source","joined "+object.getString("title"))
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
                }
                case "/removeInvitation": {
                    {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("id", id)
                                .build();
                        Request request = new Request.Builder()
                                .url(serverUrl + "/removeInvitation")
                                .method("POST", body)
                                .addHeader("key", "MyApiKEy")
                                .build();
                        Response response = client.newCall(request).execute();
                        String data = response.body().string();
                    }
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
