package com.tbsg.turnbasedstrategygame.library.engine;

public class Tile {
    int x;
    int y;
    int terrainId;

    public Tile(int x, int y, int terrainId) {
        this.x = x;
        this.y = y;
        this.terrainId = terrainId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTerrainId() {
        return terrainId;
    }

    public void setTerrainId(int terrainId) {
        this.terrainId = terrainId;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<x=");
        buffer.append(x);
        buffer.append(",y=");
        buffer.append(y);
        buffer.append(",terrainId=");
        buffer.append(terrainId);
        buffer.append(">");
        return buffer.toString();
    }
}
