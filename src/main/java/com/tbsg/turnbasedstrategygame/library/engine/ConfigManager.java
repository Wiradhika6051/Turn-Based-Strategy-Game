package com.tbsg.turnbasedstrategygame.library.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigManager {
    Map<String, String> configs;
    String path;

    static ConfigManager instance = null;

    ConfigManager(String configPath) {
        this.configs = new HashMap<>();
        loadConfig(configPath);
    }

    public static void setInstance(String configPath) {
        //TODO should it check for indempotence to help reduce memory?
        ConfigManager.instance = new ConfigManager(configPath);
    }

    public static ConfigManager getInstance() {
        return ConfigManager.instance;
    }

    void loadConfig(String path) {
        try {
            this.path = path;
            File configFile = new File(path);
            Scanner configReader = new Scanner(configFile);
            while (configReader.hasNextLine()) {
                String[] config = configReader.nextLine().split("=");
                configs.put(config[0], config[1]);
            }
            configReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return configs.get(key);
    }

    public void set(String key, String value) {
        configs.put(key, value);
    }

    public void save() {
        try {
            FileWriter configWriter = new FileWriter(path);
            for (Map.Entry<String, String> config : configs.entrySet()) {
                String configFormat = config.getKey() + "=" + config.getValue() + "\n";
                configWriter.write(configFormat);
            }
            configWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
