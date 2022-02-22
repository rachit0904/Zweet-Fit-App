package com.practise.zweet_fit_app.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.YearlyRecordModal;
import com.practise.zweet_fit_app.R;

import java.util.ArrayList;
import java.util.List;

public class YearlyStatAdapter extends RecyclerView.Adapter<YearlyStatAdapter.ViewHolder>  {

    ArrayList<YearlyRecordModal> yearlyRecordModalList;

    public YearlyStatAdapter(ArrayList<YearlyRecordModal> yearlyRecordModalList) {
        this.yearlyRecordModalList = yearlyRecordModalList;
    }

    @NonNull
    @Override
    public YearlyStatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.yearlyview_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearlyStatAdapter.ViewHolder holder, int position) {
        YearlyRecordModal modal=yearlyRecordModalList.get(position);
        holder.month.setText(modal.getMonthName());
        holder.steps.setText(modal.getSteps().concat(" Steps"));
        holder.cal.setText(modal.getCal().concat(" KCal"));
        holder.dist.setText(modal.getDist().concat(" Kms"));
    }

    @Override
    public int getItemCount() {
        return yearlyRecordModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView month,steps,cal,dist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            month=itemView.findViewById(R.id.monthName);
            steps=itemView.findViewById(R.id.steps);
            cal=itemView.findViewById(R.id.calories);
            dist=itemView.findViewById(R.id.distance);
        }
    }
}
