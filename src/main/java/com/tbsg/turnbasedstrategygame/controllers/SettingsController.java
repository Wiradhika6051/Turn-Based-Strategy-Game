package com.tbsg.turnbasedstrategygame.controllers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.tbsg.turnbasedstrategygame.library.audio.AudioPlayer;
import com.tbsg.turnbasedstrategygame.library.audio.BacksoundPlayer;
import com.tbsg.turnbasedstrategygame.library.engine.ConfigManager;
import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;
import com.tbsg.turnbasedstrategygame.library.graphics.RefreshableScene;
import com.tbsg.turnbasedstrategygame.library.graphics.SceneManager;
import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsController implements Initializable, RefreshableScene {

    @FXML
    VBox list;

    @FXML
    HBox resolutionTab;

    @FXML
    HBox masterVolumeTab;

    @FXML
    ImageView backButton;

    @FXML
    VBox root;

    @FXML
    Slider masterVolumeSlider;

    @FXML
    MenuButton resolutionDropdown;

    @FXML
    Region resolutionMargin;

    @FXML
    Region masterVolumeMargin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //update size back button
//        root.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
        //set master volume slider initial value
        masterVolumeSlider.setValue(BacksoundPlayer.getInstance().getVolume());
        //set default resolution
        resolutionDropdown.setText(ConfigManager.getInstance().get("graphics.screen.resolution"));
//            }
//        });

//        masterVolumeSlider.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                masterVolumeSlider.setValue(BacksoundPlayer.getInstance().getVolume());
//            }
//        });
    }

    void updateWidth(double width, double height) {
        //set const
        if (width != 0) {
            GraphicsConst.windowWidth = width;
        }
        if (height != 0) {
            GraphicsConst.windowHeight = height;
        }
        //change scene size
        //set size root
        if (root != null) {
            root.setPrefWidth(GraphicsConst.windowWidth);
            root.setPrefHeight(GraphicsConst.windowHeight);
            root.setPadding(new Insets(StageManager.calculateHeight(0.013), 0, 0, 0));
            //set root font
//            root.setStyle(String.format("-fx-font-size: %dpx;", (int) (10)));
//            StageManager.getInstance().setWidth(GraphicsConst.windowWidth);
//            StageManager.getInstance().setHeight(GraphicsConst.windowHeight);
        }
        //set size komponen
        if (backButton != null) {
            backButton.setFitWidth(StageManager.calculateWidth(0.07));
            backButton.setFitHeight(StageManager.calculateHeight(0.05));
        }

        if (resolutionTab != null) {
            resolutionTab.setPrefWidth(StageManager.calculateWidth(0.5));
//            resolutionTab.setPrefHeight(StageManager.calculateHeight(1.0));
        }
        if (resolutionMargin != null) {
            resolutionMargin.setPrefWidth(StageManager.calculateWidth(0.1));
        }
        if (masterVolumeTab != null) {
            resolutionTab.setPrefWidth(StageManager.calculateWidth(0.5));
        }
        if (masterVolumeMargin != null) {
            masterVolumeMargin.setPrefWidth(StageManager.calculateWidth(0.1));
        }
    }

    @FXML
    protected void backToMenu() {
        // Simpan konfigurasi
        ConfigManager.getInstance().save();
        // Pindah ke Main Menu
        Stage stage = (Stage) root.getScene().getWindow();
        Scene mainMenuScene = SceneManager.getScene("MAIN_MENU");
        StageManager.setScene(mainMenuScene);
        // Ensure the scene size matches the stage
        // mainMenuScene.getRoot().resize(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
        // Refresh layout if controller implements RefreshableScene
        // Platform.runLater(() -> {
        //     resizeAllScenes(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
        // });
    }

    void resizeAllScenes(double width, double height) {
        Map<String, Scene> sceneMap = SceneManager.getAllScenes();
        if (sceneMap != null) {
            for (Scene scene : sceneMap.values()) {
                // Resize root node
                if (scene.getRoot() instanceof Region region) {
                    // System.out.println("Resizing scene root to W=" + width + " H=" + height);
                    region.resize(width, height);
                    region.setPrefSize(width, height);
                    region.setMinSize(width, height);
                    region.setMaxSize(width, height);
                    region.autosize();
                    region.applyCss();
                    region.layout();
                }

                // Refresh controller if it implements RefreshableScene
                Object controller = scene.getUserData();
                if (controller instanceof RefreshableScene refreshable) {
                    refreshable.refreshLayout();
                }
            }
        }
    }

    @FXML
    protected void changeMasterVolume() {
        //change master volume
        AudioPlayer masterAudioPlayer = BacksoundPlayer.getInstance();
        masterAudioPlayer.changeVolume(masterVolumeSlider.getValue());
        //simpan ke config
        ConfigManager.getInstance().set("audio.music.master", Double.toString(masterAudioPlayer.getVolume()));
    }

    @FXML
    protected void handleResolutionChange(ActionEvent event) {
        String selectedResolutions = ((MenuItem) event.getSource()).getText();
        resolutionDropdown.setText(selectedResolutions);
        String[] resolutionConfig = selectedResolutions.split("x");
        int newWidth = Integer.parseInt(resolutionConfig[0]);
        int newHeight = Integer.parseInt(resolutionConfig[1]);
        //simpan ke config
        ConfigManager.getInstance().set("graphics.screen.resolution", selectedResolutions);
        //update width and height
        StageManager.getInstance().setWidth(newWidth);
        StageManager.getInstance().setHeight(newHeight);
        // Update global constants
        GraphicsConst.windowWidth = newWidth;
        GraphicsConst.windowHeight = newHeight;
        updateWidth(newWidth, newHeight);
        // Refresh all scene
        Map<String, Scene> sceneMap = SceneManager.getAllScenes();
        if (sceneMap != null) {
            for (Scene scene : sceneMap.values()) {
                // Refresh controller if it implements RefreshableScene
                Object controller = scene.getUserData();
                if (controller instanceof RefreshableScene refreshable) {
                    refreshable.refreshLayout(); // let the controller resize its own root
                }
            }
        }
    }

    @FXML
    protected void handleScalingChange(ActionEvent event) {

    }

    @Override
    public void refreshLayout() {
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
    }
}
