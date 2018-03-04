/**
 * User: Michał Radzewicz
 * Date: 2018-02-07
 * "Mini Tennis" 'game'
 * It's pretty simple but in the past a rushed for big project and always ended up with unfinished game.
 * Big shout-out to edu4java.com for inspiring me to write it myself and start/continue game development.
 */
package minitennis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings("serial")
public class Game extends JPanel{
    protected Ball ball = new Ball(this);
    protected Crossbar crossbar = new Crossbar(this);
    private Sound snd = new Sound();
    private int points = 0;
    protected int speed = 1;

    //Handle keyboard reading
    public Game(){
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                crossbar.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                crossbar.keyReleased(e);
            }
        });
        setFocusable(true);
        Sound.playBG();
    }

    //Handle movements
    private void move() {
        ball.move();
        crossbar.move();
    }
    //Add point and speed up the ball every 3-rd point
    public void addPoint(){
        this.points++;
        if( getScore()%3 == 0) speedUp();
    }

    public int getScore(){
        return this.points;
    }

    public void speedUp(){
        this.speed++;
    }
    //Handle paintings
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D gr2d = (Graphics2D) g;
        //makes the borders of the figures smoother
        gr2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //ball
        ball.paint(gr2d);
        //crossbar
        crossbar.paint(gr2d);

        //score
        gr2d.setColor(Color.BLACK);
        gr2d.setFont(new Font("Verdana", Font.BOLD, 20));
        gr2d.drawString(String.valueOf(getScore()), 10, 30);
    }

    //Shows GameOver status
    public void gameOver(){
        //System.out.println("Game Over.");
        Sound.playEnd();
        Sound.stopBG();

        JOptionPane.showMessageDialog(this, "Your score is: " + getScore() ,
                "Game Over", JOptionPane.YES_NO_OPTION);

        System.exit(ABORT);
    }


    //**************************************
    public static void main(String[] args) {
        Game game = new Game();
        //Setting window frame
        JFrame frame = new JFrame("Mini Tennis");
        frame.setSize(320, 450);
        frame.setBackground(Color.WHITE);
        frame.add(game);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //==== move the ball ====
        while(true){
            game.move();
            game.repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//END OF while loop

    }//END OF main
}
