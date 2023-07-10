package com.example.edynamixapprenticeship.ui.audio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.databinding.RecordingItemBinding;
import com.example.edynamixapprenticeship.model.audio.Recording;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    private RecordingItemBinding binding;
    private List<Recording> recordings;
    private Recording currentlyPlayingRecording;
    private Consumer<Recording> recordingConsumer;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecordingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recording recording = recordings.get(position);
        holder.bind(recording, recordingConsumer, recording.equals(currentlyPlayingRecording));
    }

    @Override
    public int getItemCount() {
        if (recordings == null) return 0;
        return recordings.size();
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
        notifyDataSetChanged();
    }

    public void setCurrentlyPlayingRecording(Recording recording) {
        this.currentlyPlayingRecording = recording;
        notifyDataSetChanged();
    }

    public void setRecordingItemButtonOnClick(Consumer<Recording> consumer) {
        this.recordingConsumer = consumer;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecordingItemBinding binding;

        ViewHolder(@NonNull RecordingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private static String formatSeconds(long seconds) {
            long minutes = TimeUnit.SECONDS.toMinutes(seconds);
            long remainingSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);

            return String.format(Locale.ENGLISH, "%02d:%02d", minutes, remainingSeconds);
        }

        void bind(final Recording recording, Consumer<Recording> recordingConsumer, boolean isPlaying) {
            if (recording.getTitle() != null) {
                binding.recordingItemTitle.setText(recording.getTitle());
            } else {
                binding.recordingItemTitle.setVisibility(View.GONE);
            }
            binding.recordingItemDuration.setText(formatSeconds(recording.getDuration()));
            setPlaying(isPlaying);

            binding.recordingItemButton.setOnClickListener(view -> recordingConsumer.accept(recording));
        }

        void setPlaying(boolean playing) {
            if (playing)
                binding.recordingItemButton.setIconResource(R.drawable.twotone_stop_circle_24);
            else binding.recordingItemButton.setIconResource(R.drawable.twotone_play_circle_24);
        }
    }
}
