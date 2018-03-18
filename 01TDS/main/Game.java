package com.mike.tds.main; /**
 * User: Michał Radzewicz
 * Date: 2018-03-04
 * Main class -
 */

import com.mike.tds.gameobject.*;
import com.mike.tds.gamestate.GameStateManager;
import com.mike.tds.sounds.AudioPlayer;


import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game extends Canvas implements Runnable{

    protected static ExecutorService pool;
    private boolean isRunning = false;
    private GameFrame gameFrame;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private GameStateManager gsm;

    public Game(){
        this.gameFrame = new GameFrame(1000, 563, "Lone Ranger TDS", this);

        //start();
    }

    private synchronized void start(){
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    protected synchronized void stop(){
        if (!isRunning)
            return;

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //GAME LOOP
    public void run() {
        isRunning = true;

        this.gsm = new GameStateManager();

        this.addMouseListener(new MouseInput(gsm));
        this.addKeyListener(new KeyInput(gsm));

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0, ticks = 0;

        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            boolean shouldRender = false;

            while(delta >= 1){
                tick(); //movements/updates
                ticks++;
                delta--;
                shouldRender = true;
            }
            if (shouldRender) {
                render();
                frames++;
            }
            if (System.currentTimeMillis() - timer >= 1000){
                //System.out.println("Ticks: "+ticks+", Frames: " + frames);
                timer += 1000;
                frames = 0;
                ticks = 0;
            }
        }//END OF WHILE
        stop();
    }

    //updates ~60-times/sec
    public void tick(){
        gsm.tick();
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        //===================================

        gsm.render(g);

        //===================================
        g.dispose();
        bs.show();
    }


    public static void main(String[] args){
        Runnable r1 = new Game();

        pool = Executors.newFixedThreadPool(2);
        pool.execute(r1);
        //pool.execute(r2);

        pool.shutdown();
    }

}
