package com.example.edynamixapprenticeship.model.audio;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Recording extends RealmObject {
    @PrimaryKey
    private UUID id;
    private String title;
    private String location;
    private long duration;

    public Recording(String location, long duration) {
        this.id = UUID.randomUUID();
        this.title = null;
        this.location = location;
        this.duration = duration;
    }

    public Recording() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }
}

