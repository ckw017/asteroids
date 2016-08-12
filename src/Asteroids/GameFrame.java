package Asteroids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GameFrame extends JFrame{
	
	private GamePanel gamePanel = new GamePanel();
    public static final String WINDOW_TITLE = "Asteroids";
    public static final int WINDOW_WIDTH = 550;
    public static final int WINDOW_HEIGHT = 420;
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private Player player;
    private char rotationDirection = 's';
    private boolean isMoving;
    
    public GameFrame(){
        super();
        setLayout(new FlowLayout());
        add(gamePanel);
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        setResizable(false);
        getContentPane().setBackground(Color.black);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(SCREEN_SIZE.width/2-this.getSize().width/2, SCREEN_SIZE.height/40);
        addKeyListener(new InputListener());
    }
    
    public GamePanel getGamePanel(){
    	return gamePanel;
    }
    
    public void setPlayer(Player p){
    	player = p;
    	gamePanel.setPlayer(player);
    }
    
    public void addObject(Asteroid a){
    	gamePanel.addObject(a);
    }
    
    public void addObject(Projectile p){
    	gamePanel.addObject(p);
    }
    
    public void addObject(ArrayList<Asteroid> a){
    	gamePanel.addObject(a);
    }
    
    public static GameFrame createAndShowGUI(){
    	return new GameFrame();
    }
    
    public static void main(String[] args){
    	createAndShowGUI();
    }
    
    public int getDirection(){
    	if(rotationDirection == 'd'){
    		return 1;
    	}
    	else if(rotationDirection == 'a'){
    		return -1;
    	}
    	else{
    		return 0;
    	}
    }
    
    public boolean playerIsMoving(){
    	return isMoving;
    }
    
    class InputListener implements KeyListener{
    	private boolean shotReleased = true;
        private char LEFT  = 'a'; //The character binded to turning left
        private char RIGHT = 'd'; //The character binded to turning right
        private char MOVE = 's';  //The character binded to applying force
        private char STATIC = 's';

        private char secondaryDirection = STATIC; //The secondary direction of turning
        
        public void keyPressed(KeyEvent e){
            char charPressed = e.getKeyChar();
            if(rotationDirection == STATIC){
                if(charPressed == LEFT){
                	rotationDirection = LEFT;
                }
                if(charPressed == RIGHT){
                	rotationDirection = RIGHT;
                }
            }
            else{
                if(charPressed == LEFT && rotationDirection != LEFT){
                    secondaryDirection = LEFT;
                }
                if(charPressed == RIGHT && rotationDirection != RIGHT){
                    secondaryDirection = RIGHT;
                }
            }
            if(charPressed == MOVE && !isMoving){
                isMoving = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
            	if(player != null){
	            	if(shotReleased && player.isAlive()){
	            		addObject(player.shoot());
	            		shotReleased = false;
	            	}
            	}
            }
        }

        public void keyReleased(KeyEvent e){
            char charReleased = e.getKeyChar();
            if(rotationDirection != STATIC){
                if(charReleased == LEFT && rotationDirection == LEFT){
                	rotationDirection = secondaryDirection;
                    secondaryDirection = STATIC;
                }
                else if(charReleased == RIGHT && rotationDirection == RIGHT){
                	rotationDirection = secondaryDirection;
                    secondaryDirection = STATIC;
                }
                else{
                    secondaryDirection = STATIC;
                }
            }
            if(charReleased == MOVE && isMoving){
                isMoving = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
            	shotReleased = true;
            }
        }
        
        public void keyTyped(KeyEvent e){
        }
    }
}