package com.example.edynamixapprenticeship.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.model.calendar.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
private List<Event> adaptList = new ArrayList<>();
private List<Date> dateList = new ArrayList<>();//

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new ViewHolder(view);
    }

    @Override //zarejda danni
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
       holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              adaptList.remove(holder.getAdapterPosition());
              dateList.remove(holder.getAdapterPosition());
              notifyDataSetChanged();
           }
       });
        holder.bind(adaptList.get(position),dateList.get(position));
    }

    @Override
    public int getItemCount() {
        return adaptList.size();
    }

    public void setAdaptList(List<Event> adaptList){
        this.adaptList = adaptList;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView displayDate;//
        private TextView displayEvent ;
        private Button deleteBtn;//
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayEvent = itemView.findViewById(R.id.DisplayEvent);
            displayDate = itemView.findViewById(R.id.DisplayDate);//
            deleteBtn = itemView.findViewById((R.id.DelBtn));//
        }
        void bind (Event event, Date date){
            displayEvent.setText(event.getEventtext());
            displayDate.setText(date.getDatetext());
        }
    }
}
