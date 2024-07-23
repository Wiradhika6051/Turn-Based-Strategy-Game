package com.tbsg.turnbasedstrategygame.library.engine;

import com.tbsg.turnbasedstrategygame.controllers.LoadingScreenController;

import java.util.HashMap;
import java.util.Map;

public class ProgressBarManager {
    Map<String, Integer> progress;
    int totalProgress;
    int currentProgress;

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
                handler.setProgress((double) currentProgress / totalProgress);
            }
            //kalau udah 0 gak ada apa apa
        }


    }
}
