package com.tbsg.turnbasedstrategygame.library.engine;

import java.io.InputStream;
import java.util.Scanner;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;

public class StaticMapGenerator implements MapGenerator {
    final String basepath = GraphicsConst.MAP_FOLDER;
    String filename;

    public StaticMapGenerator(String filename) {
        this.filename = filename;
    }
    @Override
    public MapObject generate() {
        MapObject map = new MapObject();
        try (InputStream inputStream = getClass().getResourceAsStream(basepath + filename)) {
            if (inputStream == null) {
                System.err.println("Map File not found!");
                return null;
            }

            try (Scanner scanner = new Scanner(inputStream)) {
                int i = 0;
                int j = 0;
                int width = -1;
                int height = -1;

                while (scanner.hasNextLine()) {
                    String row = scanner.nextLine();
                    if (row.length() == 0) {
                        continue;
                    }
                    if (width < 0 && height < 0) {
                        // read dimensions
                        String[] size = row.split(" ");
                        width = Integer.parseInt(size[0]);
                        height = Integer.parseInt(size[1]);
                        map.setX_longitude(width);
                        map.setY_latitude(height);
                    } else {
                        for (String tileId : row.split("")) {
                            Tile tile = new Tile(i, j, Integer.parseInt(tileId));
                            map.addTile(tile);
                            i++;
                        }
                        i = 0;
                        j++;
                    }
                }
            }
            // generate coast map
            map.initializeCoastMap();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }
}
