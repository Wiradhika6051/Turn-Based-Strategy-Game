package com.tbsg.turnbasedstrategygame.library.graphics;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class TileTextureManager {
    Map<Integer, Image> tileTextureMap;
    Image defaultTileTexture;

    public TileTextureManager(){
        this.tileTextureMap = new HashMap<>();
        this.defaultTileTexture = new Image(getClass().getResourceAsStream("@textures/tiles/default.png"));
    }

    public Image getTileTexture(int terrainId){
        return tileTextureMap.getOrDefault(terrainId, this.defaultTileTexture);
    }
    
    public void addTileTexture(int terrainId, Image texture){
        this.tileTextureMap.put(terrainId, texture);
    }
}