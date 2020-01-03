package com.example.runner.main;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.runner.R;

import java.io.IOException;

public class AudioPlayer {
    public static final int BERRY = 0;

    private static int[] AUDIO_ID;
    private Context context;
    private GameView view;
    private static SoundPool sp;
    private static MediaPlayer mp;

    AudioPlayer(Context context, GameView view) {
        AUDIO_ID = new int[1];
        this.context = context;
        this.view = view;

        loadPool();
        loadPlayer();
    }

    private void loadPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        sp = new SoundPool.Builder().setMaxStreams(5)
                .setAudioAttributes(attributes).build();

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                view.onReady();
            }
        });
        AUDIO_ID[BERRY] = sp.load(context, R.raw.berry, 1);
    }

    private void loadPlayer() {
        mp = MediaPlayer.create(context, R.raw.theme);
        mp.setVolume(0.1f, 0.1f);
        mp.setLooping(true);
    }

    static void start() {
        mp.start();
    }

    static void pause() {
        mp.pause();
    }

    static void reset() {
        mp.stop();
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void release() {
        sp.release();
        mp.release();
    }

    public static void playSound(int id, float volume) {
        long time = System.currentTimeMillis();
        sp.play(AUDIO_ID[id], volume, volume, 1, 0, 1);
        Log.i("TIME", System.currentTimeMillis() - time + "ms");
    }
}
