package com.example.sudokuproject;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;

    private MusicManager(Context context) {
        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.5f, 0.5f);
        isPrepared = true;
    }

    public static MusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicManager(context);
        }
        return instance;
    }

    public void start() {
        if (isPrepared && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (isPrepared && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (isPrepared) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isPrepared = false;
            instance = null;
        }
    }

    public boolean isPlaying() {
        return isPrepared && mediaPlayer.isPlaying();
    }

    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }
}
