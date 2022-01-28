package com.practise.zweet_fit_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.practise.zweet_fit_app.Modals.EventCardModal;
import com.practise.zweet_fit_app.R;

import java.util.List;

public class EventParentAdapter extends RecyclerView.Adapter<EventParentAdapter.ViewHolder> {
    Context context;
    List<EventCardModal> eventCardModalList;

    public EventParentAdapter(Context context, List<EventCardModal> eventCardModalList) {
        this.context = context;
        this.eventCardModalList = eventCardModalList;
    }

    @NonNull
    @Override
    public EventParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_details_parentcard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventParentAdapter.ViewHolder holder, int position) {
        EventCardModal cardModal=eventCardModalList.get(position);
        holder.date.setText(cardModal.getDate());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(),LinearLayoutManager.VERTICAL,false));
        GrpEventsHomepageAdapter adapter=new GrpEventsHomepageAdapter(context,cardModal.getGrpEventsModalList());
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return eventCardModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            recyclerView=itemView.findViewById(R.id.childEventRv);
        }
    }
}
