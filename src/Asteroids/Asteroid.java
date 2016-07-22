package Asteroids;
import java.awt.geom.Ellipse2D;
/**
 * A non-controllable GameObject capable of collision with and destruction by the player
 * @author ckw017
 */
@SuppressWarnings("serial")
public class Asteroid extends GameObject{
	private double radius;       //The Asteroid's hitbox's radius in pixels
	private double rotationSpd;  //The Asteroid's sprite's rotation speed in degrees per frame

	/**
	 * Constructs an Asteroid object
	 * @param x - the x-coordinate of the Asteroid's center
	 * @param y - the y-coordinate of the Asteroid's center
	 * @param r - the radius of the Asteroid's hitbox in pixels
	 * @param angle - the angle of the Asteroid's movement in degrees
	 * @param v - the velocity of the Asteroid in pixels per frame
	 */
	public Asteroid(double x, double y, double r, double angle, double v){
		super(x, y, NEUTRAL);
		radius = r;
		hitbox = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
		xVelocity = Math.cos(Math.toRadians(angle)) * v;
		yVelocity = Math.cos(Math.toRadians(angle)) * v;
	}
	
	/**
	 * Sets the rotation speed of the Asteroid
	 * @param rs - the rotation speed, in pixels per frame
	 */
	public void setRotationSpeed(double rs){
		rotationSpd = rs;
	}
	
	@Override
	public void travel(){
		super.travel();
		rotate(rotationSpd);
	}
	
	@Override
	public void kill() {
		((Ellipse2D)hitbox).setFrame(0, 0, 0, 0);
		isAlive = false;
	}

	@Override
	public void adjustHitbox() {
		((Ellipse2D)hitbox).setFrame(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
	}

}