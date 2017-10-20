package asteroids;

public class ProjectileUpdate extends Update {
  /** */
  private static final long serialVersionUID = -6176201687736933859L;

  short pid;
  int projID;
  double x;
  double y;
  double xv;
  double yv;
  double rot;

  public ProjectileUpdate(
      int proID,
      short playerID,
      double xPosition,
      double yPosition,
      double xVelocity,
      double yVelocity,
      double rotation) {
    super(-1, true);
    projID = proID;
    pid = playerID;
    x = xPosition;
    y = yPosition;
    xv = xVelocity;
    yv = yVelocity;
    rot = rotation;
  }

  public ProjectileUpdate(int proID, short playerID) {
    super(-1, false);
    projID = proID;
    pid = playerID;
  }
}
