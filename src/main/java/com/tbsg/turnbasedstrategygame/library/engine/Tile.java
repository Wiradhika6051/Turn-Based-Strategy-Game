package com.tbsg.turnbasedstrategygame.library.engine;

public class Tile {
    int x;
    int y;
    int terrainId;
    byte coastMap; // a byte is 8 bit, bit 1-4 in order for north, east, south west. bit 5-8 in order for NE, SE, SW, NW

    public Tile(int x, int y, int terrainId) {
        this.x = x;
        this.y = y;
        this.terrainId = terrainId;
        this.coastMap = 0x0;
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

    public void setCoastMap(byte coastMap) {
        this.coastMap = coastMap;
    }

    public byte getCoastMap() {
        return coastMap;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
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
