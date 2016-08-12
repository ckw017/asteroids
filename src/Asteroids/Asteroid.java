package Asteroids;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
/**
 * A non-controllable GameObject capable of collision with and destruction by the player
 * @author ckw017
 */
@SuppressWarnings("serial")
public class Asteroid extends GameObject{
	private double rotationSpd;  //The Asteroid's sprite's rotation speed in degrees per frame
	private int size;

	/**
	 * Constructs an Asteroid object
	 * @param x - the x-coordinate of the Asteroid's center
	 * @param y - the y-coordinate of the Asteroid's center
	 * @param r - the radius of the Asteroid's hitbox in pixels
	 * @param angle - the angle of the Asteroid's movement in degrees
	 * @param v - the velocity of the Asteroid in pixels per frame
	 */
	public Asteroid(double x, double y, double angle, double v, int s){
		super(x, y, NEUTRAL);
		xVelocity = Math.cos(Math.toRadians(angle)) * v;
		yVelocity = Math.sin(Math.toRadians(angle)) * v;
		setSize(s);
		hitbox = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
	}
	
	public ArrayList<Asteroid> split(int splitNum){
		ArrayList<Asteroid> splits = new ArrayList<Asteroid>();
		if(size < 2){
			return splits;
		}
		for(int i = 0; i < splitNum; i++){
			splits.add(generateAsteroid(size - 1, xCenter, yCenter));
		}
		return splits;
	}
	
	/**
	 * Sets the rotation speed of the Asteroid
	 * @param rs - the rotation speed, in pixels per frame
	 */
	public void setRotationSpeed(double rs){
		rotationSpd = rs;
	}
	
	public void setSize(int s){
		size = s;
		if(size <= 0){
			this.kill();
		}
		else if(size == 1){
			radius = 10;
			setSprite(Tools.getImage("aster2.png"));
		}
		else if(size == 2){
			radius = 20;
			setSprite(Tools.getImage("aster1.png"));
		}
		else{
			radius = 30;
			setSprite(Tools.getImage("aster3.png"));
		}
	}
	
	public double getRadius(){
		return radius;
	}
	
	public static Asteroid generateAsteroid(int size, int xVoid, int yVoid, int rVoid){
		int x, y;
		Random gen = new Random();
		do{
			x = gen.nextInt(GamePanel.WINDOW_WIDTH);
			y = gen.nextInt(GamePanel.WINDOW_HEIGHT);
		}while(Math.sqrt(Math.pow(xVoid - x, 2) + Math.pow(yVoid - y, 2)) < rVoid);
		return generateAsteroid(size, x, y);
	}
	
	public static Asteroid generateAsteroid(int size, double x, double y){
		Random gen = new Random();
		int speedBound = (int)(8.0/size);
		int velocity = gen.nextInt(speedBound) + 3;
		Asteroid a = new Asteroid(x, y, gen.nextInt(360), velocity, size);
		a.setRotationSpeed(velocity);
		return a;
	}
	
	@Override
	public void travel(){
		super.travel();
		rotate(rotationSpd);
	}
	
	@Override
	public void kill() {
		if(isAlive){
			((Ellipse2D)hitbox).setFrame(0, 0, 0, 0);
			isAlive = false;
		}
	}

	@Override
	public void adjustHitbox() {
		((Ellipse2D)hitbox).setFrame(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
	}

}