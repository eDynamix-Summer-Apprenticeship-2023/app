package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    @SerializedName("forecastday")
    private List<ForecastdayResponse> forecast;

    public ForecastResponse(List<ForecastdayResponse> forecast) {
        this.forecast = forecast;
    }

    public List<ForecastdayResponse> getForecast() {
        return forecast;
    }
}
