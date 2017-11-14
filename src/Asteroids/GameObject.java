package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

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

  public static Bucket null_bucket = new Bucket(new Vector(-1, -1));
  public Bucket curr_bucket = null_bucket;
  
  public Ellipse2D hitbox;
  public Color hitbox_color;
  public BufferedImage sprite;

  public GameObject(ObjectHandler handler, Vector position, double radius) {
    this.handler = handler;
    this.position = position;
    this.objectID = currID;
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
  }

  public void loopLocation() {
    double x = this.position.x;
    double y = this.position.y;
    if (x < -radius) {
      this.position.x = Settings.FRAME_WIDTH + radius;
    } else if (x > Settings.FRAME_WIDTH + radius) {
      this.position.x = -radius;
    }
    if (y < -radius) {
      this.position.y = Settings.FRAME_HEIGHT + radius;
    } else if (y > Settings.FRAME_HEIGHT + radius) {
      this.position.y = -radius;
    }
  }

  public void collide(GameObject o) {
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
    if (Settings.DRAW_HITBOXES) {
      g2.setColor(this.hitbox_color);
      g2.draw(this.hitbox);
    }
    if (Settings.DRAW_SPRITES) {
      AffineTransform defaultTransform = g2.getTransform();
      g2.rotate(rotation, this.position.x, this.position.y);
      g2.drawImage(
          sprite,
          (int) (this.position.x - sprite.getWidth() / 2 + .5),
          (int) (this.position.y - sprite.getHeight() / 2 + .5),
          this);
      g2.setTransform(defaultTransform);
    }
  }
}
