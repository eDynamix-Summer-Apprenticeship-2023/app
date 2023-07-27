package com.example.edynamixapprenticeship.ui.calendar;


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


import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.model.calendar.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private EventAdapter adapter;
    private EventAdapter dadapter;//
    private RecyclerView rec;
    private CalendarView calendar;
    private  Button btn;
    private List<Event> eventList;
    private List<Date> dateList;//
    private EditText eventTxt;
    private Date currentDate;

    public CalendarFragment() {
        eventList = new ArrayList<Event>();
        dateList =new ArrayList<Date>();//
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
        dadapter = new EventAdapter();//
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                adapter.getDateList().add(new com.example.edynamixapprenticeship.ui.calendar.Date(currentDate.toString()));
                eventList.add(new Event(evTxt));
                adapter.setAdaptList(eventList);
                adapter.notifyDataSetChanged();

            }
        });

    }
}