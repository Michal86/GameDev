package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoofRed extends GameObject {

    private BufferedImage roofRedImage;

    public RoofRed(int x, int y, ID id, SpriteSheet sprSheet, int red) {
        super(x, y, id, sprSheet);

        if (red == 160)
            this.roofRedImage = roofPart(sprSheet,4,3);
        else if (red == 155 )
            this.roofRedImage = roofPart(sprSheet,1, 3);
        else if (red == 150)
            this.roofRedImage = roofPart(sprSheet,3, 3);
        else if (red == 145)
            this.roofRedImage = roofPart(sprSheet,2, 3);
        else if (red == 140)
            this.roofRedImage = roofPart(sprSheet,6, 3);
        else if (red == 135)
            this.roofRedImage = roofPart(sprSheet,5, 3);

        this.width = 32;
        this.height = 32;
    }

    private BufferedImage roofPart(SpriteSheet sprSheet, int col, int row){
        return sprSheet.getImage(col,row,32,32);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        //Graphics2D g2d = (Graphics2D) g;

        g.drawImage(roofRedImage, x, y, null);

        //g2d.setColor(Color.RED);
        //g2d.draw(getBounds());
    }

    @Override
    public Rectangle getBounds() {
            return new Rectangle(x, y, 32, 32);
    }


}

