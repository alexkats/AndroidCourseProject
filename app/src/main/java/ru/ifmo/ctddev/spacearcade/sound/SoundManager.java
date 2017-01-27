package ru.ifmo.ctddev.spacearcade.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrey Chernyshov
 * @since 27.01.17
 */

public final class SoundManager {

    private static final int MAX_STREAMS = 10;
    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    private static final String SA_SOUNDS_PREFS = "ru.ifmo.ctddev.spacearcade.sounds.boolean";
    private static final String SA_MUSIC_PREFS = "ru.ifmo.ctddev.spacearcade.music.boolean";
    private final Context context;
    private Map<GameEvent, Integer> soundsMap;
    private SoundPool soundPool;

    private boolean soundEnabled;
    private boolean musicEnabled;

    private MediaPlayer backgroundPlayer;

    public SoundManager(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        soundEnabled = prefs.getBoolean(SA_SOUNDS_PREFS, true);
        musicEnabled = prefs.getBoolean(SA_MUSIC_PREFS, true);
        this.context = context;
        loadIfNeeded();
    }

    private void loadIfNeeded() {
        if (soundEnabled) {
            loadSounds();
        }

        if (musicEnabled) {
            loadMusic();
        }
    }

    private void loadSounds() {
        createSoundPool();
        //noinspection MapReplaceableByEnumMap
        soundsMap = new HashMap<>();
        loadEventSound(context, GameEvent.ASTEROID_HIT, "Asteroid_explosion.wav");
        loadEventSound(context, GameEvent.SPACESHIP_HIT, "Spaceship_explosion.wav");
        loadEventSound(context, GameEvent.LASER_FIRED, "Laser_shoot.wav");
    }

    private void loadEventSound(Context context, GameEvent event, String... filename) {
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + filename[0]);
            int soundId = soundPool.load(descriptor, 1);
            soundsMap.put(event, soundId);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    private void loadMusic() {
        try {
            backgroundPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd("sfx/Riccardo_Colombo_-_11_-_Something_mental.mp3");
            backgroundPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            backgroundPlayer.setLooping(true);
            backgroundPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            backgroundPlayer.prepare();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }

    public void playSoundForGameEvent(GameEvent event) {
        if (!soundEnabled) {
            return;
        }

        Integer soundId = soundsMap.get(event);

        if (soundId != null) {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void pauseBgMusic() {
        if (musicEnabled) {
            backgroundPlayer.pause();
        }
    }

    public void toggleSoundStatus() {
        soundEnabled = !soundEnabled;

        if (soundEnabled) {
            loadSounds();
        } else {
            unloadSounds();
        }

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(SA_SOUNDS_PREFS, soundEnabled)
                .apply();
    }

    private void unloadSounds() {
        soundPool.release();
        soundPool = null;
        soundsMap.clear();
    }

    public void toggleMusicStatus() {
        musicEnabled = !musicEnabled;

        if (musicEnabled) {
            loadMusic();
            resumeBgMusic();
        } else {
            unloadMusic();
        }

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(SA_MUSIC_PREFS, musicEnabled)
                .apply();
    }

    public void resumeBgMusic() {
        if (musicEnabled) {
            backgroundPlayer.start();
        }
    }

    private void unloadMusic() {
        backgroundPlayer.stop();
        backgroundPlayer.release();
    }

    public boolean getMusicStatus() {
        return musicEnabled;
    }

    public boolean getSoundStatus() {
        return soundEnabled;
    }
}
