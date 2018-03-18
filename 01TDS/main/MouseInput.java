package com.mike.tds.main;

import com.mike.tds.gameobject.Camera;
import com.mike.tds.gameobject.Player;
import com.mike.tds.gameobject.SpriteSheet;
import com.mike.tds.gamestate.GameStateManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter{

    private GameStateManager state;
    //protected int mX, mY;

    public MouseInput(GameStateManager state){
        this.state = state;
    }

    public void mousePressed(MouseEvent e){
        state.mousePressed(e);
    }//END_OF_MOUSE


}
