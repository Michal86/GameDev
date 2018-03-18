package com.mike.tds.gamestate;

import com.mike.tds.gameobject.BufferedImageLoader;
import com.mike.tds.gameobject.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapEvents {

    private int mapLevel;
    private volatile boolean startEvent;
    private volatile int eventCounter;
    private EventTexts textsContainer;
    private volatile boolean nextMap;
    private Font txtFont;
    private Color fontColor;
    private BufferedImage boxImage = null;
    // mapEvents [mapLevels][events] -> true if have event
    // can set true to specific event, but not now
    //private boolean mapEvents[][] = new boolean[2][2];

    public MapEvents(int mapLevel, Font txtFont){
        BufferedImageLoader loader = new BufferedImageLoader();
        this.boxImage = loader.loadImage("/eventTxtBox.png");
        textsContainer = new EventTexts();

        this.mapLevel = mapLevel;
        nextMap = false;
        //-----
        setStartEvent(false);
        this.fontColor = new Color(10,10,25);
        this.txtFont = txtFont;
        //-----
    }

    //===== GET TEXT ACCORDING TO EVENT =====
    private synchronized String eventText(int eventCounter){
        return textsContainer.getTxt(mapLevel, eventCounter);
    }

    //===================================================
    public synchronized void tick(int nextEvent){
        if (eventStarted()) {
            if (nextEvent == 1) {
                eventCounter++;
            }
            else if (nextEvent == -1) {
                setEventCounter(-1);
            }
            else if (nextEvent == -2){
                setEventCounter(-2);
            }
        }
    }

    public void render(Graphics g) {
        if (eventStarted()){
            g.drawImage(boxImage,0,275,null);
            g.setFont(txtFont);
            txtFont = txtFont.deriveFont(Font.PLAIN,18);
            g.setColor(fontColor);
            drawString(g, eventText(eventCounter), 40,300);
        }
    }
    //===================================================

    private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight()+2);
    }
    //===================================================
    public synchronized void setMapLevel(int mapLevel){
        this.mapLevel = mapLevel;
    }

    public synchronized boolean getNext(int next){
        if (next == 1)
            return true;
        else
            return false;
    }
    public synchronized void setEventCounter(int number){
        this.eventCounter = number;
    }

    public synchronized void setStartEvent(boolean state){
        this.startEvent = state;
    }

    public synchronized boolean eventStarted() {
        return startEvent;
    }
}


