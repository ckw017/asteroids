package asteroids;

public class AsteroidUpdate extends Update {
  private static final long serialVersionUID = -5704206039804728683L;
  int aid;
  double x;
  double y;
  boolean isAlive;
  double xv;
  double yv;
  double rotSpd;
  short s;

  AsteroidUpdate(
      int objID,
      double xPosition,
      double yPosition,
      double xVelocity,
      double yVelocity,
      double rotSpeed,
      short size) {
    super(objID, true);
    x = xPosition;
    y = yPosition;
    xv = xVelocity;
    yv = yVelocity;
    rotSpd = rotSpeed;
    s = size;
    isAlive = true;
  }

  AsteroidUpdate(int id) {
    super(id, false);
  }
}
