package com.tbsg.turnbasedstrategygame.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.tbsg.turnbasedstrategygame.library.engine.IProgressBarHandler;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class LoadingScreenController implements Initializable, IProgressBarHandler, RefreshableScene {

    @FXML
    public ProgressBar progressBar;

    @FXML
    VBox root;

    @FXML
    public Label progressLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        root.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                updateWidth(newValue.getWidth());
//            }
//        });
        updateWidth();
    }

    public void updateWidth() {
        double progressBarWidth = StageManager.calculateWidth(0.8);
        progressBar.setPrefWidth(progressBarWidth);
        double progressBarHeight = StageManager.calculateHeight(0.05);
        progressBar.setPrefHeight(progressBarHeight);
    }

    @Override
    public void setProgress(double progress,String taskName) {

        progressBar.setProgress(progress);
        progressLabel.setText(taskName);
        // System.err.println(progressLabel.getText());
        // show timestamp when called
        // String timestamp = java.time.LocalDateTime.now()
        //         .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        // System.out.println("[" + timestamp + "] setProgress(" + progress + ")");
    }

    @Override
    public void refreshLayout() {
        updateWidth();
    }
}
