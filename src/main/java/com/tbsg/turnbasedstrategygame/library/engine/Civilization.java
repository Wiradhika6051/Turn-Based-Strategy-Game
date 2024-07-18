package com.tbsg.turnbasedstrategygame.library.engine;

import java.util.ArrayList;
import java.util.List;

public class Civilization {
    int id;
    public String name;
    public List<IUnit> units;
    public List<ICity> cities;

    public Civilization(int id, String name) {
        this.id = id;
        this.name = name;
        this.units = new ArrayList<>();
        this.cities = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

}
