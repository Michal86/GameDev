package com.mike.tds.main;

import com.mike.tds.gamestate.GameStateManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{

    private GameStateManager state;

    public KeyInput(GameStateManager state){
        this.state = state;
    }

    public void keyPressed(KeyEvent e) {
        state.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        state.keyReleased(e);
    }

}
