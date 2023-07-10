package com.example.edynamixapprenticeship.ui.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.databinding.AudioFragmentBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AudioFragment extends Fragment {
    private AudioFragmentBinding binding;
    private RecordingsViewModel viewModel;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    viewModel.startRecording();
                }
            });
    private AudioAdapter adapter;

    public AudioFragment() {
        super(R.layout.audio_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AudioFragmentBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(RecordingsViewModel.class);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        adapter = new AudioAdapter();
        adapter.setRecordingItemButtonOnClick(recording -> viewModel.togglePlayback(recording));

        viewModel.getRecordings().observe(getViewLifecycleOwner(), recordings -> adapter.setRecordings(recordings));
        viewModel.getCurrentlyPlayingRecording().observe(getViewLifecycleOwner(), recording -> adapter.setCurrentlyPlayingRecording(recording));

        binding.audioRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.audioRecyclerView.setAdapter(adapter);

        binding.audioRecordFab.setOnClickListener(view -> {
            if (Boolean.FALSE.equals(viewModel.isRecording().getValue())) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                } else {
                    viewModel.startRecording();
                }
            } else {
                viewModel.stopRecording();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}