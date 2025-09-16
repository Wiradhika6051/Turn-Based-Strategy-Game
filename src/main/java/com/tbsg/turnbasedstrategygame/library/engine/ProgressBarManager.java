package com.tbsg.turnbasedstrategygame.library.engine;

import java.util.HashMap;
import java.util.Map;

import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;
import com.tbsg.turnbasedstrategygame.library.graphics.AnimationConst;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;

public class ProgressBarManager {

    Map<String, LoadingTask> progress;
    int totalProgress;
    int currentProgress;
    // PauseTransition pause;
    SequentialTransition queue = new SequentialTransition();
    double queuedDelay;

    IProgressBarHandler handler;

    public ProgressBarManager(IProgressBarHandler handler) {
        progress = new HashMap<String, LoadingTask>();
        totalProgress = 0;
        currentProgress = 0;
        queuedDelay = 0;
        this.handler = handler;
    }

    public void addProgressTask(String task,String description, int number) {
        progress.put(task, new LoadingTask(description,number));
        totalProgress += number;
    }

    public int getTotalProgress() {
        return this.totalProgress;
    }

    public void forwardProgress(String task) {
        //cek apakah ada
        if (progress.containsKey(task)) {
            LoadingTask taskData = progress.get(task);
            int taskProgress = taskData.total;
            if (taskProgress > 0) {
                currentProgress++;
                progress.get(task).total = taskProgress - 1;
                // progress.put(task, taskProgress - 1);
                queuedDelay += 1;
                //update progressbar
                // 1 pause to rule them all, kalau gak ada buat baru, kalau ada stop dulu
                // if (pause != null) {
                //     pause.stop();
                // }
                // delay task selama 1 detik, untuk memberikan ilusi progress
                // DoubleProperty animatedValue = new SimpleDoubleProperty(((LoadingScreenController) handler).progressBar.getProgress());
                double progressValue = (double) currentProgress / totalProgress;

                // Update label right away
                // ((LoadingScreenController) handler).progressLabel.setText(task);

                // "Sleep" for 1s, then call setProgress
                PauseTransition pause = new PauseTransition(Duration.millis(queuedDelay * AnimationConst.animationDelay));
                pause.setOnFinished(e -> handler.setProgress(progressValue,taskData.description));
                pause.play();
            }
        }
        //kalau udah 0 gak ada apa apa
    }

}
