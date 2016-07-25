package Asteroids;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Asteroids {
	public static Player player;
	
	public static GameFrame frame = GameFrame.createAndShowGUI();
	public static GamePanel panel = frame.getGamePanel();
	public static ArrayList<GameObject> objects = panel.getObjects();
	
	public static void main(String args[]){
		player =  new Player(210, 300, 25, 25, 20);
		player.setProjectile(2, 5, 100);
		//player.setSprite(Tools.getImage("player.png"));
		
		frame.addObject(new Asteroid(210, 300, 20, 45, 5));
		frame.addObject(new Asteroid(210, 300, 20, -30, 10));
		
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
		public MainThread(){
			
		}
		
		public void run(){
			while(true){
				moveNPCs();
				movePlayer();
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
			player.applyForce(.5);
		}
		else{
			player.applyForce(0);
		}
		player.travel();
		player.rotate(frame.getDirection() * 5);
	}
	
	public static void handleCollisions(){
		//for(Projectile p : panel.getProjectiles()){
			//for(Asteroid a: panel.getAsteroids()){
				//if(p.getHitbox().intersects)
			//}
		//}
	}
}
