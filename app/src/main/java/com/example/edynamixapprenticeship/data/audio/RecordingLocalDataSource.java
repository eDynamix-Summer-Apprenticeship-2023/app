package com.example.edynamixapprenticeship.data.audio;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.edynamixapprenticeship.model.audio.Recording;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.realm.Realm;
import io.realm.RealmResults;

public class RecordingLocalDataSource {
    private static final String LOG_TAG = RecordingLocalDataSource.class.getSimpleName();
    private final Context context;
    private final Handler handler;
    private final Realm realm;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private final RealmResults<Recording> recordingsRealmResult;
    private final MutableLiveData<List<Recording>> recordings;
    private final MutableLiveData<Recording> playingRecording;
    private final MutableLiveData<Long> playingProgress;
    private final Runnable playingProgressRunnable;
    private String recordingLocation;

    @Inject
    public RecordingLocalDataSource(@ApplicationContext Context context) {
        this.context = context;

        realm = Realm.getDefaultInstance();

        this.recordings = new MutableLiveData<>(new ArrayList<>());
        this.playingRecording = new MutableLiveData<>(null);
        this.playingProgress = new MutableLiveData<>(0L);

        this.recordingsRealmResult = realm.where(Recording.class).findAllAsync();
        recordingsRealmResult.addChangeListener((realmRecordings, changeSet) -> recordings.postValue(realm.copyFromRealm(realmRecordings)));
        this.recordings.setValue(recordingsRealmResult);

        this.handler = new Handler(Looper.getMainLooper());
        this.playingProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    playingProgress.postValue(player.getCurrentPosition() / 1000L);
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    public void startRecording() {
        String fileName = "recording_" + System.currentTimeMillis() + ".3gp";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri audioUri;
            try {
                audioUri = createAudioFileUri(context, fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(audioUri, "w");
                recorder.setOutputFile(pfd.getFileDescriptor());
                recordingLocation = audioUri.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File file = new File(directory, fileName);
            recorder.setOutputFile(file.getAbsolutePath());
            recordingLocation = file.getAbsolutePath();
        }

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "MediaRecorder prepare() failed");
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void stopRecording() {
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;

        realm.executeTransactionAsync(realm -> {
            Recording recording = realm.createObject(Recording.class, UUID.randomUUID());
            recording.setLocation(recordingLocation);
            recording.setDuration(getDurationOfRecording(recordingLocation));
            recording.setCreatedAt(Calendar.getInstance().getTime());
        });
    }

    public LiveData<List<Recording>> getRecordings() {
        return recordings;
    }


    public void startPlaying(Recording recording) {
        if (playingRecording.getValue() != null) stopPlaying();

        player = new MediaPlayer();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                player.setDataSource(context, Uri.parse(recording.getLocation()));
            } else {
                player.setDataSource(recording.getLocation());
            }
            playingRecording.setValue(recording);
            player.setOnCompletionListener(mediaPlayer -> stopPlaying());
            player.prepare();
            player.start();

            handler.postDelayed(playingProgressRunnable, 500);
        } catch (IOException e) {
            Log.e(LOG_TAG, "MediaPlayer prepare() failed");
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void stopPlaying() {
        player.release();
        player = null;
        playingRecording.setValue(null);
        handler.removeCallbacks(playingProgressRunnable);
    }

    public void seekTo(Float position) {
        handler.removeCallbacks(playingProgressRunnable);
        player.seekTo((int) (position * 1000));
        handler.postDelayed(playingProgressRunnable, 500);
    }

    public LiveData<Recording> getPlayingRecording() {
        return playingRecording;
    }

    public LiveData<Long> getPlayingProgress() {
        return playingProgress;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Uri createAudioFileUri(Context context, String fileName) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        Uri audioCollection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        ContentValues newAudioDetails = new ContentValues();
        newAudioDetails.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName);
        newAudioDetails.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        newAudioDetails.put(MediaStore.Audio.Media.IS_PENDING, 1);

        Uri audioUri = contentResolver.insert(audioCollection, newAudioDetails);

        if (audioUri == null) {
            throw new IOException("Failed to create new MediaStore record.");
        }

        return audioUri;
    }

    private long getDurationOfRecording(String location) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                retriever.setDataSource(context, Uri.parse(location));
            } else {
                retriever.setDataSource(location);
            }
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                retriever.close();
            }

            long durationMs = Long.parseLong(durationStr);
            return 1 + TimeUnit.MILLISECONDS.toSeconds(durationMs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void close() {
        if (!realm.isClosed()) realm.close();
    }
}
