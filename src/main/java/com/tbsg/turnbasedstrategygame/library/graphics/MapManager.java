package com.tbsg.turnbasedstrategygame.library.graphics;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import com.tbsg.turnbasedstrategygame.library.engine.GameManager;
import com.tbsg.turnbasedstrategygame.library.engine.MapObject;
import com.tbsg.turnbasedstrategygame.library.engine.Tile;
import com.tbsg.turnbasedstrategygame.library.io.Keyboard;
import com.tbsg.turnbasedstrategygame.library.io.KeyboardConst;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MapManager {

    GraphicsContext gc;
    MapObject map;
    Canvas canvas;

    double centralX; // IDX TILE
    double centralY; // IDX TILE

    double highlightX; // IDX TILE
    double highlightY; // IDX TILE

    int MAP_WIDTH; //in px
    int MAP_HEIGHT; // in px

    double topPadding;

    double mouseScrollX; // in px
    double mouseScrollY; // in px

    double lastMouseX;
    double lastMouseY;

    final int TILE_SIZE = 100;
    final double KEYBOARD_MOVE_GRADIENT = 0.1;
    final double MOUSE_MOVE_GRADIENT = 0.1;
    double EDGE_SCROLL_THRESHOLD = 30; // px distance from edge
    final int BORDER_SIZE = 10;

    boolean moveUp = false;
    boolean moveDown = false;
    boolean moveLeft = false;
    boolean moveRight = false;

    public MapManager(Canvas canvas, double topPadding, int MAP_WIDTH, int MAP_HEIGHT) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.centralX = 0;
        this.centralY = 0;
        this.highlightX = 0;
        this.highlightY = 0;
        this.MAP_WIDTH = MAP_WIDTH;
        this.MAP_HEIGHT = MAP_HEIGHT;
        this.lastMouseX = -1;
        this.lastMouseY = -1;
        this.mouseScrollX = 0;
        this.mouseScrollY = 0;
        this.topPadding = topPadding;
    }

    public void updateGC(GraphicsContext gc) {
        this.gc = gc;
    }

    public void updateMapSize(int MAP_WIDTH, int MAP_HEIGHT) {
        this.MAP_WIDTH = MAP_WIDTH;
        this.MAP_HEIGHT = MAP_HEIGHT;
    }

    public void updateEdgeThreshold(double threshold) {
        // EDGE_SCROLL_THRESHOLD = threshold;
        EDGE_SCROLL_THRESHOLD = 30;
    }

    public void updatePadding(double topPadding) {
        this.topPadding = topPadding;
    }

    @SuppressWarnings("CallToPrintStackTrace")
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
            // generate coast map
            map.initializeCoastMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStartingPoint() {
        Random random = new Random();
        while (map.findTile((int) centralX, (int) centralY).getTerrainId() != 0) {
            centralX = Math.abs(random.nextInt()) % map.getX_longitude();
            centralY = Math.abs(random.nextInt()) % map.getY_latitude();
        }
        highlightX = centralX;
        highlightY = centralY;
    }

    public void drawCanvas() {
        double centerScreenX = GraphicsConst.windowWidth / 2.0;
        double centerScreenY = GraphicsConst.windowHeight / 2.0;

        // gc.setFill(Color.BLUE);
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
                    Image texture = TerrainManager.getInstance().getTerrain(tile.getTerrainId()).getTexture();
                    gc.drawImage(texture, px, py, TILE_SIZE, TILE_SIZE);
                    // render coast
                    // color: #F0DCA0
                    if (tile.getTerrainId() == 0) {
                        renderCoast(tile, px, py);
                    }

                } else {
                    gc.setFill(Color.GREY);
                    gc.fillRect(
                            px, // pixel X
                            py, // pixel Y
                            TILE_SIZE, // width in pixels
                            TILE_SIZE // height in pixels
                    );
                }
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
        // fill isi
        if (map.isCoordinateValid(x, y)) {
            Tile tile = map.findTile(x, y);
            Image texture = TerrainManager.getInstance().getTerrain(tile.getTerrainId()).getTexture();
            gc.drawImage(texture, px, // pixel X
                    py, // pixel Y
                    TILE_SIZE, // width in pixels
                    TILE_SIZE // height in pixels
            );
        } else {
            gc.setFill(Color.GREY);
            gc.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        }
        // fill border
        gc.setFill(Color.WHITE);
        // top border
        gc.fillRect(px, py, TILE_SIZE, BORDER_SIZE);
        // right border
        gc.fillRect(px + TILE_SIZE - BORDER_SIZE, py, BORDER_SIZE, TILE_SIZE);
        // bottom border
        gc.fillRect(px, py + TILE_SIZE - BORDER_SIZE, TILE_SIZE, BORDER_SIZE);
        // left border
        gc.fillRect(px, py, BORDER_SIZE, TILE_SIZE);
    }

    void renderCoast(Tile tile, double px, double py) {
        gc.setFill(Color.web("#F0DCA0"));
        byte coastMap = tile.getCoastMap();
        // render north
        if ((coastMap & GraphicsConst.NORTH_COAST) != 0) {
            gc.fillRect(px, py, TILE_SIZE, BORDER_SIZE);
        }
        // render east
        if ((coastMap & GraphicsConst.EAST_COAST) != 0) {
            gc.fillRect(px + TILE_SIZE - BORDER_SIZE, py, BORDER_SIZE, TILE_SIZE);
        }
        // render south
        if ((coastMap & GraphicsConst.SOUTH_COAST) != 0) {
            gc.fillRect(px, py + TILE_SIZE - BORDER_SIZE, TILE_SIZE, BORDER_SIZE);
        }
        // render west
        if ((coastMap & GraphicsConst.WEST_COAST) != 0) {
            gc.fillRect(px, py, BORDER_SIZE, TILE_SIZE);
        }
        // render NE
        if ((coastMap & GraphicsConst.NORTH_EAST_MASK)==0 && (coastMap & GraphicsConst.NORTH_EAST_COAST) != 0) {
            gc.fillRect(px + TILE_SIZE - BORDER_SIZE, py, BORDER_SIZE, BORDER_SIZE);
        }
        // render SE
        if ((coastMap & GraphicsConst.SOUTH_EAST_MASK)==0 && (coastMap & GraphicsConst.SOUTH_EAST_COAST) != 0) {
            gc.fillRect(px + TILE_SIZE - BORDER_SIZE, py + TILE_SIZE - BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        }
        // render SW
        if ((coastMap & GraphicsConst.SOUTH_WEST_MASK)==0 && (coastMap & GraphicsConst.SOUTH_WEST_COAST) != 0) {
            gc.fillRect(px, py + TILE_SIZE - BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        }
        // render NW
        if ((coastMap & GraphicsConst.NORTH_WEST_MASK)==0 && (coastMap & GraphicsConst.NORTH_WEST_COAST) != 0) {
            gc.fillRect(px, py, BORDER_SIZE, BORDER_SIZE);
        }
    }
//TODO
    // public void onKeyPressed(KeyEvent event) {
    //     int keyId = event.getCode().getCode();
    //     switch (keyId) {
    //         case KeyboardConst.KEY_NORTH -> {
    //             moveUp = true;
    //         }
    //         case KeyboardConst.KEY_SOUTH -> {
    //             moveDown = true;
    //         }
    //         case KeyboardConst.KEY_WEST -> {
    //             moveLeft = true;
    //         }
    //         case KeyboardConst.KEY_EAST -> {
    //             moveRight = true;
    //         }
    //         default -> {
    //         }
    //     }
    // }
//TODO
    // public void onKeyReleased(KeyEvent event) {
    //     int keyId = event.getCode().getCode();
    //     switch (keyId) {
    //         case KeyboardConst.KEY_NORTH -> {
    //             moveUp = false;
    //         }
    //         case KeyboardConst.KEY_SOUTH -> {
    //             moveDown = false;
    //         }
    //         case KeyboardConst.KEY_WEST -> {
    //             moveLeft = false;
    //         }
    //         case KeyboardConst.KEY_EAST -> {
    //             moveRight = false;
    //         }

    //         default -> {
    //         }
    //     }
    // }
//TODO
    public void onMouseMoved(MouseEvent event) {
        // Point2D local = canvas.sceneToLocal(event.getSceneX(), event.getSceneY());
        lastMouseX = event.getX();
        lastMouseY = event.getY();
        Scene scene = SceneManager.getSceneFromNode(canvas);
        // System.out.println(String.format("%f %f %f %f %f %f %f", lastMouseX, lastMouseY, event.getY(), event.getSceneY(), event.getScreenY(), scene.getWidth(), scene.getHeight()));
        // 1. Move highlight to hovered tile
        double adjustedY = lastMouseY - topPadding;
        // System.out.println(gc.getCanvas().getHeight());
        int tileX = (int) Math.floor((lastMouseX - gc.getCanvas().getWidth() / 2.0) / TILE_SIZE + centralX);
        int tileY = (int) Math.floor((adjustedY - gc.getCanvas().getHeight() / 2.0) / TILE_SIZE + centralY);
        // System.err.println(String.format("%f %f %f %f %f %f %f",EDGE_SCROLL_THRESHOLD, adjustedY, gc.getCanvas().getWidth(), gc.getCanvas().getHeight(), lastMouseX,lastMouseY,topPadding));
        highlightX = tileX;
        highlightY = tileY;
    }

    public void updateCamera() {
        // if (moveUp) {
        //     centralY -= KEYBOARD_MOVE_GRADIENT;
        // }
        // if (moveDown) {
        //     centralY += KEYBOARD_MOVE_GRADIENT;
        // }
        // if (moveLeft) {
        //     centralX -= KEYBOARD_MOVE_GRADIENT;
        // }
        // if (moveRight) {
        //     centralX += KEYBOARD_MOVE_GRADIENT;
        // }
        if (Keyboard.getInstance().getKeyStatus(KeyboardConst.KEY_NORTH)) {
            centralY -= KEYBOARD_MOVE_GRADIENT;
        }
        if (Keyboard.getInstance().getKeyStatus(KeyboardConst.KEY_SOUTH)) {
            centralY += KEYBOARD_MOVE_GRADIENT;
        }
        if (Keyboard.getInstance().getKeyStatus(KeyboardConst.KEY_WEST)) {
            centralX -= KEYBOARD_MOVE_GRADIENT;
        }
        if (Keyboard.getInstance().getKeyStatus(KeyboardConst.KEY_EAST)) {
            centralX += KEYBOARD_MOVE_GRADIENT;
        }
        // Mouse edge scrolling
        mouseScrollX = 0;
        mouseScrollY = 0;
        if (lastMouseX >= 0 && lastMouseY >= 0) {
            if (lastMouseX < EDGE_SCROLL_THRESHOLD) {
                // Left edge
                double factor = 1.0 - (lastMouseX / EDGE_SCROLL_THRESHOLD);
                mouseScrollX = -MOUSE_MOVE_GRADIENT * factor;
            } else if (lastMouseX > gc.getCanvas().getWidth() - EDGE_SCROLL_THRESHOLD) {
                // Right edge
                double factor = 1.0 - (gc.getCanvas().getWidth() - lastMouseX) / EDGE_SCROLL_THRESHOLD;
                mouseScrollX = MOUSE_MOVE_GRADIENT * factor;
            }

            if (lastMouseY < EDGE_SCROLL_THRESHOLD) {
                // Top edge
                double factor = 1.0 - (lastMouseY / EDGE_SCROLL_THRESHOLD);
                mouseScrollY = -MOUSE_MOVE_GRADIENT * factor;
            } else if (lastMouseY > gc.getCanvas().getHeight() - EDGE_SCROLL_THRESHOLD) {
                double factor = 1.0 - (gc.getCanvas().getHeight() + topPadding - lastMouseY) / EDGE_SCROLL_THRESHOLD;
                mouseScrollY = MOUSE_MOVE_GRADIENT * factor;
            }
        }
        centralX += mouseScrollX;
        centralY += mouseScrollY;
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

}
