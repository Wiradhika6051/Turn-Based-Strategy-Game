package com.tbsg.turnbasedstrategygame.library.engine;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    List<Civilization> civilizations;
    static TurnManager instance;

    public static TurnManager getInstance() {
        if (instance == null) {
            instance = new TurnManager();
        }
        return instance;
    }

    private TurnManager() {
        civilizations = new ArrayList<>();
    }

    public void addCivilization(Civilization civ) {
        civilizations.add(civ);
    }

    public Civilization getCivilization(int id) {
        for (Civilization civ : civilizations) {
            if (civ.getId() == id) {
                return civ;
            }
        }
        return null;
    }
}
