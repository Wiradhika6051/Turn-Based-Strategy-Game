package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.audio.AudioPlayer;
import com.tbsg.turnbasedstrategygame.library.audio.BacksoundPlayer;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    VBox list;

    @FXML
    ImageView backButton;

    @FXML
    VBox root;

    @FXML
    Slider masterVolumeSlider;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //update size back button
        root.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateWidth(newValue.getWidth(), newValue.getHeight());
                //set master volume slider initial value
                masterVolumeSlider.setValue(BacksoundPlayer.getInstance().getVolume());
            }
        });

//        masterVolumeSlider.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                masterVolumeSlider.setValue(BacksoundPlayer.getInstance().getVolume());
//            }
//        });
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
        }
        //set size komponen
        if (backButton != null) {
            backButton.setFitWidth(GraphicsConst.windowWidth * 0.07);
            backButton.setFitHeight(GraphicsConst.windowHeight * 0.05);
        }
    }

    @FXML
    protected void backToMenu() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(SceneManager.getScene("MAIN_MENU"));
    }

    @FXML
    protected void changeMasterVolume() {
        System.out.println(masterVolumeSlider.getValue());
        //change master volume
        AudioPlayer masterAudioPlayer = BacksoundPlayer.getInstance();
        masterAudioPlayer.changeVolume(masterVolumeSlider.getValue());
    }

}
