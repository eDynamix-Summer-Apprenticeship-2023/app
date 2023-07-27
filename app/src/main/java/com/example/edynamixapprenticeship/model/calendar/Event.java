package com.example.edynamixapprenticeship.model.calendar;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject {
    @PrimaryKey
    private UUID id;

    private String eventtext;

    public Event (String eventtext){
        this.id = UUID.randomUUID();
        this.eventtext = eventtext;
    }

    public Event (){
    }

    public String getEventtext() {
        return eventtext;
    }
}
