package asteroids;

public class GameHandler {
  public GameFrame frame;
  public ObjectHandler handler;

  public GameHandler(GameFrame frame, ObjectHandler handler) {
    this.frame = frame;
    this.handler = handler;
  }

  public void run() {
    while (true) {
      long startTime = System.currentTimeMillis();
      try {
        this.handler.update();
        this.frame.update();
      } catch (Exception e) {
        System.out.println("Error in GameThread");
        e.printStackTrace(System.out);
      }
      trySleep((int) (Settings.FRAME_DELAY + startTime - System.currentTimeMillis()));
    }
  }

  public void trySleep(int milliseconds) {
    try {
      if (milliseconds < 0) {
      	Settings.ACTUAL_FRAME_RATE = 1000 / (Settings.FRAME_DELAY - milliseconds);
        milliseconds = 0;
      } else {
      	Settings.ACTUAL_FRAME_RATE = Settings.FRAME_RATE;
      }
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
