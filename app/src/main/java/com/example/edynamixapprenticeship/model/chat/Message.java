package com.example.edynamixapprenticeship.model.chat;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class Message extends RealmObject {
    @PrimaryKey
    @RealmField("_id")
    private UUID id;
    @Required
    private String message;
    private Date date;
    private String userid;

    public Message() {
        this.id = UUID.randomUUID();
    }

    public Message(String message, String userid) {
        this.id = UUID.randomUUID();
        this.message = message;
        this.userid = userid;
        this.date = Calendar.getInstance().getTime();
    }

    public UUID getId() {
        return id;
    }


    public String getMessage() {
        return message;
    }



    public Date getDate() {
        return date;
    }

    public String getUserid() {
        return userid;
    }


}
