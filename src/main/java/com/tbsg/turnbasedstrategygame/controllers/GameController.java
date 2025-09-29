package com.tbsg.turnbasedstrategygame.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.MapRenderer;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

    MapRenderer mapRenderer;

    final int TILE_SIZE = 100;

    int MAP_WIDTH;
    int MAP_HEIGHT;

    AnimationTimer gameLoop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapRenderer = new MapRenderer(gc, root.getPadding().getTop(), MAP_WIDTH, MAP_HEIGHT);
        game.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                //add onChange listener
                Stage stage = StageManager.getStageFromWindow(game);
                if (stage != null) {
                    gc = game.getGraphicsContext2D();
                    mapRenderer.updateGC(gc);
                    mapRenderer.generateMap();
                    mapRenderer.setStartingPoint();
                    mapRenderer.drawCanvas();
                    //init key listener
                    Scene scene = SceneManager.getSceneFromNode(game);
                    scene.setOnKeyPressed(mapRenderer::onKeyPressed);
                    scene.setOnKeyReleased(mapRenderer::onKeyReleased);
                    scene.setOnMouseMoved(mapRenderer::onMouseMoved);
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
                    mapRenderer.updateCamera();  // move offset smoothly
                    mapRenderer.drawCanvas();    // redraw world
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
        mapRenderer.updateMapSize(MAP_WIDTH, MAP_HEIGHT);
        //set size root
        if (root != null) {
            root.setPrefWidth(GraphicsConst.windowWidth);
            root.setPrefHeight(GraphicsConst.windowHeight);
            //set padding root
            root.setPadding(new Insets(StageManager.calculateHeight(0.013), 0, 0, 0));
            if (mapRenderer != null) {
                mapRenderer.updatePadding(root.getPadding().getTop());
            }
        }
        if (game != null) {
            game.setWidth(StageManager.calculateWidth(1.0));
            game.setHeight(StageManager.calculateHeight(1.0));
        }
    }

}
