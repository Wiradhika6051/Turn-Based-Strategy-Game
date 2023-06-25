package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newGame.setPrefWidth(GraphicsConst.windowWidth * 0.5);
        continueGame.setPrefWidth(GraphicsConst.windowWidth * 0.5);
        setting.setPrefWidth(GraphicsConst.windowWidth * 0.5);
        credits.setPrefWidth(GraphicsConst.windowWidth * 0.5);
        quit.setPrefWidth(GraphicsConst.windowWidth * 0.5);

    }
}
