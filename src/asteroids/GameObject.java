package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class GameObject extends JComponent {
  public ObjectHandler handler;

  public Vector position;
  public Vector velocity;
  public double rotation;
  public double radius;
  public int health = 1;
  public int objectID;
  public static int currID = 0;
  public ArrayList<CompareCache> compares;
  public ArrayList<CompareCache> compares_cache;

  public static Bucket null_bucket = new Bucket();
  public Bucket curr_bucket = null_bucket;

  public Ellipse2D hitbox;
  public Color hitbox_color;
  public BufferedImage sprite;

  public GameObject(ObjectHandler handler, Vector position, double radius) {
    this.handler = handler;
    this.position = position;
    this.objectID = currID;
    this.compares = new ArrayList<CompareCache>();
    this.compares_cache = compares;
    currID++;
    setRadius(radius);
  }

  public void setRadius(double r) {
    this.radius = r;
    this.hitbox =
        new Ellipse2D.Double(
            this.position.x - this.radius,
            this.position.y - this.radius,
            this.radius * 2,
            this.radius * 2);
  }

  public void update() {
    this.position.add(this.velocity);
    this.loopLocation();
    this.hitbox.setFrame(
        this.position.x - radius, this.position.y - radius, radius * 2, radius * 2);
    if (handler.settings.DRAW_COLLISIONS) {
      compares_cache = compares;
      compares = new ArrayList<CompareCache>();
    }
  }

  public void loopLocation() {
    double x = this.position.x;
    double y = this.position.y;
    if (x < -radius) {
      this.position.x = handler.settings.FRAME_WIDTH + radius;
    } else if (x > handler.settings.FRAME_WIDTH + radius) {
      this.position.x = -radius;
    }
    if (y < -radius) {
      this.position.y = handler.settings.FRAME_HEIGHT + radius;
    } else if (y > handler.settings.FRAME_HEIGHT + radius) {
      this.position.y = -radius;
    }
  }

  public void collide(GameObject o) {
    if (handler.settings.DRAW_COLLISIONS) {
      compares.add(new CompareCache(o));
    }
    if (this.intersects(o) && this.health > 0 && o.health > 0) {
      int health_cache = o.health;
      o.damage(this.health);
      this.damage(health_cache);
    }
  }

  public void damage(int d) {
    this.health -= d;
    if (this.health <= 0) {
      this.kill();
    }
  }

  public boolean intersects(GameObject o) {
    return Math.pow(this.radius + o.radius, 2) >= Math.pow(this.position.distance(o.position), 2);
  }

  public abstract void kill();

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    if (handler.settings.DRAW_HITBOXES) {
      g2.setColor(this.hitbox_color);
      g2.draw(this.hitbox);
    }
    if (handler.settings.DRAW_SPRITES) {
      AffineTransform defaultTransform = g2.getTransform();
      g2.rotate(rotation, this.position.x, this.position.y);
      g2.drawImage(
          sprite,
          (int) (this.position.x - sprite.getWidth() / 2 + .5),
          (int) (this.position.y - sprite.getHeight() / 2 + .5),
          this);
      g2.setTransform(defaultTransform);
    }
    if (this.handler.settings.DRAW_COLLISIONS && this.health > 0) {
      for (CompareCache c : compares_cache) {
        g.setColor(c.color);
        paintLine(g, c.position_a, c.position_b);
      }
    }
  }

  public void paintLine(Graphics g, Vector a, Vector b) {
    g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
  }

  public Color averageColor(Color a, Color b) {
    int red = (a.getRed() + b.getRed()) / 2;
    int green = (a.getGreen() + b.getGreen()) / 2;
    int blue = (a.getBlue() + b.getBlue()) / 2;
    return new Color(red, green, blue);
  }

  public class CompareCache {
    public Color color;
    public Vector position_a;
    public Vector position_b;

    public CompareCache(GameObject o) {
      this.position_a = new Vector(o.position);
      this.position_b = new Vector(position);
      this.color = averageColor(o.hitbox_color, hitbox_color);
    }
  }
}
