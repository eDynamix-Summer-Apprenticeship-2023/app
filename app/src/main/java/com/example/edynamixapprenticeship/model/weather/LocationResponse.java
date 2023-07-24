package com.example.edynamixapprenticeship.model.weather;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {
    @SerializedName("name")
    private String city;
    @SerializedName("country")
    private String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
