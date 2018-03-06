package asteroids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
  public ObjectHandler handler;
  public GamePanel panel;
  public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
  public State state;
  public int counter = 0;

  public enum State {
    START_MENU,
    GAME_STATE,
    GAME_OVER,
    VICTORY,
    PLAYER_1_WIN,
    PLAYER_2_WIN,
    PLAYER_3_WIN;
  }

  public State[] pvp_wins =
      new State[] {State.PLAYER_1_WIN, State.PLAYER_2_WIN, State.PLAYER_3_WIN};

  public GameFrame(ObjectHandler handler) {
    super();
    this.setLayout(new FlowLayout());
    this.handler = handler;
    this.panel = new GamePanel(this);
    this.add(panel);
    this.setTitle("Asteroids");
    this.setSize(handler.settings.FRAME_WIDTH, handler.settings.FRAME_HEIGHT);
    this.adjustScreen();
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.addKeyListener(new InputListener());
    this.getContentPane().setBackground(Color.black);
    this.setIconImage(IOTools.getImage("icon_small.png"));
    this.setVisible(true);
    this.setStartMenu();
  }

  public void setStartMenu() {
    this.state = State.START_MENU;
    for (Player p : handler.players) {
      p.rotation_direction = 2 * (p.playerID % 2) - 1;
      p.is_shooting = true;
      p.is_invulnerable = true;
    }
  }

  public void setIntermission(State next_state) {
    for (Player p : this.handler.players) {
      if (p.is_alive) {
        p.is_shooting = true;
        p.is_boosting = true;
        p.is_invulnerable = true;
        p.rotation_direction = 2 * (p.playerID % 2) - 1;
      }
    }
    this.state = next_state;
    this.counter = 0;
  }

  public void adjustScreen() {
    if (handler.settings.FULL_SCREEN_WINDOWED) {
      this.setSize(handler.settings.FRAME_WIDTH, handler.settings.FRAME_HEIGHT);
      this.setPreferredSize(new Dimension(handler.settings.FRAME_WIDTH, handler.settings.FRAME_HEIGHT));
    } else if (handler.settings.FULL_SCREEN_BORDERLESS) {
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
      this.setUndecorated(true);
    } else {
      this.setLocation(SCREEN_SIZE.width / 2 - getSize().width / 2, SCREEN_SIZE.height / 5);
    }
  }

  public GameFrame(boolean invis) {
    super();
    setVisible(false);
  }

  public static Insets getFrameInsets() {
    GameFrame g = new GameFrame(true);
    g.pack();
    return g.getInsets();
  }

  public void update() {
    this.repaint();
    this.counter++;
    if (state == State.GAME_STATE) {
      Player p1 = handler.players.get(0);
      if (handler.settings.LOCAL_MULTIPLAYER) {
        multiplayerUpdate();
      } else {
        if (!p1.is_alive) {
          this.setIntermission(State.GAME_OVER);
        }
        if (handler.asteroids.size() == 0) {
          this.setIntermission(State.VICTORY);
        }
      }
      if (handler.settings.PRANK && handler.asteroids.size() == 1) {
        Asteroid a = handler.asteroids.get(0);
        a.size = 2;
        a.health = 5;
        a.split_factor = 2048;
        handler.settings.PRANK = false;
      }
    }
  }

  public void multiplayerUpdate() {
    int alive_count = 0;
    for (Player p : handler.players) {
      alive_count = (p.is_alive) ? alive_count + 1 : alive_count;
    }
    if (alive_count == 0) {
      this.setIntermission(State.GAME_OVER);
    } else if (handler.settings.PVP && alive_count == 1) {
      for (int i = 0; i < handler.players.size(); i++) {
        if (handler.players.get(i).is_alive) {
          setIntermission(pvp_wins[i]);
        }
      }
    } else if (handler.asteroids.size() == 0) {
      this.setIntermission(State.VICTORY);
    }
  }

  public void reset() {
    state = State.GAME_STATE;
    handler.reset = true;
  }

  public class InputListener implements KeyListener {
    public void keyPressed(KeyEvent e) {
      int code = e.getKeyCode();
      if (state == State.GAME_STATE) {
        controlPress(
            handler.players.get(0),
            code,
            KeyEvent.VK_W,
            KeyEvent.VK_A,
            KeyEvent.VK_S,
            KeyEvent.VK_D);
        if (handler.settings.LOCAL_MULTIPLAYER) {
          controlPress(
              handler.players.get(1),
              code,
              KeyEvent.VK_UP,
              KeyEvent.VK_LEFT,
              KeyEvent.VK_DOWN,
              KeyEvent.VK_RIGHT);
          if (handler.settings.NUMBER_OF_PLAYERS == 3) {
            controlPress(
                handler.players.get(2),
                code,
                KeyEvent.VK_NUMPAD8,
                KeyEvent.VK_NUMPAD4,
                KeyEvent.VK_NUMPAD5,
                KeyEvent.VK_NUMPAD6);
          }
        }
      }
      switch (code) {
        case KeyEvent.VK_R:
          reset();
          break;
        case KeyEvent.VK_H:
          handler.settings.DRAW_HITBOXES ^= true;
          break;
        case KeyEvent.VK_B:
          handler.settings.DRAW_BUCKETS ^= true;
          break;
        case KeyEvent.VK_N:
          handler.settings.DRAW_SPRITES ^= true;
          break;
        case KeyEvent.VK_J:
          handler.settings.DRAW_DEBUG ^= true;
          break;
        case KeyEvent.VK_C:
        	handler.settings.DRAW_COLLISIONS ^= true;
        	break;
        case KeyEvent.VK_ENTER:
          if (state != State.GAME_STATE) {
            reset();
          }
          break;
        case KeyEvent.VK_ESCAPE:
          setVisible(false);
          dispose();
          System.exit(0);
          break;
      }
    }

    public void keyReleased(KeyEvent e) {
      int code = e.getKeyCode();
      switch (state) {
        case GAME_STATE:
          controlRelease(
              handler.players.get(0),
              code,
              KeyEvent.VK_W,
              KeyEvent.VK_A,
              KeyEvent.VK_S,
              KeyEvent.VK_D);
          if (handler.settings.LOCAL_MULTIPLAYER) {
            controlRelease(
                handler.players.get(1),
                code,
                KeyEvent.VK_UP,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_RIGHT);
            if (handler.settings.NUMBER_OF_PLAYERS == 3) {
              controlRelease(
                  handler.players.get(2),
                  code,
                  KeyEvent.VK_NUMPAD8,
                  KeyEvent.VK_NUMPAD4,
                  KeyEvent.VK_NUMPAD5,
                  KeyEvent.VK_NUMPAD6);
            }
          }
          break;
        default:
          break;
      }
    }

    public void keyTyped(KeyEvent e) {}

    public void controlPress(Player p, int code, int shoot, int left, int boost, int right) {
      if (code == shoot) {
        p.is_shooting = true;
      } else if (code == boost) {
        p.is_boosting = true;
      } else if (code == left) {
        if (p.rotation_direction == 0) {
          p.rotation_direction = -1;
        } else if (p.rotation_direction == 1) {
          p.queued_direction = -1;
        }
      } else if (code == right) {
        if (p.rotation_direction == 0) {
          p.rotation_direction = 1;
        } else if (p.rotation_direction == -1) {
          p.queued_direction = 1;
        }
      }
    }

    public void controlRelease(Player p, int code, int shoot, int left, int boost, int right) {
      if (code == shoot) {
        p.is_shooting = false;
      } else if (code == boost) {
        p.is_boosting = false;
      } else if (code == left) {
        if (p.rotation_direction == -1) {
          p.rotation_direction = p.queued_direction;
        }
        p.queued_direction = 0;
      } else if (code == right) {
        if (p.rotation_direction == 1) {
          p.rotation_direction = p.queued_direction;
        }
        p.queued_direction = 0;
      }
    }
  }
}
