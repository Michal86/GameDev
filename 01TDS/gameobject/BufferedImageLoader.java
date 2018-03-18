package com.mike.tds.gameobject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader {

    private BufferedImage image;

    public BufferedImage loadImage(String path){
        try {
            this.image = ImageIO.read(getClass().getResource(path));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}
