package com.tbsg.turnbasedstrategygame.library.engine;

import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ProgressBarManager {

    Map<String, Integer> progress;
    int totalProgress;
    int currentProgress;
    PauseTransition pause;

    IProgressBarHandler handler;

    public ProgressBarManager(IProgressBarHandler handler) {
        progress = new HashMap<String, Integer>();
        totalProgress = 0;
        currentProgress = 0;
        this.handler = handler;
    }

    public void addProgressTask(String task, int number) {
        progress.put(task, number);
        totalProgress += number;
    }

    public void forwardProgress(String task) {
        //cek apakah ada
        if (progress.containsKey(task)) {
            int taskProgress = progress.get(task);
            if (taskProgress > 0) {
                currentProgress++;
                progress.put(task, taskProgress - 1);
                //update progressbar
                // 1 pause to rule them all, kalau gak ada buat baru, kalau ada stop dulu
                if (pause != null) {
                    pause.stop();
                }
                // delay task selama 1 detik, untuk memberikan ilusi progress
                double progressValue = (double) currentProgress / totalProgress;

                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(((LoadingScreenController) handler).progressBar.progressProperty(), progressValue))
                );
                timeline.play();;
            }
            //kalau udah 0 gak ada apa apa
        }

    }
}
