package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class ForecastdayResponse {
    @SerializedName("date")
    private String date;
    @SerializedName("day")
    private DayResponse day;

    public ForecastdayResponse(String date, DayResponse day) {
        this.date = date;
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public DayResponse getDay() {
        return day;
    }
}
