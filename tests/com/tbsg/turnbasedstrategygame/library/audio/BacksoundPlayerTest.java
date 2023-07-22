package com.tbsg.turnbasedstrategygame.library.audio;

import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.*;

public class BacksoundPlayerTest {

    @BeforeClass
    public static void setupClass() {
        // Initialize the JavaFX Toolkit
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Before
    public void setUp() {
        BacksoundPlayer bp = (BacksoundPlayer) BacksoundPlayer.getInstance();
        bp.soundLists.clear();
        bp.soundIndex.clear();
    }

    @Test
    public void test_getInstance() {
        BacksoundPlayer bp1 = (BacksoundPlayer) BacksoundPlayer.getInstance();
        BacksoundPlayer bp2 = (BacksoundPlayer) BacksoundPlayer.getInstance();
        Assertions.assertEquals(bp1, bp2);
        Assertions.assertNotNull(bp1.soundIndex);
        Assertions.assertNull(bp1.sound);
        Assertions.assertNull(bp1.mediaPlayer);
        Assertions.assertNotNull(bp1.soundLists);
    }

    @Test
    public void test_addSound() {
        BacksoundPlayer bp = (BacksoundPlayer) BacksoundPlayer.getInstance();
        bp.addSound("forest-with-small-river-birds-and-nature-field-recording-6735.mp3");
        Assertions.assertEquals(bp.soundLists.size(), 1);
        Assertions.assertNotNull(bp.soundIndex.get("forest-with-small-river-birds-and-nature-field-recording-6735.mp3"));
    }

    @Test
    public void test_play() {
        BacksoundPlayer bp = (BacksoundPlayer) BacksoundPlayer.getInstance();
        bp.addSound("forest-with-small-river-birds-and-nature-field-recording-6735.mp3");
        //tes awal
        Assertions.assertEquals(bp.soundLists.size(), 1);
        Assertions.assertNotNull(bp.soundIndex.get("forest-with-small-river-birds-and-nature-field-recording-6735.mp3"));
        //play
        Platform.runLater(() -> {
            bp.play("forest-with-small-river-birds-and-nature-field-recording-6735.mp3");
        });
        // Wait for a short duration to allow the MediaPlayer to initialize and start playing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //tes play
        Assertions.assertEquals(MediaPlayer.Status.PLAYING, bp.mediaPlayer.getStatus());
        //tes akhir
        Assertions.assertEquals(bp.soundLists.size(), 1);
        Assertions.assertNotNull(bp.soundIndex.get("forest-with-small-river-birds-and-nature-field-recording-6735.mp3"));

    }

    @Test
    public void test_play_empty() {
        BacksoundPlayer bp = (BacksoundPlayer) BacksoundPlayer.getInstance();
        //tes awal
        Assertions.assertEquals(bp.soundLists.size(), 0);
        Assertions.assertNull(bp.soundIndex.get("forest-with-small-river-birds-and-nature-field-recording-6735.mp3"));
        //play
        Platform.runLater(() -> {
            bp.play("forest-with-small-river-birds-and-nature-field-recording-6735.mp3");
        });
        // Wait for a short duration to allow the MediaPlayer to initialize and start playing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //tes play
        Assertions.assertEquals(bp.mediaPlayer.getStatus(), MediaPlayer.Status.PLAYING);
        //tes akhir
        Assertions.assertEquals(bp.soundLists.size(), 1);
        Assertions.assertNotNull(bp.soundIndex.get("forest-with-small-river-birds-and-nature-field-recording-6735.mp3"));

    }

    @Test
    public void test_stop() {
        BacksoundPlayer bp = (BacksoundPlayer) BacksoundPlayer.getInstance();
        //play
        Platform.runLater(() -> {
            bp.play("forest-with-small-river-birds-and-nature-field-recording-6735.mp3");
        });
        // Wait for a short duration to allow the MediaPlayer to initialize and start playing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //tes play
        Assertions.assertEquals(bp.mediaPlayer.getStatus(), MediaPlayer.Status.PLAYING);
        //tes awal
        Assertions.assertEquals(bp.soundLists.size(), 1);
        Assertions.assertNotNull(bp.soundIndex.get("forest-with-small-river-birds-and-nature-field-recording-6735.mp3"));
        //stop
        bp.stop();
        //tes akhir
        Assertions.assertEquals(bp.mediaPlayer.getStatus(), MediaPlayer.Status.STOPPED);
    }

}