package com.tbsg.turnbasedstrategygame.library.graphics;

import javafx.stage.Stage;

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
}
