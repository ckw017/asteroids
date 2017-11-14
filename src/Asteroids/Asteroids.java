package asteroids;

public class Asteroids {
  public Asteroids() {
    ObjectHandler objectHandler = new ObjectHandler();
    GameFrame frame = new GameFrame(objectHandler);
    GameHandler gameHandler = new GameHandler(frame, objectHandler);
    gameHandler.run();
  }

  public static void main(String[] args) {
    new Asteroids();
  }
}
