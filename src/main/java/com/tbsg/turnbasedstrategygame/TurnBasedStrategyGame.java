package com.tbsg.turnbasedstrategygame;

import java.io.IOException;

import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;
import com.tbsg.turnbasedstrategygame.library.audio.BacksoundPlayer;
import com.tbsg.turnbasedstrategygame.library.engine.ConfigManager;
import com.tbsg.turnbasedstrategygame.library.engine.GameManager;
import com.tbsg.turnbasedstrategygame.library.engine.MapObject;
import com.tbsg.turnbasedstrategygame.library.engine.ProgressBarManager;
import com.tbsg.turnbasedstrategygame.library.engine.TurnManager;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TurnBasedStrategyGame extends Application {

    //    SceneManager sceneManager;
    LoadingScreenController controller;

    FXMLLoader fxmlLoader;

    ProgressBarManager pb_manager;
    //file path ke config
    static String filePath = System.getProperty("user.dir") + "/src/main/resources/com/tbsg/turnbasedstrategygame/setting.conf";

    //file fxml yanh dimuat di loading screen
    String[] fxml_files = {"main-menu", "credit-screen", "settings", "new-game", "game"};
    final String BACKSOUND_PATH = "forest-with-small-river-birds-and-nature-field-recording-6735.mp3";

    @Override
    public void start(Stage stage) {
//        sceneManager = new SceneManager();
        //register stage
        StageManager.setInstance(stage);
        //mulai
        //init progress bar manager
        try {
            loadConfig();
            initScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initProgessBarManager();
        stage.setTitle("Turn Based Strategy Game");
        stage.sizeToScene();
        stage.setResizable(false);
        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getRoot() instanceof Region region) {
                // Cek apakah ada size mismatch
                boolean needsResize
                        = region.getWidth() != GraphicsConst.windowWidth
                        || region.getHeight() != GraphicsConst.windowHeight;
                if (needsResize) {
                    region.resize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
                    region.setPrefSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
                    region.setMinSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
                    region.setMaxSize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
                    region.autosize();
                    region.applyCss();
                    region.layout();
                    if (newScene.getUserData() instanceof RefreshableScene refreshable) {
                        refreshable.refreshLayout();
                    }
                }
            }
        });
        //remove window button
//        stage.initStyle(StageStyle.UNDECORATED);

        Scene scene = SceneManager.getScene("LOADING_SCREEN");
        resizeLoadingScreen();

        StageManager.setScene(scene);
        stage.show();
        //mulai main backsound
        BacksoundPlayer.getInstance().play(BACKSOUND_PATH);
        // set volume
        BacksoundPlayer.getInstance().changeVolume(Double.parseDouble(ConfigManager.getInstance().get("audio.music.master")));
        pb_manager.forwardProgress("SETTING_SOUND");
        //init game
        GameManager.initGameManager(TurnManager.getInstance(), new MapObject());
        pb_manager.forwardProgress("INIT_GAME");
        //mulai
        try {
            startProgressBarManager();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // Wait 2s before switch
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(e -> {
            StageManager.setScene(SceneManager.getScene("MAIN_MENU"));
        });
        delay.play();
    }

    void initProgessBarManager() {
        pb_manager = new ProgressBarManager(controller);
        pb_manager.addProgressTask("LOADING_FXML", fxml_files.length + 1);
//        pb_manager.addProgressTask("LOADING_CONFIG", 2);
        pb_manager.addProgressTask("SETTING_SOUND", 1);
        pb_manager.addProgressTask("INIT_GAME", 1);
        // Add progress for loading loading screen fxml
        pb_manager.forwardProgress("LOADING_FXML");
    }

    void startProgressBarManager() throws IOException {
        //load fxml
        for (String filename : fxml_files) {
            fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource(filename + ".fxml"));
            // Scene scene = new Scene(fxmlLoader.load(), GraphicsConst.windowWidth, GraphicsConst.windowHeight);
            Scene scene = new Scene(fxmlLoader.load());
            scene.setUserData(fxmlLoader.getController());
            SceneManager.addScene(filename.toUpperCase().replace('-', '_'), scene);
            pb_manager.forwardProgress("LOADING_FXML");
            // add changeListener
//            if (filename == "game") {
//                GameController gameController = fxmlLoader.getController();
//                SceneManager.addSceneChangeListener(stage, scene, gameController::generateMap);
//            }
        }
    }

    void loadConfig() {
        // Load scaling
        Screen screen = Screen.getPrimary();
        GraphicsConst.scaleX = screen.getOutputScaleX();
        GraphicsConst.scaleY = screen.getOutputScaleY();
        //load config
        ConfigManager.setInstance(filePath);
//        pb_manager.forwardProgress("LOADING_CONFIG");
        //update screen size
        String[] resolutions = ConfigManager.getInstance().get("graphics.screen.resolution").split("x");
        GraphicsConst.windowWidth = Integer.parseInt(resolutions[0]);
        GraphicsConst.windowHeight = Integer.parseInt(resolutions[1]);
    }

    void initScene() throws IOException {
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        Scene scene = new Scene(fxmlLoader.load(), GraphicsConst.windowWidth, GraphicsConst.windowHeight);
        controller = fxmlLoader.getController();
        SceneManager.addScene("LOADING_SCREEN", scene);
    }

    void resizeLoadingScreen() {
        SceneManager.getScene("LOADING_SCREEN");
    }

    public static void main(String[] args) {
        launch();
    }
}
