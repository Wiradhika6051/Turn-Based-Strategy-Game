package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.engine.IProgressBarHandler;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingScreenController implements Initializable, IProgressBarHandler {

    @FXML
    ProgressBar progressBar;

    @FXML
    VBox root;

    private double progressBarWidth;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateWidth(newValue.getWidth());
            }
        });
    }

    public void updateWidth(double width) {
        GraphicsConst.windowWidth = width;
//                windowWidth = newValue.getWidth();
        progressBarWidth = GraphicsConst.windowWidth * 0.8;
        progressBar.setPrefWidth(progressBarWidth);
    }

    @Override
    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}