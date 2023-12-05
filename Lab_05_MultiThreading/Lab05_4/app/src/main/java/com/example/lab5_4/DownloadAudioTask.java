package com.example.lab5_4;

import android.os.AsyncTask;
import android.media.MediaPlayer;
import java.io.IOException;

public class DownloadAudioTask extends AsyncTask<String, Void, Void> {
    private MediaPlayer mediaPlayer;

    @Override
    protected Void doInBackground(String... params) {
        String url = params[0];

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
