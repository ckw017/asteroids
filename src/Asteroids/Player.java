package Asteroids;

import java.awt.Polygon;
/**
 * A player controlled GameObject capable of moving in two dimensions through vector calculations
 * @author ckw017
 *
 */
@SuppressWarnings("serial")
public class Player extends GameObject {
    private int[] xCoords = new int[4]; //An array storing the x-coordinates of the Player's hitbox's vertices
    private int[] yCoords = new int[4]; //An array storing the y-coordinates of the Player's hitbox's vertices
    private double defaultAngle;        //The angle between the x-axis and a line drawn from the hitbox's center to its upper-right corner in degrees
    private double radius;				//The length of the line drawn from the hitbox's center to its upper-right corner in pixels
    private int maxSpeed;               //The maximum speed of the Player object in pixels per frame
    private Projectile projectile;      //A base model of the Projectile shot by the Player
    
    /**
     * Constructs a Player Object
     * @param x - the x-coordinate of the Player's center
     * @param y - the y-coordinate of the Player's center
     * @param w - the width of the Player's hitbox
     * @param h - the height of the player's hitbox
     * @param ms - the maximum speed of the player
     */
    public Player(int x, int y, int w, int h, int ms){
        super(x, y, PLAYER);
        defaultAngle = Math.atan2(h, w);
        radius = Math.sqrt(Math.pow(w/2, 2) + Math.pow(h/2, 2)); /*Radius calculation*/
        adjustHitbox();
        hitbox = new Polygon(xCoords, yCoords, 4);
        maxSpeed = ms;
    }
    
    /**
     * Applies a force in the objects current rotation
     * @param f - the force applied in pixels/second^2 (the mass of the Player is treated as 1)
     */
    public void applyForce(int f){
        if(f == 0){
            scaleVelocity(.99); /*If no force is applied, the ship decelerates*/
        }
        else{
        	//Vector calculations
            xVelocity += Math.cos(rotation - Math.PI/2) * f;
            yVelocity += Math.sin(rotation - Math.PI/2) * f;
            
            //Max speed calculations
            double moveAngle = getMovementAngle();
            double xMax = Math.cos(moveAngle) * maxSpeed;
            double yMax = Math.sin(moveAngle) * maxSpeed;
            if(Math.abs(xVelocity) > Math.abs(xMax)){
                xVelocity = xMax;
            }
            if(Math.abs(yVelocity) > Math.abs(yMax)){
                yVelocity = yMax;
            }
        }
    }
    
    /**
     * Scales the Player's movement vectors based on a scalar
     * @param s - the scalar to be applied to the Player's movement
     */
    public void scaleVelocity(double s){
        xVelocity *= s;
        yVelocity *= s;
    }
    
    /**
     * Returns the Player's angle of movement
     * @return the Player's angle of movement based on its velocities
     */
    public double getMovementAngle(){
        return Math.atan2(yVelocity, xVelocity);
    }
    
    @Override
    public void setRotation(double r){
        super.setRotation(r);
        double rotationInRadians = Math.toRadians(r);
        //"top-left" vertex transform
        xCoords[0] = (int)(Math.cos(Math.PI - defaultAngle + rotationInRadians) * radius + .5 + xCenter);
        yCoords[0] = (int)(Math.sin(Math.PI - defaultAngle + rotationInRadians) * radius + .5 + yCenter);
        //"top-right" vertex transform
        xCoords[1] = (int)(Math.cos(defaultAngle + rotationInRadians) * radius + .5 + xCenter);
        yCoords[1] = (int)(Math.sin(defaultAngle + rotationInRadians) * radius + .5 + yCenter);
        //"bottom-right" vertex transform
        xCoords[2] = (int)(Math.cos(-defaultAngle + rotationInRadians) * radius + .5 + xCenter);
        yCoords[2] = (int)(Math.sin(-defaultAngle + rotationInRadians) * radius + .5 + yCenter);
        //"bottom-left" vertex transform
        xCoords[3] = (int)(Math.cos(Math.PI + defaultAngle + rotationInRadians) * radius + .5 + xCenter);
        yCoords[3] = (int)(Math.sin(Math.PI + defaultAngle + rotationInRadians) * radius + .5 + yCenter);
        hitbox = new Polygon(xCoords, yCoords, 4);
    }
    
    /**
     * Sets the Projectile to be shot by the Player
     * @param r - the radius of the projectile in pixels
     * @param v - the velocity of the projectile in pixels per frame
     * @param l - the lifetime of the projectile in frames
     */
    public void setProjectile(double r, double v, int l){
    	projectile = new Projectile(r, v, l);
    }
    
    /**
     * Returns a Projectile of appropriate location, velocity and angle relative to the player
     * @return a Projectile shot relative from the player's location and orientation
     */
    public Projectile shoot(){
    	return new Projectile(projectile, xCenter, yCenter, Math.toDegrees(rotation - Math.PI/2));
    }
    
    @Override
    public void kill() {
        isAlive = false;
        ((Polygon)hitbox).reset();
    }
    
    @Override
    public void adjustHitbox(){
        setRotation(Math.toDegrees(rotation));
    }
}
