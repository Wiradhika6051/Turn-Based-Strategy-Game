package com.tbsg.turnbasedstrategygame.controllers;

import com.tbsg.turnbasedstrategygame.library.engine.GameManager;
import com.tbsg.turnbasedstrategygame.library.engine.MapObject;
import com.tbsg.turnbasedstrategygame.library.engine.Tile;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    @FXML
    private void drawCanvas() {
//        Random rand = new Random();
//        int i = 0;
//        int j = 0;
//        while (j * 100 <= game.getHeight()) {
////            System.out.println(game.getWidth());
//            if (i * 100 > game.getWidth()) {
//                i = 0;
//                j++;
//            }
//            gc.setFill(colors[rand.nextInt(colors.length)]);
//            gc.fillRect(100 * i, 100 * j, 100, 100);
//            i++;
//        }
        // Cari titik tengah
        int startX = 0;
        int startY = 0;
        Random random = new Random();
        while (map.findTile(startX, startY).getTerrainId() != 0) {
            startX = Math.abs(random.nextInt()) % map.getX_longitude();
            startY = Math.abs(random.nextInt()) % map.getY_lattitude();
        }
//        startX = map.getX_longitude() - 1;
//        startY = map.getY_lattitude() - 1;
        System.out.println(startX);
        System.out.println(startY);

        final int MAP_SIZE = 4;
//        System.out.println(startX - MAP_SIZE);
//        System.out.println(startY - MAP_SIZE);
        for (int j = startY - MAP_SIZE; j < startY + MAP_SIZE + 1; j++) {
            for (int i = startX - MAP_SIZE; i < startX + MAP_SIZE + 1; i++) {
//                System.out.println(i + " " + j);
//                System.out.println(i - central_x - 3 + " " + (j - central_y - 3));
                if (i >= 0 && i < map.getX_longitude() && j >= 0 && j < map.getY_lattitude()) {
                    Tile tile = map.findTile(i, j);
                    gc.setFill(colors[tile.getTerrainId()]);
                    int baseX = Math.max(startX - MAP_SIZE, 0);
                    int baseY = Math.max(startY - MAP_SIZE, 0);
                    int canvasX = Math.min(i - baseX, map.getX_longitude());
                    int canvasY = Math.min(j - baseY, map.getY_lattitude());
                    gc.fillRect(canvasX * 100, canvasY * 100, 100, 100);
                }
            }
        }
//        for (Tile tile : map.getTiles()) {
//            gc.setFill(colors[tile.getTerrainId()]);
//            gc.fillRect(tile.getX() * 100, tile.getY() * 100, 100, 100);
//        }
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
            System.out.println(e);
        } catch (FileNotFoundException e) {
            System.out.println(e);
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
                    drawCanvas();
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
