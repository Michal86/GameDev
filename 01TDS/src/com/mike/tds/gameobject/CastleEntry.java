package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CastleEntry extends GameObject {

    private BufferedImage entryImage;
    private BufferedImage signImage;
    private BufferedImage windowImage;
    protected volatile boolean opened = false, trap = false;

    public CastleEntry(int x, int y, ID id, SpriteSheet sprSheet, int green) {
        super(x, y, id, sprSheet);

        if (green == 100) {//stairs
            this.entryImage = sprSheet.getImage(3, 2, 64, 32);
            this.width = entryImage.getWidth();
            this.height = 10;
        } else if (green == 105) {
            this.entryImage = sprSheet.getImage(5, 5, 64, 64);
            this.width = entryImage.getWidth();
            this.height = entryImage.getHeight();
        } else if (green == 110) {
            this.signImage = sprSheet.getImage(5, 2, 32, 32);
            this.width = signImage.getWidth();
            this.height = signImage.getHeight();
        } else if (green == 115){
            this.windowImage = sprSheet.getImage(7, 5, 64, 64);
            this.width = windowImage.getWidth();
            this.height = windowImage.getHeight();
        }

    }

    @Override
    public void tick() {
        if (opened && getId()==ID.Doors) {
            this.entryImage = sprSheet.getImage(3, 5, 64, 64);
        }

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(entryImage,x, y, null);
        g.drawImage(signImage,x, y, null);
        g.drawImage(windowImage, x, y, null);

/*        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        if (getBounds()!=null)
            g2d.draw(getBounds());*/
    }

    @Override
    public Rectangle getBounds() {
        if (this.getId() == ID.Doors)
            return new Rectangle(x, y, 64, 48);
        if (this.getId() == ID.Block)
            return new Rectangle(x+16, y-32, 16, 64);
        else
            return null;
    }

    public void setOpened(){
        this.opened = true;
    }

    public synchronized void setTrap(){
        this.trap = true;
        this.entryImage = sprSheet.getImage(3, 7, 64, 32);
    }

    public synchronized boolean isOpened(){
        return this.opened;
    }

    public synchronized boolean isTrap() {
        return this.trap;
    }
}

