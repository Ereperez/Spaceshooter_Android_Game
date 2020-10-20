package com.ereperez.spaceshooter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.io.IOException;

public class Jukebox {
    private static final float LEFT_VOLUME = GameSettings.LEFT_VOLUME;
    private static final float RIGHT_VOLUME = GameSettings.RIGHT_VOLUME;
    private static final float RATE_VOLUME = GameSettings.RATE_VOLUME;
    private static final int PRIORITY_VOLUME = GameSettings.PRIORITY_VOLUME;
    private static final int LOOP_VOLUME = GameSettings.LOOP_VOLUME;

    SoundPool soundPool = null;
    private static final int MAX_STREAMS = 4;
    static int CRASH = 0;
    static int BOOST = 1;
    static int GAME_START = 2;
    static int GAME_OVER = 3;

    //Sonification - Content type value to use when the content type is a sound used to accompany a user action,
    // such as a beep or sound effect expressing a key click, or event,
    // such as the type of a sound for a bonus being received in a game.
    Jukebox(final Context context){
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(MAX_STREAMS)
                .build();
        loadSounds(context);
    }

    private void loadSounds(final Context context){
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descCrash;
            AssetFileDescriptor descBoost;
            AssetFileDescriptor descStart;
            AssetFileDescriptor descOver;
            descCrash = assetManager.openFd("crash.wav");
            CRASH = soundPool.load(descCrash, 1);
            descBoost = assetManager.openFd("boost.wav");
            BOOST = soundPool.load(descBoost, 2);
            descStart = assetManager.openFd("game_start.wav");
            GAME_START = soundPool.load(descStart, 1);
            descOver = assetManager.openFd("game_over.wav");
            GAME_OVER = soundPool.load(descOver, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void play(final int soundID){
        if (soundID > 0){
            soundPool.play(soundID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY_VOLUME, LOOP_VOLUME, RATE_VOLUME);
        }
    }

    void destroy(){
        soundPool.release();
        soundPool = null;
    }
}
