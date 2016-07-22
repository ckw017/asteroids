package Asteroids;

import java.awt.geom.Ellipse2D;
/**
 * A GameObject intended to be used by the player to shoot Asteroids and Enemies
 * @author ckw017
 *
 */
@SuppressWarnings("serial")
public class Projectile extends GameObject {
	private double radius;   //The Projectile's hitbox's radius in pixels
	private double velocity; //The Projectile's velocity in pixels per frame
	private int lifetime;    //The Projectile's lifetime in frames
	private int age = 0;     //The Projectile's age in frames
	
	/**
	 * Constructs a basic Projectile intended for cloning at different locations and angles
	 * @param r - the radius of the Projectile's hitbox in pixels
	 * @param v - the velocity of the Projectile in pixels per frame
	 * @param l - the lifetime of the Projectile in frames
	 */
	public Projectile(double r, double v, int l){
		radius = r;
		velocity = v;
		lifetime = l;
	}
	
	/**
	 * Constructs an active Projectile
	 * @param x - the x-coordinate of the Projectile
	 * @param y - the y-coordinate of the Projectile
	 * @param r - the radius of the Projectile's hitbox
	 * @param angle - the angle of the Projectile's trajectory in degrees
	 * @param v - the velocity of the Projectile in pixels per frame
	 * @param l - the lifetime of the Projectile in frames
	 */
	public Projectile(double x, double y, double r, double angle, double v, int l){
		super(x, y, PLAYER);
		radius = r;
		velocity = v;
		lifetime = l;
		hitbox = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
		xVelocity = Math.cos(Math.toRadians(angle)) * velocity;
		yVelocity = Math.cos(Math.toRadians(angle)) * velocity;
	}
	
	/**
	 * Clones an existing Projectile and translates it to a new position and trajectory
	 * @param p - an existing Projectile intended to be cloned
	 * @param x - the new x-coordinate of the Projectile
	 * @param y - the new y-coordinate of the Projectile
	 * @param angle - the new angle of the Projectile
	 */
	public Projectile(Projectile p, double x, double y, double angle){
		super(x, y, PLAYER);
		radius = p.radius;
		velocity = p.velocity;
		lifetime = p.lifetime;
		hitbox = new Ellipse2D.Double(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
		xVelocity = Math.cos(Math.toRadians(angle)) * p.velocity;
		yVelocity = Math.sin(Math.toRadians(angle)) * p.velocity;
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
	
	@Override
	public void travel(){
		super.travel();
		age++;
		if(age > lifetime){
			kill();
		}
	}

}
