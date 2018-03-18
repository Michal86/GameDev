package com.mike.tds.gameobject;
import com.mike.tds.main.Handler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Wizard extends GameObject{

    //Store frames
    private BufferedImage[] movingUp, movingDown, movingRight, movingLeft, standing;
    //Set animations
    Animation moveDown, moveUp, moveRight, moveLeft, stand, animation;
    // ---
    private Handler handler;
    private int counter = 0;
    // ---
    private Player player; //to get coords
    private volatile int getPlayerX, getPlayerY;
    private volatile float goal_dist; //distance to Player
    private int coolDown = 0;
    Random r = new Random();
    int hp = 100;
    int move = 0;

    public Wizard(int x, int y, ID id, Handler handler, SpriteSheet sprSheet) {
        super(x, y, id, sprSheet);
        this.handler = handler;
        setObjectSound("enemy_hurt");

        //Set frames and animations
        movingUp = setFrames(sprSheet, 1, 3, 3);
        movingDown = setFrames(sprSheet, 1, 4, 3);
        movingRight = setFrames(sprSheet, 1,2,3);
        movingLeft = setFrames(sprSheet, 1,1,3);

        moveDown = new Animation(movingDown,5, this);
        moveUp = new Animation(movingUp, 5,this);
        moveRight = new Animation(movingRight, 5, this);
        moveLeft = new Animation(movingLeft, 5, this);
        stand = new Animation(
                standing = new BufferedImage[]{sprSheet.getImage(2, 4, 32, 32)},
                5,this);
        this.animation = stand;
        animation.start();

        this.width = 32;
        this.height = 32;
    }

    //Load frames to an array
    private BufferedImage[] setFrames(SpriteSheet sprSheet, int colStart, int row, int framesCount){
        BufferedImage[] framesArray = new BufferedImage[framesCount];

        for (int i=0; i< framesCount; i++){
            framesArray[i] = sprSheet.getImage(i+colStart, row, 32, 32);
        }

        return framesArray;
    }

    public synchronized void getPlayer(){
        this.player = (Player) handler.getPlayer();
    }

    private float getDelta(){
        if (player == null) getPlayer();

        getPlayerX = player.getX();
        getPlayerY = player.getY();
        int deltaX = getPlayerX - x;
        int deltaY = getPlayerY - y;

        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    public void tick() {
        if (player == null){
            getPlayer();
        }
        else if (player != null) {
            x += velX;
            y += velY;

            animation.update();

            if (velX < 0 && (velY < 0 || velY > 0 || velY == 0))
                setAnimation(moveLeft);
            else if (velX > 0 && (velY > 0 || velY < 0 || velY == 0))
                setAnimation(moveRight);
            else if (velY > 0 && velX == 0)
                setAnimation(moveDown);
            else if (velY < 0 && velX == 0)
                setAnimation(moveUp);
            else if (velX == 0 && velY == 0) animationReset();

            counter++;

            if (counter >= 25) {
                move = r.nextInt((10 - -10) + 1) + -10;
                counter = 0;
                coolDown++;
                //set attack cooldown
                if (coolDown == 3 && player.isPlayerAlive()) {
                    coolDown = 0;
                    //==== ATTACK if Player in range ====
                    goal_dist = getDelta();
                    if (goal_dist <= 300) {
                        animationReset();
                        velX = 0;
                        velY = 0;

                        handler.addObject(new Bullet(
                                this.getX() + 12, this.getY() + 12,
                                ID.Firaball, handler, this, getPlayerX + 16, getPlayerY + 16, sprSheet));
                    }
                }
            }
            //=========================================
            for (int i = 0; i < handler.object.size(); i++) {
                GameObject tempObj = handler.object.get(i);

                //Collision
                if (tempObj.getId() == ID.Block || tempObj.getId() == ID.GrassEdge) {

                    if (getBoundsBig().intersects(tempObj.getBounds())) {
                        velX *= -1;
                        velY *= -1;
                        x += (velX * 2);
                        y += (velY * 2);

                        if (getBoundsBig().intersects(tempObj.getBounds())) {
                            velX *= -1;
                            velY *= -1;
                            x += (velX * 2);
                            y += (velY * 2);
                        }
                    } else if (move == 0) {
                        animationReset();
                        velX = (r.nextInt(2 - -1) + -1);
                        velY = (r.nextInt(2 - -1) + -1);
                    }
                }

                //with Player
                if (tempObj.getId() == ID.Player) {

                    if (getBounds().intersects(tempObj.getBounds())) {
                        velX *= -1;
                        velY *= -1;
                        x += velX;
                        y += velY;

                        handler.hitPlayer();

                        //Hit (move) player a little
                        if (x <= tempObj.getX())
                            tempObj.setVelX(5);
                        else if (x > tempObj.getX())
                            tempObj.setVelX(-5);
                        if (y <= tempObj.getX())
                            tempObj.setVelY(5);
                        else if (y > tempObj.getX())
                            tempObj.setVelY(-5);

                    }
                }
                //Bullet hit
                if (tempObj.getId() == ID.Bullet) {
                    if (getBounds().intersects(tempObj.getBounds())) {
                        hp -= 50;
                        handler.removeObject(tempObj);
                    }
                }

            }

            //if hp 0 it dies
            if (hp <= 0 || x < 0 || x > 32 * 64 || y < 0 || y > 32 * 40) {
                playSound();
                handler.removeObject(this);
            }
        }
    }//END_OF_TICK

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
