package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.TurnBasedStrategyGame;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
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

import static org.junit.Assert.*;

public class CreditScreenControllerTest extends ApplicationTest {
    CreditScreenController controller;
    Stage stage;

    public void start(Stage stage) throws Exception {
        //init loading
        FXMLLoader fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        this.stage = stage;
        stage.setTitle("Turn Based Strategy Game");
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setScene(scene);
        stage.show();
        //load
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("main-menu.fxml"));
        SceneManager.addScene("MAIN_MENU", new Scene(fxmlLoader.load(), 640, 480));
        //mulai scene
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("credit-screen.fxml"));
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
        ImageView button = lookup("#backButton").query();
        double width = button.getFitWidth();
        //pastiin ukurannya sama
        System.out.println("width");
        assertEquals(width, GraphicsConst.windowWidth * 0.07, 0.1);
    }

    @Test
    public void test_componentRendered() {
        Button buttonBack = lookup("#buttonBack").query();
        FxAssert.verifyThat(buttonBack, NodeMatchers.isVisible());
        ImageView buttonLogo = lookup("#backButton").query();
        FxAssert.verifyThat(buttonLogo, NodeMatchers.isVisible());
        Separator newGameButton = lookup("#separator").query();
        FxAssert.verifyThat(newGameButton, NodeMatchers.isVisible());
        Label credits = lookup("#label").query();
        FxAssert.verifyThat(credits, NodeMatchers.isVisible());
        VBox list = lookup("#list").query();
        FxAssert.verifyThat(list, NodeMatchers.isVisible());
    }

    @Test
    public void test_backIconRendered() {
        ImageView buttonLogo = lookup("#backButton").query();
        assertTrue(buttonLogo.getImage() != null);
    }

    @Test
    public void test_creditInserted() {
        VBox list = lookup("#list").query();
        System.out.println("length: " + list.getChildren().size());
        assertTrue(list.getChildren().size() > 0);
    }

    @Test
    public void test_backButtonWorking() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#buttonBack");
        //cek bener gak di main menu
        Label title = lookup("#label").query();
        FxAssert.verifyThat(title, NodeMatchers.isVisible());
        assertEquals(title.getText(), "Turn Based Strategy Game"); // nama game
    }
}