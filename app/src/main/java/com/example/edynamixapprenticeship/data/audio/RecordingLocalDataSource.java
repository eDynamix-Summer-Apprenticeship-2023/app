package com.example.edynamixapprenticeship.data.audio;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.edynamixapprenticeship.model.audio.Recording;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    @SuppressWarnings("FieldCanBeLocal")
    private final RealmResults<Recording> recordingsRealmResult;
    private final MutableLiveData<List<Recording>> recordings;
    private final MutableLiveData<Recording> playingRecording;
    private final MutableLiveData<Long> playingProgress;
    private final Runnable playingProgressRunnable;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private Recording currentRecording;

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

    private static Cursor getRecordingCursor(Context context, Recording recording) {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projections = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DURATION,
        };

        String selection = MediaStore.Audio.Media._ID + " = ?";
        String[] selectionArgs = new String[]{
                recording.getId().toString()
        };

        return context.getContentResolver().query(collection, projections, selection, selectionArgs, null);
    }

    private static Uri getRecordingUri(Context context, Recording recording) {
        Cursor recordingCursor = getRecordingCursor(context, recording);

        int idColumn = recordingCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        long id = recordingCursor.getLong(idColumn);

        recordingCursor.close();

        return ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
    }

    public LiveData<List<Recording>> getRecordings() {
        return recordings;
    }

    private static Uri createRecordingUri(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri audioCollection = getAudioCollection();

        Log.d(LOG_TAG, audioCollection.toString());

        ContentValues newAudioDetails = new ContentValues();
        newAudioDetails.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        //newAudioDetails.put(MediaStore.Audio.Media.IS_PENDING, 1);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            newAudioDetails.put(MediaStore.Audio.Media.IS_RECORDING, 1);

        return contentResolver.insert(audioCollection, newAudioDetails);
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

    private static Uri getAudioCollection() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            return MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            return MediaStore.Audio.Media.getContentUriForPath(directory.getAbsolutePath());
        }
    }

    public void close() {
        if (!realm.isClosed()) realm.close();
    }

    public void startRecording() {
        currentRecording = new Recording();
        currentRecording.setCreatedAt(Calendar.getInstance().getTime());

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(createRecordingPfd().getFileDescriptor());

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

        Cursor recordingCursor = getRecordingCursor(context, currentRecording);
        Log.d(LOG_TAG, String.valueOf(recordingCursor.getCount()));

        currentRecording.setDuration(getDurationOfRecording(currentRecording));

        realm.executeTransactionAsync(bgRealm -> {
            bgRealm.copyToRealmOrUpdate(currentRecording);
            currentRecording = null;
        });
    }

    public void startPlaying(Recording recording) {
        if (playingRecording.getValue() != null) stopPlaying();

        Uri recordingUri = getRecordingUri(context, recording);
        Log.d(LOG_TAG, recordingUri.toString());
        player = MediaPlayer.create(context, recordingUri);
        player.setOnCompletionListener(mediaPlayer -> stopPlaying());
        player.start();

        playingRecording.setValue(recording);

        handler.postDelayed(playingProgressRunnable, 500);
    }

    private long getDurationOfRecording(Recording recording) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(getRecordingPfd(recording).getFileDescriptor());
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                retriever.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        long durationMs = Long.parseLong(durationStr);
        return 1 + TimeUnit.MILLISECONDS.toSeconds(durationMs);
    }

    private ParcelFileDescriptor createRecordingPfd() {
        try {
            return context
                    .getContentResolver()
                    .openFileDescriptor(createRecordingUri(context), "w");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ParcelFileDescriptor getRecordingPfd(Recording recording) {
        try {
            return context
                    .getContentResolver()
                    .openFileDescriptor(getRecordingUri(context, recording), "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
