package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.TurnBasedStrategyGame;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.*;

public class LoadingScreenControllerTest extends ApplicationTest {
    LoadingScreenController controller;

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        stage.setTitle("Turn Based Strategy Game");
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        controller = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_progressBarInitialization() {
        ProgressBar pb = lookup("#progressBar").query();
        //cari ukuran
        VBox root = lookup("#root").query();
        double width = root.getWidth() * 0.8;
        //pastiin ukurannya sama
        assertEquals(width, pb.getPrefWidth(), 0.1);
    }

    @Test
    public void test_setProgress() {
        ProgressBar pb = lookup("#progressBar").query();
        controller.setProgress(0.89);
        assertEquals(0.89, pb.getProgress(), 0.1);
    }
}