package com.tbsg.turnbasedstrategygame.library.engine;

import java.util.ArrayList;
import java.util.List;

public class MapObject {
    List<Tile> tiles;

    int x_longitude;//x size
    int y_latitude;//y size

    public MapObject() {
        this.tiles = new ArrayList<>();
        x_longitude = -1;
        y_latitude = -1;
    }

    public void addTile(Tile tile) {
        this.tiles.add(tile);
    }

    public Tile findTile(int x, int y) {
        if (!isCoordinateValid(x, y)) {
            return null;
        }
//        for (Tile tile : tiles) {
////            System.out.println("START");
////            System.out.println(tile);
////            System.out.println(x + " " + y);
//            if (tile.x == x && tile.y == y) {
//                return tile;
//            }
//        }
//        return null;
        int idx = y * x_longitude + x;
        return tiles.get(idx);
    }

    public boolean isCoordinateValid(int x, int y) {
        return x >= 0 && y >= 0 && x < x_longitude && y < y_latitude;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (Tile tile : tiles) {
            buffer.append(tile.toString());
        }
        buffer.append("]");
        return buffer.toString();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public int getX_longitude() {
        return x_longitude;
    }

    public void setX_longitude(int x_longitude) {
        this.x_longitude = x_longitude;
    }

    public int getY_latitude() {
        return y_latitude;
    }

    public void setY_latitude(int y_latitude) {
        this.y_latitude = y_latitude;
    }
}
