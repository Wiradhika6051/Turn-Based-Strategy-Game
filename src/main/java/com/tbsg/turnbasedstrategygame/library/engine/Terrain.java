package com.tbsg.turnbasedstrategygame.library.engine;

import com.tbsg.turnbasedstrategygame.library.graphics.GraphicsConst;

import javafx.scene.image.Image;

public class Terrain {
    Integer id;
    String name;
    transient Image texture;
    String texturePath;
    Boolean isLand;

    public Terrain(Integer id, String name, String texturePath, Boolean isLand) {
        this.id = id;
        this.name = name;
        this.texturePath = texturePath;
        this.isLand = isLand;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public Boolean getIsLand() {
        return isLand;
    }

    public void loadTexture() throws NullPointerException{
        texture = new Image(getClass().getResourceAsStream(GraphicsConst.TERRAIN_FOLDER + texturePath));
    }
}
