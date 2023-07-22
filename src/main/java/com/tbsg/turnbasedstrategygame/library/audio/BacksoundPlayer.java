package com.tbsg.turnbasedstrategygame.library.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BacksoundPlayer extends AudioPlayer {

    static BacksoundPlayer instance;
    Map<String, Media> soundLists;

    private BacksoundPlayer() {
        //init sound list
        soundLists = new HashMap<>();
    }

    public void addSound(String path) {
        Media sound = null;
        try {
            sound = new Media(getClass().getResource(AudioConst.AUDIO_FOLDER + path).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (sound != null) {
            soundLists.put(path, sound);
        }
    }

    @Override
    public void play(String path) {
        Media sound = soundLists.get(path);
        if (sound == null) {
            addSound(path);
            sound = soundLists.get(path);
        }
        if (sound != null) {
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }

    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new BacksoundPlayer();
        }
        return instance;
    }

}
