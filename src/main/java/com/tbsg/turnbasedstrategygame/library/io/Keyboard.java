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
    }

    public void onKeyReleased(KeyEvent event) {
        int keyId = event.getCode().getCode();
        pressingMap.put(keyId, false);
    }

    public Boolean getKeyStatus(int id) {
        Boolean status = pressingMap.get(id);
        return status == null ? false : status; //in case kena null, return false
    }
}
