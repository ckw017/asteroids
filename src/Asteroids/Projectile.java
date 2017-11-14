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
	
	public Projectile(Player player) {
		super(player.handler, new Vector(player.position), Settings.PROJECTILE_RADIUS);
		this.playerID = player.playerID;
		this.velocity = new Vector(player.rotation, Settings.PROJECTILE_SPEED, true);
		this.rotation = player.rotation;
		this.sprite = sprites[this.playerID - 1];
		this.hitbox_color = player.hitbox_color;
		this.lifetime = Settings.PROJECTILE_LIFETIME;
		this.health = Settings.PROJECTILE_HEALTH;
		this.age = 0;
		this.handler.projectiles.add(this);
	}
	
	public Projectile(ObjectHandler handler, Update u) {
		super(handler, u.position, Settings.PROJECTILE_RADIUS);
		this.objectID = u.id;
		this.playerID = u.playerID;
		this.velocity = u.velocity;
		this.rotation = u.velocity.angle();
		this.sprite = sprites[this.playerID - 1];
		this.hitbox_color = colors[playerID - 1];
		this.lifetime = Settings.PROJECTILE_LIFETIME;
		this.health = Settings.PROJECTILE_HEALTH;
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
	
	public void update(Update u) {
		
	}
	
	public void kill() {
		this.handler.projectiles.remove(this);
		this.curr_bucket.projectiles.remove(this);
	}
}
