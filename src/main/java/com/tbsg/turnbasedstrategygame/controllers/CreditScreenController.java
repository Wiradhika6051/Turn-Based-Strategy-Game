package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreditScreenController implements Initializable {
    @FXML
    VBox list;

    @FXML
    ImageView backButton;

    @FXML
    VBox root;
    final String[] creditList = {"Game Designer", "Programmer", "QA", "Icons", "Audio"};
    final String[][] credits = {
            {"Fawwaz (Wiradhika6051)"},
            {"Fawwaz (Wiradhika6051)"},
            {"Fawwaz (Wiradhika6051)"},
            {"Back Icon: Kirill Kazachek - Flaticon"},
            {"Forest With Small River Birds By Pixabay"}
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //update size back button
        root.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateWidth(newValue.getWidth(), newValue.getHeight());
            }
        });
//        backButton.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                updateWidth(newValue.getWidth(), newValue.getHeight());
//            }
//        });

        //tambahin credit
        for (int i = 0; i < creditList.length; i++) {
            //tambahin judul kredit
            Label creditsCategory = new Label(creditList[i]);
            //styling
            creditsCategory.setFont(Font.font("System Bold", FontWeight.BOLD, 15.0));
            list.getChildren().add(creditsCategory);
            for (int j = 0; j < credits[i].length; j++) {
                //tambahin nama
                list.getChildren().addAll(new Label(credits[i][j]));
            }
        }
    }

    void updateWidth(double width, double height) {
        System.out.println(width);
        System.out.println(height);
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
}
