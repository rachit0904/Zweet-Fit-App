package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
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


import java.util.List;

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
        holder.sender.setText(modal.getInviteFrom());
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

        @Override
        public void onClick(View view) {
            InviteCardModal cardModal=inviteCardModalList.get(getAdapterPosition());
            if(view==accept){
                Snackbar.make(view, cardModal.getEventName()+" from "+cardModal.getInviteFrom()+" accepted!", Snackbar.LENGTH_SHORT).show();
                inviteCardModalList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
            if(view==reject){
                Snackbar.make(view, cardModal.getEventName()+" event declined!", Snackbar.LENGTH_SHORT).show();
                inviteCardModalList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }
}
