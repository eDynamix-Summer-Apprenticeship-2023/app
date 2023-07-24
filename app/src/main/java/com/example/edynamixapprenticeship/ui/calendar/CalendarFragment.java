package com.example.edynamixapprenticeship.ui.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.model.calendar.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private EventAdapter adapter;
    private RecyclerView rec;
    private CalendarView calendar;
      private  Button btn;
       private List<Event> eventList;
       private EditText eventTxt;

       private Date currentDate;
    public CalendarFragment() {
        // Required empty public constructor
        eventList = new ArrayList<Event>();
        currentDate = Calendar.getInstance().getTime();
    }


    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new EventAdapter() ;


        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rec = view.findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter.setAdaptList(eventList);
        rec.setAdapter(adapter);

        calendar =view.findViewById(R.id.cal);
        btn = view.findViewById(R.id.btn);
        eventTxt =view.findViewById(R.id.EventTxt);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                currentDate=new Date ( c.getTimeInMillis()); //this is what you want to use later
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String evTxt = String.valueOf(eventTxt.getText());


                eventList.add(new Event(evTxt, currentDate));
                adapter.setAdaptList(eventList);

            }
        });

        /*calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override

            // Listener koito vzema dd,mm,yyyy
            public void onSelectedDayChange(CalendarView view,
                                            int year,
                                            int month,
                                            int dayOfMonth
            )
            {
                String Date = " ";
                // Store the value of date with format String zaradi  txt   *  +1 bc starts w 0
                if(month<10) {
                    if (dayOfMonth<10){
                        Date = "0"+dayOfMonth + ". 0" + (month + 1) + ". " + year ;}
                    else {  Date = +dayOfMonth + ". 0" + (month + 1) + ". " + year; }}
                else {
                    if (dayOfMonth<10){
                        Date = "0"+dayOfMonth + ". " + (month + 1) + ". " + year ;}
                    else {  Date = +dayOfMonth + ". " + (month + 1) + ". " + year; }}


                // set date
                date.setText(Date);
            }
        });*/
    }
}