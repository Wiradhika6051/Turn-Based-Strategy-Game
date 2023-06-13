package com.tbsg.turnbasedstrategygame.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TurnBasedStrategyGameController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}