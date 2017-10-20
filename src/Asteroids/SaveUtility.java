package asteroids;

import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public final class SaveUtility {
  private static File asteroids_settings = new File("asteroids_settings.txt");
  private static Scanner in;

  //public static boolean MULTIPLAYER;
  public static int NUMBER_OF_PLAYERS;
  public static boolean LOCAL_MULTIPLAYER;
  public static boolean LAN_MULTIPLAYER;
  public static boolean PVP;
  public static boolean AUTO_RESET;
  public static int BUCKET_SIZE;
  public static boolean DRAW_HITBOXES;
  public static boolean DRAW_SPRITES;
  public static boolean DRAW_BUCKETS;
  public static boolean FULL_SCREEN;
  public static int SCREEN_WIDTH;
  public static int SCREEN_HEIGHT;
  public static int ASTEROID_SPLIT_FACTOR;
  public static int ASTEROID_COUNT;
  public static boolean ASTEROID_FLOAT_VELOCITIES;
  public static double ASTEROID_BASE_SPEED;
  public static double ASTEROID_SPEED_RANGE;
  public static boolean ASTEROID_RAINBOW;
  public static boolean PLAYER_RESPAWN;
  public static boolean PLAYER_INVULNERABILITY;
  public static double PLAYER_THRUST;
  public static double PLAYER_FRICTION;
  public static double PLAYER_TURN_SPEED;
  public static int PLAYER_MAX_SPEED;
  public static int PLAYER_SHOOT_DELAY;
  public static int PLAYER_HEALTH;
  public static int PROJECTILE_SPEED;
  public static int PROJECTILE_HEALTH;
  public static int PROJECTILE_LIFETIME;
  public static int PROJECTILE_RADIUS;
  public static int JUST_A_PRANK;
  public static boolean SMART_GRAPHICS;
  private static boolean alt_file;
  private static String alt_file_name;

  public static int FRAME_HEIGHT;
  public static int FRAME_WIDTH;

  static {
    load();
  }

  public static void setDefaults() {
    try {
      PrintWriter out = new PrintWriter(asteroids_settings);
      out.println("<<SETTINGS>>");
      out.println("Delete file and run game to reset to default settings.");
      out.println("booleans-> 0 = false; 1 = true");
      out.println("");
      out.println("<GAME VALUES>");
      out.println("Number of Players (int): 1");
      out.println("Local Multiplayer (boolean): 1");
      out.println("LAN Multiplayer (boolean): 0");
      out.println("PvP (boolean): 0");
      out.println("Auto reset (boolean): 1");
      out.println("Bucket size (int): 75");
      out.println("");
      out.println("<DISPLAY>");
      out.println("Full screen (boolean): 0");
      out.println("Window width (pixels): 600");
      out.println("Window height (pixels): 480");
      out.println("Draw hitboxes (boolean): 1");
      out.println("Draw sprites (boolean): 1");
      out.println("Draw buckets (boolean): 1");
      out.println("");
      out.println("<ASTEROIDS>");
      out.println("Asteroid split factor (int): 2");
      out.println("Asteroid base speed (pixels/frame): 1");
      out.println("Asteroid speed range (pixels/frame): 5");
      out.println("Asteroid count (int): 4");
      out.println("Float velocities (boolean): 0");
      out.println("");
      out.println("<PLAYER>");
      out.println("Player invulnerability (bool): 0");
      out.println("Player respawn (boolean): 0");
      out.println("Shooting delay (frames): 3");
      out.println("Player max speed (pixels per frame): 25");
      out.println("Player thrust (mass*pixels/frame^2): 2");
      out.println("Player friction coefficient (double): .95");
      out.println("Player health (hitpoints): 1");
      out.println("Player turn speed (degrees per frame): 15");
      out.println("");
      out.println("<PLAYER PROJECTILE>");
      out.println("Projectile speed(pixels/frame): 10");
      out.println("Projectile lifetime(frames): 30");
      out.println("Projectile radius(pixels): 2");
      out.println("Projectile health (hitpoints): 1");
      out.println("");
      out.println("<MISCELLANEOUS>");
      out.println("Just a prank bro: 0");
      out.println("Smart graphics (for stupid people): 0");
      out.println("Skittle mode: 0");
      out.println("Custom settings file (boolean): 0");
      out.println("Setting file name (path): asteroids_settings.txt");
      out.close();
    } catch (FileNotFoundException ex) {
      System.out.println(ex);
      //Tools.displayError("rip settings file (fix it xd)");
    }
  }

  public static void load() {
    if (!asteroids_settings.exists()) {
      setDefaults();
    }
    try {
      in = new Scanner(asteroids_settings);
      in.nextLine();
      in.nextLine();
      in.nextLine();
      NUMBER_OF_PLAYERS = nextInt();
      LOCAL_MULTIPLAYER = nextBoolean();
      LAN_MULTIPLAYER = nextBoolean();
      PVP = nextBoolean();
      AUTO_RESET = nextBoolean();
      BUCKET_SIZE = nextInt();
      FULL_SCREEN = nextBoolean();
      SCREEN_WIDTH = nextInt();
      SCREEN_HEIGHT = nextInt();
      DRAW_HITBOXES = nextBoolean();
      DRAW_SPRITES = nextBoolean();
      DRAW_BUCKETS = nextBoolean();
      ASTEROID_SPLIT_FACTOR = nextInt();
      ASTEROID_BASE_SPEED = nextInt();
      ASTEROID_SPEED_RANGE = nextInt();
      ASTEROID_COUNT = nextInt();
      ASTEROID_FLOAT_VELOCITIES = nextBoolean();
      PLAYER_INVULNERABILITY = nextBoolean();
      PLAYER_RESPAWN = nextBoolean();
      PLAYER_SHOOT_DELAY = nextInt();
      PLAYER_MAX_SPEED = nextInt();
      PLAYER_THRUST = nextDouble();
      PLAYER_FRICTION = nextDouble();
      PLAYER_HEALTH = nextInt();
      PLAYER_TURN_SPEED = nextDouble();
      PROJECTILE_SPEED = nextInt();
      PROJECTILE_LIFETIME = nextInt();
      PROJECTILE_RADIUS = nextInt();
      PROJECTILE_HEALTH = nextInt();
      JUST_A_PRANK = nextInt();
      SMART_GRAPHICS = nextBoolean();
      ASTEROID_RAINBOW = nextBoolean();
      alt_file = nextBoolean();
      in.next();
      in.next();
      in.next();
      in.next();
      alt_file_name = in.next();
      in.close();

      FRAME_WIDTH = SCREEN_WIDTH;
      ;
      FRAME_HEIGHT = SCREEN_HEIGHT;
      if (FULL_SCREEN) {

        Rectangle winSize =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Insets i = GameFrame.getFrameInsets();
        FRAME_WIDTH = winSize.width;
        FRAME_HEIGHT = winSize.height;
        SCREEN_WIDTH = FRAME_WIDTH - i.left - i.right;
        SCREEN_HEIGHT = FRAME_HEIGHT - i.top;
      }
      if (alt_file) {
        asteroids_settings = new File(alt_file_name);
        load();
      }
    } catch (Exception x) {
      //Tools.displayError("A syntax error has occurred while reading the settings file:" + alt_file_name + "(\n" + "Try deleting the settings file and executing the program again\n if you can't find the error");
      System.exit(1);
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
