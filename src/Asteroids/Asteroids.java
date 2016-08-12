package Asteroids;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Asteroids {
	public static boolean DRAW_HITBOXES = true;
	public static Player player;
	
	public static GameFrame frame = GameFrame.createAndShowGUI();
	public static GamePanel panel = frame.getGamePanel();
	public static ArrayList<GameObject> objects = panel.getObjects();
	
	public static void main(String args[]){
		player =  new Player(225, 210, 25, 25, 20);
		player.setProjectile(2, 10, 30);
		player.setSprite(Tools.getImage("player.png"));
		player.setHealth(10000000);
		
		frame.addObject(Asteroid.generateAsteroid(3, 225, 210, 100));
		frame.addObject(Asteroid.generateAsteroid(3, 225, 210, 100));
		frame.addObject(Asteroid.generateAsteroid(3, 225, 210, 100));
		frame.addObject(Asteroid.generateAsteroid(3, 225, 210, 100));
		
		frame.setPlayer(player);
		while(true){
			try{
				new MainThread().run();
			}
			catch(ConcurrentModificationException e){
				
			}
		}
	}
	
	public static class MainThread extends Thread{
		
		public void run(){
			while(true){
				moveNPCs();
				movePlayer();
				handleCollisions();
				panel.flush();
				frame.repaint();
				trySleep(40);
			}
		}
		
		public void trySleep(int delay){
			try {
				sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void moveNPCs(){
		for(GameObject o : objects){
			o.travel();
		}
	}
	
	public static void movePlayer(){
		if(frame.playerIsMoving()){
			player.applyForce(1);
		}
		else{
			player.applyForce(0);
		}
		player.travel();
		player.rotate(frame.getDirection() * 15);
	}
	
	public static void handleCollisions(){
		for(GameObject p : panel.getPlayerObjects()){
			for(Asteroid a: panel.getAsteroids()){
				if(p instanceof Player && ((Player)p).intersects(a) && p.isAlive() && a.isAlive()){
					p.damage(1);
					if(!a.damage(1)){
						frame.addObject(a.split(32));
					}
				}
				else if(p instanceof Projectile && ((Projectile)p).intersects(a) && p.isAlive() && a.isAlive()){
					p.damage(1);
					if(!a.damage(1)){
						frame.addObject(a.split(32));
					}
				}
			}
		}
	}
}
