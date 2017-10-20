package asteroids;

import java.io.Serializable;

public class Update implements Serializable {
  private static final long serialVersionUID = -2101438759568231019L;
  public int objectID;
  public boolean isAlive;

  Update(int id, boolean iA) {
    objectID = id;
    isAlive = iA;
  }
}
