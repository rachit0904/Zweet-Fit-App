package com.practise.zweet_fit_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.practise.zweet_fit_app.Modals.StatRecordModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

public class StatViewCardAdapter extends RecyclerView.Adapter<StatViewCardAdapter.ViewHolder> {

    List<StatRecordModal> statRecordModalList;

    public StatViewCardAdapter(List<StatRecordModal> list) {
        this.statRecordModalList=list;
    }

    @NonNull
    @Override
    public StatViewCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewCardAdapter.ViewHolder holder, int position) {
        StatRecordModal modal=statRecordModalList.get(position);
        {
            {
                holder.date.setText(modal.getDate());
                holder.steps.setText(modal.getSteps() + " Steps");
                if (modal.getStreakAchieved()) {
                    holder.streatStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.streatStatus.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return statRecordModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date,steps;
        ImageButton streatStatus;
        CoordinatorLayout statViewCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            steps=itemView.findViewById(R.id.statSteps);
            streatStatus=itemView.findViewById(R.id.streakStatus);
            statViewCard=itemView.findViewById(R.id.statViewCard);
            statViewCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
