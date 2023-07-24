package com.example.edynamixapprenticeship.data.weather.network;

import com.example.edynamixapprenticeship.model.weather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWeatherService {

    @GET("/current")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String city);
}
