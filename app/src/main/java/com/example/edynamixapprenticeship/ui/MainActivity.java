package com.example.edynamixapprenticeship.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.ui.news.NewsFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, NewsFragment.class, null)
                    .commit();
    }
}