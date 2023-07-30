package com.example.edynamixapprenticeship.ui.audio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.edynamixapprenticeship.data.audio.RecordingRepository;
import com.example.edynamixapprenticeship.model.audio.Recording;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RecordingsViewModel extends ViewModel {
    private final RecordingRepository recordingRepository;
    private final LiveData<List<Recording>> recordings;
    private final LiveData<Recording> playingRecording;
    private final LiveData<Long> playingProgress;
    private final MutableLiveData<Boolean> isRecording;

    @Inject
    public RecordingsViewModel(RecordingRepository repository) {
        this.isRecording = new MutableLiveData<>(false);
        recordingRepository = repository;
        recordings = recordingRepository.getRecordings();
        playingRecording = recordingRepository.getPlayingRecording();
        playingProgress = recordingRepository.getPlayingProgress();
    }

    public LiveData<List<Recording>> getRecordings() {
        return recordings;
    }

    public void startRecording() {
        recordingRepository.startRecording();
        this.isRecording.setValue(true);
    }

    public void stopRecording() {
        recordingRepository.stopRecording();
        this.isRecording.setValue(false);
    }

    public void togglePlayback(Recording recording) {
        if (playingRecording.getValue() == null) {
            recordingRepository.startPlayback(recording);
        } else if (!playingRecording.getValue().equals(recording)) {
            recordingRepository.stopPlayback();
            recordingRepository.startPlayback(recording);
        } else recordingRepository.stopPlayback();
    }

    public void seekTo(Float position) {
        recordingRepository.seekTo(position);
    }

    public LiveData<Boolean> isRecording() {
        return isRecording;
    }

    public LiveData<Recording> getPlayingRecording() {
        return playingRecording;
    }

    public LiveData<Long> getPlayingProgress() {
        return playingProgress;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        recordingRepository.cleanup();
    }
}

