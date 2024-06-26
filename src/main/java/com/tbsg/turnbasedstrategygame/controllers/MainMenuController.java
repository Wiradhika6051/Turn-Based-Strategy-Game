package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
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
        //set root margin
        newGame.setPrefWidth(StageManager.calculateWidth(0.5));
        continueGame.setPrefWidth(StageManager.calculateWidth(0.5));
        setting.setPrefWidth(StageManager.calculateWidth(0.5));
        credits.setPrefWidth(StageManager.calculateWidth(0.5));
        quit.setPrefWidth(StageManager.calculateWidth(0.5));
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
        stage.setScene(SceneManager.getScene("CREDIT_SCREEN"));
    }

    @FXML
    protected void openSettings() {
        Stage stage = (Stage) setting.getScene().getWindow();
        stage.setScene(SceneManager.getScene("SETTINGS"));
    }

    @FXML
    protected void startNewGame() {
        Stage stage = (Stage) setting.getScene().getWindow();
        stage.setScene(SceneManager.getScene("NEW_GAME"));
    }
}
