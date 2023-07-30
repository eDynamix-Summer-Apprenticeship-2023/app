package com.example.edynamixapprenticeship.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.data.weather.network.GetWeatherService;
import com.example.edynamixapprenticeship.data.weather.network.RetrofitClientInstance;
import com.example.edynamixapprenticeship.model.weather.CurrentWeatherResponse;
import com.example.edynamixapprenticeship.model.weather.WeatherCodeDecoder;
import com.example.edynamixapprenticeship.ui.weather.WeatherForecastActivity;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class WeatherItem {
    private String city;
    private View view;
    private CurrentWeatherResponse currentWeather = null;

    public WeatherItem(String city, View view) {
        this.city = city;
        this.view = view;
    }

    public String getCity() {
        return city;
    }

    public View getId() {
        return view;
    }

    public CurrentWeatherResponse getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeatherResponse currentWeather) {
        this.currentWeather = currentWeather;
    }
}

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private boolean citySelect = true;
    public static void updateItems(View item, CurrentWeatherResponse res){
        TextView city = item.findViewById(R.id.cityName);
        city.setText(res.getLocation().getCity());
        TextView temp = item.findViewById(R.id.temp);
        temp.setText(Math.round(res.getCurrent().getTemp()) + "℃");
        TextView emoji = item.findViewById(R.id.emoji);
        emoji.setText(WeatherCodeDecoder.getEmoji(res.getCurrent().getCondition().getCode()));
    }
    public void StrartForecastActivity(String city){
        Intent i = new Intent(this, WeatherForecastActivity.class);
        i.putExtra("city",city);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main_fragment);

        GetWeatherService service = RetrofitClientInstance.getRetrofitInstance().create(GetWeatherService.class);
        WeatherItem[] weatherItems = {new WeatherItem("Sofia", findViewById(R.id.weatherBg1)),
                new WeatherItem("Plovdiv", findViewById(R.id.weatherBg2)),
                new WeatherItem("Varna", findViewById(R.id.weatherBg3)),
                new WeatherItem("Berlin", findViewById(R.id.weatherEu1)),
                new WeatherItem("Paris", findViewById(R.id.weatherEu2)),
                new WeatherItem("Rome", findViewById(R.id.weatherEu3))};
        WeatherItem[] weatherItemsResorts = {new WeatherItem("Sozopol", findViewById(R.id.weatherBg1)),
                new WeatherItem("Nesebar", findViewById(R.id.weatherBg2)),
                new WeatherItem("Borovec", findViewById(R.id.weatherBg3)),
                new WeatherItem("Kitzbuhel", findViewById(R.id.weatherEu1)),
                new WeatherItem("Verbier", findViewById(R.id.weatherEu2)),
                new WeatherItem("Avoriaz", findViewById(R.id.weatherEu3))};

        for (int i = 0; i < weatherItems.length; i++) {
            WeatherItem weatherItem = weatherItems[i];
            Call<CurrentWeatherResponse> call = service.getCurrentWeather(weatherItems[i].getCity());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                    for(int k = 0; k<weatherItems.length; k++){
                        if(weatherItems[k].getCurrentWeather() == null && weatherItems[k].getCity().equals(response.body().getLocation().getCity())) {
                            weatherItems[k].setCurrentWeather(response.body());
                            break;
                        }
                    }
                    View item = weatherItem.getId();
                    updateItems(item, response.body());
                }

                @Override
                public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                    Log.e("currentWeatherOnFailure", t.getMessage());
                }
            });

        }

        Button buttonCity = findViewById(R.id.buttonCity);
        buttonCity.setOnClickListener(v -> {
            citySelect = true;
            if(weatherItems[0].getCurrentWeather() != null){
                for (WeatherItem weatherItem : weatherItems) {
                    View item = weatherItem.getId();
                    updateItems(item, weatherItem.getCurrentWeather());
                }
            }
        });

        Button buttonResort = findViewById(R.id.buttonResort);
        buttonResort.setOnClickListener(v -> {
            citySelect = false;
            Log.d("citySelect", citySelect+"");
            if (weatherItemsResorts[0].getCurrentWeather() != null) {
                for (WeatherItem weatherItem : weatherItemsResorts) {
                    View item = weatherItem.getId();
                    updateItems(item, weatherItem.getCurrentWeather());
                }
            } else {
                for (int i = 0; i < weatherItemsResorts.length; i++) {
                    WeatherItem weatherItem = weatherItemsResorts[i];
                    Call<CurrentWeatherResponse> call = service.getCurrentWeather(weatherItem.getCity());
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                            for(int k = 0; k<weatherItemsResorts.length; k++){
                                if(weatherItemsResorts[k].getCurrentWeather() == null && weatherItemsResorts[k].getCity() == response.body().getLocation().getCity()) {
                                    weatherItemsResorts[k].setCurrentWeather(response.body());
                                    break;
                                }
                            }

                            View item = weatherItem.getId();
                            try {
                                updateItems(item, response.body());
                            }catch (Exception e){
                                Log.e("weatherResort", e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                            Log.e("currentWeatherOnFailure", t.getMessage());
                        }
                    });
                }
            }
        });
            for(int i = 0; i < weatherItems.length; i++){
                View item = weatherItems[i].getId();
                String city = weatherItems[i].getCity();
                String resort = weatherItemsResorts[i].getCity();
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StrartForecastActivity(citySelect? city:resort);
                    }
                });
            }

//        if (savedInstanceState == null)
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container_view, AudioFragment.class, null)
//                    .commit();

    }
}