package com.tbsg.turnbasedstrategygame.library.engine;

public class GameManager {

    TurnManager turnManager;
    MapObject map;

    static GameManager instance;

    private GameManager(TurnManager turnManager, MapObject map) {
        this.turnManager = turnManager;
        this.map = map;
    }

    public static void initGameManager(TurnManager turnManager, MapObject mapObject) {
        if (instance == null) {
            instance = new GameManager(turnManager, mapObject);
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

}
