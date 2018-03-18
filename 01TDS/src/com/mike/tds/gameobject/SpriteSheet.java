package com.mike.tds.gameobject;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage image;

    public SpriteSheet(BufferedImage image){
        this.image = image;
    }

    //Crop columns/rows in 32pixel packs [com.mike.tds.gameobject.Player 32x48]
    public BufferedImage getImage(int column, int row, int width, int height) {

        return image.getSubimage( (column*32)-32, (row*32)-32, width, height);
    }
}
