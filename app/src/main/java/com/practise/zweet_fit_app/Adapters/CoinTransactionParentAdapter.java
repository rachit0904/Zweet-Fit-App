package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.CoinTransactionParentModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

public class CoinTransactionParentAdapter extends RecyclerView.Adapter<CoinTransactionParentAdapter.ViewHolder> {
    Context context;
    List<CoinTransactionParentModal> parentModalList;

    public CoinTransactionParentAdapter(Context context, List<CoinTransactionParentModal> parentModalList) {
        this.context = context;
        this.parentModalList = parentModalList;
    }

    @NonNull
    @Override
    public CoinTransactionParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coins_parent_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinTransactionParentAdapter.ViewHolder holder, int position) {
        CoinTransactionParentModal modal=parentModalList.get(position);
        holder.date.setText(modal.getDate());
        holder.childRv.setHasFixedSize(true);
        holder.childRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        CoinTransactionChildAdapter adapter=new CoinTransactionChildAdapter(context,modal.getChildModalList());
        holder.childRv.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return parentModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView childRv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.coinTransactionDate);
            childRv=itemView.findViewById(R.id.coinChildRv);
        }
    }
}
