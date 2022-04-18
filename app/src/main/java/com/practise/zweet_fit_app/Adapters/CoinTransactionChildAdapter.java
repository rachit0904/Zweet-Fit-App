package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.practise.zweet_fit_app.Modals.CoinTransactionChildModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

public class CoinTransactionChildAdapter extends RecyclerView.Adapter<CoinTransactionChildAdapter.ViewHolder>{
    Context context;
    List<CoinTransactionChildModal> childModalList;

    public CoinTransactionChildAdapter(Context context, List<CoinTransactionChildModal> childModalList) {
        this.context = context;
        this.childModalList = childModalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coins_child_card,parent,false);
        return new CoinTransactionChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinTransactionChildModal coinTransactionChildModal=childModalList.get(position);
        if(Integer.parseInt(coinTransactionChildModal.getCoins())<0){
            holder.coins.setTextColor(Color.parseColor("#EDE61510"));
            holder.coins.setText(coinTransactionChildModal.getCoins());
        }else{
            holder.coins.setTextColor(Color.parseColor("#8053F00F"));
            holder.coins.setText(coinTransactionChildModal.getCoins());
        }
        holder.source.setText(coinTransactionChildModal.getSource());
    }

    @Override
    public int getItemCount() {
        return childModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView source,coins;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            source=itemView.findViewById(R.id.source);
            coins=itemView.findViewById(R.id.lvl);
        }
    }
}
