package com.example.edynamixapprenticeship.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.model.calendar.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
private List<Event> adaptList;

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new ViewHolder(view);
    }

    @Override //zarejda danni
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        holder.bind(adaptList.get(position));
    }

    @Override
    public int getItemCount() {

        return adaptList.size();
    }

    public void setAdaptList(List<Event> adaptList){
        this.adaptList = adaptList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView displayEvent ;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            displayEvent = itemView.findViewById(R.id.DisplayEvent);
        }
        void bind (Event event){
            displayEvent.setText(event.getEventtext());
        }
    }
}
