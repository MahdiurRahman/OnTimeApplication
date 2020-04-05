package com.example.ontime;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{
    List<Event> events;

    RVAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, final int i) {
        eventViewHolder.eventName.setText(events.get(i).eventName);
        //eventViewHolder.startDate.setText(events.get(i).startDate);
        eventViewHolder.time.setText(events.get(i).time);
        eventViewHolder.weeklySchedule.setText(events.get(i).days);
        eventViewHolder.editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editEvent = new Intent(v.getContext(), EditEvent.class);
                editEvent.putExtra("event", events.get(i));
                v.getContext().startActivity(editEvent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView editEvent;
        CardView cv;
        TextView eventName;
        TextView startDate;
        TextView time;
        TextView weeklySchedule;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventName = (TextView)itemView.findViewById(R.id.eventName);
            startDate = (TextView)itemView.findViewById(R.id.startDate);
            time = (TextView)itemView.findViewById(R.id.time);
            weeklySchedule = (TextView)itemView.findViewById(R.id.weeklySchedule);
            editEvent = (TextView)itemView.findViewById(R.id.editEvent);
        }
    }

}