package com.tbsg.turnbasedstrategygame.library.graphics;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import com.tbsg.turnbasedstrategygame.TurnBasedStrategyGame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

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

    public static void addSceneChangeListener(Stage stage, Scene targetScene, Runnable callback) {
        stage.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
                if (newScene == targetScene) {
                    callback.run();
                }
            }
        });
    }

    public static Scene getSceneFromNode(Node node) {
        if (node == null) {
            return null;
        }
        return node.getScene();
    }

    public static Map<String, Scene> getAllScenes() {
        return sceneMap;
    }

    public static void reloadScene(String name, double width, double height) throws IOException {
        // Get original FXML file path from your mapping (you can store it somewhere)
        String fxmlPath = name.toLowerCase().replace('_', '-') + ".fxml";
        FXMLLoader loader = new FXMLLoader(TurnBasedStrategyGame.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        scene.setUserData(loader.getController());
        addScene(name, scene);
    }
}
