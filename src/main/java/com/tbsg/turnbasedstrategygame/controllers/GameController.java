package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.engine.GameManager;
import com.tbsg.turnbasedstrategygame.library.engine.MapObject;
import com.tbsg.turnbasedstrategygame.library.engine.Tile;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GameController implements Initializable {

    @FXML
    private Canvas game;

    private GraphicsContext gc;

    @FXML
    BorderPane root;

    MapObject map;

    Color[] colors = {Color.GREEN, Color.BLUE};

    float centralX = 0;
    float centralY = 0;

    final int TILE_SIZE = 100;
    final float KEYBOARD_MOVE_GRADIENT = 1f;
    final int MAP_WIDTH = (int) Math.ceil(((GraphicsConst.windowWidth / TILE_SIZE) - 1) / 2);
    final int MAP_HEIGHT = (int) Math.ceil(((GraphicsConst.windowHeight / TILE_SIZE) - 1) / 2);

    void setStartingPoint() {
        centralX = 0;
        centralY = 0;
        Random random = new Random();
        while (map.findTile((int) centralX, (int) centralY).getTerrainId() != 0) {
            centralX = Math.abs(random.nextInt()) % map.getX_longitude();
            centralY = Math.abs(random.nextInt()) % map.getY_lattitude();
        }
    }

    private void drawCanvas(float gradient) {
//        System.out.println(MAP_WIDTH + " " + MAP_HEIGHT);
        final float RIGHT_IDX = Math.max(centralX - MAP_WIDTH, 0) + 2 * MAP_WIDTH + 1;//batas astas kudu + 1
        final float BOTTOM_IDX = Math.max(centralY - MAP_HEIGHT, 0) + 2 * MAP_HEIGHT + 1;
//        System.out.println(RIGHT_IDX + " " + BOTTOM_IDX);
        for (float j = centralY - MAP_HEIGHT; j < BOTTOM_IDX; j += gradient) {
            for (float i = centralX - MAP_WIDTH; i < RIGHT_IDX; i += gradient) {
//                System.out.println(i + " " + j);
//                System.out.println(i - central_x - 3 + " " + (j - central_y - 3));
                float baseX = Math.max(centralX - MAP_WIDTH, 0);
                float baseY = Math.max(centralY - MAP_HEIGHT, 0);
                float canvasX = Math.min(i - baseX, map.getX_longitude());
                float canvasY = Math.min(j - baseY, map.getY_lattitude());
//                if (MathUtils.isFloatEqual(i, centralX) && MathUtils.isFloatEqual(j, centralY)) {
//                    System.out.println((int) canvasX + " " + (int) canvasY);
//                    System.out.println((int) canvasX * TILE_SIZE + " " + (int) canvasY * TILE_SIZE);
//                    gc.setFill(Color.RED);
//                    gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//                } else

                if (i >= 0 && i < map.getX_longitude() && j >= 0 && j < map.getY_lattitude()) {
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
        gc.setFill(Color.RED);
        float baseX = Math.max(centralX - MAP_WIDTH, 0);
        float baseY = Math.max(centralY - MAP_HEIGHT, 0);
        float canvasX = Math.min(centralX - baseX, map.getX_longitude());
        float canvasY = Math.min(centralY - baseY, map.getY_lattitude());
        gc.fillRect(canvasX * TILE_SIZE, canvasY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }


    public void generateMap() {
        try {
            URI mapPath = getClass().getResource(GraphicsConst.MAP_FOLDER + "default.txt").toURI();
            File f = new File(mapPath);
            Scanner scanner = new Scanner(f);
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
                    // baca ukuran
                    String[] size = row.split(" ");
                    width = Integer.parseInt(size[0]);
                    height = Integer.parseInt(size[1]);
                    map = GameManager.getInstance().getMap();
                    map.setX_longitude(width);
                    map.setY_lattitude(height);
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
            System.out.println(map);
        } catch (URISyntaxException e) {
            System.err.println("Invalid Path!");
        } catch (FileNotFoundException e) {
            System.err.println("Map File not found!");
        }
    }

    public void onKeyPressed(KeyEvent event) {
        int keyId = event.getCode().getCode();
        switch (keyId) {
            case KeyboardConst.KEY_NORTH:
                if (centralY > 0) {
                    centralY -= KEYBOARD_MOVE_GRADIENT;
                }
                break;
            case KeyboardConst.KEY_SOUTH:
                if (centralY < map.getY_lattitude() - 1) {
                    centralY += KEYBOARD_MOVE_GRADIENT;
                }
                break;
            case KeyboardConst.KEY_EAST:
                if (centralX < map.getX_longitude() - 1) {
                    centralX += KEYBOARD_MOVE_GRADIENT;
                }
                break;
            case KeyboardConst.KEY_WEST:
                if (centralX > 0) {
                    centralX -= KEYBOARD_MOVE_GRADIENT;
                }
                break;
            default:
                return;
        }
        drawCanvas(KEYBOARD_MOVE_GRADIENT);
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
                }
            }
        });
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
    }

}
