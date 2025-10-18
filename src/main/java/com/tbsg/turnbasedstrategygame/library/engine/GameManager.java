package com.tbsg.turnbasedstrategygame.library.engine;

public class GameManager {

    TurnManager turnManager;
    MapObject map;

    static GameManager instance;

    private GameManager(TurnManager turnManager) {
        this.turnManager = turnManager;
        // this.map = map;
    }

    public static void initGameManager(TurnManager turnManager) {
        if (instance == null) {
            instance = new GameManager(turnManager);
        }
    }

    public static GameManager getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public MapObject getMap() {
        return map;
    }

    public void setMap(MapObject map){
        this.map = map;
    }

}
