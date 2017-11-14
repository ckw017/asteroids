package asteroids;

import java.util.ArrayList;
import java.util.Random;

public class ObjectHandler {
  public static Random gen = new Random();
  public ArrayList<Asteroid> asteroids;
  public ArrayList<Projectile> projectiles;
  public ArrayList<Player> players;
  public CollisionHandler collision_handler;
  public boolean reset;

  public ObjectHandler() {
    this.asteroids = new ArrayList<Asteroid>();
    this.projectiles = new ArrayList<Projectile>();
    this.players = new ArrayList<Player>();
    this.spawnPlayers();
    this.collision_handler = new CollisionHandler(this);
    this.generateAsteroids();
    this.reset = false;
  }

  public void spawnPlayers() {
    if (Settings.LOCAL_MULTIPLAYER) {
      assert Settings.NUMBER_OF_PLAYERS > 1 : "Local Multiplayer requires more than 1 player";
      if (Settings.NUMBER_OF_PLAYERS == 2) {
        players.add(
            new Player(this, new Vector(Settings.SCREEN_WIDTH / 4, Settings.SCREEN_HEIGHT / 2), 1));
        players.add(
            new Player(
                this, new Vector(Settings.SCREEN_WIDTH * 3 / 4, Settings.SCREEN_HEIGHT / 2), 2));
      } else if (Settings.NUMBER_OF_PLAYERS == 3) {
        players.add(
            new Player(this, new Vector(Settings.SCREEN_WIDTH / 4, Settings.SCREEN_HEIGHT / 4), 1));
        players.add(
            new Player(
                this, new Vector(Settings.SCREEN_WIDTH * 3 / 4, Settings.SCREEN_HEIGHT / 4), 2));
        players.add(
            new Player(
                this, new Vector(Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT * 3 / 4), 3));
      }
    } else if (Settings.LAN_MULTIPLAYER) {

    } else {
      players.add(
          new Player(this, new Vector(Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2), 1));
    }
  }

  public void reset() {
    this.asteroids.clear();
    this.projectiles.clear();
    for (Player p : players) {
      p.reset();
    }
    this.collision_handler.reset();
    this.generateAsteroids();
  }

  public void update() {
    if (this.reset) {
    	this.reset = false;
    	this.reset();
    } else {
      updateList(this.asteroids);
      updateList(this.projectiles);
      updateList(this.players);
      this.collision_handler.update();
    }
  }

  public void updateList(ArrayList<? extends GameObject> list) {
    for (int i = list.size(); i > 0; i--) {
      list.get(i - 1).update();
      i = (list.size() < i) ? list.size() : i;
    }
  }

  public void generateAsteroids() {
    Vector[] centers = getPlayerCenters();
    for (int i = 0; i < Settings.ASTEROID_COUNT; i++) {
      while (true) {
        double x = gen.nextDouble() * Settings.FRAME_WIDTH;
        double y = gen.nextDouble() * Settings.FRAME_HEIGHT;
        Vector v = new Vector(x, y);
        boolean valid = true;
        for (Vector c : centers) {
          if (v.distance(c) < 200) {
            valid = false;
          }
        }
        if (valid) {
          new Asteroid(this, v, 3);
          break;
        }
      }
    }
  }

  public Vector[] getPlayerCenters() {
    Vector[] centers = new Vector[players.size()];
    for (int i = 0; i < players.size(); i++) {
      centers[i] = new Vector(players.get(i).position);
    }
    return centers;
  }
}
