package minitennis;

import java.awt.*;
import java.awt.geom.Ellipse2D;


public class Ball {

    public final static Color OLIVE = new Color( 107, 142, 35 );
    private static final int DIAMETER = 30;
    private Game game;
    private int x = 0;
    private int y = 0;
    private int middle = 0;
    //Directions +1 right/down; -1 left/up
    private int dirX = 1;
    private int dirY = 1;

    public Ball(Game game){
        this.game = game;
    }

    protected void move(){
        boolean changeDirection = true;
        middle = y+DIAMETER/2;
        //if border is touched then change direction
        if( x+dirX < 0 )
            dirX = game.speed;
        else if( x+dirX > game.getWidth()-DIAMETER )
            dirX = -game.speed;
        else if( y+dirY < 0)
            dirY = game.speed;

        //if get through then Game Over
        else if( y+dirY > game.getHeight()-30)
            game.gameOver();

        //if collision occurred
        else if( collision() ){
            //To bounce in oposite direction while hitting edge (more or less)
            if ( middle+5+game.speed > game.crossbar.getTopBound() &&
                    (middle+game.speed <= game.crossbar.getTopBound()+5) ) {
                dirY *= -1;
                dirX *= -1;
                game.addPoint();
                y = y-game.speed;
            }
            else if ( (middle+5) > game.crossbar.getTopBound()+5 ){
                dirX *= -1;
            }
            else {
                dirY *= -1;
                game.addPoint();
                y = game.crossbar.getTopBound() - DIAMETER;
            }
        } else
            changeDirection = false;

        if (changeDirection)
            Sound.playBall();

        x += dirX;
        y += dirY;
    }

    public void paint(Graphics2D gr2d){
        gr2d.setColor(OLIVE);
        gr2d.fillOval(x,y,DIAMETER,DIAMETER);
    }

    public Ellipse2D.Double getBounds(){
        return new Ellipse2D.Double(x, y,DIAMETER,DIAMETER);
    }

    private boolean collision(){
        return ( this.getBounds().intersects(game.crossbar.getBounds()) );

    }
}
