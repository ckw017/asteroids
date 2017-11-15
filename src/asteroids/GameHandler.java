package asteroids;

public class GameHandler {
  public GameFrame frame;
  public ObjectHandler handler;
  public double update_rate;
  public double update_delay;
  
  public GameHandler(GameFrame frame, ObjectHandler handler) {
    this.frame = frame;
    this.handler = handler;
    this.update_rate = handler.settings.FRAME_RATE;
    this.update_delay = 1000/update_rate;
  }

  public void run() {
    while (true) {
      long startTime = System.currentTimeMillis();
      try {
        this.handler.update();
        this.frame.update();
        this.frame.panel.update_rate = this.update_rate;
      } catch (Exception e) {
        System.out.println("Error in GameThread");
        e.printStackTrace(System.out);
      }
      trySleep((int) (update_delay + startTime - System.currentTimeMillis()));
    }
  }

  public void trySleep(int milliseconds) {
    try {
      if (milliseconds < 0) {
      	update_rate = 1000 / (update_delay - milliseconds);
        milliseconds = 0;
      } else {
      	update_rate = handler.settings.FRAME_RATE;
      }
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
