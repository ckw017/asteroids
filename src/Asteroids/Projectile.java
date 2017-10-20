package asteroids;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Projectile extends GameObject {
  public static BufferedImage spr1 = getSprite("playerProj1.png");
  public static BufferedImage spr2 = getSprite("playerProj2.png");

  public int lt = SaveUtility.PROJECTILE_LIFETIME;
  public int age = 0;
  public int spd = SaveUtility.PROJECTILE_SPEED;
  public short pid;
  public int projID;
  public static ArrayList<Integer> counters =
      new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

  public Projectile(short ownerID) {
    super(false);
    pid = ownerID;
    radius = SaveUtility.PROJECTILE_RADIUS;
    hp = SaveUtility.PROJECTILE_HEALTH;
    setGraphics();
  }

  public Projectile(Projectile p, double xPosition, double yPosition, double angle) {
    super(false);
    pid = p.pid;
    projID = counters.get(pid - 1);
    projID = counters.set(pid - 1, counters.get(pid - 1) + 1);
    radius = p.radius;
    lt = p.lt;
    spd = p.spd;
    x = xPosition;
    y = yPosition;
    hb = new Ellipse2D.Double(x, y, radius * 2, radius * 2);
    rotation = angle;
    hbColor = p.hbColor;
    sprite = p.sprite;
    xv = spd * Math.cos(Math.toRadians(angle - 90));
    yv = spd * Math.sin(Math.toRadians(angle - 90));
  }

  public Projectile(ProjectileUpdate u) {
    super(false);
    objectID = u.objectID;
    pid = u.pid;
    projID = u.projID;
    radius = SaveUtility.PROJECTILE_RADIUS;
    hp = SaveUtility.PROJECTILE_HEALTH;
    hb = new Ellipse2D.Double(x, y, radius * 2, radius * 2);
    setGraphics();
    x = u.x;
    y = u.y;
    xv = u.xv;
    yv = u.yv;
    rotation = u.rot;
  }

  public void setGraphics() {
    if (pid == 1) {
      hbColor = new Color(0, 255, 255);
      sprite = spr1;
    } else if (pid == 2) {
      hbColor = new Color(255, 255, 0);
      sprite = spr2;
    } else if (pid >= 3) {
      hbColor = new Color(0, 255, 255);
      sprite = spr1;
    }
  }

  @Override
  public void travel() {
    super.travel();
    age++;
    if (age >= lt) {
      isAlive = false;
      handleBuckets();
    }
  }
}
