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

    double cameraOffsetX = 0; // canvas tile
    double cameraOffsetY = 0; // canvas tile

    private double mouseScrollX = 0;
    private double mouseScrollY = 0;

    private double lastMouseX = -1;
    private double lastMouseY = -1;

    final int TILE_SIZE = 100;
    final double KEYBOARD_MOVE_GRADIENT = 0.1;
    final double MOUSE_MOVE_GRADIENT = 0.1;
    final int BORDER_MARGIN = 50;
    final double EDGE_SCROLL_THRESHOLD = 30; // px distance from edge

    final int BORDER_SIZE = 10;
    int MAP_WIDTH;
    int MAP_HEIGHT;

//    double highlight_x = 0;
//    double highlight_y = 0;
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
        cameraOffsetX = centralX * TILE_SIZE;
        cameraOffsetY = centralY * TILE_SIZE;
    }

    private void draw1Canvas() {
        double canvasWidth = game.getWidth();
        double canvasHeight = game.getHeight();

        // figure out which tiles are visible
        int startTileX = (int) Math.floor(cameraOffsetX / TILE_SIZE);
        int startTileY = (int) Math.floor(cameraOffsetY / TILE_SIZE);
        int endTileX = (int) Math.ceil((cameraOffsetX + canvasWidth) / TILE_SIZE);
        int endTileY = (int) Math.ceil((cameraOffsetY + canvasHeight) / TILE_SIZE);

        for (int j = startTileY; j < endTileY; j++) {
            for (int i = startTileX; i < endTileX; i++) {
                // world pixel position of this tile
                double baseX = centralX - MAP_WIDTH;
                double baseY = centralY - MAP_HEIGHT;
                double worldX = Math.min(i - baseX, map.getX_longitude()) * TILE_SIZE;
                double worldY = Math.min(j - baseY, map.getY_latitude()) * TILE_SIZE;

                // screen position = world - camera
                double screenX = worldX - cameraOffsetX;
                double screenY = worldY - cameraOffsetY;
                if (!map.isCoordinateValid(i, j)) {
                    gc.setFill(Color.GREY);
                } else {
                    Tile tile = map.findTile(i, j);
                    gc.setFill(colors[tile.getTerrainId()]);

                }
                gc.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw highlight
        double highlightScreenX = highlightX * TILE_SIZE - cameraOffsetX;
        double highlightScreenY = highlightY * TILE_SIZE - cameraOffsetY;
        System.out.println(String.format("HIglight(x,y) %f,%f", highlightScreenX, highlightScreenY));

        gc.setFill(Color.WHITE);
        gc.fillRect(highlightScreenX, highlightScreenY, TILE_SIZE, TILE_SIZE);

        Tile tile = map.findTile((int) highlightX, (int) highlightY);
        gc.setFill(colors[tile.getTerrainId()]);
        gc.fillRect(highlightScreenX + BORDER_SIZE, highlightScreenY + BORDER_SIZE,
                TILE_SIZE - 2 * BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE);
    }

    private void drawCanvas() {

        int rows = map.getY_latitude(); // how many tiles fit vertically
        int cols = map.getX_longitude();  // how many tiles fit horizontally

        double centerScreenX = GraphicsConst.windowWidth / 2.0;
        double centerScreenY = GraphicsConst.windowHeight / 2.0;
        System.out.println(String.format("row,cosl: %d %d", rows, cols));

        gc.setFill(Color.BLUE);
        for (int y = (int) centralY - MAP_HEIGHT - 1; y <= (int) centralY + MAP_HEIGHT + 1; y++) {
            for (int x = (int) centralX - MAP_WIDTH - 1; x <= (int) centralX + MAP_WIDTH + 1; x++) {
                // Compute tile position in pixels
                // double px = x * TILE_SIZE + offsetX;
                // double py = y * TILE_SIZE + offsetY;
                double dx = x - centralX;
                double dy = y - centralY;

                // convert to pixels
                double px = centerScreenX + dx * TILE_SIZE;
                double py = centerScreenY + dy * TILE_SIZE;
                // Optional: skip tiles completely outside the screen (faster)
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
        // Optional: skip tiles completely outside the screen (faster)
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

    private void ori_drawCanvas() {
//        System.out.println(MAP_WIDTH + " " + MAP_HEIGHT);
//        final double RIGHT_IDX = Math.max(centralX - MAP_WIDTH, 0) + 2 * MAP_WIDTH + 1;//batas atas kudu + 1
//        final double BOTTOM_IDX = Math.max(centralY - MAP_HEIGHT, 0) + 2 * MAP_HEIGHT + 1;
        final double RIGHT_IDX = centralX - MAP_WIDTH + 2 * MAP_WIDTH + 1;//batas atas kudu + 1
        final double BOTTOM_IDX = centralY - MAP_HEIGHT + 2 * MAP_HEIGHT + 1;
        double baseX = centralX - MAP_WIDTH;
        double baseY = centralY - MAP_HEIGHT;
//        System.out.println(RIGHT_IDX + " " + BOTTOM_IDX);
        for (double j = centralY - MAP_HEIGHT; j < BOTTOM_IDX; j++) {
            for (double i = centralX - MAP_WIDTH; i < RIGHT_IDX; i++) {
//                System.out.println(i + " " + j);
//                System.out.println(i - central_x - 3 + " " + (j - central_y - 3));
//                double baseX = Math.max(centralX - MAP_WIDTH, 0);
//                double baseY = Math.max(centralY - MAP_HEIGHT, 0);

                // System.out.println("BASE (X,Y):");
                // System.out.print(baseX);
                // System.out.print(baseY);
                // double canvasX = Math.min(i - baseX, map.getX_longitude()); //clamp to make sure it >= 0
                // double canvasY = Math.min(j - baseY, map.getY_latitude()); //clamp to make sure it >= 0
                double canvasX = i; //clamp to make sure it >= 0
                double canvasY = j; //clamp to make sure it >= 0

                if (map.isCoordinateValid((int) i, (int) j)) {
                    Tile tile = map.findTile((int) i, (int) j);
                    gc.setFill(colors[tile.getTerrainId()]);
                    gc.fillRect(canvasX * TILE_SIZE - cameraOffsetX, canvasY * TILE_SIZE - cameraOffsetY, TILE_SIZE, TILE_SIZE);
                } else {
                    gc.setFill(Color.GREY);
                    gc.fillRect(canvasX * TILE_SIZE - cameraOffsetX, canvasY * TILE_SIZE - cameraOffsetY, TILE_SIZE, TILE_SIZE);
                }

            }
        }
        // double baseX = centralX - MAP_WIDTH;
        // double baseY = centralY - MAP_HEIGHT;
        // double canvasX = Math.max(0,Math.min(highlightX - baseX, map.getX_longitude()));
        // double canvasY = Math.max(0,Math.min(highlightY - baseY, map.getY_latitude()));
        // double canvasX = highlightX - baseX;
        // double canvasY = highlightY - baseY;
        double canvasX = highlightX;
        double canvasY = highlightY;
        //render highlight
        //render current position
        // System.out.println(canvasX + " " + canvasY);
        // System.out.println(highlightX + " " + highlightY);
        gc.setFill(Color.WHITE);

        gc.fillRect(canvasX * TILE_SIZE - cameraOffsetX, canvasY * TILE_SIZE - cameraOffsetY, TILE_SIZE, TILE_SIZE);
//        double baseX = Math.max(centralX - MAP_WIDTH, 0);
//        double baseY = Math.max(centralY - MAP_HEIGHT, 0);
//        double canvasX = Math.min(highlight_x - baseX, map.getX_longitude());
//        double canvasY = Math.min(highlight_y - baseY, map.getY_latitude());=
//        if (MathUtils.isdoubleEqual(centralX, highlight_x) && MathUtils.isdoubleEqual(centralY, highlight_y)) {
//            //render current point
//            gc.setFill(Color.RED);
//            gc.fillRect(canvasX * TILE_SIZE + BORDER_SIZE, canvasY * TILE_SIZE + BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE);
//        } else {
//            //render sel nya
        Tile tile = map.findTile((int) highlightX, (int) highlightY);
        gc.setFill(colors[tile.getTerrainId()]);
        gc.fillRect(canvasX * TILE_SIZE - cameraOffsetX + BORDER_SIZE, canvasY * TILE_SIZE - cameraOffsetY + BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE, TILE_SIZE - 2 * BORDER_SIZE);

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

    public void onKey1Pressed(KeyEvent event) {
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
        drawCanvas();
    }

    private void onKeyPressed(KeyEvent event) {
        int keyId = event.getCode().getCode();
        switch (keyId) {
            case KeyboardConst.KEY_NORTH -> {
                // if (centralY > 0) {
                //     centralY--;
                // }
                moveUp = true;
            }
            case KeyboardConst.KEY_SOUTH -> {
                // if (centralY < map.getY_latitude() - 1) {
                //     centralY++;
                // }
                moveDown = true;
            }

            case KeyboardConst.KEY_WEST -> {
                // if (centralX > 0) {
                //     centralX--;
                // }
                moveLeft = true;
            }
            case KeyboardConst.KEY_EAST -> {
                // if (centralX < map.getX_longitude() - 1) {
                //     centralX++;
                // }
                moveRight = true;
            }

            default -> {
            }
        }

        // Re-render after movement
        // drawCanvas();
    }

    private void onKeyReleased(KeyEvent event) {
        int keyId = event.getCode().getCode();
        switch (keyId) {
            case KeyboardConst.KEY_NORTH -> {
                // if (centralY > 0) {
                //     centralY--;
                // }
                moveUp = false;
            }
            case KeyboardConst.KEY_SOUTH -> {
                // if (centralY < map.getY_latitude() - 1) {
                //     centralY++;
                // }
                moveDown = false;
            }

            case KeyboardConst.KEY_WEST -> {
                // if (centralX > 0) {
                //     centralX--;
                // }
                moveLeft = false;
            }
            case KeyboardConst.KEY_EAST -> {
                // if (centralX < map.getX_longitude() - 1) {
                //     centralX++;
                // }
                moveRight = false;
            }

            default -> {
            }
        }

        // Re-render after movement
        // drawCanvas();
    }

    public void onKey11Pressed(KeyEvent event) {
        int keyId = event.getCode().getCode();
        System.out.println("Limit x: " + (centralX - MAP_WIDTH) + " -> " + (centralX + MAP_WIDTH));
        System.out.println("Limit y: " + (centralY - MAP_HEIGHT) + " -> " + (centralY + MAP_HEIGHT));

        switch (keyId) {
            case KeyboardConst.KEY_NORTH -> {
                if (highlightY > 0 || centralY > 0) {
                    if (highlightY > centralY - MAP_HEIGHT && highlightY > 0) {
                        // Move highlight up
                        highlightY -= KEYBOARD_MOVE_GRADIENT;
                    } else if (centralY > 0) {
                        // Move camera up
                        cameraOffsetY -= MOUSE_MOVE_GRADIENT * TILE_SIZE;
                        centralY -= KEYBOARD_MOVE_GRADIENT;
                    }
                }
            }
            case KeyboardConst.KEY_SOUTH -> {
                if (highlightY < map.getY_latitude() - 1 || centralY < map.getY_latitude() - 1) {
                    if (highlightY < centralY + MAP_HEIGHT && highlightY < map.getY_latitude() - 1) {
                        // Move highlight down
                        highlightY += KEYBOARD_MOVE_GRADIENT;
                    } else if (centralY < map.getY_latitude() - 1) {
                        // Move camera down
                        cameraOffsetY += MOUSE_MOVE_GRADIENT * TILE_SIZE;
                        centralY += KEYBOARD_MOVE_GRADIENT;
                    }
                }
            }
            case KeyboardConst.KEY_EAST -> {
                if (highlightX < map.getX_longitude() - 1 || centralX < map.getX_longitude() - 1) {
                    if (highlightX < centralX + MAP_WIDTH && highlightX < map.getX_longitude() - 1) {
                        // Move highlight right
                        highlightX += KEYBOARD_MOVE_GRADIENT;
                    } else if (centralX < map.getX_longitude() - 1) {
                        // Move camera right
                        cameraOffsetX += MOUSE_MOVE_GRADIENT * TILE_SIZE;
                        centralX += KEYBOARD_MOVE_GRADIENT;
                    }
                }
            }
            case KeyboardConst.KEY_WEST -> {
                if (highlightX > 0 || centralX > 0) {
                    if (highlightX > centralX - MAP_WIDTH && highlightX > 0) {
                        // Move highlight left
                        highlightX -= KEYBOARD_MOVE_GRADIENT;
                    } else if (centralX > 0) {
                        // Move camera left
                        cameraOffsetX -= MOUSE_MOVE_GRADIENT * TILE_SIZE;
                        centralX -= KEYBOARD_MOVE_GRADIENT;
                    }
                }
            }
            default -> {
                return;
            }
        }

        System.out.println("central(X,Y): " + centralX + " " + centralY
                + " , highlight(X,Y): " + highlightX + " " + highlightY);
        drawCanvas();
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
        // // Apply scroll
        // centralX += scrollX;
        // centralY += scrollY;

        // // Clamp inside map
        // centralX = Math.max(0, Math.min(centralX, map.getX_longitude() - 1));
        // centralY = Math.max(0, Math.min(centralY, map.getY_latitude() - 1));
    }

    public void old_onMouseMoved(MouseEvent event) {
        double tileX = (double) event.getX() / TILE_SIZE;
        double tileY = (double) event.getY() / TILE_SIZE;
        double baseX = centralX - MAP_WIDTH;
        double baseY = centralY - MAP_HEIGHT;
        double screenX = baseX + (double) Math.floor(tileX);
        double screenY = baseY + (double) Math.floor(tileY);
        // double screenX = baseX + tileX;
        // double screenY = baseY + tileY;

        // Update highlight if coordinate valid
        if (map.isCoordinateValid((int) screenX, (int) screenY)) {
            highlightX = screenX;
            highlightY = screenY;
            //        Cari tau posisi
//        Tile tile = map.findTile((int) x, (int) y);
            // drawCanvas(MOUSE_MOVE_GRADIENT);
        }
        // Auto scroll
        double canvasWidth = game.getWidth();
        double canvasHeight = game.getHeight();

        // geser horizontal
        if (event.getX() < BORDER_MARGIN && centralX > 0) {
            // centralX -= (double)Math.floor(MOUSE_MOVE_GRADIENT);
            cameraOffsetX -= MOUSE_MOVE_GRADIENT * TILE_SIZE;
        } else if (event.getX() > canvasWidth - BORDER_MARGIN && centralX < map.getX_longitude() - 1) {
            // centralX +=  (double)Math.floor(MOUSE_MOVE_GRADIENT);
            cameraOffsetX += MOUSE_MOVE_GRADIENT * TILE_SIZE;
        }
        // geser vertikal
        if (event.getY() < BORDER_MARGIN && centralY > 0) {
            // centralY -=  (double)Math.floor(MOUSE_MOVE_GRADIENT);
            cameraOffsetY -= MOUSE_MOVE_GRADIENT * TILE_SIZE;
        } else if (event.getY() > canvasHeight - BORDER_MARGIN && centralY < map.getY_latitude() - 1) {
            // centralY +=  (double)Math.floor(MOUSE_MOVE_GRADIENT);
            cameraOffsetY += MOUSE_MOVE_GRADIENT * TILE_SIZE;
        }
        // Redraw
        System.out.println(String.format("Central (X,Y): %f %f, Highlight(X,Y): %f %f", centralX, centralY, highlightX, highlightY));
        drawCanvas();
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

    private void startGameLoop() {
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

    private void updateCamera() {
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
