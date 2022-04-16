package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Activity.BlankActivity;
import com.practise.zweet_fit_app.Modals.GrpEventsModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

public class GrpEventsHomepageAdapter extends RecyclerView.Adapter<GrpEventsHomepageAdapter.ViewHolder> {
    Context context;
    List<GrpEventsModal> grpEventsModalList;

    public GrpEventsHomepageAdapter(Context context, List<GrpEventsModal> grpEventsModalList) {
        this.context = context;
        this.grpEventsModalList = grpEventsModalList;
    }

    //E4424141
    @NonNull
    @Override
    public GrpEventsHomepageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grp_event_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrpEventsModal eventsModal=grpEventsModalList.get(position);
        if(eventsModal.getStatus().equals("ongoing")){
            holder.eventCard.setCardBackgroundColor(Color.parseColor("#CF016560"));
        }else if(eventsModal.getStatus().equals("finished")){
            holder.eventCard.setCardBackgroundColor(Color.parseColor("#A9373737"));
        }
        holder.tt.setText(eventsModal.getTitle());
        if(eventsModal.getType().equals("p2p"))
        {
            holder.im1.setVisibility(View.GONE);
            holder.im3.setVisibility(View.GONE);
            holder.im5.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return grpEventsModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView eventCard;
        TextView tt;
        ImageView im1, im2, im3, im4, im5;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventCard=itemView.findViewById(R.id.eventCard);
            tt = itemView.findViewById(R.id.grouptitle);
            im1 = itemView.findViewById(R.id.supimg1);
            im2 = itemView.findViewById(R.id.supimg2);
            im3 = itemView.findViewById(R.id.supimg3);
            im4 = itemView.findViewById(R.id.supimg4);
            im5 = itemView.findViewById(R.id.supimg5);
            eventCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GrpEventsModal modal=grpEventsModalList.get(getAdapterPosition());
                    Intent intent=new Intent(context, BlankActivity.class);
                    intent.putExtra("activity","grp_event");
                    intent.putExtra("title",modal.getTitle());
                    intent.putExtra("lvlup",modal.getLevelUp());
                    intent.putExtra("coins",modal.getEntryCoins());
                    intent.putExtra("target",modal.getTarget());
                    intent.putExtra("status",modal.getStatus());
                    intent.putExtra("participants",modal.getParticipants());
                    intent.putExtra("maxP",modal.getMaxP());
                    intent.putExtra("minP",modal.getMinP());
                    intent.putExtra("id",modal.getgId());
                    intent.putExtra("dur",modal.getDur());
                    context.startActivity(intent);
                }
            });
        }
    }
}
