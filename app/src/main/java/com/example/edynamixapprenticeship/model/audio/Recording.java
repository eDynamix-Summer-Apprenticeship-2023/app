package com.example.edynamixapprenticeship.model.audio;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class Recording extends RealmObject {
    @PrimaryKey
    @RealmField("_id")
    private UUID id = UUID.randomUUID();
    private String title;
    private String location;
    private long duration;
    private Date createdAt;

    public Recording(String location, long duration, Date createdAt) {
        this.title = null;
        this.location = location;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public Recording() {
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        if (title == null) return id.toString().toLowerCase().substring(0, 7);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}