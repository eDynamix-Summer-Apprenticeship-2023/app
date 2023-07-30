package com.example.edynamixapprenticeship.data.weather.network;

import com.example.edynamixapprenticeship.model.weather.CurrentWeatherResponse;
import com.example.edynamixapprenticeship.model.weather.ForecastWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWeatherService {

    @GET("/current")
    Call<CurrentWeatherResponse> getCurrentWeather(@Query("q") String city);
    @GET("/forecast")
    Call<ForecastWeatherResponse> getForecastWeather(@Query("q") String city);
    @GET("/current")
    Call<CurrentWeatherResponse> getCurrentWeatherByIp(@Query("byip") String byip);
}
