package com.tbsg.turnbasedstrategygame;

import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;
import com.tbsg.turnbasedstrategygame.library.engine.ProgressBarManager;
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
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;

import static org.junit.Assert.*;

public class TurnBasedStrategyGameTest extends ApplicationTest {
    SceneManager sceneManager;
    LoadingScreenController controller;

    FXMLLoader fxmlLoader;

    ProgressBarManager pb_manager;

    @Override
    public void start(Stage stage) throws Exception {
        sceneManager = new SceneManager();
        try {
            initScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Turn Based Strategy Game");
//        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
//        Scene scene2 = new Scene(fxmlLoader.load(),320,240);
        //set scene
        Scene scene = sceneManager.getScene("LOADING_SCREEN");
        System.out.println(scene);
        stage.setScene(scene);
//        PauseTransition pause = new PauseTransition(Duration.seconds(2));
//        pause.setOnFinished(event ->{
//            System.out.println("halo");
//            stage.setScene(scene2);
//        });
//
        stage.show();
        //mulai
        //init progress bar manager
//        initProgessBarManager();
//        //mulai
//        try {
//            startProgressBarManager();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Hi");
//        pause.play();
    }

    void initProgessBarManager() {
        pb_manager = new ProgressBarManager(controller);
        pb_manager.addProgressTask("LOADING_FXML", 1);
    }

    void startProgressBarManager() throws IOException {
        //load fxml
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        sceneManager.addScene("TEST", scene);
        pb_manager.forwardProgress("LOADING_FXML");
    }

    void initScene() throws IOException {
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        System.out.println("masuk");
        System.out.println(fxmlLoader);
        System.out.println("masuk2");
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        controller = fxmlLoader.getController();
        System.out.println("Scene:");
//        System.out.println(scene);
        System.out.println("aman");
        sceneManager.addScene("LOADING_SCREEN", scene);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testProgressBarRendered() {
        ProgressBar pb = lookup("#progressBar").query();
        //cek apakah kerender
        FxAssert.verifyThat(pb, NodeMatchers.isVisible());
        //cek apakah valuenya 0
        System.out.println(pb.getProgress());
        assertEquals(0, pb.getProgress(), 0.01);
//        GuiTest.find("progressBar");
//        assertEquals(pb.getProgress(), 0.0);
    }
//    @Test
//    public void test() {
//        assertEquals(2, 2);
//    }
}