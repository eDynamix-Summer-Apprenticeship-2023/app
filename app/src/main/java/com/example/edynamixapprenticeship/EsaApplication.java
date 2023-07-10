package com.example.edynamixapprenticeship;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

@HiltAndroidApp
public class EsaApplication extends Application {
    private App realmApp;

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

        Realm.init(this);

        realmApp = new App(new AppConfiguration.Builder("application-0-ifkdv")
                .build());
    }
}
