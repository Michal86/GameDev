package com.mike.tds.main;

import com.mike.tds.gameobject.*;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;

public class Handler {

    public LinkedList<GameObject> object = new LinkedList<GameObject>();
    private volatile boolean objectListCleared = false;
    //for rendering purpose
    private Comparator<GameObject> renderSorter =
        (first, second) -> {
            if (first.getY()+first.getHeight() < second.getY()+second.getHeight())
                return -1;
            else if (first.getY()+first.getHeight() > second.getY()+second.getHeight())
                return 1;
            else
                return 0;
    };

    GameObject player = null;
    GameObject playerBackUp = null;
    CastleEntry doors = null;
    private int enemiesToKill = 0;
    //------------------------

    private boolean up = false, down = false, right = false, left =false;
    private boolean attack = false;

    public void tick(){
        enemiesToKill = 0;

        for(int i=0; i< object.size(); i++){
            GameObject tempObject = object.get(i);
            tempObject.tick();

            if (tempObject.getId() == ID.Enemy)
                enemiesToKill++;

        }

        if (enemiesToKill == 0 && doors!=null)
            doors.setOpened();

        //compare objects for rendering, which renders first
        object.sort(renderSorter);
    }

    public void render(Graphics g){
        for(int i=0; i<object.size(); i++){
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }
    }

    public synchronized int getEnemiesToKill(){
        return this.enemiesToKill;
    }

    //--- add object to the list ---
    public void addObject(GameObject tempObject) {
        object.add(tempObject);


        if (tempObject.getId() == ID.Player) {
            this.player = tempObject;
        }
        else if (tempObject.getId() == ID.Doors)
            doors = (CastleEntry) tempObject;
        else if (tempObject.getId() == ID.Stairs)
            this.player.setInteractionObject(tempObject);
        else if (tempObject.getId() == ID.Enemy) {
            enemiesToKill++;
        }
    }

    //--- remove object to the list ---
    public void removeObject(GameObject tempObject){
        object.remove(tempObject);
    }

    public synchronized GameObject getPlayer(){
        return player;
    }

    public void hitPlayer(){
        if (player != null) {
            Player temp = (Player) player;
            if (!temp.immuneSystem()) {
                temp.gotHurt();

            }
        }
    }

    public synchronized void clearOldObjects(){
        object.clear();
        object = new LinkedList<>();
        objectListCleared = true;
    }

    public synchronized boolean cleared(){
        return this.objectListCleared;
    }

    public synchronized void setCleared(){
        this.objectListCleared = false;
    }

    public boolean isAttacking() {
        return attack;
    }

    public void setAttack(boolean attack){
        this.attack = attack;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}