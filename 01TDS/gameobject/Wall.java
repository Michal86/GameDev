package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject{
    private BufferedImage wallImage;

    public Wall(int x, int y, ID id, SpriteSheet sprSheet, int red, int green, int blue){
        super(x, y, id, sprSheet);

        if (red == 255 && green==40 && blue==10) {
            this.wallImage =
                    sprSheet.getImage(2, 7, 32, 32);
        }
        else if (red == 180 && green==40 && blue==40){
            this.wallImage =
                    sprSheet.getImage(1, 7, 32, 32);
        }
        else if (red == 160 && green==30 && blue==50){
            this.wallImage =
                    sprSheet.getImage(7, 7, 32, 32);
        }
        else if (red == 132 && green==97 && blue==65) {
            this.wallImage =
                    sprSheet.getImage(6, 7, 32, 32);
        }
        else if (red == 105 && green==60 && blue==60){
            this.wallImage =
                    sprSheet.getImage(8, 7, 32, 32);
        }
        else if (red == 104 && green==55 && blue==36){
            this.wallImage =
                    sprSheet.getImage(5, 7, 32, 32);
        }

        this.width = wallImage.getWidth();
        this.height = wallImage.getHeight();
    }


    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(wallImage,x,y,null);

    }

    @Override
    public Rectangle getBounds() {
            return new Rectangle(x,y,32,20);
    }
}
