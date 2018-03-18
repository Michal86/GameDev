package com.mike.tds.gameobject;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class GameObject {

    protected int x, y,width,height=0;      //location
    protected float velX = 0, velY = 0; //speed at direction object is going
    protected ID id;
    protected SpriteSheet sprSheet;
    private volatile Clip sound;
    private volatile GameObject interactionObject;

    public GameObject(int x, int y, ID id, SpriteSheet sprSheet){
        this.x = x;
        this.y = y;
        this.id = id;
        this.sprSheet = sprSheet;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    //--- getters & setters ---

    public SpriteSheet getSprSheet() {
        return sprSheet;
    }

    public ID getId(){
        return id;
    }

    public void setId(ID id){
        this.id = id;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setInteractionObject(GameObject temp){
        this.interactionObject = temp;
    }

    public void setObjectSound(String soundName){
        AudioInputStream ais = null;
        try {

            InputStream inputStream = this.getClass().getResourceAsStream("/sound/"+soundName+".wav");
            ais = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(inputStream));

            this.sound = AudioSystem.getClip();
            sound.open(ais);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public synchronized void playSound(){
        sound.start();
    }
}
