package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class DayResponse {
    @SerializedName("maxtemp_c")
    private double maxTemp;
    @SerializedName("mintemp_c")
    private double minTemp;
    @SerializedName("avgtemp_c")
    private double avgTemp;
    @SerializedName("condition")
    private ConditionResponse condition;

    public DayResponse(double maxTemp, double minTemp, double avgTemp, ConditionResponse condition) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
        this.condition = condition;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public ConditionResponse getCondition() {
        return condition;
    }
}
