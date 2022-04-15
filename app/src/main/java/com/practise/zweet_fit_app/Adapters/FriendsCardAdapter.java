package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.Fragments.StatsFragment;
import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;
import com.practise.zweet_fit_app.Util.Constant;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendsCardAdapter extends RecyclerView.Adapter<FriendsCardAdapter.ViewHolder> {
    List<UsersDataModal> usersList;
    private Context cnt;

    public FriendsCardAdapter(List<UsersDataModal> usersList, Context context) {
        this.usersList = usersList;
        this.cnt=context;
    }

    @NonNull
    @Override
    public FriendsCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequests_carditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsCardAdapter.ViewHolder holder, int position) {
        UsersDataModal modal=usersList.get(position);
        if(!modal.getCardType().equals("request")){
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            holder.more.setVisibility(View.VISIBLE);
        }
        holder.name.setText(modal.getName());
        if(!modal.getImagePath().isEmpty()) {
            Picasso.get().load(modal.getImagePath()).placeholder(R.drawable.avatar_1).into(holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profilePic;
        TextView name;
        ImageView accept,decline,more;
        CoordinatorLayout cardItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardItem=itemView.findViewById(R.id.cardItem);
            profilePic=itemView.findViewById(R.id.userImg);
            name=itemView.findViewById(R.id.name);
            accept=itemView.findViewById(R.id.acceptRequest);
            decline=itemView.findViewById(R.id.declineRequest);
            more=itemView.findViewById(R.id.more);
            accept.setOnClickListener(this);
            decline.setOnClickListener(this);
            more.setOnClickListener(this);
            cardItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SharedPreferences pref= cnt.getSharedPreferences("user data", Context.MODE_PRIVATE);
            if(view==accept){
                requestToDb(view,Constant.ServerUrl,"/acceptRequest",usersList.get(getAdapterPosition()).getUid(),pref.getString("id",""));
                usersList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
            if(view==decline){
                requestToDb(view,Constant.ServerUrl,"/rejectRequest",usersList.get(getAdapterPosition()).getUid(),pref.getString("id",""));
                usersList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
            if(view==more|| view==cardItem)
            {
                Intent intent = new Intent(cnt,BlankActivity.class);
                if(usersList.get(getAdapterPosition()).getUid().equals(pref.getString("id",""))){
                    intent.putExtra("activity", "profile");
                }else {
                    intent.putExtra("activity", "friend profile");
                    intent.putExtra("type",usersList.get(getAdapterPosition()).getIsFriend());
                    intent.putExtra("uid", usersList.get(getAdapterPosition()).getUid());
                }
                cnt.startActivity(intent);
            }
        }
    }

    private void requestToDb(View view,String serverUrl, String endpt, String uid, String fid) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fid",fid)
                .addFormDataPart("uid",uid)
                .build();
        Request request = new Request.Builder()
                .url(serverUrl+endpt)
                .method("POST", body)
                .addHeader("key", "MyApiKEy")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data=response.body().string();
            JSONObject object=new JSONObject(data);
            Log.i("response :",data);
            if(object.getString("status").equals("200") && endpt.contains("accept")){
                Snackbar.make(view,"Friend Added!",Snackbar.LENGTH_SHORT).show();
            }else if(object.getString("status").equals("200") && endpt.contains("reject")){
                Snackbar.make(view,"Request Declined!",Snackbar.LENGTH_SHORT).show();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Snackbar.make(view,"Unable to process request!",Snackbar.LENGTH_SHORT).show();
        }
    }
}
