package com.example.edynamixapprenticeship;

import android.app.Application;
import android.util.Log;

import com.google.android.material.color.DynamicColors;

import java.util.Objects;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;

@HiltAndroidApp
public class EsaApplication extends Application {
    private App realmApp;

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

        Realm.init(this);

        String realmAppID = "application-0-iynqf";

        realmApp = new App(new AppConfiguration.Builder(realmAppID).build());

        Credentials credentials = Credentials.anonymous();

        realmApp.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.e("AUTH", "Logged in as: " + it.get().getId());
                SyncConfiguration config = new SyncConfiguration.Builder(Objects.requireNonNull(realmApp.currentUser())).build();
                Realm.setDefaultConfiguration(config);
            } else {
                Log.e("AUTH", "Failed to log in: " + it.getError().getErrorMessage());
            }
        });
    }
}
