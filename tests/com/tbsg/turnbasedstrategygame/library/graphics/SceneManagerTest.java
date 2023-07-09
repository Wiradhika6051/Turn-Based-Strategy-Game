package com.tbsg.turnbasedstrategygame.library.graphics;

import com.tbsg.turnbasedstrategygame.TurnBasedStrategyGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.Assert.*;

public class SceneManagerTest extends ApplicationTest {
//    SceneManager manager;

    @Before
    public void setUp() throws Exception {
//        manager = new SceneManager();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void test_addScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
//        manager.addScene("TEST", new Scene(fxmlLoader.load(), 100, 100));
        SceneManager.sceneMap.clear();
        SceneManager.addScene("TEST", new Scene(fxmlLoader.load(), 100, 100));
        //cek apakah sizenya >1
        assertEquals(1, SceneManager.sceneMap.size());
    }

    @Test
    public void test_getScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        //pasttiin hasilnya bukan null
        SceneManager.addScene("TEST_1", new Scene(fxmlLoader.load(), 150, 100));
        Scene scene = SceneManager.getScene("TEST_1");
        assertNotNull(scene);
        //pastiin ukurannya nya bener
        assertEquals(150, scene.getWidth(), 0.1);
        assertEquals(100, scene.getHeight(), 0.1);
    }
}