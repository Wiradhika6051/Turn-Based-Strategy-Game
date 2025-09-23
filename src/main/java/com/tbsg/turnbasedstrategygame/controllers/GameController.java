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

    float centralX = 0;
    float centralY = 0;

    float highlightX = 0;
    float highlightY = 0;

    final int TILE_SIZE = 100;
    final float KEYBOARD_MOVE_GRADIENT = 1f;
    final float MOUSE_MOVE_GRADIENT = 1f;

    final int BORDER_SIZE = 10;
    int MAP_WIDTH;
    int MAP_HEIGHT;

//    float highlight_x = 0;
//    float highlight_y = 0;
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

    private void drawCanvas(float gradient) {
//        System.out.println(MAP_WIDTH + " " + MAP_HEIGHT);
//        final float RIGHT_IDX = Math.max(centralX - MAP_WIDTH, 0) + 2 * MAP_WIDTH + 1;//batas atas kudu + 1
//        final float BOTTOM_IDX = Math.max(centralY - MAP_HEIGHT, 0) + 2 * MAP_HEIGHT + 1;
        final float RIGHT_IDX = centralX - MAP_WIDTH + 2 * MAP_WIDTH + 1;//batas atas kudu + 1
        final float BOTTOM_IDX = centralY - MAP_HEIGHT + 2 * MAP_HEIGHT + 1;
//        System.out.println(RIGHT_IDX + " " + BOTTOM_IDX);
        for (float j = centralY - MAP_HEIGHT; j < BOTTOM_IDX; j += gradient) {
            for (float i = centralX - MAP_WIDTH; i < RIGHT_IDX; i += gradient) {
//                System.out.println(i + " " + j);
//                System.out.println(i - central_x - 3 + " " + (j - central_y - 3));
//                float baseX = Math.max(centralX - MAP_WIDTH, 0);
//                float baseY = Math.max(centralY - MAP_HEIGHT, 0);
                float baseX = centralX - MAP_WIDTH;
                float baseY = centralY - MAP_HEIGHT;
                float canvasX = Math.min(i - baseX, map.getX_longitude());
                float canvasY = Math.min(j - baseY, map.getY_latitude());
//                if (MathUtils.isFloatEqual(i, centralX) && MathUtils.isFloatEqual(j, centralY)) {
//                    System.out.println((int) canvasX + " " + (int) canvasY);
//                    System.out.println((int) canvasX * TILE_SIZE + " " + (int) canvasY * TILE_SIZE);
//                    gc.setFill(Color.RED);
//                    gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//                } else

                if (map.isCoordinateValid((int) i, (int) j)) {
                    Tile tile = map.findTile((int) i, (int) j);
                    gc.setFill(colors[tile.getTerrainId()]);
                    gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    gc.setFill(Color.GREY);
                    gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

            }
        }
        //render current point
//        gc.setFill(Color.RED);
//        float baseX = Math.max(centralX - MAP_WIDTH, 0);
//        float baseY = Math.max(centralY - MAP_HEIGHT, 0);
//        float baseX = centralX - MAP_WIDTH;
//        float baseY = centralY - MAP_HEIGHT;
//        float canvasX = Math.min(centralX - baseX, map.getX_longitude());
//        float canvasY = Math.min(centralY - baseY, map.getY_latitude());
        float baseX = centralX - MAP_WIDTH;
        float baseY = centralY - MAP_HEIGHT;
        float canvasX = Math.min(highlightX - baseX, map.getX_longitude());
        float canvasY = Math.min(highlightY - baseY, map.getY_latitude());
//        gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        //render highlight
        //render current position
        // System.out.println(canvasX + " " + canvasY);
        // System.out.println(highlightX + " " + highlightY);
        gc.setFill(Color.WHITE);

        gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//        float baseX = Math.max(centralX - MAP_WIDTH, 0);
//        float baseY = Math.max(centralY - MAP_HEIGHT, 0);
//        float canvasX = Math.min(highlight_x - baseX, map.getX_longitude());
//        float canvasY = Math.min(highlight_y - baseY, map.getY_latitude());=
//        if (MathUtils.isFloatEqual(centralX, highlight_x) && MathUtils.isFloatEqual(centralY, highlight_y)) {
//            //render current point
//            gc.setFill(Color.RED);
//            gc.fillRect(canvasX * TILE_SIZE + BORDER_SIZE, canvasY * TILE_SIZE + BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE);
//        } else {
//            //render sel nya
        Tile tile = map.findTile((int) highlightX, (int) highlightY);
        gc.setFill(colors[tile.getTerrainId()]);
        gc.fillRect(canvasX * TILE_SIZE + BORDER_SIZE, canvasY * TILE_SIZE + BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE);

//        }
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
                // System.out.println(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onKeyPressed(KeyEvent event) {
        int keyId = event.getCode().getCode();
        System.out.println("Limit x: " + String.valueOf(centralX - MAP_WIDTH) + " -> " + String.valueOf(centralX + MAP_WIDTH));
        System.out.println("Limit y: " + String.valueOf(centralY - MAP_HEIGHT) + " -> " + String.valueOf(centralY + MAP_HEIGHT));
        switch (keyId) {
            case KeyboardConst.KEY_NORTH -> {
                if (highlightY > 0 || centralY > 0) {
                    // cek apakah masih di dalam bound, kalau diluar itu, geser centralY
                    if (highlightY > centralY - MAP_HEIGHT && highlightY > 0) {
                        highlightY -= KEYBOARD_MOVE_GRADIENT;
                    } else if (centralY > 0) {
                        centralY -= KEYBOARD_MOVE_GRADIENT;
                        if (highlightY > centralY - MAP_HEIGHT && highlightY > 0) {
                            highlightY -= KEYBOARD_MOVE_GRADIENT;
                        }
                    }
                }
            }
            case KeyboardConst.KEY_SOUTH -> {
                if (highlightY < map.getY_latitude() - 1 || centralY < map.getY_latitude() - 1) {
                    // cek apakah masih di dalam bound, kalau diluar itu, geser centralY
                    if (highlightY < centralY + MAP_HEIGHT && highlightY < map.getY_latitude() - 1) {
                        highlightY += KEYBOARD_MOVE_GRADIENT;
                    } else if (centralY < map.getY_latitude() - 1) {
                        centralY += KEYBOARD_MOVE_GRADIENT;
                        if (highlightY < centralY + MAP_HEIGHT && highlightY < map.getY_latitude() - 1) {
                            highlightY += KEYBOARD_MOVE_GRADIENT;
                        }
                    }
                }
            }
            case KeyboardConst.KEY_EAST -> {
                if (highlightX < map.getX_longitude() - 1 || centralX < map.getX_longitude() - 1) {
                    // cek apakah masih di dalam bound, kalau diluar itu, geser centralY
                    if (highlightX < centralX + MAP_WIDTH && highlightX < map.getX_longitude() - 1) {
                        highlightX += KEYBOARD_MOVE_GRADIENT;
                    } else if (centralX < map.getX_longitude() - 1) {
                        centralX += KEYBOARD_MOVE_GRADIENT;
                        if (highlightX < centralX + MAP_WIDTH && highlightX < map.getX_longitude() - 1) {
                            highlightX += KEYBOARD_MOVE_GRADIENT;
                        }
                    }
                }
            }
            case KeyboardConst.KEY_WEST -> {
                // System.out.println(centralX);
                if (highlightX > 0 || centralX > 0) {
                    // cek apakah masih di dalam bound, kalau diluar itu, geser centralY
                    if (highlightX > centralX - MAP_WIDTH && highlightX > 0) {
                        highlightX -= KEYBOARD_MOVE_GRADIENT;
                    } else if (centralX > 0) {
                        centralX -= KEYBOARD_MOVE_GRADIENT;
                        if (highlightX > centralX - MAP_WIDTH && highlightX > 0) {
                            highlightX -= KEYBOARD_MOVE_GRADIENT;
                        }
                    }
                }
            }
            default -> {
                return;
            }
        }
        System.out.println("central(X,Y): " + centralX + " " + centralY + " , highlight(X,Y): " + highlightX + " " + highlightY);
//        highlightX = centralX;
//        highlightY = centralY;
        drawCanvas(KEYBOARD_MOVE_GRADIENT);
    }

    public void onMouseMoved(MouseEvent event) {
//        System.out.println("X:" + event.getX() + " " + event.getSceneX() + " " + event.getScreenX());
//        System.out.println("Y:" + event.getY() + " " + event.getSceneY() + " " + event.getScreenY());
//        System.out.println(game.getLayoutX() + " " + game.getLayoutY());
//        float baseX = Math.max(centralX - MAP_WIDTH, 0);
//        float baseY = Math.max(centralY - MAP_HEIGHT, 0);
//        float canvasX = Math.min(centralX - baseX, map.getX_longitude());
//        float canvasY = Math.min(centralY - baseY, map.getY_latitude());
        float x = (float) event.getX() / TILE_SIZE;
        float y = (float) event.getY() / TILE_SIZE;
        float baseX = centralX - MAP_WIDTH;
        float baseY = centralY - MAP_HEIGHT;
        float screenX = baseX + (float) Math.floor(x);
        float screenY = baseY + (float) Math.floor(y);
        if (map.isCoordinateValid((int) screenX, (int) screenY)) {
            highlightX = screenX;
            highlightY = screenY;
            //        Cari tau posisi
//        Tile tile = map.findTile((int) x, (int) y);
            drawCanvas(MOUSE_MOVE_GRADIENT);
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
                    drawCanvas(KEYBOARD_MOVE_GRADIENT);
                    //init key listener
                    Scene scene = SceneManager.getSceneFromNode(game);
                    scene.setOnKeyPressed(this::onKeyPressed);
                    scene.setOnMouseMoved(this::onMouseMoved);
                }
            }
        });
        updateLayout();
    }

    @Override
    public void refreshLayout() {
        updateLayout();
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
        MAP_WIDTH = (int) Math.ceil(((GraphicsConst.windowWidth / TILE_SIZE) - 1) / 2);
        MAP_HEIGHT = (int) Math.ceil(((GraphicsConst.windowHeight / TILE_SIZE) - 1) / 2);
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
