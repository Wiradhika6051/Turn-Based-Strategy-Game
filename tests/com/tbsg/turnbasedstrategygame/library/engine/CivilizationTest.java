package com.tbsg.turnbasedstrategygame.library.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CivilizationTest {
    Civilization civ;

    @Before
    public void setup() {
        System.out.println("Instantiating a civ...");
        civ = new Civilization(0, "Aloha");
    }

    @Test
    public void test_instantiation() {
        assertEquals(0, civ.getId());
        assertEquals("Aloha", civ.name);
        assertNotNull(civ.cities);
        assertNotNull(civ.units);
        assertEquals(0, civ.cities.size());
        assertEquals(0, civ.units.size());
    }

    public void test_add_city() {
        civ.cities.add(new City());
        assertEquals(1, civ.cities.size());
    }

    public void test_add_unit() {
        civ.units.add(new Unit());
        assertEquals(1, civ.units.size());
    }
}