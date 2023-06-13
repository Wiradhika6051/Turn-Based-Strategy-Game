package com.tbsg.turnbasedstrategygame.library.graphics;

import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    Map<String, Scene> sceneMap;

    public SceneManager(){
        sceneMap = new HashMap<>();
    }
    public void addScene(String name, Scene scene) {
        sceneMap.put(name,scene);
    }
    public Scene getScene(String name){
        return sceneMap.get(name);
    }

}
