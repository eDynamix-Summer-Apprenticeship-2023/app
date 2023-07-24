package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class ConditionResponse {
    @SerializedName("text")
    private String description;
    @SerializedName("icon")
    private String icon;
    @SerializedName("code")
    private String code;

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getCode() {
        return code;
    }
}
