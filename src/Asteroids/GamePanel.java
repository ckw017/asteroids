package asteroids;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
  public ArrayList<GameObject> objects = new ArrayList<GameObject>();
  public static CollisionBucket[] buckets = new CollisionBucket[0];

  public GamePanel() {
    super();
    setSize(SaveUtility.SCREEN_WIDTH, SaveUtility.SCREEN_HEIGHT);
    setPreferredSize(new Dimension(SaveUtility.SCREEN_WIDTH, SaveUtility.SCREEN_HEIGHT));
  }

  public void addObject(GameObject o) {
    objects.add(o);
  }

  public void resetObjects() {
    objects = new ArrayList<GameObject>();
  }

  public void flush() {
    for (int i = objects.size() - 1; i >= 0; i--) {
      if (!objects.get(i).isAlive) {
        objects.remove(i);
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    try {
      for (CollisionBucket b : buckets) {
        b.paintComponent(g);
      }
      for (GameObject o : objects) {
        o.paintComponent(g);
      }
    } catch (ConcurrentModificationException e) {
      paintComponent(g);
    }
  }
}
