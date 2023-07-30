package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastWeatherResponse {
    @SerializedName("location")
    private LocationResponse location;
    @SerializedName("current")
    private CurrentResponse current;
    @SerializedName("forecast")
    private ForecastResponse forecast;

    public ForecastWeatherResponse(LocationResponse location, CurrentResponse current, ForecastResponse forecast) {
        this.location = location;
        this.current = current;
        this.forecast = forecast;
    }

    public LocationResponse getLocation() {
        return location;
    }
    public CurrentResponse getCurrent() {
        return current;
    }

    public ForecastResponse getForecast() {
        return forecast;
    }
}
