package com.tbsg.turnbasedstrategygame.library.engine;

import com.tbsg.turnbasedstrategygame.library.graphics.StageManager;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StageManagerTest {


    @Before
    public void init() {
//        StageManager.setInstance(new Stage());
    }

    @Test
    public void test_set_getInstance() {
        StageManager.setInstance(new Stage());
        assertNotNull(StageManager.getInstance());
    }

    @Test
    public void test_calculateWidth() {
        assertEquals(StageManager.calculateWidth(0.1), 192, 0.1);
    }

    @Test
    public void test_calculateHeight() {
        assertEquals(StageManager.calculateWidth(0.1), 108, 0.1);
    }
}