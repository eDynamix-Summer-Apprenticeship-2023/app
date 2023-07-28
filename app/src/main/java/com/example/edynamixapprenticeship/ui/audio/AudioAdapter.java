package com.example.edynamixapprenticeship.ui.audio;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edynamixapprenticeship.databinding.RecordingItemBinding;
import com.example.edynamixapprenticeship.model.audio.Recording;
import com.google.android.material.slider.Slider;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    private List<Recording> recordings;
    private Recording playingRecording;
    private long playingPosition;
    private Consumer<Recording> toggleRecordingConsumer;
    private Consumer<Float> seekRecordingConsumer;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecordingItemBinding binding = RecordingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recording recording = recordings.get(position);

        holder.binding.recordingItemSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                seekRecordingConsumer.accept(slider.getValue());
            }
        });
        holder.binding.recordingItemSlider.setLabelFormatter(value -> ViewHolder.formatSeconds((long) value));

        holder.bind(recording, toggleRecordingConsumer);

        boolean isPlaying = recording.equals(playingRecording);
        holder.setPlaying(isPlaying);
        holder.setPlayingProgress(isPlaying ? playingPosition : 0);
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

    public void setPlayingRecording(Recording recording) {
        this.playingRecording = recording;
        this.playingPosition = 0;
        notifyDataSetChanged();
    }

    public void setPlayingPosition(long position) {
        this.playingPosition = position;
        notifyItemChanged(recordings.indexOf(playingRecording));
    }

    public void setRecordingCardOnClick(Consumer<Recording> consumer) {
        this.toggleRecordingConsumer = consumer;
        notifyDataSetChanged();
    }

    public void setSeekRecordingConsumer(Consumer<Float> seekRecordingConsumer) {
        this.seekRecordingConsumer = seekRecordingConsumer;
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

        void bind(final Recording recording, Consumer<Recording> toggleRecordingConsumer) {
            binding.recordingItemTitle.setText(recording.getTitle());
            if (recording.getDuration() != 0f)
                binding.recordingItemSlider.setValueTo(recording.getDuration());
            binding.recordingItemSlider.setStepSize(1f);
            binding.recordingItemDuration.setText(formatSeconds(recording.getDuration()));
            if (recording.getCreatedAt() != null)
                binding.recordingItemDatetime.setText(
                        DateFormat
                                .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                                .format(recording.getCreatedAt()));
            binding.recordingItemCard.setOnClickListener(view -> toggleRecordingConsumer.accept(recording));
        }

        void setPlaying(boolean playing) {
            binding.recordingItemSlider.setEnabled(playing);
        }

        void setPlayingProgress(long position) {
            binding.recordingItemSlider.setValue((float) position);
        }
    }
}
