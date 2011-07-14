import org.newdawn.slick.Image
import java.awt.Insets

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: May 1, 2010
 * Time: 7:03:52 PM
 * 
 * License: The code is released under Creative Commons Attribution 2.5 License
 * (http://creativecommons.org/licenses/by/2.5/)
 * 
 * Adapted from Pulpcore Bubblemark demo
 *
 * @author rbair
 * 
 */
class Ball extends Image {
    private Model model = new Model()
    protected double _x = 0
    protected double _y = 0
    protected double _vx = 0
    protected double _vy = 0
    protected double _r = 0
    protected double _d = 0
    protected double _d2 = 0

    public Ball() {
        super("/resources/ball.png")
        //default provisioning
        // default provisioning
        this._x = (model.walls.right - model.walls.left - 2*model.ballRadius)*Math.random()
        this._y = (model.walls.bottom - model.walls.top - 2*model.ballRadius)*Math.random()
        this._vx = 2*model.maxSpeed*Math.random() - model.maxSpeed
        this._vy = 2*model.maxSpeed*Math.random() - model.maxSpeed
        this._r = model.ballRadius // d = 52 px
        this._d = 2*this._r
        this._d2 = this._d*this._d
    }

    public void move(delta) {
        this._x += this._vx * (delta/50f)
        this._y += this._vy * (delta/50f)
        // walls collisons

        // left
        if (this._x < model.walls.left && this._vx<0) {
            //this._vx += (this._x - walls.left)*elastity
            this._vx = -this._vx
        }
        // top
        if (this._y < model.walls.top && this._vy<0) {
            //this._vy += (this._y - walls.top)*elastity
            this._vy = -this._vy
        }
        // left
        if (this._x > model.walls.right - this._d && this._vx>0) {
            //this._vx += (this._x - walls.right + this._d)*elastity
            this._vx = -this._vx
        }
        // top
        if (this._y > model.walls.bottom - this._d && this._vy>0) {
            //this._vy += (this._y - walls.bottom + this._d)*elastity
            this._vy = -this._vy
        }
    }
    

    public boolean doCollide(Ball b) {
        // calculate some vectors
        double dx = this._x - b._x
        double dy = this._y - b._y
        double dvx = this._vx - b._vx
        double dvy = this._vy - b._vy
        double distance2 = dx*dx + dy*dy

        if (Math.abs(dx) > this._d || Math.abs(dy) > this._d)
            return false
        if (distance2 > this._d2)
            return false

        // make absolutely elastic collision
        double mag = dvx*dx + dvy*dy

        // test that balls move towards each other
        if (mag > 0)
            return false

        mag /= distance2

        double delta_vx = dx*mag
        double delta_vy = dy*mag

        this._vx -= delta_vx
        this._vy -= delta_vy

        b._vx += delta_vx
        b._vy += delta_vy

        return true
    }

    static final class Model {
        private Insets walls = new Insets(0,0, 600, 800)
        private double elasticity = -0.02
        private double ballRadius = 26
        private double maxSpeed = 3.0
    }
}
