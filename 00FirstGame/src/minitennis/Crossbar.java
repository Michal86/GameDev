package minitennis;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class Crossbar {
    public final static Color ORANGERED = new Color( 255, 69, 0 );
    private static final int Y = 350;
    private static final int WIDTH = 60;
    private static final int HEIGHT = 10;
    private Game game;
    private int x = 50;
    private int moveX = 0;

    public Crossbar(Game game){
        this.game = game;
    }

    protected void move(){
        if( x+moveX > 0 && x+moveX < game.getWidth()-WIDTH )
            x += moveX;
    }

    public void paint(Graphics2D gr2d) {
        gr2d.setColor(ORANGERED);
        gr2d.fillRect(x, Y, WIDTH, HEIGHT);
    }

    //Handle keyListener
    public void keyPressed(KeyEvent e){
        if( e.getKeyCode() == KeyEvent.VK_LEFT )  moveX = -2;
        if( e.getKeyCode() == KeyEvent.VK_RIGHT ) moveX = 2;
        //System.out.println("keyPressed = "+KeyEvent.getKeyText(e.getKeyCode()));
    }
    public void keyReleased(KeyEvent e){
        moveX = 0;
    }

    public Rectangle2D getBounds(){
        return new Rectangle2D.Double(x, Y, WIDTH, HEIGHT);
    }

    public int getTopBound(){
        return Y;
    }
}
