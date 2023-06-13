package com.tbsg.turnbasedstrategygame;

import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TurnBasedStrategyGame extends Application {
    SceneManager sceneManager;
    @Override
    public void start(Stage stage){
        sceneManager = new SceneManager();
        try {
            registerScene();
        }catch (IOException e){
            e.printStackTrace();
        }
        stage.setTitle("Turn Based Strategy Game");
//        fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
//        Scene scene2 = new Scene(fxmlLoader.load(),320,240);
        //set scene
        Scene scene = sceneManager.getScene("LOADING_SCREEN");
        System.out.println(scene);
        stage.setScene(scene);
//        PauseTransition pause = new PauseTransition(Duration.seconds(2));
//        pause.setOnFinished(event ->{
//            System.out.println("halo");
//            stage.setScene(scene2);
//        });
//
        stage.show();
//        System.out.println("Hi");
//        pause.play();
    }

    void registerScene()throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TurnBasedStrategyGame.class.getResource("loading-screen.fxml"));
        System.out.println("masuk");
        System.out.println(fxmlLoader);
        System.out.println("masuk2");
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        System.out.println("Scene:");
//        System.out.println(scene);
        System.out.println("aman");
        sceneManager.addScene("LOADING_SCREEN",scene);
    }

    public static void main(String[] args) {
        launch();
    }
}