package com.example.edynamixapprenticeship.model.audio;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class Recording extends RealmObject {
    @SuppressWarnings("FieldMayBeFinal")
    @PrimaryKey
    @RealmField("_id")
    private UUID id;
    private String title;
    @Required
    private Long duration;
    @Required
    private Date createdAt;

    public Recording(long duration, Date createdAt) {
        this.id = UUID.randomUUID();
        this.title = null;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public Recording() {
        this.id = UUID.randomUUID();
        this.duration = 0L;
        this.createdAt = new Date();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return "recording_" + id + ".3gp";
    }
}