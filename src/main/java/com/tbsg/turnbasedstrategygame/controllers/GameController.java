package com.tbsg.turnbasedstrategygame.controllers;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.tbsg.turnbasedstrategygame.library.engine.GameManager;
import com.tbsg.turnbasedstrategygame.library.engine.MapObject;
import com.tbsg.turnbasedstrategygame.library.engine.Tile;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import com.tbsg.turnbasedstrategygame.library.io.KeyboardConst;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameController implements Initializable, RefreshableScene {

    @FXML
    private Canvas game;

    private GraphicsContext gc;

    @FXML
    BorderPane root;

    MapObject map;

    Color[] colors = {Color.GREEN, Color.BLUE};

    double centralX = 0; // IDX TILE
    double centralY = 0; // IDX TILE

    double highlightX = 0; // IDX TILE
    double highlightY = 0; // IDX TILE

    private double mouseScrollX = 0;
    private double mouseScrollY = 0;

    private double lastMouseX = -1;
    private double lastMouseY = -1;

    final int TILE_SIZE = 100;
    final double KEYBOARD_MOVE_GRADIENT = 0.1;
    final double MOUSE_MOVE_GRADIENT = 0.1;
    final double EDGE_SCROLL_THRESHOLD = 30; // px distance from edge

    final int BORDER_SIZE = 10;
    int MAP_WIDTH;
    int MAP_HEIGHT;

    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private AnimationTimer gameLoop;

    void setStartingPoint() {
        centralX = 0;
        centralY = 0;
        Random random = new Random();
        while (map.findTile((int) centralX, (int) centralY).getTerrainId() != 0) {
            centralX = Math.abs(random.nextInt()) % map.getX_longitude();
            centralY = Math.abs(random.nextInt()) % map.getY_latitude();
        }
        highlightX = centralX;
        highlightY = centralY;
    }

    void drawCanvas() {

        int rows = map.getY_latitude(); // how many tiles fit vertically
        int cols = map.getX_longitude();  // how many tiles fit horizontally

        double centerScreenX = GraphicsConst.windowWidth / 2.0;
        double centerScreenY = GraphicsConst.windowHeight / 2.0;
        System.out.println(String.format("row,cosl: %d %d", rows, cols));

        gc.setFill(Color.BLUE);
        for (int y = (int) centralY - MAP_HEIGHT - 1; y <= (int) centralY + MAP_HEIGHT + 1; y++) {
            for (int x = (int) centralX - MAP_WIDTH - 1; x <= (int) centralX + MAP_WIDTH + 1; x++) {
                // Compute tile position in pixels
                double dx = x - centralX;
                double dy = y - centralY;

                // convert to pixels
                double px = centerScreenX + dx * TILE_SIZE;
                double py = centerScreenY + dy * TILE_SIZE;
                //  skip tiles completely outside the screen 
                if (px + TILE_SIZE < 0 || px > GraphicsConst.windowWidth) {
                    continue;
                }
                if (py + TILE_SIZE < 0 || py > GraphicsConst.windowHeight) {
                    continue;
                }

                if (map.isCoordinateValid(x, y)) {
                    Tile tile = map.findTile(x, y);
                    gc.setFill(colors[tile.getTerrainId()]);
                } else {
                    gc.setFill(Color.GREY);
                }
                gc.fillRect(
                        px, // pixel X
                        py, // pixel Y
                        TILE_SIZE, // width in pixels
                        TILE_SIZE // height in pixels
                );
            }
        }
        // Render highlight
        int x = (int) highlightX;
        int y = (int) highlightY;
        if (!map.isCoordinateValid(x, y)) {
            // dont render highlight if invalid
            return;
        }
        double dx = highlightX - centralX;
        double dy = highlightY - centralY;
        // convert to pixels
        double px = centerScreenX + dx * TILE_SIZE;
        double py = centerScreenY + dy * TILE_SIZE;
        // skip tiles completely outside the screen
        if (px + TILE_SIZE < 0 || px > GraphicsConst.windowWidth) {
            return;
        }
        if (py + TILE_SIZE < 0 || py > GraphicsConst.windowHeight) {
            return;
        }
        // fill border
        gc.setFill(Color.WHITE);

        gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        // fill isi
        if (map.isCoordinateValid(x, y)) {
            Tile tile = map.findTile((int) x, (int) y);
            gc.setFill(colors[tile.getTerrainId()]);
        } else {
            gc.setFill(Color.GREY);
        }
        gc.fillRect(
                px + BORDER_SIZE, // pixel X
                py + BORDER_SIZE, // pixel Y
                TILE_SIZE - 2 * BORDER_SIZE, // width in pixels
                TILE_SIZE - 2 * BORDER_SIZE // height in pixels
        );
    }

    public void generateMap() {
        try (InputStream inputStream = getClass().getResourceAsStream(GraphicsConst.MAP_FOLDER + "default.txt")) {
            if (inputStream == null) {
                System.err.println("Map File not found!");
                return;
            }

            try (Scanner scanner = new Scanner(inputStream)) {
                int i = 0;
                int j = 0;
                int width = -1;
                int height = -1;

                while (scanner.hasNextLine()) {
                    String row = scanner.nextLine();
                    if (row.length() == 0) {
                        continue;
                    }
                    if (width < 0 && height < 0) {
                        // read dimensions
                        String[] size = row.split(" ");
                        width = Integer.parseInt(size[0]);
                        height = Integer.parseInt(size[1]);
                        map = GameManager.getInstance().getMap();
                        map.setX_longitude(width);
                        map.setY_latitude(height);
                    } else {
                        for (String tileId : row.split("")) {
                            Tile tile = new Tile(i, j, Integer.parseInt(tileId));
                            map.addTile(tile);
                            i++;
                        }
                        i = 0;
                        j++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onKeyPressed(KeyEvent event) {
        int keyId = event.getCode().getCode();
        switch (keyId) {
            case KeyboardConst.KEY_NORTH -> {
                moveUp = true;
            }
            case KeyboardConst.KEY_SOUTH -> {
                moveDown = true;
            }

            case KeyboardConst.KEY_WEST -> {
                moveLeft = true;
            }
            case KeyboardConst.KEY_EAST -> {
                moveRight = true;
            }

            default -> {
            }
        }
    }

    void onKeyReleased(KeyEvent event) {
        int keyId = event.getCode().getCode();
        switch (keyId) {
            case KeyboardConst.KEY_NORTH -> {
                moveUp = false;
            }
            case KeyboardConst.KEY_SOUTH -> {
                moveDown = false;
            }

            case KeyboardConst.KEY_WEST -> {
                moveLeft = false;
            }
            case KeyboardConst.KEY_EAST -> {
                moveRight = false;
            }

            default -> {
            }
        }
    }

    public void onMouseMoved(MouseEvent event) {
        lastMouseX = event.getX();
        lastMouseY = event.getY();

        // 1. Move highlight to hovered tile
        double adjustedY = lastMouseY - root.getPadding().getTop();
        int tileX = (int) Math.floor((lastMouseX - GraphicsConst.windowWidth / 2.0) / TILE_SIZE +  centralX);
        int tileY = (int) Math.floor((adjustedY - GraphicsConst.windowHeight / 2.0) / TILE_SIZE +  centralY);
        highlightX = tileX;
        highlightY = tileY;

        // 2. Handle edge scrolling
        mouseScrollX = 0;
        mouseScrollY = 0;

        // Left edge
        if (lastMouseX < EDGE_SCROLL_THRESHOLD) {
            double factor = 1.0 - (lastMouseX / EDGE_SCROLL_THRESHOLD); // semakin ke kiri semakin cepet scrollnya
            mouseScrollX = -MOUSE_MOVE_GRADIENT * factor;
        } // Right edge
        else if (lastMouseX > GraphicsConst.windowWidth - EDGE_SCROLL_THRESHOLD) {
            double factor = 1.0 - (lastMouseX - (GraphicsConst.windowWidth - EDGE_SCROLL_THRESHOLD)) / EDGE_SCROLL_THRESHOLD;
            mouseScrollX = MOUSE_MOVE_GRADIENT * factor;
        }
        // Upper edge
        if (lastMouseY < EDGE_SCROLL_THRESHOLD) {
            double factor = 1.0 - (lastMouseY / EDGE_SCROLL_THRESHOLD);
            mouseScrollY = -MOUSE_MOVE_GRADIENT * factor;
        } // Low edge
        else if (lastMouseY > GraphicsConst.windowHeight - EDGE_SCROLL_THRESHOLD) {
            double factor = 1.0 - (lastMouseY - (GraphicsConst.windowHeight - EDGE_SCROLL_THRESHOLD)) / EDGE_SCROLL_THRESHOLD;
            mouseScrollY = MOUSE_MOVE_GRADIENT * factor;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                //add onChange listener
                Stage stage = StageManager.getStageFromWindow(game);
                if (stage != null) {
                    gc = game.getGraphicsContext2D();
                    generateMap();
                    setStartingPoint();
                    drawCanvas();
                    //init key listener
                    Scene scene = SceneManager.getSceneFromNode(game);
                    scene.setOnKeyPressed(this::onKeyPressed);
                    scene.setOnKeyReleased(this::onKeyReleased);
                    scene.setOnMouseMoved(this::onMouseMoved);
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
                    updateCamera();  // move offset smoothly
                    drawCanvas();    // redraw world
                }
            };
        }
        gameLoop.start();
    }

    void updateCamera() {
        if (moveUp) {
            centralY -= KEYBOARD_MOVE_GRADIENT;
        }
        if (moveDown) {
            centralY += KEYBOARD_MOVE_GRADIENT;
        }
        if (moveLeft) {
            centralX -= KEYBOARD_MOVE_GRADIENT;
        }
        if (moveRight) {
            centralX += KEYBOARD_MOVE_GRADIENT;
        }
        // Mouse edge scrolling
        if (lastMouseX >= 0 && lastMouseY >= 0) {
            mouseScrollX = 0;
            mouseScrollY = 0;

            if (lastMouseX < EDGE_SCROLL_THRESHOLD) {
                double factor = 1.0 - (lastMouseX / EDGE_SCROLL_THRESHOLD);
                mouseScrollX = -MOUSE_MOVE_GRADIENT * factor;
            } else if (lastMouseX > GraphicsConst.windowWidth - EDGE_SCROLL_THRESHOLD) {
                double factor = (lastMouseX - (GraphicsConst.windowWidth - EDGE_SCROLL_THRESHOLD)) / EDGE_SCROLL_THRESHOLD;
                mouseScrollX = MOUSE_MOVE_GRADIENT * factor;
            }

            if (lastMouseY < EDGE_SCROLL_THRESHOLD) {
                double factor = 1.0 - (lastMouseY / EDGE_SCROLL_THRESHOLD);
                mouseScrollY = -MOUSE_MOVE_GRADIENT * factor;
            } else if (lastMouseY > GraphicsConst.windowHeight - EDGE_SCROLL_THRESHOLD) {
                double factor = (lastMouseY - (GraphicsConst.windowHeight - EDGE_SCROLL_THRESHOLD)) / EDGE_SCROLL_THRESHOLD;
                mouseScrollY = MOUSE_MOVE_GRADIENT * factor;
            }

            centralX += mouseScrollX;
            centralY += mouseScrollY;
        }
        // Clamp camera inside map bounds
        if (centralX < 0) {
            centralX = 0;
        }
        if (centralY < 0) {
            centralY = 0;
        }
        if (centralX > map.getX_longitude() - 1) {
            centralX = map.getX_longitude() - 1;
        }
        if (centralY > map.getY_latitude() - 1) {
            centralY = map.getY_latitude() - 1;
        }
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
    }

}
