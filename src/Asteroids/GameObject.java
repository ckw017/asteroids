package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class GameObject extends JComponent {
  public static ArrayList<GameObject> objects = new ArrayList<GameObject>();
  public static int counter = 0;

  public int objectID;
  public double x;
  public double y;
  public double xv = 0;
  public double yv = 0;
  public double rotation = 0;
  public int radius = 0;
  public int hp = 1;
  public boolean isAlive = true;
  public CollisionBucket currBucket = CollisionBucket.bucketMap.get("0|0");

  public Ellipse2D hb;
  public Color hbColor;
  public BufferedImage sprite;

  public GameObject() {
    objectID = counter;
    counter++;
    objects.add(this);
  }

  public GameObject(boolean assignObjectID) {
    objectID = -1;
  }

  public static BufferedImage getSprite(String path) {
    BufferedImage rImage;
    try {
      rImage = ImageIO.read(GameObject.class.getResource("/" + path));
    } catch (IOException ex) {
      rImage = null;
    }
    return rImage;
  }

  public void damage(int d) {
    hp -= d;
    if (hp <= 0) {
      kill();
    }
  }

  public void travel() {
    x += xv;
    y += yv;
    loopLocation();
    hb.setFrame(x - radius, y - radius, radius * 2, radius * 2);
    handleBuckets();
  }

  public void handleBuckets() {
    if (isAlive) {
      int xBuck = ((int) x) / CollisionBucket.SIZE;
      int yBuck = ((int) y) / CollisionBucket.SIZE;
      if (xBuck > CollisionBucket.bucketsWidth - 1) {
        xBuck = CollisionBucket.bucketsWidth - 1;
      }
      if (yBuck > CollisionBucket.bucketsHeight - 1) {
        yBuck = CollisionBucket.bucketsHeight - 1;
      }
      if (xBuck < 0) {
        xBuck = 0;
      }
      if (yBuck < 0) {
        yBuck = 0;
      }
      if (xBuck != currBucket.getX() || yBuck != currBucket.getY()) {
        currBucket.removeObject(this);
        currBucket = CollisionBucket.bucketMap.get(xBuck + "|" + yBuck);
        currBucket.addObject(this);
      }
    } else {
      currBucket.removeObject(this);
      currBucket = CollisionBucket.bucketMap.get("0|0");
    }
  }

  public boolean intersects(GameObject o) {
    return (x - o.x) * (x - o.x) + (y - o.y) * (y - o.y)
        <= (radius + o.radius) * (radius + o.radius);
  }

  public boolean collide(GameObject o) {
    if (intersects(o) && isAlive && o.isAlive) {
      int d = o.hp;
      o.damage(hp);
      damage(d);
    }
    return isAlive;
  }

  public void loopLocation() {
    if (x < -radius) {
      x = SaveUtility.SCREEN_WIDTH + radius;
    } else if (x > SaveUtility.SCREEN_WIDTH + radius) {
      x = -radius;
    }
    if (y < -radius) {
      y = SaveUtility.SCREEN_HEIGHT + radius;
    } else if (y > SaveUtility.SCREEN_HEIGHT + radius) {
      y = -radius;
    }
  }

  public void kill() {
    isAlive = false;
    handleBuckets();
  }

  @Override
  public void paintComponent(Graphics g) {
    if (isAlive) {
      Graphics2D g2 = (Graphics2D) g;
      if (SaveUtility.DRAW_HITBOXES) {
        g2.setColor(hbColor);
        g2.draw(hb);
      }
      if (sprite != null && SaveUtility.DRAW_SPRITES) {
        AffineTransform defaultTransform = g2.getTransform();
        g2.rotate(Math.toRadians(rotation), x, y);
        g2.drawImage(
            sprite,
            (int) (x - sprite.getWidth() / 2 + .5),
            (int) (y - sprite.getHeight() / 2 + .5),
            this);
        g2.setTransform(defaultTransform);
      }
    }
  }
}
