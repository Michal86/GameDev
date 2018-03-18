package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rock extends GameObject{

    private BufferedImage rockImage;

    public Rock(int x, int y, ID id, SpriteSheet sprSheet) {
        super(x, y, id, sprSheet);
        this.rockImage = sprSheet.getImage(3,4,32,32);
        this.width = rockImage.getWidth();
        this.height = rockImage.getHeight();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(rockImage, x, y, null);

/*        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.draw(getBounds());*/
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x+5,y+5, 22,22);
    }
}
