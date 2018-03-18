package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WallGrass extends GameObject{

    private BufferedImage wallImage;

    public WallGrass(int x, int y, ID id, SpriteSheet sprSheet){
        super(x, y, id, sprSheet);
        this.wallImage = sprSheet.getImage(1, 2, 32, 32);

        this.width = wallImage.getWidth();
        this.height = wallImage.getHeight();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(wallImage,x, y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
