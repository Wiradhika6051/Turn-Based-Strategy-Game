package com.tbsg.turnbasedstrategygame.controllers;

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

public class LoadingScreenController implements Initializable {
    //public class LoadingScreenController {
    @FXML
    private Label welcomeText;

    @FXML
    ProgressBar progressBar;

    @FXML
    VBox root;

    private double progressBarWidth;
    //
    private double windowWidth;

    @FXML
    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
        progressBar.setProgress(0.6767);
    }

//    public double getProgressBarWidth() {
//        return progressBarWidth;
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        //dapetin screen
//        Screen screen = Screen.getPrimary();
//
//        //dapetin visual bound screen
//        double screenWidth = screen.getVisualBounds().getWidth();
//
//        //set width progress bar
//        progressBar.setPrefWidth(screenWidth * 0.2);
//        progressBarWidth = root.getWidth();
//        System.out.println(progressBarWidth);
//        progressBar.setPrefWidth(progressBarWidth * 0.5);
        //tunggu scenenya kemuat
//        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
//            if (newScene != null) {
//                Platform.runLater(() -> {
//                    //dapetin stage
////                Stage stage = (Stage) progressBar.getScene().getWindow();
////                //dapetin widthnya
////                windowWidth = stage.getWidth();
//                    windowWidth = newScene.getWidth();
//                    System.out.println("ahou");
//                    System.out.println(windowWidth);
//                    //set width progress bar
//                    progressBarWidth = windowWidth * 0.5;
//                });
//
//            }
//        });
        root.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                windowWidth = newValue.getWidth();
                progressBarWidth = windowWidth * 0.8;
                progressBar.setPrefWidth(progressBarWidth);
            }
        });


    }
}