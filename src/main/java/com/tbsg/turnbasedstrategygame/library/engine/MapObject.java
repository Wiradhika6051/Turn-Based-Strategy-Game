package com.tbsg.turnbasedstrategygame.library.engine;

import java.util.ArrayList;
import java.util.List;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;

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

    public void initializeCoastMap(){
        for(int i=0;i<x_longitude;i++){
            for(int j=0;j<y_latitude;j++){
                byte coastMap = 0x0;
                Tile tile = findTile(i, j);
                // kalau dia water skip
                if(tile.getTerrainId()==1){
                    continue;
                }
                // cek utara
                if(j>0 && findTile(i, j-1).getTerrainId()==1){
                    coastMap |= GraphicsConst.NORTH_COAST;
                }
                // cek timur
                if(i<x_longitude-1 && findTile(i+1, j).getTerrainId()==1){
                    coastMap |= GraphicsConst.EAST_COAST;
                }
                // cek selatan
                if(j<y_latitude-1 && findTile(i, j+1).getTerrainId()==1){
                    coastMap |= GraphicsConst.SOUTH_COAST;
                }
                // cek barat
                if(i>0 && findTile(i-1, j).getTerrainId()==1){
                    coastMap |= GraphicsConst.WEST_COAST;
                }
                // cek NE
                if(i<x_longitude-1 && j>0 && findTile(i+1, j-1).getTerrainId()==1){
                    coastMap |= GraphicsConst.NORTH_EAST_COAST;
                }
                // cek SE
                if(i<x_longitude-1 && j<y_latitude-1 && findTile(i+1, j+1).getTerrainId()==1){
                    coastMap |= GraphicsConst.SOUTH_EAST_COAST;
                }
                // cek SW
                if(i>0 && j<y_latitude-1 && findTile(i-1, j+1).getTerrainId()==1){
                    coastMap |= GraphicsConst.SOUTH_WEST_COAST;
                }
                // cek NW
                if(i>0 && j>0 && findTile(i-1, j-1).getTerrainId()==1){
                    coastMap |= GraphicsConst.NORTH_WEST_COAST;
                }
                tile.setCoastMap(coastMap);
            }
        }
    }
}
