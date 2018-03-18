package com.mike.tds.gameobject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TreeOne extends GameObject {

    private BufferedImage treeImage;
    private BufferedImage rootImage;
    private int green;


    public TreeOne(int x, int y, ID id, SpriteSheet sprSheet, int green) {
        super(x, y, id, sprSheet);
        this.green = green;

        if (green==45) {
            this.rootImage = createTree(sprSheet, 9, 1);
            this.width = 32;
            this.height = 32;
        }
        else if (green==50) {
            this.rootImage = createTree(sprSheet, 4, 4);
            this.width = 32;
            this.height = 32;
        }
        //lewy korzen
        else if (green==55) {
            this.treeImage = sprSheet.getImage(1, 4, 64, 92);
            this.width = 64;
            this.height = 96;
        }
    }

    private BufferedImage createTree(SpriteSheet sprSheet, int col, int row){
        return sprSheet.getImage(col,row,32,32);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        //Graphics2D g2d = (Graphics2D) g;
        if (green == 50 || green == 45) {
            g.drawImage(rootImage, x, y, null);
/*            g2d.setColor(Color.RED);
            g2d.draw(getBounds());*/
        }
        else{
            g.drawImage(treeImage ,x, y, null);
/*            g2d.setColor(Color.RED);
            g2d.draw(getBounds());*/
        }

    }

    @Override
    public Rectangle getBounds() {
        if (green == 50 || green==45)
            return new Rectangle(x+6, y, 16, 24);
        if (green==55)
            return new Rectangle(x+24, y+64, 16, 16);


        return null;
    }


}
