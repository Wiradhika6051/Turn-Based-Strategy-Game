package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.engine.ConfigManager;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewGameController implements Initializable {
    @FXML
    VBox root;
    @FXML
    ImageView backButton;
    @FXML
    TextField civNameInput;
    @FXML
    Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
    }

    void updateWidth(double width, double height) {
        //set const
        if (width != 0) {
            GraphicsConst.windowWidth = width;
        }
        if (height != 0) {
            GraphicsConst.windowHeight = height;
        }
        //set size root
        if (root != null) {
            root.setPrefWidth(GraphicsConst.windowWidth);
            root.setPrefHeight(GraphicsConst.windowHeight);
            //set padding root
            root.setPadding(new Insets(StageManager.calculateHeight(0.013), 0, 0, 0));
        }
        //set size komponen
        if (backButton != null) {
            backButton.setFitWidth(StageManager.calculateWidth(0.07));
            backButton.setFitHeight(StageManager.calculateHeight(0.05));
        }
    }

    @FXML
    protected void backToMenu() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(SceneManager.getScene("MAIN_MENU"));
    }

    @FXML
    protected void onStartButtonClicked() {
        // Dapatkan nama civilization
        String civilization_name = civNameInput.getText();
        //TODO konekin ke map dan game engine
    }
}
