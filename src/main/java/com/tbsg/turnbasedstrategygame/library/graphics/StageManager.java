package com.tbsg.turnbasedstrategygame.library.graphics;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StageManager {

    private static Stage instance;
    private static boolean isFullScreen;

    private StageManager() {
        isFullScreen = false;
    }

    public static void setInstance(Stage stage) {
        StageManager.instance = stage;
    }

    public static void toggleFullScreen(boolean isFullScreen) {
        StageManager.isFullScreen = isFullScreen;
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
        // save current size
        double prevWidth = stage.getWidth();
        double prevHeight = stage.getHeight();
        stage.setScene(scene);
        double extraWidth = Math.max(0, stage.getWidth() - scene.getWidth());
        double extraHeight = Math.max(0, stage.getHeight() - scene.getHeight());
        if (extraWidth > 0 && extraHeight > 0) {
            GraphicsConst.widthOffset = extraWidth;
            GraphicsConst.heightOffset = extraHeight;
            // stage.setWidth(prevWidth + extraWidth);
            // stage.setHeight(prevHeight + extraHeight);
            // stage.setWidth(prevWidth);
            // stage.setHeight(prevHeight);
        } else {
            // stage.setWidth(prevWidth);
            // stage.setHeight(prevHeight);
        }
        // TODO: stage getwidth is NAN?
        System.err.println(stage.getWidth() + " " + scene.getWidth());
        System.err.println(stage.getHeight() + " 2 " + scene.getHeight());
        System.err.println((prevWidth + extraWidth) + " total " + (prevHeight + extraHeight));
        stage.setWidth(GraphicsConst.windowWidth + GraphicsConst.widthOffset);
        stage.setHeight(GraphicsConst.windowHeight + GraphicsConst.heightOffset);
        System.err.println(extraWidth + " 1 " + extraHeight);
        System.err.println((prevWidth + GraphicsConst.widthOffset) + " hehoh " + (prevHeight + GraphicsConst.heightOffset));
        // System.out.println("Prev Width:"+stage.getWidth());
        // System.out.println("Prev Height:"+stage.getHeight());
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
            // set full screen
            // FadeTransition ft = new FadeTransition(Duration.millis(120),scene.getRoot());
            // scene.getRoot().setOpacity(0);
            // ft.setToValue(1);
            // ft.play();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getBounds();
            double screenWidth = bounds.getWidth();
            double screenHeight = bounds.getHeight();
            System.out.println("Screen Width:" + screenWidth);
            System.out.println("Screen Height:" + screenHeight);
            if (StageManager.isFullScreen && !stage.isFullScreen()) {
                // reset full screen, karena default change stage, full screen off
                // stage.setFullScreenExitHint("");
                // stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
                System.out.println("Width:" + stage.getWidth());
                System.out.println("Height:" + stage.getHeight());
                System.out.println("Scene Width:" + stage.getScene().getWidth());
                System.out.println("Scene Height:" + stage.getScene().getHeight());
            }
            // change
            // stage.getScene().
            // System.out.println(StageManager.isFullScreen);
            // stage.setFullScreen(StageManager.isFullScreen);

            // set width and height
            // if (!StageManager.isFullScreen && GraphicsConst.windowWidth>0 && GraphicsConst.windowHeight>0) {
            //     double overheadW = stage.getWidth() - scene.getWidth();
            //     double overheadH = stage.getHeight() - scene.getHeight();
            //     // check monitor resolutions
            //     Screen screen = Screen.getPrimary();
            //     Rectangle2D bounds = screen.getBounds();
            //     double screenWidth = bounds.getWidth();
            //     double screenHeight = bounds.getHeight();
            //     stage.setWidth(Math.min(GraphicsConst.windowWidth + overheadW, screenWidth));
            //     stage.setHeight(Math.min(GraphicsConst.windowHeight + overheadH, screenHeight));
            // }
        });
    }

}
