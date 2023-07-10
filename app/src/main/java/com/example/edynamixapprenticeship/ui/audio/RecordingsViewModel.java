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
    private final MutableLiveData<Recording> currentlyPlayingRecording;
    private final MutableLiveData<Boolean> isRecording;

    @Inject
    public RecordingsViewModel(RecordingRepository repository) {
        this.currentlyPlayingRecording = new MutableLiveData<>(null);
        this.isRecording = new MutableLiveData<>(false);
        recordingRepository = repository;
        recordings = recordingRepository.getRecordings();
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
        if (currentlyPlayingRecording.getValue() == null) {
            currentlyPlayingRecording.setValue(recording);
            recordingRepository.startPlayback(recording.getLocation(), mediaPlayer -> currentlyPlayingRecording.setValue(null));
        } else if (!currentlyPlayingRecording.getValue().equals(recording)) {
            recordingRepository.stopPlayback();
            currentlyPlayingRecording.setValue(recording);
            recordingRepository.startPlayback(recording.getLocation(), mediaPlayer -> currentlyPlayingRecording.setValue(null));
        } else recordingRepository.stopPlayback();
    }

    public LiveData<Boolean> isRecording() {
        return isRecording;
    }

    public LiveData<Recording> getCurrentlyPlayingRecording() {
        return currentlyPlayingRecording;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        recordingRepository.cleanup();
    }
}

