package com.tbsg.turnbasedstrategygame.library.engine;

import com.tbsg.turnbasedstrategygame.TurnBasedStrategyGame;
import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ProgressBarManagerTest extends ApplicationTest {
    ProgressBarManager manager;
    LoadingScreenController handler;
    SceneManager sceneManager;
    FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        sceneManager = new SceneManager();
        try {
            initScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Turn Based Strategy Game");
        //set scene
        Scene scene = sceneManager.getScene("LOADING_SCREEN");
        System.out.println(scene);
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setUp() throws IOException {
        manager = new ProgressBarManager(handler);
    }

    void initScene() throws IOException {
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        handler = fxmlLoader.getController();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void test_initialized() {
        //pastiin map nya gak null
        assertNotNull(manager.progress);
        //pastiin panjangnya 0
        assertEquals(0, manager.progress.size());
        //pastiin total progressnya 0
        assertEquals(0, manager.totalProgress);
        //pastiin current progressnya 0
        assertEquals(0, manager.currentProgress);
        //pastiin handler nya gak null
        assertNotNull(manager.handler);
    }

    @Test
    public void test_addProgressTask() {
        manager.addProgressTask("LOADING_FXML", 5);
        //pastiin udah ketambah
        assertTrue(manager.progress.size() > 0);
        //pastiin key nya ada
        assertTrue(manager.progress.containsKey("LOADING_FXML"));
        //pastiin jumlah task nya bener
        assertEquals(5, (int) manager.progress.get("LOADING_FXML"));
    }

    @Test
    public void test_forwardProgress() throws NoSuchFieldException, IllegalAccessException {
        manager.addProgressTask("LOADING_IMG", 10);
        manager.forwardProgress("LOADING_IMG");
        //pastiin jumlah task nya jadi 9
        assertEquals(9, (int) manager.progress.get("LOADING_IMG"));
        //cek current progress
        assertEquals(1, manager.currentProgress);
        //cek progress bar
        Field field = handler.getClass().getDeclaredField("progressBar");
        field.setAccessible(true);
        System.out.println(handler);
        ProgressBar pb = (ProgressBar) field.get(handler);
        assertEquals(0.1, pb.getProgress(), 0.01);
    }
}