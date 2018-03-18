package com.mike.tds.sounds;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.*;


public class AudioPlayer implements Runnable{

    private Thread thread;
    private boolean isRunning = false;
    private AdvancedPlayer advancedPlayer;
    private BufferedInputStream bis;
    private InputStream inputStream;

    public AudioPlayer() {
        //isRunning = true;
        start();
    }

    //--------------------------------
    private synchronized void start(){
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){
        if (!isRunning)
            return;

        isRunning = false;
        advancedPlayer.close();
    }

    public void close(){
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning){
            try {

                inputStream = getClass().getResourceAsStream("/sound/ancient_path2.mp3");
                this.bis = new BufferedInputStream(inputStream);

                advancedPlayer = new AdvancedPlayer(bis);
                advancedPlayer.play(0,3800);

            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
        stop();
    }


    public static void main(String[] args){
        //System.out.println("Sound class tests.");
        new AudioPlayer();
    }

}
