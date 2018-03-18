package com.mike.tds.gameobject;

import com.mike.tds.gamestate.MapHandler;
import com.mike.tds.main.Handler;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Player extends GameObject{

    private final int HEIGHT = 48;
    protected MapHandler mapHandler;
    protected Handler handler;
    private volatile int trigger, immuneToAttacks, immuneTicks; //immuneTicks- 60 -> ~1s
    private int arrowsSpeed;
    private int mouseX, mouseY;
    private volatile Clip hurtSound;
    private volatile boolean isVisible = true;
    //Player walk animation
    private BufferedImage[] walkingRight, walkingLeft, walkingDown, walkingUp;
    private BufferedImage[] standing;
    private BufferedImage[] falling = {
            sprSheet.getImage(4,13,32,HEIGHT),
            sprSheet.getImage(5,13,32,HEIGHT),
            sprSheet.getImage(6,13,32,HEIGHT)
    };
    private BufferedImage[] attackingUp = {
            sprSheet.getImage(1,11,32,HEIGHT),
            sprSheet.getImage(2,11,43,HEIGHT),
            sprSheet.getImage(4,11,43,HEIGHT)
    };
    private BufferedImage[] attackingDown;
    private BufferedImage[] attackingRight = {
            sprSheet.getImage(2,1,32,HEIGHT),
            sprSheet.getImage(1,15,32,HEIGHT),
            sprSheet.getImage(2,15,36,HEIGHT)
    };
    private BufferedImage[] attackingLeft = {
            sprSheet.getImage(3,1,32,HEIGHT),
            sprSheet.getImage(4,15,36,HEIGHT),
            sprSheet.getImage(6,15,32,HEIGHT)
    };
    private Animation dead;
    // These are animation states
    private Animation walkRight, walkLeft, walkDown, walkUp, stand,
                      attackUp, attackDown, attackRight, attackLeft, fallDown;

    // This is the actual animation
    private volatile GameObject tempInteraction = null;
    protected Animation animation;
    protected volatile boolean attack = false, attackCooldown = false, alive;
    protected int arrowReleased;
    private  float speed;
    private int directionStand;

    public Player(int x, int y, ID id, MapHandler mapHandler, Handler handler, SpriteSheet sprSheet){
        super(x, y, id, sprSheet);
        this.mapHandler = mapHandler;
        this.handler = handler;
        this.width = 32;
        this.height = HEIGHT;
        this.alive = true;
        this.immuneToAttacks = 0;   //max is immuneTicks [around 1s = 60ticks()]
        this.immuneTicks = 30;
        this.arrowsSpeed = 4;
        this.speed = 4;
        setArrowToRelease(0);
        setHurtSound();
        this.trigger = 0;
        //Load frames
        walkingRight = setFrames(sprSheet, 3, 6);
        walkingLeft = setFrames(sprSheet, 5, 6);
        walkingDown = setFrames(sprSheet, 7, 6);
        walkingUp = setFrames( sprSheet, 9, 6);
        standing = setFrames(sprSheet,1, 4);
        attackingDown = setFrames(sprSheet, 13,3);

        //Set states of animation (loaded frames to List<Frames>)
        walkRight = new Animation(walkingRight, 6, this);
        walkLeft = new Animation(walkingLeft, 6, this);
        walkDown = new Animation(walkingDown, 6, this);
        walkUp = new Animation(walkingUp, 6, this);
        stand = new Animation(standing, 6, this);
        fallDown = new Animation(falling, 10, this);
        dead = new Animation(new BufferedImage[]{sprSheet.getImage(5,2,48,32)},6,this);
        //Attacks
        attackUp = new Animation(attackingUp, arrowsSpeed, this);
        attackDown = new Animation(attackingDown,arrowsSpeed, this);
        attackRight = new Animation(attackingRight,arrowsSpeed, this);
        attackLeft = new Animation(attackingLeft,arrowsSpeed, this);

        this.animation = stand;
        directionStand = 0;
    }
    //COPY
    public Player(GameObject copyFrom){
        super(copyFrom.getX(),copyFrom.getY(),copyFrom.getId(),copyFrom.getSprSheet());
    }

    private BufferedImage[] setFrames(SpriteSheet sprSheet, int row, int framesCount){
        BufferedImage[] framesArray = new BufferedImage[framesCount];

        for (int i=0; i< framesCount; i++){
            framesArray[i] = sprSheet.getImage(i+1, row, 32, HEIGHT);
        }

        return framesArray;
    }

    //UPDATES
    public void tick(){
        //===== EVENTS =====
        //IF UPDATED -> MAKE CLASS TO HANDLE VARIETY OF EVENTS DEPENDING ON LVL etc.
        //Trigger counts frames [just to do some animation delay]
        if(trigger > 0 && trigger <12) {
            if (trigger == 11){
                velX -= 1;
                velY += 35;
                isVisible = false;
                //Display text
                mapHandler.startEvent(true);
                mapHandler.setEventTick(1);
                //CHANGE TO MAP 2
                mapHandler.setMapAsFinished(true);
            }
            animation = fallDown;
            trigger++;
            x += 0;
            y += 1;
            animation.update();
        }
        //===== NORMAL =====
        else if (mapHandler.getPlayersHp() > 0) {
            x += velX;
            y += velY;

            animation.update();
            immuneSystem();
            collision();

            //if animation is done and got arrow
            if (releaseArrow() == 1) {
                handler.addObject(new Bullet
                        (this.getX() + 16, this.getY() + 16, ID.Bullet, handler,
                                this, mouseX, mouseY, sprSheet));
                mapHandler.setArrows(-1);
                setArrowToRelease(0);
            }

            //Set fire arrow attack and make some kind of "cooldown"
            if (fireArrow() && !attackCoolDown()) {
                animationReset();

                //Pick attack direction
                if (directionStand == 0)
                    animation = attackDown;
                else if (directionStand == 1)
                    animation = attackRight;
                else if (directionStand == 2)
                    animation = attackLeft;
                else if (directionStand == 3)
                    animation = attackUp;

                animation.directionStanding(0);

                animation.setAnimationAttack(true);
                animation.start();

                this.attackCooldown = true;
            }

            //All moves beside attack
            if (handler.isDown() && !attackCoolDown()) {
                velY = speed;
                animation = walkDown;
                animation.start();
                directionStand = 0;
            } else if (!handler.isUp()) {
                velY = 0;
            }

            if (handler.isUp() && !attackCoolDown()) {
                velY = -speed;
                animation = walkUp;
                animation.start();
                directionStand = 3;

            } else if (!handler.isDown()) {
                velY = 0;
            }

            if (handler.isRight() && !attackCoolDown()) {
                velX = speed;
                animation = walkRight;
                animation.start();
                directionStand = 1;
            } else if (!handler.isLeft()) {
                velX = 0;
            }

            if (handler.isLeft() && !attackCoolDown()) {
                velX = -speed;
                animation = walkLeft;
                animation.start();
                directionStand = 2;
            } else if (!handler.isRight()) {
                velX = 0;
            }

            if (!handler.isRight() && !handler.isLeft() && !handler.isDown()
                    && !handler.isUp() && !attackCoolDown()) {
                animationReset();
            }

        }//if hp>0
        else if (mapHandler.getPlayersHp() <=0) {
            animation.stop();
            animation.reset();
            animation = dead;
            animation.start();
            mapHandler.gameOver();
        }
    }
    //===============================================

    private void animationReset(){
        animation.stop();
        animation.reset();

        animation = stand;
        animation.directionStanding(directionStand);
    }

    public synchronized boolean immuneSystem(){
        if (immuneToAttacks >= immuneTicks){ //60 ticks ~ 1s
            return false;
        }
        else{
            immuneToAttacks++;
            return true;
        }
    }

    //ATTACK
    public void setArrack(boolean b){
        this.attack = b;
    }

    private boolean fireArrow(){
        return attack;
    }

    public boolean attackCoolDown(){
        return animation.getCoolDown();
    }
    protected void setAttackCooldown(boolean b){
        this.attackCooldown = b;
        setArrack(b);
    }

    protected void setArrowToRelease(int arrowAmount){
        this.arrowReleased = arrowAmount;
    }
    protected int releaseArrow(){
        return arrowReleased;
    }

    public void setMouseX(int mX){
        this.mouseX = mX;
    }
    public void setMouseY(int mY){
        this.mouseY = mY;
    }

    //========= COLLISIONS =========
    private void collision(){
        for (int i=0; i<handler.object.size(); i++){
            GameObject tempObj = handler.object.get(i);

            if (tempObj.getId() == ID.Block || tempObj.getId() == ID.Doors
                    || tempObj.getId() == ID.GrassEdge || tempObj.getId() == ID.Enemy){
                //--- If BOUNDS intersects ---
                if ( this.getBounds().intersects(tempObj.getBounds()) ){
                    //enemy
                    if (tempObj.getId() == ID.Enemy && !immuneSystem()){
                        gotHurt();
                        immuneToAttacks = 0;

                        velX *= -4;
                        velY *= -4;

                        x += velX;
                        y += velY;
                        if ( this.getBounds().intersects(tempObj.getBounds()) ){
                            velX *= -4;
                            velY *= -4;

                            x += velX;
                            y += velY;
                        }
                    }
                    //Things
                    else {
                        x += velX * -1;
                        y += velY * -1;
                    }

                    //==== FOR LVL1 ENDING ===
                    if (tempObj.getId() == ID.Doors) {
                        CastleEntry tmp = (CastleEntry) tempObj;
                        CastleEntry stairt = (CastleEntry) tempInteraction;

                        if (tmp.isOpened()) {
                            stairt.setTrap();

                            animation.reset();
                            animation = fallDown;
                            animation.start();
                            trigger ++;
                        }
                    }

                }//END_IF_BOUNDS
            }
            //--- If "get" the crate/pot ---
            if (tempObj.getId() == ID.Crate || tempObj.getId()==ID.Potion){
                if ( this.getBounds().intersects(tempObj.getBounds()) ){

                    if (tempObj.getId() == ID.Crate) {
                        mapHandler.setArrows(10);
                    }
                    else if (tempObj.getId()==ID.Potion){
                        mapHandler.drinkHpPot(20);
                    }
                    handler.removeObject(tempObj);
                    tempObj.playSound();
                }
            }
        }//END_OF_LOOP_OBJECTS
    }

    //DISPLAY
    public void render(Graphics g){
        if (isVisible)
            g.drawImage(animation.getSprite(), x, y, null);

/*        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.magenta);
        g2d.draw(getBounds());*/
    }

    public Rectangle getBounds(){
        return new Rectangle(x+2, y+18, 27, 29);
    }

    //keep it from accessing too many times
    public synchronized void gotHurt(){
        if (mapHandler.getPlayersHp() > 0) {
            mapHandler.setPlayersHpAfterHit(-10);
        }
        else{
            int tmp = mapHandler.getPlayersHp();
            mapHandler.setPlayersHpAfterHit(-tmp);
            this.alive = false;
        }
        playHurt();
    }

    public synchronized boolean isPlayerAlive(){
        return alive;
    }

    public synchronized void setInteractionObject(GameObject tmp){
        this.tempInteraction = tmp;
    }

    //===============SOUNDS =====================
    private synchronized void playHurt(){
        hurtSound.loop(1);
    }

    //hurt sound - I'm keeping it, even though I have it in GameObject //bad??
    private void setHurtSound(){
        AudioInputStream ais = null;
        try {

        InputStream inputStream = this.getClass().getResourceAsStream("/sound/hurt2.wav");
        ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(inputStream));

        hurtSound = AudioSystem.getClip();
        hurtSound.open(ais);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
