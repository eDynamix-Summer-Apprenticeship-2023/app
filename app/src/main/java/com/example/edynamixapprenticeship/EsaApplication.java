package com.example.edynamixapprenticeship;

import android.app.Application;
import android.util.Log;

import com.example.edynamixapprenticeship.model.audio.Recording;
import com.google.android.material.color.DynamicColors;

import java.util.Objects;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;

@HiltAndroidApp
public class EsaApplication extends Application {
    private App realmApp;

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

        Realm.init(this);

        String realmAppID = BuildConfig.REALM_APP_ID;

        realmApp = new App(new AppConfiguration.Builder(realmAppID).build());

        Credentials credentials = Credentials.anonymous();

        realmApp.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                Log.i("AUTH", "Logged in as: " + it.get().getId());
                SyncConfiguration config = new SyncConfiguration.Builder(Objects.requireNonNull(realmApp.currentUser()))
                        .initialSubscriptions((realm, subscriptions) -> subscriptions.add(Subscription.create("allRecordings", realm.where(Recording.class))))
                        .build();
                Realm.setDefaultConfiguration(config);

                SyncSession syncSession = realmApp.getSync().getSession(config);
                syncSession.start();
            } else {
                Log.e("AUTH", "Failed to log in: " + it.getError().getErrorMessage());
            }
        });
    }
}
