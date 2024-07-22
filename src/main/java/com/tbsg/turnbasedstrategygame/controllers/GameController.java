package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Canvas game;

    private GraphicsContext gc;

    @FXML
    BorderPane root;

    Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.VIOLET, Color.PURPLE};

    @FXML
    private void drawCanvas(ActionEvent event) {
        Random rand = new Random();
        int i = 0;
        int j = 0;
        while (j * 100 <= game.getHeight()) {
//            System.out.println(game.getWidth());
            if (i * 100 > game.getWidth()) {
                i = 0;
                j++;
            }
            gc.setFill(colors[rand.nextInt(colors.length)]);
            gc.fillRect(100 * i, 100 * j, 100, 100);
            i++;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = game.getGraphicsContext2D();
        //set const
        double width = GraphicsConst.windowWidth;
        double height = GraphicsConst.windowHeight;
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
        if (game != null) {
            game.setWidth(StageManager.calculateWidth(1.0));
            game.setHeight(StageManager.calculateHeight(1.0));
        }
        drawCanvas(new ActionEvent());
        //set size komponen
//        if (backButton != null) {
//            backButton.setFitWidth(StageManager.calculateWidth(0.07));
//            backButton.setFitHeight(StageManager.calculateHeight(0.05));
//        }
//        System.out.println(game.getWidth() + " " + game.getHeight());
//        gc.setFill(Color.BLACK);
//        System.out.println("color set to black");
//        gc.fillRect(50, 50, 100, 100);
//        System.out.println("draw rectangle");
    }

}
