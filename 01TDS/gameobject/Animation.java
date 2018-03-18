package com.mike.tds.gameobject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    private List<Frame> framesList = new ArrayList<Frame>();

    private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame;               // animations current frame
    private int animationDirection;         // animation direction (i.e counting forward or backward)
    private int totalFrames;                // total amount of frames for your animation
    private boolean attackType = false;
    private boolean releaseArrow = false;
    private Player player;
    private Enemy enemy;
    private Wizard wizard;
    private boolean stopped;                // has animations stopped

    public Animation(BufferedImage[] frames, int frameDelay, GameObject getObj){
        this.frameDelay = frameDelay;
        this.stopped = true;

        if (getObj.id == ID.Player)
            this.player = (Player) getObj;

        else if (getObj.id == ID.Enemy){
            if (getObj.equals(enemy))
                this.enemy = (Enemy) getObj;
            else if (getObj.equals(wizard))
                this.wizard = (Wizard) getObj;
        }


        for (int i = 0; i < frames.length ; i++) {
            addFrame(frames[i], frameDelay);
        }

        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.animationDirection = 1;
        this.totalFrames = this.framesList.size();
    }

    private void addFrame(BufferedImage frame, int duration){
        if (duration <= 0){
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        framesList.add( new Frame(frame, duration) );
        currentFrame = 0;
    }

    public BufferedImage getSprite(){
        return framesList.get(currentFrame).getFrame();
    }

    public void update(){
        if (!stopped){
            frameCount++;

            if (frameCount > frameDelay){
                frameCount = 0;
                currentFrame += animationDirection;

                if (currentFrame > totalFrames -1){
                    currentFrame = 0;
                }

                else if (currentFrame < 0)
                    currentFrame = totalFrames -1;

                if (ifAttack() && currentFrame==0) {
                    stop();
                    setAnimationAttack(false);
                    player.setAttackCooldown(false);

                    //com.mike.tds.gameobject.Animation is done - shoot
                    this.releaseArrow = true;
                    //System.out.println("Attack");
                }
            }
        }


        if (this.releaseArrow) {
            player.setArrowToRelease(1);
            this.releaseArrow = false;
        }

    }//update

    public void setAnimationAttack(boolean b){
        this.attackType = b;
    }
    public boolean ifAttack(){
        return attackType;
    }
    public boolean getCoolDown(){
        return this.attackType;
    }

    public void directionStanding(int dir){
        currentFrame = dir;
    }

    public void start(){
        if (!stopped)
            return;
        if (framesList.size() == 0)
            return;

        stopped = false;
    }

    public void stop(){
        if (framesList.size() == 0)
            return;

        stopped = true;
    }

    public void restart(){
        if (framesList.size() == 0)
            return;

        stopped = false;
        currentFrame = 0;
    }

    public void reset(){
        stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
    }
}
