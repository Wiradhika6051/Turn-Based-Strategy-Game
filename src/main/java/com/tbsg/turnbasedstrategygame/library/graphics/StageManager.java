package com.tbsg.turnbasedstrategygame.library.graphics;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StageManager {

    private static Stage instance;

    private StageManager() {
    }

    public static void setInstance(Stage stage) {
        StageManager.instance = stage;
    }

    public static Stage getInstance() {
        return StageManager.instance;
    }

    public static double calculateWidth(double factor) {
        if (StageManager.instance == null) {
            return 0.0;
        }
        //only support single screen
        return factor * GraphicsConst.windowWidth;

    }

    public static double calculateHeight(double factor) {
        if (StageManager.instance == null) {
            return 0.0;
        }
        //only support single screen
        return factor * GraphicsConst.windowHeight;
    }

    public static Stage getStageFromWindow(Node node) {
        if (node == null) {
            return null;
        }
        Scene scene = SceneManager.getSceneFromNode(node);
        if (scene == null) {
            return null;
        }
        Window window = scene.getWindow();
        if (window instanceof Stage stage) {
            return stage;
        }
        return null;
    }

    public static void setScene(Scene scene) {
        Stage stage = StageManager.getInstance();
        stage.setScene(scene);
        Platform.runLater(() -> {
            if (scene.getRoot() instanceof Region region) {
                region.setPrefSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
                region.setMinSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
                region.setMaxSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
            }
            Object controller = scene.getUserData();
            if (controller instanceof RefreshableScene refreshable) {
                refreshable.refreshLayout();
            }
        });
    }

}
