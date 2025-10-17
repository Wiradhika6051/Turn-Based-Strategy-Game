package com.tbsg.turnbasedstrategygame.library.io;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyEvent;

public class Keyboard {

    static Keyboard instance;
    Map<Integer, Boolean> pressingMap;

    private Keyboard() {
        pressingMap = new HashMap<>();
    }

    public static Keyboard getInstance() {
        if (instance == null) {
            instance = new Keyboard();
        }
        return instance;
    }

    public void onKeyPressed(KeyEvent event) {
        int keyId = event.getCode().getCode();
        pressingMap.put(keyId, true);
        // switch (keyId) {
        //     case KeyboardConst.KEY_NORTH -> {
        //         moveUp = true;
        //     }
        //     case KeyboardConst.KEY_SOUTH -> {
        //         moveDown = true;
        //     }
        //     case KeyboardConst.KEY_WEST -> {
        //         moveLeft = true;
        //     }
        //     case KeyboardConst.KEY_EAST -> {
        //         moveRight = true;
        //     }
        //     default -> {
        //     }
        // }
    }

    public void onKeyReleased(KeyEvent event) {
        int keyId = event.getCode().getCode();
        pressingMap.put(keyId, false);
        // switch (keyId) {
        //     case KeyboardConst.KEY_NORTH -> {
        //         moveUp = false;
        //     }
        //     case KeyboardConst.KEY_SOUTH -> {
        //         moveDown = false;
        //     }
        //     case KeyboardConst.KEY_WEST -> {
        //         moveLeft = false;
        //     }
        //     case KeyboardConst.KEY_EAST -> {
        //         moveRight = false;
        //     }

        //     default -> {
        //     }
        // }
    }

    public Boolean getKeyStatus(int id) {
        Boolean status = pressingMap.get(id);
        return status == null ? false : status; //in case kena null, return false
    }
}
