package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.TurnBasedStrategyGame;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;


import static org.junit.Assert.assertEquals;

public class MainMenuControllerTest extends ApplicationTest {
    MainMenuController controller;
    Stage stage;

    public void start(Stage stage) throws Exception {
        //init loading
        FXMLLoader fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        this.stage = stage;
        stage.setTitle("Turn Based Strategy Game");
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setScene(scene);
        stage.show();
        //mulai scene
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("main-menu.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        controller = fxmlLoader.getController();
        stage.setScene(scene);

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test_sizeMatching() {
        //cari ukuran
        VBox root = lookup("#root").query();
        double width = root.getWidth();
        //pastiin ukurannya sama
        assertEquals(width, GraphicsConst.windowWidth, 0.1);
    }

    @Test
    public void test_componentRendered() {
        Label label = lookup("#label").query();
        FxAssert.verifyThat(label, NodeMatchers.isVisible());
        Button newGameButton = lookup("#newGame").query();
        FxAssert.verifyThat(newGameButton, NodeMatchers.isVisible());
        Button continueButton = lookup("#continueGame").query();
        FxAssert.verifyThat(continueButton, NodeMatchers.isVisible());
        Button settingButton = lookup("#setting").query();
        FxAssert.verifyThat(settingButton, NodeMatchers.isVisible());
        Button creditsButton = lookup("#credits").query();
        FxAssert.verifyThat(creditsButton, NodeMatchers.isVisible());
        Button quitButton = lookup("#quit").query();
        FxAssert.verifyThat(quitButton, NodeMatchers.isVisible());
    }

    @Test
    public void test_exitButtonClosed() {
        FxRobot robot = new FxRobot();
        Assert.assertTrue(stage.isShowing());
        robot.clickOn("#quit");
        Assert.assertFalse(stage.isShowing());
    }

}
