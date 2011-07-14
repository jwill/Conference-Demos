package main.groovy

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: May 1, 2010
 * Time: 5:56:38 PM
 * To change this template use File | Settings | File Templates.
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SimpleGame extends BasicGame{

    public SimpleGame() {
        super("Slick2D - SimpleGame")
    }

    @Override
    public void init(GameContainer gc) throws SlickException {

    }

    @Override
    public void update(GameContainer gc, int delta)throws SlickException {

    }

    public void render(GameContainer gc, Graphics g) throws SlickException {

    }

    public static void main(args) throws SlickException {
         def app = new AppGameContainer(new SimpleGame())

         app.setDisplayMode(800, 600, false)
         app.start()
    }
}