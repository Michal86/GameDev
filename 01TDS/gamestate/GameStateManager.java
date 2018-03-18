package com.mike.tds.gamestate;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class GameStateManager {

    private Stack<GameState> states;

    public GameStateManager() {
        states = new Stack<>();
        states.push(new MainMenu(this));
    }

    public void statePush(GameState state){
        //System.out.println("Pushing new state: "+ state.getClass().getSimpleName());
        states.push(state);
    }

    public void statePop(boolean displayInfo){
        this.states.pop();

        if (displayInfo)
            this.states.peek().init();
    }

    public synchronized GameState getState(int index){
        if (index>=0 && !states.isEmpty() && index<states.size())
            return this.states.elementAt(index);
        else
            return null;
    }

    public void tick(){
        states.peek().tick();
    }

    public void render(Graphics g){
        states.peek().render(g);
    }

    public synchronized void mousePressed(MouseEvent e){
        states.peek().mousePressed(e);
    }

    public void keyPressed(KeyEvent e){
        states.peek().keyPressed(e);
    }

    public void keyReleased(KeyEvent e){
        states.peek().keyReleased(e);
    }

}
