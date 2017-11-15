package asteroids;

public class Asteroids {
  public Asteroids() {
  	Settings settings = new Settings();
    ObjectHandler objectHandler = new ObjectHandler(settings);
    GameFrame frame = new GameFrame(objectHandler);
    GameHandler gameHandler = new GameHandler(frame, objectHandler);
    gameHandler.run();
  }

  public static void main(String[] args) {
    new Asteroids();
  }
}
