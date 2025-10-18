package com.tbsg.turnbasedstrategygame.library.io;

import javafx.scene.input.MouseEvent;

public class Mouse {

    static Mouse instance;
    public double x;
    public double y;
    MouseCallback callback;

    private Mouse() {
        x = -1;
        y = -1;
    }

    public void setCallback(MouseCallback callback){
        if(callback==null){
            throw new NullPointerException("Mouse callback cannot be null!");
        }
        this.callback = callback;
    }

    public static Mouse getInstance(){
        if (instance == null) {
            instance = new Mouse();
        }
        return instance;
    }

    public void onMouseMoved(MouseEvent event) {
        if(callback==null){
            throw new NullPointerException("Mouse callback must be initialized!");
        }
        x = event.getX();
        y = event.getY();
        callback.call(x,y);
        // // 1. Move highlight to hovered tile
        // highlightX = tileX;
        // highlightY = tileY;
    }
}
