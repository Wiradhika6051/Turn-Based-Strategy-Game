package com.tbsg.turnbasedstrategygame.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.MapManager;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import com.tbsg.turnbasedstrategygame.library.io.Keyboard;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GameController implements Initializable, RefreshableScene {

    @FXML
    private Canvas game;

    private GraphicsContext gc;

    @FXML
    BorderPane root;

    MapManager mapManager;

    final int TILE_SIZE = 100;

    int MAP_WIDTH;
    int MAP_HEIGHT;

    AnimationTimer gameLoop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // game.widthProperty().bind(root.widthProperty());
        // game.heightProperty().bind(root.heightProperty());
        mapManager = new MapManager(game, root.getPadding().getTop(), MAP_WIDTH, MAP_HEIGHT);
        // game.widthProperty().bind(root.widthProperty());
        // game.heightProperty().bind(root.heightProperty());
        game.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                //add onChange listener
                Stage stage = StageManager.getStageFromWindow(game);
                if (stage != null) {
                    gc = game.getGraphicsContext2D();
                    mapManager.updateGC(gc);
                    mapManager.generateMap();
                    mapManager.setStartingPoint();
                    mapManager.drawCanvas();
                    //init key listener
                    Scene scene = SceneManager.getSceneFromNode(game);
                    // scene.setPrefWidth(GraphicsConst.windowWidth);
                    // scene.setPrefHeight(GraphicsConst.windowHeight);

                    // scene.setOnKeyPressed(mapManager::onKeyPressed);
                    scene.setOnKeyPressed(Keyboard.getInstance()::onKeyPressed);
                    // scene.setOnKeyReleased(mapManager::onKeyReleased);
                    scene.setOnKeyReleased(Keyboard.getInstance()::onKeyReleased);
                    scene.setOnMouseMoved(mapManager::onMouseMoved);
                    // start game loop
                    startGameLoop();
                }
            }
        });
        updateLayout();
    }

    @Override
    public void refreshLayout() {
        updateLayout();
    }

    void startGameLoop() {
        if (gameLoop == null) {
            gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    mapManager.updateCamera();  // move offset smoothly
                    mapManager.drawCanvas();    // redraw world
                }
            };
        }
        gameLoop.start();
    }

    void updateLayout() {
        //set const
        double width = GraphicsConst.windowWidth;
        double height = GraphicsConst.windowHeight;
        if (width != 0) {
            GraphicsConst.windowWidth = width;
        }
        if (height != 0) {
            GraphicsConst.windowHeight = height;
        }
        MAP_WIDTH = (int) Math.ceil(GraphicsConst.windowWidth / (2.0 * TILE_SIZE)) + 1;
        MAP_HEIGHT = (int) Math.ceil(GraphicsConst.windowHeight / (2.0 * TILE_SIZE)) + 1;
        
        mapManager.updateMapSize(MAP_WIDTH, MAP_HEIGHT);
        mapManager.updateEdgeThreshold(0.1*GraphicsConst.windowHeight);
        //set size root
        if (root != null) {
            root.setPrefWidth(GraphicsConst.windowWidth);
            root.setPrefHeight(GraphicsConst.windowHeight);
            //set padding root
            if (mapManager != null) {
                mapManager.updatePadding(root.getPadding().getTop());
            }
        }
        if (game != null) {
            // Force a proper layout pass
            Platform.runLater(() -> {
                if (game.getParent() != null) {
                    game.setWidth(GraphicsConst.windowWidth);
                    game.setHeight(GraphicsConst.windowHeight);
                    // System.out.println("Canvas size: " + game.getWidth() + " x " + game.getHeight());
                    // System.out.println("Bounds in parent: " + game.getBoundsInParent());
                    // System.out.println("Bounds in local: " + game.getBoundsInLocal());
                    // System.out.println("Root padding: " + root.getPadding());
                    // System.err.println(String.format(" Padding %f", root.getPadding()));
                }
            });
        }
    }

}
