package com.example.edynamixapprenticeship.ui.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.data.weather.network.GetWeatherService;
import com.example.edynamixapprenticeship.data.weather.network.RetrofitClientInstance;
import com.example.edynamixapprenticeship.model.weather.ForecastWeatherResponse;
import com.example.edynamixapprenticeship.model.weather.WeatherCodeDecoder;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecastActivity extends AppCompatActivity {
    public WeatherForecastActivity() {
        super(R.layout.forecast_fragment);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_fragment);
        Bundle extras = getIntent().getExtras();
        String city = "";
        if (extras != null) {
            city = extras.getString("city");
        }

        TextView curCity = findViewById(R.id.curCity);
        TextView curTemp = findViewById(R.id.curTemp);
        TextView curEmoji = findViewById(R.id.curEmoji);
        TextView curDesc = findViewById(R.id.curDesc);
        View firstDay = findViewById(R.id.firstDay);
        View secondDay = findViewById(R.id.secondDay);
        View thirdDay = findViewById(R.id.thirdDay);



        GetWeatherService service = RetrofitClientInstance.getRetrofitInstance().create(GetWeatherService.class);
        Call<ForecastWeatherResponse> call = service.getForecastWeather(city);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ForecastWeatherResponse> call, Response<ForecastWeatherResponse> response) {
                curCity.setText(response.body().getLocation().getCity()+", "+response.body().getLocation().getCountry());
                curTemp.setText(response.body().getCurrent().getTemp()+"℃");
                curEmoji.setText(WeatherCodeDecoder.getEmoji(response.body().getCurrent().getCondition().getCode()));
                curDesc.setText(response.body().getCurrent().getCondition().getDescription());

                TextView date = firstDay.findViewById(R.id.date);
                //TextView weekday = firstDay.findViewById(R.id.weekday);
                TextView weatherDescription = firstDay.findViewById(R.id.weatherDescription);
                TextView minTemp = firstDay.findViewById(R.id.minTemp);
                TextView avgTemp = firstDay.findViewById(R.id.avgTemp);
                TextView maxTemp = firstDay.findViewById(R.id.maxTemp);
                TextView condEmoji = firstDay.findViewById(R.id.condEmoji);

                String dateRes = response.body().getForecast().getForecast().get(0).getDate();

                date.setText(dateRes);
                weatherDescription.setText(response.body().getForecast().getForecast().get(0).getDay().getCondition().getDescription());
                condEmoji.setText(WeatherCodeDecoder.getEmoji(response.body().getForecast().getForecast().get(0).getDay().getCondition().getCode()));
                minTemp.setText("min - " + response.body().getForecast().getForecast().get(0).getDay().getMinTemp()+"℃");
                avgTemp.setText("avg - " + response.body().getForecast().getForecast().get(0).getDay().getAvgTemp()+"℃");
                maxTemp.setText("max - " + response.body().getForecast().getForecast().get(0).getDay().getMaxTemp()+"℃");


                date = secondDay.findViewById(R.id.date);
                //weekday = firstDay.findViewById(R.id.weekday);
                weatherDescription = secondDay.findViewById(R.id.weatherDescription);
                minTemp = secondDay.findViewById(R.id.minTemp);
                avgTemp = secondDay.findViewById(R.id.avgTemp);
                maxTemp = secondDay.findViewById(R.id.maxTemp);
                condEmoji = secondDay.findViewById(R.id.condEmoji);

                dateRes = response.body().getForecast().getForecast().get(1).getDate();

                date.setText(dateRes);
                weatherDescription.setText(response.body().getForecast().getForecast().get(1).getDay().getCondition().getDescription());
                condEmoji.setText(WeatherCodeDecoder.getEmoji(response.body().getForecast().getForecast().get(1).getDay().getCondition().getCode()));
                minTemp.setText("min - " + response.body().getForecast().getForecast().get(1).getDay().getMinTemp()+"℃");
                avgTemp.setText("avg - " + response.body().getForecast().getForecast().get(1).getDay().getAvgTemp()+"℃");
                maxTemp.setText("max - " + response.body().getForecast().getForecast().get(1).getDay().getMaxTemp()+"℃");


                date = thirdDay.findViewById(R.id.date);
                //weekday = firstDay.findViewById(R.id.weekday);
                weatherDescription = thirdDay.findViewById(R.id.weatherDescription);
                minTemp = thirdDay.findViewById(R.id.minTemp);
                avgTemp = thirdDay.findViewById(R.id.avgTemp);
                maxTemp = thirdDay.findViewById(R.id.maxTemp);
                condEmoji = thirdDay.findViewById(R.id.condEmoji);

                dateRes = response.body().getForecast().getForecast().get(1).getDate();

                date.setText(dateRes);
                weatherDescription.setText(response.body().getForecast().getForecast().get(2).getDay().getCondition().getDescription());
                condEmoji.setText(WeatherCodeDecoder.getEmoji(response.body().getForecast().getForecast().get(2).getDay().getCondition().getCode()));
                minTemp.setText("min - " + response.body().getForecast().getForecast().get(2).getDay().getMinTemp()+"℃");
                avgTemp.setText("avg - " + response.body().getForecast().getForecast().get(2).getDay().getAvgTemp()+"℃");
                maxTemp.setText("max - " + response.body().getForecast().getForecast().get(2).getDay().getMaxTemp()+"℃");
            }
            @Override
            public void onFailure(Call<ForecastWeatherResponse> call, Throwable t) {
                Log.e("forecastWeather", t.getMessage());
            }
        });
    }

}
