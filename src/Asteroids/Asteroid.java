package asteroids;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Asteroid extends GameObject {
  public static BufferedImage spr1 = getSprite("aster1.png");
  public static BufferedImage spr2 = getSprite("aster2.png");
  public static BufferedImage spr3 = getSprite("aster3.png");
  public static Random gen = new Random();

  public double rotSpd;
  public short size;

  public Asteroid(double xPosition, double yPosition, short asteroidSize) {
    super();
    x = xPosition;
    y = yPosition;
    setSize(asteroidSize);
    int angle = gen.nextInt(360);
    double speed = generateSpeed();
    xv = Math.cos(Math.toRadians(angle)) * speed;
    yv = Math.sin(Math.toRadians(angle)) * speed;
    rotSpd = speed;
    setGraphics(angle);
  }

  public Asteroid(AsteroidUpdate u) {
    super();
    objectID = u.objectID;
    x = u.x;
    y = u.y;
    xv = u.xv;
    yv = u.yv;
    rotSpd = u.rotSpd;
    setSize(u.s);
    setGraphics((int) Math.toDegrees(Math.atan2(yv, xv)));
  }

  public void setGraphics(int angle) {
    if (SaveUtility.ASTEROID_RAINBOW) {
      hbColor = generateColor(angle);
    } else {
      hbColor = Color.RED;
    }
  }

  public void setSize(short s) {
    size = s;
    if (size <= 0) {
      isAlive = false;
    } else if (size == 1) {
      radius = 10;
      sprite = spr1;
    } else if (size == 2) {
      radius = 20;
      sprite = spr2;
    } else {
      radius = 30;
      sprite = spr3;
    }
    hb = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
  }

  public Color generateColor(int angle) {
    double r = 255 - Math.abs(-angle / 90.0 * 255) + 127;
    if (angle > 180) {
      r = 255 - Math.abs((360 - angle) / 90.0 * 255) + 127;
    }
    double g = 255 - Math.abs((120 - angle) / 120.0 * 255) + 127;
    if (angle < 60) {
      g = 255 - Math.abs((540 - angle) / 120.0 * 255) + 127;
    }
    double b = 255 - Math.abs((240 - angle) / 120.0 * 255) + 127;
    if (angle < 180 & angle > 120) {
      b = 255 - Math.abs((240 - angle) / 120.0 * 255) + 127;
    }
    r = (r < 0) ? 0 : ((r > 255) ? 255 : r);
    g = (g < 0) ? 0 : ((g > 255) ? 255 : g);
    b = (b < 0) ? 0 : ((b > 255) ? 255 : b);
    return new Color((int) r, (int) g, (int) b);
  }

  public double generateSpeed() {
    int speedBound = (int) (SaveUtility.ASTEROID_SPEED_RANGE / size);
    double spd;
    if (speedBound > 0) {
      if (SaveUtility.ASTEROID_FLOAT_VELOCITIES) {
        spd = gen.nextDouble() * speedBound + SaveUtility.ASTEROID_BASE_SPEED;
      } else {
        spd = gen.nextInt(speedBound) + SaveUtility.ASTEROID_BASE_SPEED;
      }
    } else {
      spd = SaveUtility.ASTEROID_BASE_SPEED;
    }
    return spd;
  }

  public ArrayList<Asteroid> split() {
    ArrayList<Asteroid> splits = new ArrayList<Asteroid>();
    if (size < 2) {
      return splits;
    }
    for (int i = 0; i < SaveUtility.ASTEROID_SPLIT_FACTOR; i++) {
      splits.add(new Asteroid(x, y, (short) (size - 1)));
    }
    return splits;
  }

  public static Asteroid generateAsteroid(short size, int rVoid, ArrayList<Player> players) {
    int xPos = 0, yPos = 0;
    double radius = rVoid;
    int count = 1;
    Random gen = new Random();
    boolean validPos = false;
    while (!validPos) {
      xPos = gen.nextInt(SaveUtility.SCREEN_WIDTH);
      yPos = gen.nextInt(SaveUtility.SCREEN_HEIGHT);
      check:
      for (Player p : players) {
        if (Math.pow(p.x - xPos, 2) + Math.pow(p.y - yPos, 2) < radius * radius) {
          validPos = false;
          count++;
          break check;
        } else {
          validPos = true;
        }
      }
      count %= 100;
      if (count == 0) {
        radius *= .8;
      }
    }
    return new Asteroid(xPos, yPos, size);
  }

  @Override
  public void travel() {
    super.travel();
    rotation += rotSpd;
  }
}
