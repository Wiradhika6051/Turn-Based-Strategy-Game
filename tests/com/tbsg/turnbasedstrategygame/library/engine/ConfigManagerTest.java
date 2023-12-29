package com.tbsg.turnbasedstrategygame.library.engine;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ConfigManagerTest {
    ConfigManager manager;
    static String filePath = System.getProperty("user.dir") + "/src/main/resources/com/tbsg/turnbasedstrategygame/setting.conf";

    @Before
    public void setup() {
        manager = new ConfigManager(filePath);
    }

    @Test
    public void test_loadConfig() {
        //confignya harusnya kemuat
        assertTrue(manager.configs.size() > 0);
        System.out.println("Configurations Loaded");
    }

    @Test
    public void test_get() {
        assertEquals("1.0", manager.get("audio.music.master"));
        System.out.println("Configuration retrieved");
    }

    @Test
    public void test_set() {
        manager.set("audio.music.master", "0.5");
        assertEquals("0.5", manager.get("audio.music.master"));
        System.out.println("Configuration can be updated");
        manager.set("audio.music.master", "1.0");
    }

    @Test
    public void test_save() {
        manager.set("audio.music.master", "0.5");
        manager.save();
        //cek isi file
        File confFile = new File(filePath);
        try {
            assertTrue(FileUtils.readFileToString(confFile).contains("audio.music.master=0.5"));
            System.out.println("Configuration saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        manager.set("audio.music.master", "1.0");
        manager.save();
    }
}