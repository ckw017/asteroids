package asteroids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import asteroids.GameFrame.State;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
  public GameFrame frame;
  public ObjectHandler handler;

  public BufferedImage logo;
  public BufferedImage enter;
  public BufferedImage game_over;
  public BufferedImage win;
  public BufferedImage win_1;
  public BufferedImage win_2;
  public BufferedImage win_3;
  public HashMap<GameFrame.State, BufferedImage> text_images;

  public double prev_end = 0;
  public double frame_rate;
  public double update_rate;
  public int collision_count;
  public Color debug_bg = new Color(0, 0, 0, 128);

  public GamePanel(GameFrame frame) {
    this.frame = frame;
    this.handler = this.frame.handler;
    this.setSize(handler.settings.SCREEN_WIDTH, handler.settings.SCREEN_HEIGHT);
    this.setPreferredSize(new Dimension(handler.settings.SCREEN_WIDTH, handler.settings.SCREEN_HEIGHT));
    this.frame_rate = handler.settings.FRAME_RATE;
    this.update_rate = this.frame_rate;
    this.collision_count = 0;
    this.setSprites();
  }

  public void setSprites() {
    double scalar = (handler.settings.SCREEN_WIDTH >= 1000) ? 1 : handler.settings.SCREEN_WIDTH / 1000.0;
    loadSprites(scalar);
    this.text_images = new HashMap<GameFrame.State, BufferedImage>();
    this.text_images.put(State.START_MENU, logo);
    this.text_images.put(State.GAME_OVER, game_over);
    this.text_images.put(State.VICTORY, win);
    this.text_images.put(State.PLAYER_1_WIN, win_1);
    this.text_images.put(State.PLAYER_2_WIN, win_2);
    this.text_images.put(State.PLAYER_3_WIN, win_3);
  }

  public void loadSprites(double scalar) {
    if (handler.settings.ASTEROID_RAINBOW) {
      this.logo = IOTools.getImage("logo_rainbow.png", scalar);
    } else {
      this.logo = IOTools.getImage("logo.png", scalar);
    }
    this.enter = IOTools.getImage("enter.png", scalar);
    this.game_over = IOTools.getImage("game_over.png", scalar);
    this.win = IOTools.getImage("win.png", scalar);
    this.win_1 = IOTools.getImage("win_1.png", scalar);
    this.win_2 = IOTools.getImage("win_2.png", scalar);
    this.win_3 = IOTools.getImage("win_3.png", scalar);
  }

  public void paintComponent(Graphics g) {
    try {
      Graphics2D g2 = (Graphics2D) g;
      paintGame(g);
      if (handler.settings.DRAW_DEBUG) {
        drawDebug(g2);
      }
      if (frame.state != State.GAME_STATE) {
        drawText(text_images.get(frame.state), g2);
        drawEnter(g2);
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
      paintComponent(g);
    }
    double new_end = System.currentTimeMillis();
    double frame_delay = new_end - this.prev_end;
    this.prev_end = new_end;
    this.frame_rate =
        round(
            (frame_delay < 1000 / handler.settings.FRAME_RATE) ? handler.settings.FRAME_RATE : 1000 / frame_delay,
            1);
  }

  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public void paintGame(Graphics g) {
    if (handler.settings.DRAW_BUCKETS) {
      this.handler.collision_handler.paintBuckets(g);
    }
    paintList(this.handler.asteroids, g);
    paintList(this.handler.projectiles, g);
    paintList(this.handler.players, g);
  }

  public void paintList(ArrayList<? extends GameObject> list, Graphics g) {
    for (int i = list.size(); i > 0; i--) {
      try {
        list.get(i - 1).paintComponent(g);
        i = (list.size() < i) ? list.size() : i;
      } catch (Exception e) {
        e.printStackTrace(System.out);
        i = (list.size() < i) ? list.size() : i;
      }
    }
  }

  public void drawDebug(Graphics g2) {
    g2.setColor(this.debug_bg);
    g2.fillRect(5, 0, 135, 55);
    g2.setColor(Color.WHITE);
    this.update_rate = round(update_rate, 1);
    this.collision_count = handler.collision_handler.collision_count;
    g2.drawString("Update rate: " + this.update_rate + "/s", 10, 10);
    g2.drawString("Frame rate:  " + this.frame_rate  + "/s", 10, 20);
    g2.drawString("Asteroid count:   " + handler.asteroids.size(), 10, 30);
    g2.drawString("Projectile count: " + handler.projectiles.size(), 10, 40);
    g2.drawString("Collision checks: " + this.collision_count, 10, 50);
  }

  public void drawText(BufferedImage t, Graphics2D g2) {
    g2.drawImage(
        t,
        (handler.settings.SCREEN_WIDTH - t.getWidth()) / 2,
        (handler.settings.SCREEN_HEIGHT - t.getHeight()) / 3,
        this);
  }

  public void drawEnter(Graphics2D g2) {
    if (frame.counter % (1.25 * handler.settings.FRAME_RATE) < .675 * handler.settings.FRAME_RATE) {
      g2.drawImage(
          enter,
          (handler.settings.SCREEN_WIDTH - enter.getWidth()) / 2,
          (handler.settings.SCREEN_HEIGHT - enter.getHeight()) / 2,
          this);
    }
  }
}