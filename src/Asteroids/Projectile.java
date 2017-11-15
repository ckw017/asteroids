package asteroids;

import java.awt.Color;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Projectile extends GameObject{
	public static BufferedImage[] sprites = IOTools.loadSprites("projectile_", 3);
	public static Color[] colors = new Color[] {Color.CYAN, Color.YELLOW, Color.MAGENTA};
	public int playerID;
	public int age;
	public int lifetime;
	public Player owner;
	
	public Projectile(Player player) {
		super(player.handler, new Vector(player.position), player.handler.settings.PROJECTILE_RADIUS);
		this.playerID = player.playerID;
		this.velocity = new Vector(player.rotation, handler.settings.PROJECTILE_SPEED, true);
		this.rotation = player.rotation;
		this.sprite = sprites[this.playerID - 1];
		this.hitbox_color = player.hitbox_color;
		this.lifetime = handler.settings.PROJECTILE_LIFETIME;
		this.health = handler.settings.PROJECTILE_HEALTH;
		this.age = 0;
		this.handler.projectiles.add(this);
	}
	
	public Projectile(ObjectHandler handler, Update u) {
		super(handler, u.position, handler.settings.PROJECTILE_RADIUS);
		this.objectID = u.id;
		this.playerID = u.playerID;
		this.velocity = u.velocity;
		this.rotation = u.velocity.angle();
		this.sprite = sprites[this.playerID - 1];
		this.hitbox_color = colors[playerID - 1];
		this.lifetime = handler.settings.PROJECTILE_LIFETIME;
		this.health = handler.settings.PROJECTILE_HEALTH;
		this.age = 0;
		this.handler.projectiles.add(this);
	}
	
	public void update() {
		super.update();
		this.age++;
		if(this.age > lifetime) {
			this.kill();
		}
	}
	
  public void loopLocation() {
    double x = this.position.x;
    double y = this.position.y;
    if (x < -25) {
      this.position.x = handler.settings.FRAME_WIDTH + 25;
    } else if (x > handler.settings.FRAME_WIDTH + 25) {
      this.position.x = -25;
    }
    if (y < -25) {
      this.position.y = handler.settings.FRAME_HEIGHT + 25;
    } else if (y > handler.settings.FRAME_HEIGHT + 25) {
      this.position.y = -25;
    }
  }
	
	public void kill() {
		this.handler.projectiles.remove(this);
		this.curr_bucket.projectiles.remove(this);
	}
}
