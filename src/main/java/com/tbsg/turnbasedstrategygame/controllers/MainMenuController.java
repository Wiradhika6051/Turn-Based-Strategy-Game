package com.tbsg.turnbasedstrategygame.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuController implements Initializable, RefreshableScene {

    @FXML
    Button newGame;
    @FXML
    Button continueGame;
    @FXML
    Button setting;
    @FXML
    Button credits;
    @FXML
    Button quit;

    @FXML
    VBox root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setFillWidth(true);
        root.setMinSize(0, 0);
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
    }

    void updateWidth(double width, double height) {
        if (root != null) {
            root.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            root.setMaxSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
            root.setPrefWidth(GraphicsConst.windowWidth);
            root.setPrefHeight(GraphicsConst.windowHeight);
        }
        //set root margin
        newGame.setPrefWidth(StageManager.calculateWidth(0.5));
        continueGame.setPrefWidth(StageManager.calculateWidth(0.5));
        setting.setPrefWidth(StageManager.calculateWidth(0.5));
        credits.setPrefWidth(StageManager.calculateWidth(0.5));
        quit.setPrefWidth(StageManager.calculateWidth(0.5));
        Stage stage = StageManager.getInstance();
        // System.out.println("Stage W=" + stage.getWidth() + " H=" + stage.getHeight());
        // System.out.println("Scene W=" + stage.getScene().getWidth() + " H=" + stage.getScene().getHeight());
        // System.out.println("Root W=" + root.getWidth() + " H=" + root.getHeight());
        //play backsound
    }

    @FXML
    protected void exitGame() {
        Stage stage = (Stage) quit.getScene().getWindow();
        //handle buat tutup data
        //tutup stage
        stage.close();
    }

    @FXML
    protected void seeCredit() {
        Stage stage = (Stage) credits.getScene().getWindow();
        StageManager.setScene(SceneManager.getScene("CREDIT_SCREEN"));
    }

    @FXML
    protected void openSettings() {
        Stage stage = (Stage) setting.getScene().getWindow();
        StageManager.setScene(SceneManager.getScene("SETTINGS"));
    }

    @FXML
    protected void startNewGame() {
        Stage stage = (Stage) setting.getScene().getWindow();
        StageManager.setScene(SceneManager.getScene("NEW_GAME"));
    }

    @Override
    public void refreshLayout() {
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
    }
}
