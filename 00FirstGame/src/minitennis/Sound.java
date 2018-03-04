package minitennis;

import javax.sound.sampled.*;

public class Sound {
    private static final String BG   = "backgroundLooping.wav";
    private static final String BALL = "ball.wav";
    private static final String END  = "gameOver.wav";
    private static Clip bgClip;
    private static Clip ballClip;
    private static Clip gameOverClip;

    public Sound(){
        MyFilePath myBg = new MyFilePath();
        try {
            //--- set background sound ---

            bgClip = AudioSystem.getClip();
            bgClip.open(myBg.getSound(BG));

            //--- set ball sound ---
            ballClip = AudioSystem.getClip();
            ballClip.open(myBg.getSound(BALL));

            //--- set gameOver sound ---
            gameOverClip = AudioSystem.getClip();
            gameOverClip.open(myBg.getSound(END));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected static void playBG(){
        bgClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    protected static void playBall(){
        ballClip.loop(1);
    }

    protected static void playEnd(){
        gameOverClip.loop(1);
    }
    protected static void stopBG(){
        bgClip.stop();
    }

}
