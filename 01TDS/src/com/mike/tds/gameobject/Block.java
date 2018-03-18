package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends GameObject{

    private BufferedImage blockImage;

    public Block(int x, int y, ID id, SpriteSheet sprSheet){
        super(x, y, id, sprSheet);
        this.blockImage = sprSheet.getImage(2, 2,32, 32);
        this.width = blockImage.getWidth();
        this.height = blockImage.getHeight();

    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(blockImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
