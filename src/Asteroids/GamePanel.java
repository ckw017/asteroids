package Asteroids;


import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<GameObject> playerObjects = new ArrayList<GameObject>();
	private ArrayList<GameObject> enemyObjects = new ArrayList<GameObject>();
	
	private Player player;

    public final static int WINDOW_WIDTH = 550;
    public final static int WINDOW_HEIGHT = 420;
    
	public GamePanel(){
		super();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
	}
	
	public ArrayList<GameObject> getObjects(){
		return objects;
	}
	
	public void addObject(Asteroid a){
		asteroids.add(a);
		objects.add(a);
	}
	
	public void addObject(ArrayList<Asteroid> asteroids){
		for(Asteroid a: asteroids){
			addObject(a);
		}
	}
	
	public void addObject(Projectile p){
		projectiles.add(p);
		objects.add(p);
		if(p.getAllegiance() == GameObject.PLAYER){
			playerObjects.add(p);
		}
		else{
			enemyObjects.add(p);
		}
	}
	
	public ArrayList<GameObject> getPlayerObjects(){
		return playerObjects;
	}

	public ArrayList<GameObject> getEnemyObjects(){
		return enemyObjects;
	}
	
	public void setPlayer(Player p){
		player = p;
		playerObjects.add(p);
	}
	
	public ArrayList<Asteroid> getAsteroids(){
		return asteroids;
	}
	
	public ArrayList<Projectile> getProjectiles(){
		return projectiles;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void flush(){
        for(int i = objects.size() - 1; i >= 0; i--){
            if(!objects.get(i).isAlive()){
                objects.remove(i);
            }
        }
        
        for(int i = projectiles.size() - 1; i >= 0; i--){
            if(!projectiles.get(i).isAlive()){
                projectiles.remove(i);
            }
        }
        
        for(int i = asteroids.size() - 1; i >= 0; i--){
            if(!asteroids.get(i).isAlive()){
                asteroids.remove(i);
            }
        }
        
        for(int i = playerObjects.size() - 1; i >= 0; i--){
            if(!playerObjects.get(i).isAlive()){
            	playerObjects.remove(i);
            }
        }
        
        for(int i = enemyObjects.size() - 1; i >= 0; i--){
            if(!enemyObjects.get(i).isAlive()){
            	enemyObjects.remove(i);
            }
        }
	}
	
	@Override
	public void paintComponent(Graphics g){
		for(GameObject o: objects){
			o.paintComponent(g);
		}
		if(player != null){
			player.paintComponent(g);
		}
	}
}
