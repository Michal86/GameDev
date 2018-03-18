package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GrassEdge extends GameObject{

    private BufferedImage grassImage;

    public GrassEdge(int x, int y, ID id, SpriteSheet sprSheet) {
        super(x, y, id, sprSheet);
        this.grassImage = sprSheet.getImage(2,1,32, 32);

        this.width = grassImage.getWidth();
        this.height = grassImage.getHeight();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(grassImage, x, y, null);

/*        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.draw(getBounds());*/
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y+22,32,12);
    }
}
