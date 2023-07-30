package com.example.edynamixapprenticeship.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edynamixapprenticeship.ui.weather.WeatherForecastActivity;
import com.example.edynamixapprenticeship.ui.weather.WeatherMainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, WeatherMainActivity.class);
        startActivity(i);
        //        if (savedInstanceState == null)
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container_view, AudioFragment.class, null)
//                    .commit();
    }



}