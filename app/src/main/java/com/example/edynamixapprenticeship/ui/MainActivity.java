package com.example.edynamixapprenticeship.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edynamixapprenticeship.R;

import dagger.hilt.android.AndroidEntryPoint;

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
//        if (savedInstanceState == null)
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container_view, AudioFragment.class, null)
//                    .commit();

    }
}