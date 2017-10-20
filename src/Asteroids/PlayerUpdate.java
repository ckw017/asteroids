package asteroids;

public class PlayerUpdate extends Update {
  /** */
  private static final long serialVersionUID = -2563838679337969221L;

  public short pid;
  public double x;
  public double y;
  public double xv;
  public double yv;
  public double rotation;

  PlayerUpdate(
      int objectID,
      short playerID,
      double xPosition,
      double yPosition,
      double xVelocity,
      double yVelocity,
      double rot,
      boolean alive) {
    super(objectID, alive);
    pid = playerID;
    x = xPosition;
    y = yPosition;
    xv = xVelocity;
    yv = yVelocity;
    rotation = rot;
  }
}
