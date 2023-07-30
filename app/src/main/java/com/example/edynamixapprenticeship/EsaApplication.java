package com.example.edynamixapprenticeship;

import android.app.Application;
import android.util.Log;

import com.example.edynamixapprenticeship.model.audio.Recording;
import com.google.android.material.color.DynamicColors;

import java.util.Objects;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.HiltAndroidApp;
import dagger.hilt.components.SingletonComponent;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

@Module
@InstallIn(SingletonComponent.class)
@HiltAndroidApp
public class EsaApplication extends Application {
    private App realmApp;
    private User realmUser;

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
                        .initialSubscriptions((realm, subscriptions) ->
                                subscriptions.addOrUpdate(Subscription.create("allRecordings", realm.where(Recording.class))))
                        .build();
                realmUser = it.getOrThrow();
                Realm.setDefaultConfiguration(config);
            } else {
                Log.e("AUTH", "Failed to log in: " + it.getError().getErrorMessage());
            }
        });
    }

    @Provides
    public User getRealmUser() {
        return realmUser;
    }
}
