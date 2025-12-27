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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SettingsController implements Initializable, RefreshableScene {

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
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
        //set master volume slider initial value
        masterVolumeSlider.setValue(BacksoundPlayer.getInstance().getVolume());
        //set default resolution
        resolutionDropdown.setText(ConfigManager.getInstance().get("graphics.screen.resolution"));
    }

    void updateWidth(double width, double height) {
        //set const
        // if (width != 0) {
        //     GraphicsConst.windowWidth = width;
        // }
        // if (height != 0) {
        //     GraphicsConst.windowHeight = height;
        // }
        //change scene size
        //set size root
        if (root != null) {
            root.setPrefWidth(GraphicsConst.windowWidth);
            root.setPrefHeight(GraphicsConst.windowHeight);
            root.setPadding(new Insets(StageManager.calculateHeight(0.013), 0, 0, 0));
        }
        //set size komponen
        if (backButton != null) {
            backButton.setFitWidth(StageManager.calculateWidth(0.07));
            backButton.setFitHeight(StageManager.calculateHeight(0.05));
        }
        if (resolutionTab != null) {
            resolutionTab.setPrefWidth(StageManager.calculateWidth(0.5));
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
        Scene mainMenuScene = SceneManager.getScene("MAIN_MENU");
        StageManager.setScene(mainMenuScene);
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
        Stage stage = StageManager.getInstance();
        Scene currentScene = stage.getScene();
        // double overheadW = stage.getWidth() - currentScene.getWidth();
        // double overheadH = stage.getHeight() - currentScene.getHeight();

        // check monitor resolutions
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();
        // Update global constants
        GraphicsConst.windowWidth = Math.min(newWidth, screenWidth);
        GraphicsConst.windowHeight = Math.min(newHeight, screenHeight);
        // turn to fullscreen if the new resolution exceed screen
        if (newWidth >= screenWidth - 1 && newHeight >= screenHeight - 1) {
            stage.setFullScreen(true);
            StageManager.toggleFullScreen(true);
        } else {
            // set stage size
            stage.setFullScreen(false);
            StageManager.toggleFullScreen(false);
            stage.setWidth(GraphicsConst.windowWidth);
            stage.setHeight(GraphicsConst.windowHeight);
        }
        // Refresh all scene
        Platform.runLater(() -> {
            Map<String, Scene> sceneMap = SceneManager.getAllScenes();
            if (sceneMap != null) {
                for (Scene scene : sceneMap.values()) {
                    if(scene==currentScene){
                        // skip update scene
                        continue;
                    }
                    // Refresh controller if it implements RefreshableScene
                    Object controller = scene.getUserData();
                    // Resize scene
                    scene.getRoot().resize(newWidth, newHeight);
                    scene.getRoot().requestLayout();
                    if (controller instanceof RefreshableScene refreshable) {
                        refreshable.refreshLayout(); // let the controller resize its own root
                    }
                }
            }
        });

    }

    @FXML
    protected void handleScalingChange(ActionEvent event) {

    }

    @Override
    public void refreshLayout() {
        updateWidth(GraphicsConst.windowWidth, GraphicsConst.windowHeight);
    }
}
