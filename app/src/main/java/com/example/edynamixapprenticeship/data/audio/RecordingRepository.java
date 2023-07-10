package com.example.edynamixapprenticeship.data.audio;

import android.media.MediaPlayer;

import androidx.lifecycle.LiveData;

import com.example.edynamixapprenticeship.model.audio.Recording;

import java.util.List;

import javax.inject.Inject;

public class RecordingRepository {
    private final RecordingLocalDataSource localDataSource;

    @Inject
    public RecordingRepository(RecordingLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public void startRecording() {
        localDataSource.startRecording();
    }

    public void stopRecording() {
        localDataSource.stopRecording();
    }

    public void startPlayback(String location, MediaPlayer.OnCompletionListener onCompletionListener) {
        localDataSource.startPlaying(location, onCompletionListener);
    }

    public void stopPlayback() {
        localDataSource.stopPlaying();
    }

    public LiveData<List<Recording>> getRecordings() {
        return localDataSource.getRecordings();
    }

    public void cleanup() {
        localDataSource.close();
    }
}
