package com.tbsg.turnbasedstrategygame.library.graphics;

import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    static Map<String, Scene> sceneMap;

    //    public SceneManager(){
//        sceneMap = new HashMap<>();
//    }

    private SceneManager() {
    }

    public static void addScene(String name, Scene scene) {
        if (SceneManager.sceneMap == null) {
            SceneManager.sceneMap = new HashMap<>();
        }
        SceneManager.sceneMap.put(name, scene);
    }

    public static Scene getScene(String name) {
        if (SceneManager.sceneMap == null) {
            return null;
        }
        return SceneManager.sceneMap.get(name);
    }

}
