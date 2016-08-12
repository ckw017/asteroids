package Asteroids;


import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
    public void applyForce(double f){
        if(f == 0){
            scaleVelocity(.95); /*If no force is applied, the ship decelerates*/
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
    	projectile.setSprite(Tools.getImage("playerProj.png"));
    }
    
    /**
     * Returns a Projectile of appropriate location, velocity and angle relative to the player
     * @return a Projectile shot relative from the player's location and orientation
     */
    public Projectile shoot(){
    	Projectile p = new Projectile(projectile, xCenter, yCenter, Math.toDegrees(rotation - Math.PI/2));
    	p.setRotation(Math.toDegrees(rotation));
    	return p;
    }
    
    public boolean intersects(GameObject o){
    	return centerIntersects(o) || intersects(0, 1, o) || intersects(1, 2, o) || intersects(2, 3, o) || intersects(3, 0, o)||
    		   cornerIntersects(0, o) || cornerIntersects(1, o) || cornerIntersects(2, o) || cornerIntersects(3, o);
    }
    
    public boolean cornerIntersects(int v, GameObject o){
    	return Math.sqrt(Math.pow(xCoords[v] - o.getXCenter(), 2) + Math.pow(yCoords[v] - o.getYCenter(), 2)) < o.getRadius();
    }
    
    public boolean centerIntersects(GameObject o){
    	Point2D center = new Point2D.Double(o.getXCenter(), o.getYCenter());
    	for(int i = 0; i < 4; i ++){
    		Line2D a;
    		int crossCount = 0;
			if(i == 0){
				Point2D up = new Point2D.Double(o.getXCenter(), o.getYCenter() - 100);
				a = new Line2D.Double(center, up);
			}
			else if(i == 1){
				Point2D down = new Point2D.Double(o.getXCenter(), o.getYCenter() + 100);
				a = new Line2D.Double(center, down);
			}
			else if(i == 2){
				Point2D left = new Point2D.Double(o.getXCenter() - 100, o.getYCenter());
				a = new Line2D.Double(center, left);
			}
			else{
				Point2D right = new Point2D.Double(o.getXCenter() + 100, o.getYCenter());
				a = new Line2D.Double(center, right);
			}
	    	for(int j = 0; j < 4; j++){
	    		Point2D b1 = new Point2D.Double(xCoords[j] -.01, yCoords[j] - .01);
	    		Point2D b2 = new Point2D.Double(xCoords[(j + 1) % 4], yCoords[(j + 1) % 4]);
	    		Line2D b = new Line2D.Double(b1, b2);
	    		if(b.intersectsLine(a)){
	    			crossCount++;
	    		}
	    	}
			if(crossCount != 1){
				return false;
			}
    	}
    	return true;
    }
    
    public boolean intersects(int p1, int p2, GameObject o){
    	double big = 1000000;
    	
    	double x1 = xCoords[p1];
    	double x2 = xCoords[p2];
    	double y1 = yCoords[p1];
    	double y2 = yCoords[p2];
    	
    	double m1 = (y1 - y2)/(x1 -x2);
    	double m2 = (x1 - x2)/(y1 -y2) * -1;
    	
    	if(Double.isInfinite(m1)){
    		if(m1 > 0){
    			m1 = big;
    		}
    		else{
    			m1 = -big;
    		}
    	}
    	if(Double.isInfinite(m2)){
    		if(m2 > 0){
    			m2 = big;
    		}
    		else{
    			m2 = -big;
    		}
    	}
    	
    	double cx = o.getXCenter();
    	double cy = o.getYCenter();
    	double r = o.getRadius();
    	double b1 = (m1 * -1 * x1) + y1;
    	double b2 = (m2 * -1 * cx) + cy;
    	
    	
    	double x3 = (b2 - b1)/(m1 - m2);
    	double y3 = (m1 * x3) + b1;
    	
    	/**
    	 * May this commented out section of debug code be a testament to the
    	 * struggle that took place during the implementation of this most
    	 * unholy of methods. Let us not only remember our triumphs, but
    	 * that struggles that came before them as well.
    	 *
    	 * System.out.println(p1 + "->" + p2);
    	 * System.out.println("Line 1: (" + x1 + ", " + y1 + ") -> " + "(" + x2 + ", " + y2 + ")");
    	 * System.out.println("Line 1: Y = " + m1 + "X + " + b1);
    	 * System.out.println("Center(" + cx + ", " + cy + ")");
    	 * System.out.println("Line 2: Y = " + m2 + "X + " + b2);
    	 * System.out.println("Point 3: (" + x3 + ", " + y3 + ")");
    	 **/
    	 
    	if(((x3 >= x1 && x3 <= x2) || (x3 <= x1 && x3 >= x2)) && 
    	   ((y3 >= y1 && y3 <= y2) || (y3 <= y1 && y3 >= y2))){
    		double l = Math.sqrt(Math.pow((cx - x3), 2) + Math.pow((cy - y3), 2));
    		return l <= r;
    	}
    	else{
    		return false;
    	}
    }
    
    @Override
    public void kill() {
    	if(isAlive){
	        isAlive = false;
	        ((Polygon)hitbox).reset();
    	}
    }
    
    @Override
    public void adjustHitbox(){
        setRotation(Math.toDegrees(rotation));
    }

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
}
