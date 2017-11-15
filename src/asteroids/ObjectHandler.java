package asteroids;

import java.util.ArrayList;
import java.util.Random;

public class ObjectHandler {
  public static Random gen = new Random();
  public ArrayList<Asteroid> asteroids;
  public ArrayList<Projectile> projectiles;
  public ArrayList<Player> players;
  public CollisionHandler collision_handler;
  public Settings settings;
  public boolean reset;

  public ObjectHandler(Settings settings) {
  	this.settings = settings;
    this.asteroids = new ArrayList<Asteroid>();
    this.projectiles = new ArrayList<Projectile>();
    this.players = new ArrayList<Player>();
    this.spawnPlayers();
    this.collision_handler = new CollisionHandler(this);
    this.generateAsteroids();
    this.reset = false;
  }

  public void spawnPlayers() {
    if (settings.LOCAL_MULTIPLAYER) {
      assert settings.NUMBER_OF_PLAYERS > 1 : "Local Multiplayer requires more than 1 player";
      if (settings.NUMBER_OF_PLAYERS == 2) {
        players.add(
            new Player(this, new Vector(settings.SCREEN_WIDTH / 4, settings.SCREEN_HEIGHT / 2), 1));
        players.add(
            new Player(
                this, new Vector(settings.SCREEN_WIDTH * 3 / 4, settings.SCREEN_HEIGHT / 2), 2));
      } else if (settings.NUMBER_OF_PLAYERS == 3) {
        players.add(
            new Player(this, new Vector(settings.SCREEN_WIDTH / 4, settings.SCREEN_HEIGHT / 4), 1));
        players.add(
            new Player(
                this, new Vector(settings.SCREEN_WIDTH * 3 / 4, settings.SCREEN_HEIGHT / 4), 2));
        players.add(
            new Player(
                this, new Vector(settings.SCREEN_WIDTH / 2, settings.SCREEN_HEIGHT * 3 / 4), 3));
      }
    } else if (settings.LAN_MULTIPLAYER) {

    } else {
      players.add(
          new Player(this, new Vector(settings.SCREEN_WIDTH / 2, settings.SCREEN_HEIGHT / 2), 1));
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
    for (int i = 0; i < settings.ASTEROID_COUNT; i++) {
      while (true) {
        double x = gen.nextDouble() * settings.FRAME_WIDTH;
        double y = gen.nextDouble() * settings.FRAME_HEIGHT;
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
