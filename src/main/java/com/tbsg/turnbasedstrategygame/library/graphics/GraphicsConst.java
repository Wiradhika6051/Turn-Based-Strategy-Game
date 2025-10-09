package com.tbsg.turnbasedstrategygame.library.graphics;

//import javafx.stage.Screen;

public class GraphicsConst {
    public static double windowWidth = 0;
    public static double windowHeight = 0;
    public static double scaleX = 1;
    public static double scaleY = 1;

    public static String MAP_FOLDER = "/com/tbsg/turnbasedstrategygame/map/";
    public static String TILE_TEXTURES_FOLDER = "/com/tbsg/turnbasedstrategygame/textures/tiles/";

    public static byte NORTH_COAST = 0b0000001;
    public static byte EAST_COAST  = 0b0000010;
    public static byte SOUTH_COAST = 0b0000100;
    public static byte WEST_COAST  = 0b0001000;
    public static byte NORTH_EAST_COAST = 0b0010000;
    public static byte SOUTH_EAST_COAST = 0b0100000;
    public static byte SOUTH_WEST_COAST = 0b1000000;
    public static byte NORTH_WEST_COAST = (byte)0b10000000;

    // mask
    public static byte NORTH_EAST_MASK = 0b0000011;
    public static byte SOUTH_EAST_MASK = 0b0000110;
    public static byte SOUTH_WEST_MASK = 0b0001100;
    public static byte NORTH_WEST_MASK = 0b0001001;
}
