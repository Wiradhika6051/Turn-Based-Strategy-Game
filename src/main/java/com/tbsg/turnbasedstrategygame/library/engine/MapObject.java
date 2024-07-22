package com.tbsg.turnbasedstrategygame.library.engine;

import java.util.ArrayList;
import java.util.List;

public class MapObject {
    List<Tile> tiles;

    public MapObject() {
        this.tiles = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        this.tiles.add(tile);
    }

    public Tile findTile(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.x == x && tile.y == y) {
                return tile;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (Tile tile : tiles) {
            buffer.append(tile.toString());
        }
        buffer.append("]");
        return buffer.toString();
    }
}
