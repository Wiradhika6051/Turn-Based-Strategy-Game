package com.tbsg.turnbasedstrategygame.library.graphics;

import javafx.stage.Screen;
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

    public static double calculateWidth(double factor) {
        if (StageManager.instance == null) {
            return 0.0;
        }
        //only support single screen
//        return scale * (GraphicsConst.screenWidth / GraphicsConst.windowWidth) * GraphicsConst.screenWidth;
//        return scale * (StageManager.getInstance().getWidth() / GraphicsConst.windowWidth) * StageManager.getInstance().getWidth();
        return factor * GraphicsConst.windowWidth;

    }

    public static double calculateHeight(double factor) {
        if (StageManager.instance == null) {
            return 0.0;
        }
        //only support single screen
        return factor * GraphicsConst.windowHeight;
    }
}
