package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class ConditionResponse {
    @SerializedName("text")
    private String description;
    @SerializedName("icon")
    private String icon;
    @SerializedName("code")
    private int code;

    public ConditionResponse(String description, String icon, int code) {
        this.description = description;
        this.icon = icon;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getCode() {
        return code;
    }
}
