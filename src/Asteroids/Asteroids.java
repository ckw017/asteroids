package asteroids;

import java.util.ArrayList;

public class Asteroids {
  public ArrayList<Player> players = new ArrayList<Player>();
  public ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
  public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
  public UpdateHandler updateHandler;
  public short playerID = 1;
  public GameFrame frame;
  public GamePanel panel;
  public ArrayList<Point> spawnPoints = new ArrayList<Point>();
  public boolean isHost = true;

  public Asteroids() {
    CollisionBucket.generateBucketList();
    createPlayers(SaveUtility.NUMBER_OF_PLAYERS);
    initializeGraphics();
    if (SaveUtility.LAN_MULTIPLAYER) {
      updateHandler = new UpdateHandler(players, asteroids, projectiles);
      updateHandler.setFrame(frame);
      playerID = updateHandler.clientID;
      if (playerID == 1) {
        generateAsteroids();
      }
    } else {
      generateAsteroids();
    }
    setPlayers();
    new GameThread().start();
  }

  public static void main(String[] args) {
    new Asteroids();
  }

  class Point {
    public int x;
    public int y;

    public Point(int xPos, int yPos) {
      x = xPos;
      y = yPos;
    }
  }

  /* * * * * * * * * *
   * PLAYER CREATION *
   * * * * * * * * * */
  public void createPlayers(int playerAmount) {
    players.clear();
    setSpawnPoints(playerAmount);
    for (int i = 0; i < playerAmount; i++) {
      players.add(new Player((short) (i + 1), spawnPoints.get(i).x, spawnPoints.get(i).y));
    }
  }

  public void setPlayers() {
    for (Player p : players) {
      frame.addObject(p);
    }
    frame.setPlayer(players.get(playerID - 1));
    if (SaveUtility.LOCAL_MULTIPLAYER) {
      frame.setPlayerTwo(players.get(1));
    }
  }

  public void setSpawnPoints(int playerAmount) {
    spawnPoints.clear();
    if (playerAmount == 1) {
      spawnPoints.add(new Point(SaveUtility.SCREEN_WIDTH / 2, SaveUtility.SCREEN_HEIGHT / 2));
    } else if (playerAmount == 2) {
      spawnPoints.add(new Point(SaveUtility.SCREEN_WIDTH / 4, SaveUtility.SCREEN_HEIGHT / 2));
      spawnPoints.add(new Point(SaveUtility.SCREEN_WIDTH * 3 / 4, SaveUtility.SCREEN_HEIGHT / 2));
    } else if (playerAmount == 3) {
      spawnPoints.add(new Point(SaveUtility.SCREEN_WIDTH / 4, SaveUtility.SCREEN_HEIGHT / 4));
      spawnPoints.add(new Point(SaveUtility.SCREEN_WIDTH * 3 / 4, SaveUtility.SCREEN_HEIGHT / 4));
      spawnPoints.add(new Point(SaveUtility.SCREEN_WIDTH / 2, SaveUtility.SCREEN_HEIGHT * 3 / 4));
    }
  }

  /* * * * * * * * * * * *
   * GAME INITIALIZATION *
   * * * * * * * * * * * */

  public void initializeGraphics() {
    frame = GameFrame.createAndShowGUI();
    panel = frame.getGamePanel();
  }

  public void generateAsteroids() {
    for (int i = 0; i < SaveUtility.ASTEROID_COUNT; i++) {
      addObject(Asteroid.generateAsteroid((short) 3, 200, players));
    }
  }

  /* * * * * * *
   * GAME LOOP *
   * * * * * * */

  class GameThread extends Thread implements Runnable {
    public void run() {
      while (true) {
        long startTime = System.currentTimeMillis();
        try {
          handleCollision();
          frame.repaint();
          handleMovement();
          if (SaveUtility.LAN_MULTIPLAYER) {
            if (playerID == 1) {
              handleCollision();
            }
            updateHandler.outputUpdates();
          } else {
            //handleCollision();
          }
          trySleep((int) (40.0 + startTime - System.currentTimeMillis()));
        } catch (Exception e) {
          System.out.println("Error in GameThread");
          e.printStackTrace(System.out);
          trySleep((int) (40.0 + startTime - System.currentTimeMillis()));
          //break;
        }
      }
    }

