package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.GrpLeaderboardModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

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
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, steps, lvl, coins, rank;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.participantLevel);
            steps = itemView.findViewById(R.id.participantSteps);
            lvl = itemView.findViewById(R.id.participantLevel);
            coins = itemView.findViewById(R.id.coins);
            rank = itemView.findViewById(R.id.participantRank);
        }
    }
}
