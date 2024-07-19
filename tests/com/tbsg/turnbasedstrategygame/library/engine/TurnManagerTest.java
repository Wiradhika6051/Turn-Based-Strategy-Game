package com.tbsg.turnbasedstrategygame.library.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnManagerTest {
    TurnManager manager;

    @Before
    public void init() {
        manager = TurnManager.getInstance();
    }

    @Test
    public void test_getInstance() {
        assertNotNull(manager);
        assertNotNull(manager.civilizations);
        assertEquals(0, manager.civilizations.size());
    }

    @Test
    public void test_addCivilization() {
        int current_length = manager.civilizations.size();
        manager.addCivilization(new Civilization(0, "Aloha"));
        assertEquals(current_length + 1, manager.civilizations.size());
    }

    @Test
    public void test_getCivilization() {
        Civilization civ = new Civilization(0, "Aloha");
        int current_length = manager.civilizations.size();
        manager.addCivilization(civ);
        assertEquals(civ, manager.getCivilization(0));
        assertEquals(current_length + 1, manager.civilizations.size());
    }
}