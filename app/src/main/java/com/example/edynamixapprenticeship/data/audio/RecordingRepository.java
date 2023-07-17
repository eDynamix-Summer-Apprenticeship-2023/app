package com.example.edynamixapprenticeship.data.audio;

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

    public void startPlayback(Recording recording) {
        localDataSource.startPlaying(recording);
    }

    public void stopPlayback() {
        localDataSource.stopPlaying();
    }

    public void seekTo(Float position) {
        localDataSource.seekTo(position);
    }

    public LiveData<List<Recording>> getRecordings() {
        return localDataSource.getRecordings();
    }

    public LiveData<Recording> getPlayingRecording() {
        return localDataSource.getPlayingRecording();
    }

    public LiveData<Long> getPlayingProgress() {
        return localDataSource.getPlayingProgress();
    }

    public void cleanup() {
        localDataSource.close();
    }
}
