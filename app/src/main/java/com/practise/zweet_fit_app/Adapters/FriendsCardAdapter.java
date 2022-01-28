package com.practise.zweet_fit_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.UsersDataModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsCardAdapter extends RecyclerView.Adapter<FriendsCardAdapter.ViewHolder> {
    List<UsersDataModal> usersList;

    public FriendsCardAdapter(List<UsersDataModal> usersList) {
        this.usersList = usersList;
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
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profilePic;
        TextView name;
        ImageView accept,decline,more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.userImg);
            name=itemView.findViewById(R.id.name);
            accept=itemView.findViewById(R.id.acceptRequest);
            decline=itemView.findViewById(R.id.declineRequest);
            more=itemView.findViewById(R.id.more);
            accept.setOnClickListener(this);
            decline.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==accept){
                usersList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
            if(view==decline){
                usersList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }
}
