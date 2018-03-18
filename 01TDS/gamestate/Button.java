package com.mike.tds.gamestate;

import java.awt.image.BufferedImage;

public class Button {

    private BufferedImage buttonImage = null;
    private int btnX, btnY, btnW, btnH;
    private volatile boolean isActive;
    private String name;

    public Button(BufferedImage image, int x, int y, String name){
        this.buttonImage = image;
        this.btnX = x;
        this.btnY = y;
        this.btnW = buttonImage.getWidth();
        this.btnH = buttonImage.getHeight();
        this.isActive = false;
        this.name = name;
    }

    public Button(Button newButton){
        this.buttonImage = newButton.buttonImage;
        this.btnX = newButton.getBtnX();
        this.btnY = newButton.getBtnY();
        this.btnW = newButton.buttonImage.getWidth();
        this.btnH = newButton.buttonImage.getHeight();
        this.isActive = false;
        this.name = newButton.getName();
    }

    public BufferedImage getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(BufferedImage image){
        this.buttonImage = image;
    }
    public void setIsActive(boolean b) {
        this.isActive = b;
    }

    public boolean getIsActive(){
        return isActive;
    }
    public int getBtnX() {
        return btnX;
    }

    public void setBtnX(int btnX) {
        this.btnX = btnX;
    }

    public int getBtnY() {
        return btnY;
    }

    public void setBtnY(int btnY) {
        this.btnY = btnY;
    }

    public int getBtnW() {
        return btnW;
    }

    public void setBtnW(int btnW) {
        this.btnW = btnW;
    }

    public int getBtnH() {
        return btnH;
    }

    public void setBtnH(int btnH) {
        this.btnH = btnH;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Button: "+ this.name + ", active: "+ getIsActive();
    }
}
