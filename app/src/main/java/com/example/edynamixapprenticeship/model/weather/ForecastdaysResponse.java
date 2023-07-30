package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class ForecastdaysResponse {
    @SerializedName("0")
    private ForecastdayResponse firstDay;
    @SerializedName("1")
    private ForecastdayResponse secondDay;
    @SerializedName("2")
    private ForecastdayResponse thirdDay;

    public ForecastdaysResponse(ForecastdayResponse firstDay, ForecastdayResponse secondDay, ForecastdayResponse thirdDay) {
        this.firstDay = firstDay;
        this.secondDay = secondDay;
        this.thirdDay = thirdDay;
    }

    public ForecastdayResponse getFirstDay() {
        return firstDay;
    }

    public ForecastdayResponse getSecondDay() {
        return secondDay;
    }

    public ForecastdayResponse getThirdDay() {
        return thirdDay;
    }
}
