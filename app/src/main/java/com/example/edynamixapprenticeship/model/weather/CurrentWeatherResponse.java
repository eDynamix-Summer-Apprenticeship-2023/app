package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class CurrentWeatherResponse {
    @SerializedName("location")
    private LocationResponse location;
    @SerializedName("current")
    private CurrentResponse current;

    public CurrentWeatherResponse(LocationResponse location, CurrentResponse current) {
        this.location = location;
        this.current = current;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public CurrentResponse getCurrent() {
        return current;
    }
}
