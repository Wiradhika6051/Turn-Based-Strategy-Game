package com.tbsg.turnbasedstrategygame.library.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public abstract class AudioPlayer {

    MediaPlayer mediaPlayer;

    public AudioPlayer() {
    }

    public abstract void play(String path);

    public abstract void stop();

    public abstract void changeVolume(double value);

    public abstract double getVolume();
}
