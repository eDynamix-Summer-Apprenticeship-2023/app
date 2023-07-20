package com.example.edynamixapprenticeship.model.calendar;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject {
    @PrimaryKey
    private UUID id;
    private Date date;
    private String eventtext;

    public Event (String eventtext, Date date){
        this.id = UUID.randomUUID();
        this.eventtext = eventtext;
        this.date = date;

    }

    public Event (){

    }

    public Date getDate() {
        return date;

    }

    public String getEventtext() {
        return eventtext;
    }
}
