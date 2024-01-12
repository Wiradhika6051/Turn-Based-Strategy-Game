package com.tbsg.turnbasedstrategygame.library.engine;

import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

public class StageManagerTest {

    @Test
    public void test_set_getInstance() {
        StageManager.setInstance(new Stage());
        assertNotNull(StageManager.getInstance());
    }
}