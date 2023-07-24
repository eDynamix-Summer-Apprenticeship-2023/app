package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class CurrentResponse {
    @SerializedName("temp_c")
    private double temp;
    @SerializedName("wind_kph")
    private double wind;
    @SerializedName("condition")
    private ConditionResponse condition;

    public CurrentResponse(double temp, double wind, ConditionResponse condition) {
        this.temp = temp;
        this.wind = wind;
        this.condition = condition;
    }

    public double getTemp() {
        return temp;
    }

    public double getWind() {
        return wind;
    }

    public ConditionResponse getCondition() {
        return condition;
    }
}
