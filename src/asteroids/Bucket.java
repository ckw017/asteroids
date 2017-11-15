package asteroids;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bucket {
  public Vector position;
  public Vector scaled_position;
  public ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
  public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
  public ArrayList<Player> players = new ArrayList<Player>();
  public ArrayList<ArrayList<? extends GameObject>> list_array =
      new ArrayList<ArrayList<? extends GameObject>>();

  public Color draw_color = Color.WHITE;
  public Color fill_color = Color.BLACK;
  public Rectangle shape;
  public Font debug_font = new Font("", Font.PLAIN, 8);
  public Font default_font = new Font("", Font.PLAIN, 12);
  public Settings settings;
  public int collision_count;

  public Bucket(Settings settings, Vector position) {
  	this.settings = settings;
    this.position = position;
    this.scaled_position = (new Vector(this.position)).scale(settings.BUCKET_SIZE);
    this.shape =
        new Rectangle(
            (int) (this.position.x * settings.BUCKET_SIZE),
            (int) (this.position.y * settings.BUCKET_SIZE),
            settings.BUCKET_SIZE - 1,
            settings.BUCKET_SIZE - 1);
    this.list_array.add(asteroids);
    this.list_array.add(projectiles);
    this.list_array.add(players);
    this.draw_color = Color.WHITE;
    this.collision_count = 0;
  }
  
  public Bucket() {
  	this.position = new Vector(-1, -1);
  }

  public void add(GameObject o) {
    if (o instanceof Asteroid) {
      asteroids.add((Asteroid) o);
    } else if (o instanceof Projectile) {
      projectiles.add((Projectile) o);
    } else if (o instanceof Player) {
      players.add((Player) o);
    }
  }

  public void remove(GameObject o) {
    if (o instanceof Asteroid) {
      asteroids.remove(o);
    } else if (o instanceof Projectile) {
      projectiles.remove(o);
    } else if (o instanceof Player) {
      players.remove(o);
    }
  }

  public void collide(Bucket b) {
    collideLists(this.asteroids, b.projectiles);
    collideLists(this.asteroids, b.players);
    collideLists(b.asteroids, this.projectiles);
    collideLists(b.asteroids, this.players);
    if (settings.PVP) {
      collideLists(this.players, b.players);
      collideLists(this.players, b.projectiles);
      collideLists(b.players, this.projectiles);
    }
  }

  public void collide() {
    this.collide(this);
  }

  public void collideLists(
      ArrayList<? extends GameObject> list1, ArrayList<? extends GameObject> list2) {
    for (int i = list1.size(); i > 0; i--) {
      for (int j = list2.size(); j > 0; j--) {
        if (list1.size() <= 0 || list2.size() <= 0) {
          break;
        }
        list1.get(i - 1).collide(list2.get(j - 1));
        collision_count++;
        j = (list2.size() < j) ? list2.size() : j;
        i = (list1.size() < i) ? list1.size() : i;
      }
      if (i <= 0) {
        break;
      }
    }
  }

  public void reset() {
    this.asteroids.clear();
    this.projectiles.clear();
    this.players.clear();
  }

  public void updateColor() {
    int total = asteroids.size() + projectiles.size() + players.size();
    if (total == 0) {
      this.draw_color = Color.WHITE;
      this.fill_color = Color.BLACK;
    } else {
      int r, g, b;
      r = g = b = 0;
      for (ArrayList<? extends GameObject> list : this.list_array) {
        for (int i = list.size(); i > 0; i--) {
          GameObject o = list.get(i - 1);
          Color c = o.hitbox_color;
          r += c.getRed();
          g += c.getGreen();
          b += c.getBlue();
        }
      }
      r /= total;
      g /= total;
      b /= total;
      draw_color = new Color(r, g, b);
      fill_color = new Color(r / 10, g / 10, b / 10);
    }
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.WHITE);
    g2.setColor(fill_color);
    g2.fill(this.shape);
    g2.setColor(this.draw_color);
    g2.draw(this.shape);
    if (settings.DRAW_DEBUG) {
      //paintDebug(g2);
    }
  }

  public void paintDebug(Graphics2D g2) {
    g2.setFont(this.debug_font);
    int x = (int) scaled_position.x + 2;
    int y = (int) scaled_position.y + 2;
    g2.setColor(Color.WHITE);
    g2.drawString("a: " + asteroids.size(), x, y + 10);
    g2.drawString("p: " + projectiles.size(), x, y + 20);
    g2.setFont(this.default_font);
  }
}
