package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class CollisionBucket extends JComponent {
  public ArrayList<Player> players = new ArrayList<Player>();
  public ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
  public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
  private int xPos;
  private int yPos;
  private Rectangle hitbox;
  private Color drawColor = Color.WHITE;
  private Color bgColor = null;
  private boolean colorChange = true;
  public static int SIZE = SaveUtility.BUCKET_SIZE;
  public static HashMap<String, CollisionBucket> bucketMap;
  public static int bucketsWidth = SaveUtility.SCREEN_WIDTH / SIZE + 1;
  public static int bucketsHeight = SaveUtility.SCREEN_HEIGHT / SIZE + 1;

  public CollisionBucket(int x, int y) {
    xPos = SIZE * x;
    yPos = SIZE * y;
    hitbox = new Rectangle(xPos + 1, yPos + 1, SIZE - 1, SIZE - 1);
  }

  public int getX() {
    return xPos / SIZE;
  }

  public int getY() {
    return yPos / SIZE;
  }

  public void addObject(GameObject o) {
    if (o instanceof Asteroid) {
      asteroids.add((Asteroid) o);
    } else if (o instanceof Projectile) {
      projectiles.add((Projectile) o);
    } else if (o instanceof Player) {
      players.add((Player) o);
    }
    updateColor();
  }

  public void removeObject(GameObject o) {
    if (o instanceof Asteroid) {
      asteroids.remove((Asteroid) o);
    } else if (o instanceof Projectile) {
      projectiles.remove((Projectile) o);
    } else if (o instanceof Player) {
      players.remove((Player) o);
    }
    updateColor();
  }

  public static void generateBucketList() {
    bucketMap = new HashMap<String, CollisionBucket>();
    for (int x = 0; x < SaveUtility.SCREEN_WIDTH / SIZE + 1; x++) {
      for (int y = 0; y < SaveUtility.SCREEN_HEIGHT / SIZE + 1; y++) {
        bucketMap.put(x + "|" + y, new CollisionBucket(x, y));
      }
    }
    drawBuckets();
  }

  public static void drawBuckets() {
    if (SaveUtility.DRAW_BUCKETS) {
      GamePanel.buckets = bucketMap.values().toArray(GamePanel.buckets);
    }
  }

  public void updateColor() {
    boolean containsAsteroids = asteroids.size() > 0;
    boolean containsPlayerObjs = players.size() + projectiles.size() > 0;
    if (containsAsteroids || containsPlayerObjs) {
      if (containsPlayerObjs && containsAsteroids) {
        if (drawColor != Color.MAGENTA) {
          drawColor = Color.MAGENTA;
          bgColor = new Color(25, 0, 25);
          colorChange = false;
        }
      } else if (containsAsteroids) {
        if (drawColor != Color.RED) {
          drawColor = Color.RED;
          bgColor = new Color(15, 0, 0);
          colorChange = false;
        }
      } else {
        if (drawColor != Color.CYAN) {
          drawColor = Color.CYAN;
          bgColor = new Color(0, 15, 15);
          colorChange = false;
        }
      }
    } else {
      if (drawColor != Color.WHITE) {
        drawColor = Color.WHITE;
        bgColor = null;
        colorChange = false;
      }
    }
  }

  public static ArrayList<Player> mergePlayers(int x, int y) {
    return mergeLists(
        bucketMap.get(x + "|" + y).players,
        bucketMap.get((x + 1) + "|" + y).players,
        bucketMap.get(x + "|" + (y + 1)).players,
        bucketMap.get((x + 1) + "|" + (y + 1)).players);
  }

  public static ArrayList<Projectile> mergeProjectiles(int x, int y) {
    return mergeLists(
        bucketMap.get(x + "|" + y).projectiles,
        bucketMap.get((x + 1) + "|" + y).projectiles,
        bucketMap.get(x + "|" + (y + 1)).projectiles,
        bucketMap.get((x + 1) + "|" + (y + 1)).projectiles);
  }

  public static ArrayList<Asteroid> mergeAsteroids(int x, int y) {
    return mergeLists(
        bucketMap.get(x + "|" + y).asteroids,
        bucketMap.get((x + 1) + "|" + y).asteroids,
        bucketMap.get(x + "|" + (y + 1)).asteroids,
        bucketMap.get((x + 1) + "|" + (y + 1)).asteroids);
  }

  public static Color mergeColors(ArrayList<Color> colorArray) {
    int r = 0, g = 0, b = 0;
    for (Color c : colorArray) {
      r += c.getRed();
      g += c.getGreen();
      b += c.getBlue();
    }
    double scalar;
    if (r >= g && r >= b) {
      scalar = 255.0 / r;
    } else if (g >= b) {
      scalar = 255.0 / g;
    } else {
      scalar = 255.0 / b;
    }
    r = (int) (r * scalar);
    g = (int) (g * scalar);
    b = (int) (b * scalar);
    return new Color(r, g, b);
  }

  @SafeVarargs
  public static <E> ArrayList<E> mergeLists(ArrayList<E>... arrays) {
    ArrayList<E> returnList = new ArrayList<E>();
    for (ArrayList<E> a : arrays) {
      for (E e : a) {
        returnList.add(e);
      }
    }
    return returnList;
  }

  @Override
  public void paintComponent(Graphics g) {
    if (colorChange) {
      Graphics2D g2 = (Graphics2D) g;
      if (bgColor != null) {
        g2.setColor(bgColor);
        g2.fill(hitbox);
      }
      g2.setColor(drawColor);
      g2.draw(hitbox);
    }
    colorChange = true;
  }
}
