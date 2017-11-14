package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Player extends GameObject {
  public static BufferedImage[] sprites = IOTools.loadSprites("player_", 3);
  public static Color[] colors =
      new Color[] {new Color(0, 255, 255), new Color(255, 255, 0), new Color(255, 0, 255)};

  public int playerID;
  public int shot_counter = 0;
  public int rotation_direction = 0;
  public int queued_direction = 0;
  public boolean is_shooting = false;
  public boolean is_boosting = false;
  public boolean is_alive = true;
  public boolean is_invulnerable;
  public Vector initial_position;

  public Player(ObjectHandler handler, Vector position, int id) {
    super(handler, position, 15);
    this.playerID = id;
    this.initial_position = new Vector(this.position);
    this.velocity = new Vector();
    this.setGraphics();
    this.rotation = -Math.PI / 2;
    this.health = Settings.PLAYER_HEALTH;
    this.is_invulnerable = Settings.PLAYER_INVULNERABILITY;
  }

  public void setGraphics() {
    this.sprite = sprites[this.playerID - 1];
    this.hitbox_color = colors[this.playerID - 1];
  }
  
  public void update(Update u) {
  	this.position = u.position;
  	this.velocity = u.velocity;
  	this.rotation = u.rotation;
  }

  public void shoot() {
    if (this.shot_counter > Settings.PLAYER_SHOOT_DELAY && this.is_shooting) {
      this.shot_counter = 0;
      new Projectile(this);
    }
    this.shot_counter += 1;
  }

  public void boost() {
    if (this.is_boosting) {
      this.velocity.add(new Vector(this.rotation, Settings.PLAYER_THRUST, true));
      if (this.velocity.magnitude() > Settings.PLAYER_MAX_SPEED) {
        this.velocity.setMagnitude(Settings.PLAYER_MAX_SPEED);
      }
    } else {
      this.velocity.scale(Settings.PLAYER_FRICTION);
    }
  }

  public void update() {
    if (this.is_alive) {
      super.update();
      this.shoot();
      this.boost();
      this.rotation += this.rotation_direction * Math.toRadians(Settings.PLAYER_TURN_SPEED);
      this.rotation %= Math.PI * 2;
    }
  }

  public void reset() {
    this.velocity = new Vector();
    this.position = new Vector(this.initial_position);
    this.is_alive = true;
    this.curr_bucket.players.remove(this);
    this.curr_bucket = null_bucket;
    this.health = Settings.PLAYER_HEALTH;
    this.rotation = -Math.PI / 2;
    this.is_shooting = false;
    this.is_boosting = false;
    this.is_invulnerable = Settings.PLAYER_INVULNERABILITY;
    this.rotation_direction = 0;
  }

  public void damage(int d) {
    if (!this.is_invulnerable) {
      super.damage(d);
    }
  }

  public void kill() {
    this.curr_bucket.players.remove(this);
    this.is_alive = false;
  }

  public void collide(GameObject o) {
    if ((o instanceof Projectile && ((Projectile) o).playerID != this.playerID)
        || (o instanceof Player && ((Player) o).playerID != this.playerID)
        || (!(o instanceof Player || o instanceof Projectile))) {
      super.collide(o);
    }
  }

  public void paintComponent(Graphics g) {
    if (this.is_alive) {
      super.paintComponent(g);
      if (Settings.DRAW_HITBOXES) {
      	int start_x = (int)(this.position.x + Math.cos(this.rotation) * (this.radius - 6));
      	int start_y = (int)(this.position.y + Math.sin(this.rotation) * (this.radius - 6));
        int end_x = (int)(this.position.x + Math.cos(this.rotation) * this.radius);
        int end_y = (int)(this.position.y + Math.sin(this.rotation) * this.radius);
        g.drawLine(start_x, start_y, end_x, end_y);
      }
    }
  }
}
