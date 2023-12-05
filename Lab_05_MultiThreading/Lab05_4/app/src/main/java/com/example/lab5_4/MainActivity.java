package com.example.lab5_4;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DownloadAudioTask downloadAudioTask;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button playPauseButton = findViewById(R.id.playPauseButton);
        seekBar = findViewById(R.id.seekBar);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseAudio();
                } else {
                    playAudio("https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3");
                    updateSeekBar();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * 1000); // Chuyển đổi từ giây sang mili giây
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }
        });
    }

    private void playAudio(String url) {
        downloadAudioTask = new DownloadAudioTask() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mediaPlayer = getMediaPlayer();

                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    isPlaying = true;
                    Toast.makeText(MainActivity.this, "Audio playing", Toast.LENGTH_SHORT).show();

                    seekBar.setMax(mediaPlayer.getDuration() / 1000); // Chuyển đổi từ mili giây sang giây
                }
            }
        };

        downloadAudioTask.execute(url);
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            Toast.makeText(this, "Audio paused", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekBar() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000; // Chuyển đổi từ mili giây sang giây
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000); // Cập nhật mỗi giây
            }
        };
        handler.postDelayed(runnable, 0);
    }

}