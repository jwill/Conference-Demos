/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: May 1, 2010
 * Time: 5:56:38 PM
 *
 * Adapted from Pulpcore Bubblemark demo
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Bubblemark extends BasicGame{
    def numBalls = 64
    def balls = [ ]
    boolean running = true

    public Bubblemark() {
        super("Bubblemark - SimpleGame")
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        numBalls.times {
            def b = new Ball()
            balls[it] = b
        }
    }

    @Override
    public void update(GameContainer gc, int delta)throws SlickException {
         if (numBalls != balls.size()) {
             balls.each {
                 it.endUse()
                 it = null
             }
         }
         balls.each {
             it.move(delta)
         }
         for (int i = 0; i < balls.size(); i++) {
                for (int j = i+1; j < balls.size(); j++) {
                    balls[i].doCollide(balls[j]);
                }
            }
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
         balls.each {
             Ball b = (Ball)it
             b.draw((Float)b._x, (Float)b._y)
         }
    }

    public static void main(args) throws SlickException {
         def app = new AppGameContainer(new Bubblemark())
         app.setTargetFrameRate 80
         app.setDisplayMode(800, 600, false)
         app.start()
    }
}