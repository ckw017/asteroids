package Asteroids;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * An abstract superclass for all vector related objects in the Asteroids game.
 * @author ckw017
 */
@SuppressWarnings("serial")
public abstract class GameObject extends JComponent{
	
	protected int health;            //The health of the GameObject; default = 1
    protected int allegiance;        //The allegiance of the GameObject, used to determine if damage is taken on collisions
    protected boolean isAlive;       //The status of the object; default = true
    protected double rotation;       //The rotation of the GameObject's sprite, in radians; default = 0
    protected double xVelocity;      //The horizontal velocity of the object, in pixels per frame; default = 0
    protected double yVelocity;      //The vertical velocity of the object, in pixels per frame; default = 0
    protected double xCenter;        //The center x-coordinate of the object's sprite and hitbox
    protected double yCenter;        //The center y-coordinate of the object's sprite and hitbox
    protected Shape hitbox;          //The hitbox of the enemy; default = null, defined in subclasses
    protected BufferedImage sprite;  //The sprite of the GameObject; default = null
    protected double radius;         //The GameObject's radius, in pixels
    
    //Allegiance IDs
    final static int PLAYER = 1;
    final static int ENEMY  = -1;
    final static int NEUTRAL = 0;
    
    /**
     * Empty Constructor
     */
    public GameObject(){
        
    }
    
    /**
     * Constructs a GameObject with a location and allegiance ID
     * @param x - the x-coordinate of the object's center
     * @param y - the y-coordinate of the object's center
     * @param alleg - the allegiance ID of the object
     */
    public GameObject(double x, double y, int alleg){
        xCenter = x;
        yCenter = y;
        allegiance = alleg;
        isAlive = true;
        rotation = 0;
        xVelocity = 0;
        yVelocity = 0;
        health = 1;
    }
    
    /**
     * Returns the hitbox of the Object
     * @return the hitbox of the Object
     */
    public Shape getHitbox(){
    	return hitbox;
    }
    
    /**
     * Sets the sprite of the object
     * @param i - the image sprite of the object
     */
    public void setSprite(BufferedImage i){
        this.sprite = i;
    }
    
    /**
     * Sets the rotation of the object
     * @param r - the desired rotation, in degrees
     */
    public void setRotation(double r){
        rotation = Math.toRadians(r % 360);
    }
    
    /**
     * Sets the health of the object
     * @param h - the desired health
     */
    public void setHealth(int h){
    	health = h;
    }
    
    /**
     * Damages the object's health
     * @param d - the amount of damage inflicted
     * @return the mortality state of the object after being damaged
     */
    public boolean damage(int d){
    	health -= d;
    	if(health <= 0){
    		kill();
    	}
    	return isAlive;
    }
    
    /**
     * Rotates the object's position according to input
     * @param r - the desired rotation, in degrees
     */
    public void rotate(double r){
        setRotation(Math.toDegrees(rotation) + r);
    }
    
    /**
     * Returns the allegiance ID of the object
     * @return the allegiance ID of the object
     */
    public int getAllegiance(){
        return this.allegiance;
    }
    
    /**
     * Returns the mortality status of the object
     * @return the boolean value of the isAlive variable
     */
    public boolean isAlive(){
        return this.isAlive;
    }
    
    public double getXCenter(){
    	return xCenter;
    }
    
    public double getYCenter(){
    	return yCenter;
    }
    
    /**
     * Moves the object's and hitbox based on its current velocities, meant to be invoked once per frame
     */
    public void travel(){
        xCenter += xVelocity;
        yCenter += yVelocity;
        loopLocation();
        adjustHitbox();
    }
    
    /**
     * Moves the object back into the frame on the opposite side, if it moves outside of set boundaries
     */
    public void loopLocation(){
        if(xCenter < -radius){
            xCenter = GamePanel.WINDOW_WIDTH + radius;
        }
        else if(xCenter > GamePanel.WINDOW_WIDTH + radius){
            xCenter = -radius;
        }
        if(yCenter < -radius){
            yCenter = GamePanel.WINDOW_HEIGHT + radius;
        }
        else if(yCenter > GamePanel.WINDOW_HEIGHT + radius){
            yCenter = -radius;
        }
    }

    @Override
    public void paintComponent(Graphics g){
    	if(isAlive()){
	        Graphics2D g2 = (Graphics2D)g;
	        if(sprite != null){
	            AffineTransform defaultTransform = g2.getTransform();
	            g2.rotate(rotation, xCenter, yCenter);
	            g2.drawImage(sprite, (int)(xCenter - sprite.getWidth()/2 + .5), (int)(yCenter - sprite.getHeight()/2 + .5), this);
	            g2.setTransform(defaultTransform);
	        }
	        if(Asteroids.DRAW_HITBOXES){
		        if(this instanceof Player){
		            g2.setColor(new Color(0, 255, 0));
		        }
		        else if(this instanceof Projectile){
		            g2.setColor(new Color(0, 255, 255));
		        }
		        else if(this instanceof Asteroid){
		            g2.setColor(new Color(255, 0, 0));
		        }
		        g2.draw(hitbox);
	        }
    	}
    }
    
    /**
     * Removes the hitbox of the object and sets isAlive to false
     */
    public abstract void kill();
    
    /**
     * Adjusts the hitbox of the object when the travel method is invoked
     */
    public abstract void adjustHitbox();
    
    public abstract double getRadius();
}