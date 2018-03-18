package com.mike.tds.gameobject;
import com.mike.tds.gamestate.MapHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Potion extends GameObject {

    protected MapHandler mapHandler;
    private BufferedImage potionImage;

    public Potion(int x, int y, ID id, MapHandler mapHandler, SpriteSheet sprSheet) {
        super(x, y, id, sprSheet);
        this.mapHandler = mapHandler;
        this.potionImage = sprSheet.getImage(9, 7,32,32);
        this.width = potionImage.getWidth();
        this.height = potionImage.getHeight();
        setObjectSound("item2");
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(potionImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x+1, y+1, 30, 30);
    }
}

