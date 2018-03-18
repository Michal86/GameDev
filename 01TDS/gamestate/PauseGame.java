package com.mike.tds.gamestate;

import com.mike.tds.gameobject.BufferedImageLoader;
import com.mike.tds.sounds.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PauseGame extends GameState{

    private MainMenu menu;
    private Map<String, Button> buttons;
    private BufferedImage pauseImage = null,
                resumeBtnImage = null;
    private volatile boolean gamePaused = false;
    private volatile AudioPlayer bgSound;

    public PauseGame(GameStateManager gsm) {
        super(gsm);
    }

    public PauseGame(GameStateManager gsm, MainMenu menu, AudioPlayer bgSound){
        this(gsm);
        this.menu = menu;
        BufferedImageLoader loader = new BufferedImageLoader();
        this.pauseImage = loader.loadImage("/Pause.png");
        this.resumeBtnImage = loader.loadImage("/resume_button.png");
        //load button to my HashMap - take coords from MainMenu
        getButtons();
        this.bgSound = bgSound;
        init();
    }

    private void getButtons(){
        this.buttons = new HashMap<>();

        //Keep same coords as play button
        Button resume = menu.getButton("play");
        resume.setButtonImage(resumeBtnImage);
        resume.setName("resume");
        this.buttons.put("resume", resume);

        Button quit = menu.getButton("quit");
        this.buttons.put(quit.getName(), quit);
    }

    @Override
    public void init() {
        if (gamePaused)
            resumeGame();
        else
            pauseGame();
    }

    private synchronized void resumeGame(){
        if (!gamePaused)
            return;

        gamePaused = false;
        gsm.statePop(false);
    }

    private synchronized void pauseGame(){
        if (gamePaused)
            return;

        gamePaused = true;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

        if (gamePaused){
            g.drawImage(pauseImage,0,0,null);

            g.drawImage(buttons.get("resume").getButtonImage(),buttons.get("resume").getBtnX(),
                    buttons.get("resume").getBtnY(),null);

            g.drawImage(buttons.get("quit").getButtonImage(),buttons.get("quit").getBtnX(),
                    buttons.get("quit").getBtnY(),null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mX = e.getX();
        int mY = e.getY();


        for (Button currentBtn : buttons.values()) {
            int btX = currentBtn.getBtnX();
            int btY = currentBtn.getBtnY();

            if (mX > btX && mX < (btX + currentBtn.getBtnW()) &&
                        mY > btY && mY < (btY + currentBtn.getBtnH())) {
                ifButtonPressed(currentBtn);
            }

        }//END_OF_FOR
    }

    private void ifButtonPressed(Button btn){
        if (btn.getName().equals("resume"))
            init();

        else if (btn.getName().equals("quit")){
            if (bgSound != null)
                bgSound.stop();
            gsm.statePop(false);
            gsm.statePop(false);
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
