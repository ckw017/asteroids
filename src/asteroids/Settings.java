package asteroids;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.Scanner;

public class Settings {
  public File asteroids_settings = new File("asteroids_settings.txt");
  public Scanner in;

  public String[] paths =
      new String[] {"asteroids_settings.txt", "coop.txt", "pvp.txt", "skittles.txt"};
  
  public int NUMBER_OF_PLAYERS;
  public boolean LOCAL_MULTIPLAYER;
  public boolean LAN_MULTIPLAYER;
  public boolean PVP;
  public int BUCKET_SIZE;
  public boolean DRAW_HITBOXES;
  public boolean DRAW_SPRITES;
  public boolean DRAW_BUCKETS;
  public boolean DRAW_DEBUG;
  public boolean DRAW_COLLISIONS;
  public boolean FULL_SCREEN_WINDOWED;
  public boolean FULL_SCREEN_BORDERLESS;
  public double FRAME_RATE;
  public int SCREEN_WIDTH;
  public int SCREEN_HEIGHT;
  public int ASTEROID_SPLIT_FACTOR;
  public int ASTEROID_COUNT;
  public double ASTEROID_BASE_SPEED;
  public double ASTEROID_SPEED_RANGE;
  public boolean ASTEROID_RAINBOW;
  public boolean PLAYER_INVULNERABILITY;
  public double PLAYER_THRUST;
  public double PLAYER_FRICTION;
  public double PLAYER_TURN_SPEED;
  public double PLAYER_MAX_SPEED;
  public int PLAYER_SHOOT_DELAY;
  public int PLAYER_HEALTH;
  public double PROJECTILE_SPEED;
  public int PROJECTILE_HEALTH;
  public int PROJECTILE_LIFETIME;
  public int PROJECTILE_RADIUS;
  public boolean PRANK;
  private boolean alt_file;
  private String alt_file_name;

  public int FRAME_HEIGHT;
  public int FRAME_WIDTH;
  
  public Settings() {
  	this.setDefaults();
  	this.load();
  }

  public void setDefaults() {
  	for(String p: paths) {
  		File f = new File(p);
  		if(!f.exists()) {
  			System.out.println("Exporting: " + p);
  			IOTools.exportResource("/" + p);
  		}
  	}
  }

  public void load() {
    try {
      in = new Scanner(asteroids_settings);
      in.nextLine();
      in.nextLine();
      in.nextLine();
      NUMBER_OF_PLAYERS = nextInt();
      LOCAL_MULTIPLAYER = nextBoolean();
      LAN_MULTIPLAYER = nextBoolean();
      PVP = nextBoolean();
      BUCKET_SIZE = nextInt();
      FULL_SCREEN_WINDOWED = nextBoolean();
      FULL_SCREEN_BORDERLESS = nextBoolean();
      FRAME_RATE = nextDouble();
      SCREEN_WIDTH = nextInt();
      SCREEN_HEIGHT = nextInt();
      DRAW_HITBOXES = nextBoolean();
      DRAW_SPRITES = nextBoolean();
      DRAW_BUCKETS = nextBoolean();
      DRAW_DEBUG = nextBoolean();
      DRAW_COLLISIONS = nextBoolean();
      ASTEROID_SPLIT_FACTOR = nextInt();
      ASTEROID_BASE_SPEED = nextDouble();
      ASTEROID_SPEED_RANGE = nextDouble();
      ASTEROID_COUNT = nextInt();
      PLAYER_INVULNERABILITY = nextBoolean();
      PLAYER_SHOOT_DELAY = nextInt();
      PLAYER_MAX_SPEED = nextDouble();
      PLAYER_THRUST = nextDouble();
      PLAYER_FRICTION = nextDouble();
      PLAYER_HEALTH = nextInt();
      PLAYER_TURN_SPEED = nextDouble();
      PROJECTILE_SPEED = nextDouble();
      PROJECTILE_LIFETIME = nextInt();
      PROJECTILE_RADIUS = nextInt();
      PROJECTILE_HEALTH = nextInt();
      ASTEROID_RAINBOW = nextBoolean();
      PRANK = nextBoolean();
      alt_file = nextBoolean();
      in.next();
      in.next();
      in.next();
      in.next();
      alt_file_name = in.next();
      in.close();
      adjustWindow();
      if (alt_file) {
        asteroids_settings = new File(alt_file_name);
        load();
      }
    } catch (Exception e) {
      System.out.println("Error while loading settings file \"" + asteroids_settings.getName() +"\"");
      System.out.println("To reset a default file, delete it then run the jar file again.");
      e.printStackTrace(System.out);
    	System.exit(1);
    }
  }

  public void adjustWindow() {
    if (FULL_SCREEN_WINDOWED) {
      Rectangle winSize =
          GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
      FRAME_WIDTH = winSize.width;
      FRAME_HEIGHT = winSize.height;
      SCREEN_WIDTH = FRAME_WIDTH;
      SCREEN_HEIGHT = FRAME_HEIGHT;
      Insets i = GameFrame.getFrameInsets();
      SCREEN_WIDTH -= i.left + i.right;
      SCREEN_HEIGHT -= i.top + i.bottom;
    } else if (FULL_SCREEN_BORDERLESS) {
      Dimension winSize = Toolkit.getDefaultToolkit().getScreenSize();
      FRAME_WIDTH = winSize.width;
      FRAME_HEIGHT = winSize.height;
      SCREEN_WIDTH = FRAME_WIDTH;
      SCREEN_HEIGHT = FRAME_HEIGHT;
    } else {
      FRAME_WIDTH = SCREEN_WIDTH;
      FRAME_HEIGHT = SCREEN_HEIGHT;
    }
  }

  public int nextInt() {
    while (!in.hasNextInt()) {
      in.next();
    }
    return in.nextInt();
  }

  public double nextDouble() {
    while (!in.hasNextDouble()) {
      in.next();
    }
    return in.nextDouble();
  }

  public boolean nextBoolean() {
    return toBoolean(nextInt());
  }

  public boolean toBoolean(int b) {
    return (b == 0) ? false : true;
  }
}