    public void trySleep(int milliseconds) {
      try {
        if (milliseconds < 0) {
          milliseconds = 0;
        }
        sleep(milliseconds);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* * * * * * *
   * MOVEMENT  *
   * * * * * * */

  public void handleMovement() {
    for (Player p : players) {
      p.travel();
    }
    for (Projectile p : projectiles) {
      p.travel();
    }
    for (Asteroid a : asteroids) {
      a.travel();
    }
    if (SaveUtility.LAN_MULTIPLAYER) {
      updateHandler.addUpdate(players.get(playerID - 1));
    }
    handleShooting();
  }

  public void handleShooting() {
    //for(Player p: players){
    Player p = players.get(playerID - 1);
    if (p.isAlive && p.isShooting && p.shotCounter == 0) {
      Projectile shot = p.shoot();
      addObject(shot);
      if (SaveUtility.LAN_MULTIPLAYER) {
        updateHandler.addUpdate(shot);
      }
      p.shotCounter++;
    } else if (p.shotCounter != 0) {
      p.shotCounter++;
      p.shotCounter %= SaveUtility.PLAYER_SHOOT_DELAY + 1;
    }
    //}
  }

  /* * * * * * *
   * COLLISION *
   * * * * * * */
  public void handleCollision() {
    for (int x = 0; x < CollisionBucket.bucketsWidth - 1; x++) {
      for (int y = 0; y < CollisionBucket.bucketsHeight - 1; y++) {
        for (Asteroid a : CollisionBucket.mergeAsteroids(x, y)) {
          for (Projectile p : CollisionBucket.mergeProjectiles(x, y)) {
            if (a.isAlive && !a.collide(p)) {
              if (playerID == 1) {
                addObject(a.split());
              }
              updateHandler.addDeathUpdate(a.objectID);
              if (!p.isAlive && SaveUtility.LAN_MULTIPLAYER) {
                updateHandler.addProjectileDeathUpdate(p.projID, p.pid);
              }
            }
          }
          for (Player p : CollisionBucket.mergePlayers(x, y)) {
            if (a.isAlive && !a.collide(p)) {
              if (playerID == 1) {
                addObject(a.split());
              }
              if (SaveUtility.LAN_MULTIPLAYER) {
                updateHandler.addDeathUpdate(a.objectID);
                if (!p.isAlive && SaveUtility.LAN_MULTIPLAYER) {
                  updateHandler.addDeathUpdate(p.objectID);
                }
              }
            }
          }
        }
      }
    }
    checkForWinner();
    updateLists();
  }

  /* * * * * * * * *
   * OBJECT ADDING *
   * * * * * * * * */

  public void addObject(GameObject o) {
    frame.addObject(o);
    if (o instanceof Asteroid) {
      Asteroid a = (Asteroid) o;
      asteroids.add(a);
      if (SaveUtility.LAN_MULTIPLAYER && playerID == 1) {
        updateHandler.addUpdate(a);
      }
    } else if (o instanceof Projectile) {
      Projectile p = (Projectile) o;
      projectiles.add(p);
    } else if (o instanceof Player) {
      Player p = (Player) o;
      players.add(p);
    }
  }

  public void addObject(ArrayList<Asteroid> objs) {
    for (Asteroid o : objs) {
      addObject(o);
    }
  }

  public void updateLists() {
    for (int i = players.size() - 1; i >= 0; i--) {
      if (!players.get(i).isAlive) {
        //players.remove(i);
      }
    }
    for (int i = asteroids.size() - 1; i >= 0; i--) {
      if (!asteroids.get(i).isAlive) {
        asteroids.remove(i);
      }
    }
    for (int i = projectiles.size() - 1; i >= 0; i--) {
      if (!projectiles.get(i).isAlive) {
        projectiles.remove(i);
      }
    }
  }

  /* * * * * * * * *
   * WIN DETECTION *
   * * * * * * * * */

  public void checkForWinner() {
    if (asteroids.size() == 0) {
      System.out.println("YOU WON");
      //reset();
    }
    boolean livePlayer = false;
    for (Player p : players) {
      if (p.isAlive) {
        livePlayer = true;
      }
    }
    if (!livePlayer) {
      System.out.println("YOU LOST");
      //reset();
    }
  }

  public void killIfHost(int objID) {
    if (playerID == 1) {
      updateHandler.addDeathUpdate(objID);
    }
  }

  public void reset() {
    for (Player p : players) {
      p.kill();
      updateHandler.addDeathUpdate(p.objectID);
    }
    for (Asteroid a : asteroids) {
      a.kill();
      updateHandler.addDeathUpdate(a.objectID);
    }
    for (Projectile p : projectiles) {
      p.kill();
      if (playerID == 1) {
        updateHandler.addProjectileDeathUpdate(p.projID, p.pid);
      }
    }
    updateLists();
    createPlayers(SaveUtility.NUMBER_OF_PLAYERS);
    if (playerID == 1) {
      generateAsteroids();
    }
    setPlayers();
  }
}
