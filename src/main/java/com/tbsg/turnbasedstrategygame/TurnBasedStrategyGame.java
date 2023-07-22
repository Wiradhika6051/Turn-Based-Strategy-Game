package com.tbsg.turnbasedstrategygame;

import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;
import com.tbsg.turnbasedstrategygame.library.audio.BacksoundPlayer;
import com.tbsg.turnbasedstrategygame.library.engine.ProgressBarManager;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TurnBasedStrategyGame extends Application {
    //    SceneManager sceneManager;
    LoadingScreenController controller;

    FXMLLoader fxmlLoader;

    ProgressBarManager pb_manager;

    //file fxml yanh dimuat di loading screen
    String[] fxml_files = {"main-menu", "credit-screen"};

    //    final String BACKSOUND_PATH = "@sound/forest-with-small-river-birds-and-nature-field-recording-6735.ogg";
    final String BACKSOUND_PATH = "forest-with-small-river-birds-and-nature-field-recording-6735.mp3";

    int WIDTH = 640;
    int HEIGHT = 480;

    @Override
    public void start(Stage stage) {
//        sceneManager = new SceneManager();
        try {
            initScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Turn Based Strategy Game");
//        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
//        Scene scene2 = new Scene(fxmlLoader.load(),320,240);
        //set scene
//        Scene scene = sceneManager.getScene("LOADING_SCREEN");
        Scene scene = SceneManager.getScene("LOADING_SCREEN");
        System.out.println(scene);
        stage.setScene(scene);
//        PauseTransition pause = new PauseTransition(Duration.seconds(2));
//        pause.setOnFinished(event ->{
//            System.out.println("halo");
//            stage.setScene(scene2);
//        });
//
        stage.show();
        //mulai
        //init progress bar manager
        initProgessBarManager();
        //mulai
        try {
            startProgressBarManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(SceneManager.getScene("MAIN_MENU"));
        //mulai main backsound
        BacksoundPlayer.getInstance().play(BACKSOUND_PATH);
//        System.out.println("Hi");
//        pause.play();
    }

    void initProgessBarManager() {
        pb_manager = new ProgressBarManager(controller);
        pb_manager.addProgressTask("LOADING_FXML", 2);
    }

    void startProgressBarManager() throws IOException {
        //load fxml
        for (String filename : fxml_files) {
            fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource(filename + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
            SceneManager.addScene(filename.toUpperCase().replace('-', '_'), scene);
            pb_manager.forwardProgress("LOADING_FXML");
        }
        //load fxml main menu
//        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("main-menu.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
//        sceneManager.addScene("MAIN_MENU", scene);
//        pb_manager.forwardProgress("LOADING_FXML");
//        //load fxml credit screen
//        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("credit-screen.fxml"));

    }

    void initScene() throws IOException {
        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        controller = fxmlLoader.getController();
        SceneManager.addScene("LOADING_SCREEN", scene);
    }

    public static void main(String[] args) {
        launch();
    }
}