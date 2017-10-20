package asteroids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

  private GamePanel gamePanel = new GamePanel();
  public static String WINDOW_TITLE = "Asteroids";
  public static final int WINDOW_WIDTH = SaveUtility.SCREEN_WIDTH;
  public static final int WINDOW_HEIGHT = SaveUtility.SCREEN_HEIGHT;
  public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
  private Player player;
  private Player player2;
  private boolean multiplayer;

  public GameFrame() {
    super();
    setLayout(new FlowLayout());
    add(gamePanel);
    setTitle(WINDOW_TITLE);
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    if (SaveUtility.FULL_SCREEN) {
      setExtendedState(JFrame.MAXIMIZED_BOTH);
      setSize(
          (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
          (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
      setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    } else {
      int x = (int) ((SCREEN_SIZE.getWidth() - getWidth()) / 2);
      int y = (int) ((SCREEN_SIZE.getHeight() - getHeight()) / 2);
      setLocation(x, y);
    }
    setIcon();
    setVisible(true);
    setResizable(false);
    getContentPane().setBackground(Color.black);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(new InputListener());
  }

  public GameFrame(boolean invis) {
    super();
    setVisible(false);
  }

  public GamePanel getGamePanel() {
    return gamePanel;
  }

  public void setIcon() {
    try {
      setIconImage(ImageIO.read(new File("resources/icon2.png")));
    } catch (IOException exc) {
      exc.printStackTrace();
    }
  }

  public void setPlayer(Player p) {
    player = p;
  }

  public void setPlayerTwo(Player p) {
    player2 = p;
    multiplayer = SaveUtility.LOCAL_MULTIPLAYER;
    if (multiplayer && SaveUtility.LAN_MULTIPLAYER) {
      System.out.println("NOTE: LAN AND LOCAL MULTIPLAYER ARE BOTH ON");
    }
  }

  public void addObject(GameObject o) {
    gamePanel.addObject(o);
  }

  public static GameFrame createAndShowGUI() {
    return new GameFrame();
  }

  class InputListener implements KeyListener {
    private char LEFT = 'a'; //The character binded to turning left
    private char RIGHT = 'd'; //The character binded to turning right
    private char MOVE = 's'; //The character binded to applying force
    private char STATIC = 's';
    private char rotationDirection = 's';
    private char secondaryDirection = STATIC; //The secondary direction of turning
    private char rotationDirection2 = 's';
    private char secondaryDirection2 = STATIC; //The secondary direction of turning

    public void keyPressed(KeyEvent e) {
      char charPressed = e.getKeyChar();
      if (charPressed == 'r') {
        //Asteroids.reset();
      }
      if (rotationDirection == STATIC) {
        if (charPressed == LEFT) {
          rotationDirection = LEFT;
          player.rotDir = -1;
        }
        if (charPressed == RIGHT) {
          rotationDirection = RIGHT;
          player.rotDir = 1;
        }
      } else {
        if (charPressed == LEFT && rotationDirection != LEFT) {
          secondaryDirection = LEFT;
        }
        if (charPressed == RIGHT && rotationDirection != RIGHT) {
          secondaryDirection = RIGHT;
        }
      }
      if (charPressed == MOVE && !player.isMoving) {
        player.isMoving = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_ENTER
          || e.getKeyCode() == KeyEvent.VK_SPACE
          || charPressed == 'w') {
        if (player != null) {
          if (!player.isShooting && player.isAlive) {
            player.isShooting = true;
          }
        }
      }
      if (multiplayer) {
        if (rotationDirection2 == STATIC) {
          if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rotationDirection2 = KeyEvent.VK_LEFT;
            player2.rotDir = -1;
          }
          if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rotationDirection2 = KeyEvent.VK_RIGHT;
            player2.rotDir = 1;
          }
        } else {
          if (e.getKeyCode() == KeyEvent.VK_LEFT && rotationDirection2 != KeyEvent.VK_LEFT) {
            secondaryDirection2 = KeyEvent.VK_LEFT;
          }
          if (e.getKeyCode() == KeyEvent.VK_RIGHT && rotationDirection2 != KeyEvent.VK_RIGHT) {
            secondaryDirection2 = KeyEvent.VK_RIGHT;
          }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && !player2.isMoving) {
          player2.isMoving = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          if (player2 != null) {
            if (!player2.isShooting && player2.isAlive) {
              player2.isShooting = true;
            }
          }
        }
      }
    }

    public void keyReleased(KeyEvent e) {
      char charReleased = e.getKeyChar();
      if (rotationDirection != STATIC) {
        if ((charReleased == LEFT) && rotationDirection == LEFT) {
          rotationDirection = secondaryDirection;
          player.rotDir = translateDirection(secondaryDirection);
          secondaryDirection = STATIC;
        } else if ((charReleased == RIGHT) && rotationDirection == RIGHT) {
          rotationDirection = secondaryDirection;
          player.rotDir = translateDirection(secondaryDirection);
          secondaryDirection = STATIC;
        } else {
          secondaryDirection = STATIC;
        }
      }
      if (charReleased == MOVE && player.isMoving) {
        player.isMoving = false;
      }

      if (e.getKeyCode() == KeyEvent.VK_ENTER
          || e.getKeyCode() == KeyEvent.VK_SPACE
          || charReleased == 'w') {
        player.isShooting = false;
      }
      if (multiplayer) {
        if (rotationDirection2 != STATIC) {
          if (e.getKeyCode() == KeyEvent.VK_LEFT && rotationDirection2 == KeyEvent.VK_LEFT) {
            rotationDirection2 = secondaryDirection2;
            player2.rotDir = translateDirection(secondaryDirection2);
            secondaryDirection2 = STATIC;
          } else if (e.getKeyCode() == KeyEvent.VK_RIGHT
              && rotationDirection2 == KeyEvent.VK_RIGHT) {
            rotationDirection2 = secondaryDirection2;
            player2.rotDir = translateDirection(secondaryDirection2);
            secondaryDirection2 = STATIC;
          } else {
            secondaryDirection2 = STATIC;
          }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && player2.isMoving) {
          player2.isMoving = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
          player2.isShooting = false;
        }
      }
    }

    public void keyTyped(KeyEvent e) {}

    public short translateDirection(char c) {
      if (c == 'd' || c == KeyEvent.VK_RIGHT) {
        return 1;
      } else if (c == 'a' || c == KeyEvent.VK_LEFT) {
        return -1;
      } else {
        return 0;
      }
    }

    public void reset() {
      STATIC = 's';
      rotationDirection = 's';
      secondaryDirection = STATIC; //The secondary direction of turning
      rotationDirection2 = 's';
      secondaryDirection2 = STATIC; //The secondary direction of turning
    }
  }

  public static Insets getFrameInsets() {
    GameFrame g = new GameFrame(true);
    g.pack();
    return g.getInsets();
  }

  public void resetKeyListener() {
    for (KeyListener k : getKeyListeners()) {
      if (k instanceof InputListener) {
        ((InputListener) k).reset();
        break;
      }
    }
  }

  public void reset() {
    resetKeyListener();
    gamePanel.resetObjects();
  }
}
