package com.mike.tds.gameobject;

import com.mike.tds.gamestate.MapHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Crate extends GameObject {

    protected MapHandler mapHandler;
    private BufferedImage ammoImage;

    public Crate(int x, int y, ID id, MapHandler mapHandler, SpriteSheet sprSheet) {
        super(x, y, id, sprSheet);
        this.mapHandler = mapHandler;
        this.ammoImage = sprSheet.getImage(8, 1,32,32);
        this.width = ammoImage.getWidth();
        this.height = ammoImage.getHeight();

        setObjectSound("item2");
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(ammoImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
