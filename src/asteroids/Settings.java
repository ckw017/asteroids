package asteroids;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Settings {
  private static File asteroids_settings = new File("asteroids_settings.txt");
  private static Scanner in;

  public static final String[] paths =
      new String[] {"asteroids_settings.txt", "coop.txt", "pvp.txt", "skittles.txt"};
  
  public static int NUMBER_OF_PLAYERS;
  public static boolean LOCAL_MULTIPLAYER;
  public static boolean LAN_MULTIPLAYER;
  public static boolean PVP;
  public static int BUCKET_SIZE;
  public static boolean DRAW_HITBOXES;
  public static boolean DRAW_SPRITES;
  public static boolean DRAW_BUCKETS;
  public static boolean DRAW_DEBUG;
  public static boolean FULL_SCREEN_WINDOWED;
  public static boolean FULL_SCREEN_BORDERLESS;
  public static double FRAME_RATE;
  public static double ACTUAL_FRAME_RATE;
  public static double FRAME_DELAY;
  public static int SCREEN_WIDTH;
  public static int SCREEN_HEIGHT;
  public static int ASTEROID_SPLIT_FACTOR;
  public static int ASTEROID_COUNT;
  public static double ASTEROID_BASE_SPEED;
  public static double ASTEROID_SPEED_RANGE;
  public static boolean ASTEROID_RAINBOW;
  public static boolean PLAYER_INVULNERABILITY;
  public static double PLAYER_THRUST;
  public static double PLAYER_FRICTION;
  public static double PLAYER_TURN_SPEED;
  public static double PLAYER_MAX_SPEED;
  public static int PLAYER_SHOOT_DELAY;
  public static int PLAYER_HEALTH;
  public static double PROJECTILE_SPEED;
  public static int PROJECTILE_HEALTH;
  public static int PROJECTILE_LIFETIME;
  public static int PROJECTILE_RADIUS;
  public static boolean PRANK;
  private static boolean alt_file;
  private static String alt_file_name;

  public static int FRAME_HEIGHT;
  public static int FRAME_WIDTH;

  static {
  	setDefaults();
    load();
  }

  public static void setDefaults() {
  	for(String p: paths) {
  		File f = new File(p);
  		if(!f.exists()) {
  			System.out.println("Exporting: " + p);
  			IOTools.exportResource("/" + p);
  		}
  	}
  }

  public static void load() {
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
      FRAME_DELAY = 1000 / FRAME_RATE;
      SCREEN_WIDTH = nextInt();
      SCREEN_HEIGHT = nextInt();
      DRAW_HITBOXES = nextBoolean();
      DRAW_SPRITES = nextBoolean();
      DRAW_BUCKETS = nextBoolean();
      DRAW_DEBUG = nextBoolean();
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
    } catch (Exception x) {
      System.out.println("Something went wrong");
      x.printStackTrace(System.out);
    	System.exit(1);
    }
  }

  public static void adjustWindow() {
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

  public static int nextInt() {
    while (!in.hasNextInt()) {
      in.next();
    }
    return in.nextInt();
  }

  public static double nextDouble() {
    while (!in.hasNextDouble()) {
      in.next();
    }
    return in.nextDouble();
  }

  public static boolean nextBoolean() {
    return toBoolean(nextInt());
  }

  public static boolean toBoolean(int b) {
    if (b != 1) {
      return false;
    }
    return true;
  }
}
