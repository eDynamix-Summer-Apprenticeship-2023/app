package com.example.edynamixapprenticeship.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.data.weather.network.GetWeatherService;
import com.example.edynamixapprenticeship.data.weather.network.RetrofitClientInstance;
import com.example.edynamixapprenticeship.model.weather.WeatherResponse;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private void getMarsPhotos() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main_fragment);
        TextView txt = findViewById(R.id.locationCurText);
        txt.setText("Стара Загора");
        TextView w = findViewById(R.id.weatherBg1).findViewById(R.id.cityName);
        w.setText("Нова Загора");
        TextView g = findViewById(R.id.weatherBg2).findViewById(R.id.cityName);
        g.setText("Пловдив");
        GetWeatherService service = RetrofitClientInstance.getRetrofitInstance().create(GetWeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeather("Sozopol");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.d("response", response.body().getCurrent().getTemp()+"");
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("weatherOnFailure", t.getMessage());
            }
        });
//        if (savedInstanceState == null)
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container_view, AudioFragment.class, null)
//                    .commit();

    }
}