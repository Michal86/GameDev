package com.mike.tds.gameobject;

import com.mike.tds.main.Handler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{

    private Handler handler;
    private BufferedImage arrowImage;
    private float deltaX;
    private float deltaY;
    private float speed;
    private double theta;
    //=========
    private int anchX,anchY;
    private volatile boolean hurts;

    public Bullet(int x, int y, ID id, Handler handler, GameObject whos, int mX, int mY, SpriteSheet sprSheet){
        super(x, y, id, sprSheet);
        this.handler = handler;
        hurts = false;
        //---------------
        if (whos.getId() == ID.Player) {
            this.arrowImage = sprSheet.getImage(5, 1, 6, 30);
            speed = 30.0f;
        }
        else if (whos.getId() == ID.Enemy) {
            speed = 9.0f;
            this.arrowImage = sprSheet.getImage(4, 1, 12, 18);
            anchX = 6;
            anchY = 9;
            this.hurts = true;
        }
        this.width = arrowImage.getWidth();
        this.height = arrowImage.getHeight();

        deltaX = mX - x;
        deltaY = mY - y;
        float goal_dist = (float) Math.sqrt( (deltaX*deltaX) + (deltaY*deltaY) );

        if (goal_dist > speed){
            float ratio = speed / goal_dist;
            velX = ratio * deltaX;
            velY = ratio * deltaY;
        }

        this.theta = getRadius(x, y, mX, mY);
    }

    //count streight line
    private double getRadius(float x1, float y1, float x2, float y2){
        //===== get the angle between these points [-90 degree to rotate that 0 is North]
        theta =  Math.atan2(y2 - y1, x2 - x1) - Math.toRadians(90);

        //check if degrees are as planned
        //System.out.println("Radius: " + theta);
        //double angle = theta * (180/Math.PI);
        //System.out.println("Angle: " + angle);
        //=====================
        return theta;
    }

    public void tick() {
        x += velX;
        y += velY;

        //check if bullet is moving
        if ( velX == 0 && velY == 0) handler.removeObject(this);

        //remove bullet if it hits the wall
        for(int i=0; i<handler.object.size(); i++){
            GameObject tempObj = handler.object.get(i);

            if (tempObj.getId() == ID.Block || tempObj.getId() == ID.GrassEdge)
            {
                if (getBounds().intersects(tempObj.getBounds())){
                    handler.removeObject(this);
                }
            }

            if (tempObj.getId() == ID.Player && hurts && getBounds().intersects(tempObj.getBounds())){
                handler.hitPlayer();
                handler.removeObject(this);
            }
        }

    }

    public void render(Graphics g) {


        Graphics2D g2d = (Graphics2D) g;
/*        g2d.setColor(Color.RED);
        g2d.rotate(theta, x, y);
        g2d.draw(getBounds());*/

        //Rotate our arrow
        AffineTransform backUp = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.rotate(theta, x+anchX, y+anchY);
        g2d.transform(transform);
        g2d.drawImage(arrowImage, x, y, null);
        g2d.setTransform(backUp);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 8, 24);
    }
}
