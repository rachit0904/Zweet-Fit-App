package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.GrpLeaderboardModal;
import com.practise.zweet_fit_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>  {
    List<GrpLeaderboardModal> grpleaderboard;
    Context context;

    public LeaderboardAdapter(List<GrpLeaderboardModal> grpleaderboard, Context context)
    {
        this.grpleaderboard = grpleaderboard;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return grpleaderboard.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrpLeaderboardModal modal = grpleaderboard.get(position);
        holder.name.setText(modal.getName());
        holder.steps.setText(modal.getSteps());
        holder.lvl.setText(modal.getParticipantlvl());
        holder.coins.setText(modal.getCoins());
        holder.rank.setText(modal.getRank());
        if(!modal.getDp().isEmpty()){
            Picasso.get().load(modal.getDp()).into(holder.dp);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, steps, lvl, coins, rank;
        CircleImageView dp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.participantName);
            steps = itemView.findViewById(R.id.participantSteps);
            lvl = itemView.findViewById(R.id.participantLevel);
            coins = itemView.findViewById(R.id.lvl);
            rank = itemView.findViewById(R.id.participantRank);
        }
    }
}
