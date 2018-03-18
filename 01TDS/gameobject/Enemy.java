package com.mike.tds.gameobject;
import com.mike.tds.main.Handler;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Enemy extends GameObject{

    //Store frames
    private BufferedImage[] movingDown, movingRight, movingLeft, standing;
    private BufferedImage[] movingUp = {
            sprSheet.getImage(9,3,32,32),
            sprSheet.getImage(9,4,32,32),
            sprSheet.getImage(9,5,32,32),
            sprSheet.getImage(9,6,32,32)};
    //Set animations
    Animation moveDown, moveUp, moveRight, moveLeft, stand, animation;
    // ---
    private Handler handler;
    private int counter = 0;
    Random r = new Random();
    int hp = 100;
    int move = 0;

    public Enemy(int x, int y, ID id, Handler handler, SpriteSheet sprSheet) {
        super(x, y, id, sprSheet);
        this.handler = handler;
        setObjectSound("enemy_hurt");

        //Set frames and animations
        movingDown = setFrames(sprSheet, 4, 1, 4);
        movingRight = setFrames(sprSheet, 6,2,4);
        movingLeft = setFrames(sprSheet, 5,4,4);

        moveDown = new Animation(movingDown,5, this);
        moveUp = new Animation(movingUp, 5,this);
        moveRight = new Animation(movingRight, 5, this);
        moveLeft = new Animation(movingLeft, 5, this);
        stand = new Animation(
                standing = new BufferedImage[]{sprSheet.getImage(8, 3, 32, 32)},
                5,this);
        this.animation = stand;
        animation.start();

        this.width = 30;
        this.height = 30;
    }

    //Load frames to an array
    private BufferedImage[] setFrames(SpriteSheet sprSheet, int colStart, int row, int framesCount){
        BufferedImage[] framesArray = new BufferedImage[framesCount];

        for (int i=0; i< framesCount; i++){
            framesArray[i] = sprSheet.getImage(i+colStart, row, 32, 32);
        }

        return framesArray;
    }

    //

    public void tick() {
        x += velX;
        y += velY;

        animation.update();

        if ( velX<0 && (velY<0 || velY>0 || velY==0) )
            setAnimation(moveLeft);
        else if ( velX>0 && (velY>0 || velY<0 || velY==0) )
            setAnimation(moveRight);
        else if (velY > 0 && velX==0)
            setAnimation(moveDown);
        else if (velY < 0 && velX==0)
            setAnimation(moveUp);
        else if ( velX == 0 && velY ==0 ) animationReset();

        counter++;

        if (counter>= 20) {
            move = r.nextInt((10 - -10) + 1) + -10;
            counter=0;
        }

        for (int i=0; i<handler.object.size(); i++){
            GameObject tempObj = handler.object.get(i);

            //Collision
            if (tempObj.getId() == ID.Block || tempObj.getId() == ID.GrassEdge){

                if (getBoundsBig().intersects(tempObj.getBounds())){
                    velX *= -1;
                    velY *= -1;
                    x += (velX * 2);
                    y += (velY * 2);

                    if (getBoundsBig().intersects(tempObj.getBounds())){
                        velX *= -1;
                        velY *= -1;
                        x += (velX * 2);
                        y += (velY * 2);
                    }
                }
                else if (move == 0){
                    animationReset();
                    velX = (r.nextInt(2 - -1) + -1);
                    velY = (r.nextInt(2 - -1) + -1);
                }
            }



            //with com.mike.tds.gameobject.Player
            if (tempObj.getId() == ID.Player) {

                if (getBounds().intersects(tempObj.getBounds())) {
                    velX *= -1;
                    velY *= -1;
                    x += velX;
                    y += velY;

                    handler.hitPlayer();

                    //Hit (move) player a little
                    if ( x <= tempObj.getX())
                        tempObj.setVelX(5);
                    else if ( x > tempObj.getX())
                        tempObj.setVelX(-5);
                    if ( y <= tempObj.getX())
                        tempObj.setVelY(5);
                    else if ( y > tempObj.getX())
                        tempObj.setVelY(-5);

                }
            }
            //Bullet hit
            if (tempObj.getId() == ID.Bullet ){
                if (getBounds().intersects(tempObj.getBounds())){
                    hp -= 50;
                    handler.removeObject(tempObj);
                }
            }

        }

        //if hp 0 it dies
        if (hp <= 0 || x<0 || x>32*64 || y<0 || y > 32*40) {
            playSound();
            handler.removeObject(this);
        }
    }

    private void setAnimation(Animation animation){
        this.animation = animation;
        animation.start();
    }

    private void animationReset(){
        animation.stop();
        animation.reset();

        animation = stand;
        //animation.directionStanding(directionStand);
    }

    public void render(Graphics g) {
        g.drawImage(animation.getSprite(), x, y, null);

/*        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.magenta);
        g2d.draw(getBoundsBig());*/
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }

    public Rectangle getBoundsBig(){
        return new Rectangle(x-16, y -16, 64, 64);
    }

}
