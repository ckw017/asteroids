package asteroids;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Player extends GameObject {
  public static BufferedImage spr1 = getSprite("player1.png");
  public static BufferedImage spr2 = getSprite("player2.png");

  public short pid;
  public int maxSpd = SaveUtility.PLAYER_MAX_SPEED;
  public double rotSpd = SaveUtility.PLAYER_TURN_SPEED;
  public short rotDir = 0;
  public boolean isMoving = false;
  public boolean isShooting = false;
  public Projectile proj;
  public int shotCounter = 0;

  public Player(short id, int xPosition, int yPosition) {
    super();
    pid = id;
    proj = new Projectile(pid);
    x = xPosition;
    y = yPosition;
    radius = 12;
    hb = new Ellipse2D.Double(xPosition - radius, yPosition - radius, 24, 24);

    if (id == 1) {
      sprite = spr1;
      hbColor = new Color(0, 255, 255);
    } else if (id == 2) {
      sprite = spr2;
      hbColor = new Color(255, 255, 0);
    } else if (id >= 3) {
      sprite = spr1;
      hbColor = new Color(0, 255, 255);
    }
  }

  public void applyForce(double f) {
    if (f == 0) {
      scaleVelocity(SaveUtility.PLAYER_FRICTION); /*If no force is applied, the ship decelerates*/
    } else {
      //Vector calculations
      xv += Math.cos(Math.toRadians(rotation - 90)) * f;
      yv += Math.sin(Math.toRadians(rotation - 90)) * f;
    }
    //Max speed calculations
    double moveAngle = Math.atan2(yv, xv);
    double xMax = Math.cos(moveAngle) * maxSpd;
    double yMax = Math.sin(moveAngle) * maxSpd;
    if (Math.abs(xv) > Math.abs(xMax)) {
      xv = xMax;
    }
    if (Math.abs(yv) > Math.abs(yMax)) {
      yv = yMax;
    }
  }

  public void scaleVelocity(double s) {
    xv *= s;
    yv *= s;
  }

  public Projectile shoot() {
    return new Projectile(proj, x, y, rotation);
  }

  public void update(PlayerUpdate u) {
    x = u.x;
    y = u.y;
    xv = u.xv;
    yv = u.yv;
    rotation = u.rotation;
    isAlive = u.isAlive;
  }

  @Override
  public void travel() {
    if (isMoving) {
      applyForce(SaveUtility.PLAYER_THRUST);
    } else {
      applyForce(0);
    }
    rotation += rotDir * SaveUtility.PLAYER_TURN_SPEED;
    super.travel();
  }

  @Override
  public void damage(int d) {
    if (!SaveUtility.PLAYER_INVULNERABILITY) {
      super.damage(d);
    }
  }
}
