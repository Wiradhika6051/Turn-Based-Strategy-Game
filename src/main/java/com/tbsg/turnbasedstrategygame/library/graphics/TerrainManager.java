package com.tbsg.turnbasedstrategygame.library.graphics;

import java.util.HashMap;
import java.util.Map;

import com.tbsg.turnbasedstrategygame.library.engine.Terrain;

public class TerrainManager {
    Map<Integer, Terrain> terrainsMap;
    Terrain defaultTerrain;

    private static TerrainManager instance = null;

    private TerrainManager(){
        this.terrainsMap = new HashMap<>();
        this.defaultTerrain = new Terrain(-1,"unknown","default.png",false);
        this.defaultTerrain.loadTexture();
    }

    public static TerrainManager getInstance(){
        if(instance==null){
            instance = new TerrainManager();
        }
        return instance;
    }

    public Terrain getTerrain(int terrainId){
        return terrainsMap.getOrDefault(terrainId, this.defaultTerrain);
    }
    
    public void addTerrain(int terrainId, Terrain terrain){
        this.terrainsMap.put(terrainId, terrain);
    }
}