package com.example.edynamixapprenticeship.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.ui.chat.ChatFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ChatFragment.class, null)
                    .commit();

    }
}