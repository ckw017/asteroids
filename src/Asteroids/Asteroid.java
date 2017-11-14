package asteroids;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Asteroid extends GameObject {
  private static final long serialVersionUID = -6806368405789577592L;
  public int size;
  public int split_factor = Settings.ASTEROID_SPLIT_FACTOR;
  public static Random gen = new Random();

  public static BufferedImage[] sprites = IOTools.loadSprites("asteroid_", 3);

  public Asteroid(ObjectHandler handler, Vector position, int size) {
    super(handler, position, size * 10);
    this.size = size;
    this.rotation = gen.nextDouble() * 2 * Math.PI;
    this.velocity =
        new Vector(
            this.rotation,
            Settings.ASTEROID_BASE_SPEED + gen.nextDouble() * Settings.ASTEROID_SPEED_RANGE / size,
            true);
    this.setGraphics();
    this.handler.asteroids.add(this);
  }
  
  public Asteroid(ObjectHandler handler, Update u) {
  	super(handler, u.position, u.size * 10);
  	this.objectID = u.id;
  	this.size = u.size;
    this.rotation = u.rotation;
    this.velocity = u.velocity;
    this.setGraphics();
    this.handler.asteroids.add(this);
  }

  public void setGraphics() {
    if (Settings.ASTEROID_RAINBOW) {
    	this.hitbox_color = generateColor(this.rotation);
    } else {
      this.hitbox_color = Color.RED;
    }
    this.sprite = sprites[this.size - 1];
  }

  public Color generateColor(double angle) {
    angle = Math.toDegrees(angle);
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

  public void update() {
    super.update();
    this.rotation += this.velocity.magnitude() / 360 * Math.PI;
    this.rotation %= Math.PI * 2;
  }

  public void kill() {
    if (this.size > 1) {
      for (int i = 0; i < this.split_factor; i++) {
        new Asteroid(this.handler, new Vector(this.position), this.size - 1);
      }
    }
    this.handler.asteroids.remove(this);
    this.curr_bucket.asteroids.remove(this);
  }
}
