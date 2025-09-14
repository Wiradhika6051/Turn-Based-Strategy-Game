package com.tbsg.turnbasedstrategygame.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.tbsg.turnbasedstrategygame.library.engine.IProgressBarHandler;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class LoadingScreenController implements Initializable, IProgressBarHandler,RefreshableScene {

    @FXML
    ProgressBar progressBar;

    @FXML
    VBox root;

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
//        System.out.println("Width: " + width);
//        GraphicsConst.windowWidth = width;
//                windowWidth = newValue.getWidth();

        double progressBarWidth = StageManager.calculateWidth(0.8);
        progressBar.setPrefWidth(progressBarWidth);
    }

    @Override
    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void refreshLayout() {
        updateWidth();
    }
}