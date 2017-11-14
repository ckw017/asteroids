package asteroids;

public class Vector {
  public static Vector origin = new Vector();
  public static int vertical = 100000; //Arbitrarily large number for undefined slopes
  public double x, y;

  public Vector() {
    this.x = 0;
    this.y = 0;
  }

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector(double angle, double magnitude, boolean ang_constructor) {
    this.x = Math.cos(angle) * magnitude;
    this.y = Math.sin(angle) * magnitude;
  }

  public Vector(Vector v) {
    this.x = v.x;
    this.y = v.y;
  }

  public void add(Vector v) {
    this.x += v.x;
    this.y += v.y;
  }

  public double distance(Vector v) {
    return Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2));
  }

  public double magnitude() {
    return this.distance(origin);
  }

  public double angle() {
    if (x != 0) {
      return Math.atan2(this.y, this.x);
    }
    return vertical * this.y / Math.abs(this.y);
  }

  public void setMagnitude(double magnitude) {
    double angle = this.angle();
    this.x = Math.cos(angle) * magnitude;
    this.y = Math.sin(angle) * magnitude;
  }

  public Vector scale(double scalar) {
    this.x *= scalar;
    this.y *= scalar;
    return this;
  }
}
