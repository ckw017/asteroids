package asteroids;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class UpdateHandler {
  public ArrayList<Player> players;
  public ArrayList<Asteroid> asteroids;
  public ArrayList<Projectile> projectiles;
  public GameFrame frame;
  public ArrayList<Update> updateList;

  public String ip = "localhost";
  public int port = 12345;
  public Socket socket;
  public ObjectOutputStream dos;
  public ObjectInputStream dis;
  public ServerSocket serverSocket;
  public boolean accepted = false;
  public short clientID = 1;

  public UpdateHandler(ArrayList<Player> pla, ArrayList<Asteroid> ast, ArrayList<Projectile> pro) {
    players = pla;
    asteroids = ast;
    projectiles = pro;
    updateList = new ArrayList<Update>();
    if (SaveUtility.LAN_MULTIPLAYER) {
      if (!connect()) {
        System.out.println("Initializing Server");
        initializeServer();
      }
      if (!accepted) {
        clientID = 2;
        System.out.println("Listening for server request");
        listenForServerRequest();
      }
    }
  }

  public void setFrame(GameFrame f) {
    frame = f;
    new InputStreamThread().start();
  }

  /* * * * * * *
   * LAN STUFF *
   * * * * * * */

  public void listenForServerRequest() {
    Socket socket = null;
    try {
      socket = serverSocket.accept();
      dos = new ObjectOutputStream(socket.getOutputStream());
      dis = new ObjectInputStream(socket.getInputStream());
      accepted = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean connect() {
    try {
      socket = new Socket(ip, port);
      dos = new ObjectOutputStream(socket.getOutputStream());
      dis = new ObjectInputStream(socket.getInputStream());
      accepted = true;
    } catch (IOException e) {
      System.out.println(
          "Unable to connect to the address: " + ip + ":" + port + " | Starting a server");
      return false;
    }
    System.out.println("Successfully connected to the server.");
    return true;
  }

  public void initializeServer() {
    try {
      serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void outputUpdates() {
    try {
      dos.writeObject(updateList);
      dos.flush();
      dos.reset();
      updateList.clear();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  public class InputStreamThread extends Thread implements Runnable {
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
      while (true) {
        try {
          Object o = dis.readObject();
          handleUpdates((ArrayList<Update>) o);
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Opponent has disconnected");
          break;
        } catch (ClassNotFoundException e) {
          System.out.println("Class not found Exception");
        }
      }
    }
  }

  /* * * * * * * * *
   * UPDATE STUFF  *
   * * * * * * * * */

  public void handleUpdates(ArrayList<Update> updates) {
    for (Update u : updates) {
      if (u.isAlive) {
        if (u instanceof AsteroidUpdate) {
          Asteroid a = new Asteroid((AsteroidUpdate) u);
          asteroids.add(a);
          frame.addObject(a);
        } else if (u instanceof ProjectileUpdate) {
          Projectile p = new Projectile((ProjectileUpdate) u);
          projectiles.add(p);
          frame.addObject(p);
        } else if (u instanceof PlayerUpdate) {
          PlayerUpdate pu = (PlayerUpdate) u;
          for (Player p : players) {
            pu = (PlayerUpdate) u;
            if (pu.pid == p.pid) {
              p.update(pu);
              break;
            }
          }
        }
      } else {
        kill(u);
      }
    }
  }

  public void addDeathUpdate(int objectID) {
    updateList.add(new Update(objectID, false));
  }

  public void addProjectileDeathUpdate(int projID, short pid) {
    updateList.add(new ProjectileUpdate(projID, pid));
  }

  public void addUpdate(Asteroid a) {
    updateList.add(new AsteroidUpdate(a.objectID, a.x, a.y, a.xv, a.yv, a.rotSpd, a.size));
  }

  public void addUpdate(Projectile p) {
    updateList.add(new ProjectileUpdate(p.projID, p.pid, p.x, p.y, p.xv, p.yv, p.rotation));
  }

  public void addUpdate(Player p) {
    updateList.add(
        new PlayerUpdate(p.objectID, p.pid, p.x, p.y, p.xv, p.yv, p.rotation, p.isAlive));
  }

  public void kill(Update u) {
    int id = u.objectID;
    for (Asteroid a : asteroids) {
      if (a.objectID == id) {
        a.kill();
        return;
      }
    }
    if (u instanceof ProjectileUpdate) {
      ProjectileUpdate pu = (ProjectileUpdate) u;
      for (Projectile p : projectiles) {
        if (p.projID == pu.projID && p.pid == pu.pid) {
          p.kill();
          return;
        }
      }
    }
    for (Player p : players) {
      if (p.objectID == id) {
        p.kill();
        return;
      }
    }
  }
}
