package com.tbsg.turnbasedstrategygame.library.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BacksoundPlayer extends AudioPlayer {

    static BacksoundPlayer instance;
    List<AudioData> soundLists;
    Map<String, Integer> soundIndex;

    Media sound;

    private BacksoundPlayer() {
        //init sound list
        soundLists = new ArrayList<>();
        soundIndex = new HashMap<>();
        sound = null;
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
            AudioData metadata = new AudioData(path, sound);
            soundLists.add(metadata);
            soundIndex.put(path, soundLists.indexOf(metadata));
        }
    }

    @Override
    public void play(String path) {
        Integer index = soundIndex.get(path);
        if (index == null && soundIndex.size() > 0) {
            index = 0;
        }
        //dapetin sound
        if (index != null) {
            sound = soundLists.get(index).audio;
        }
        if (sound == null) {
            addSound(path);
            index = soundIndex.get(path);
            sound = soundLists.get(index).audio;
        }
        if (sound != null) {
            mediaPlayer = new MediaPlayer(sound);
            //autoplay

            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                }
            });
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
