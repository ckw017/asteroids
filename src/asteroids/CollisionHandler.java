package asteroids;

import java.awt.Graphics;
import java.util.ArrayList;

public class CollisionHandler {
  public ObjectHandler handler;
  public Bucket[][] bucket_table;

  public CollisionHandler(ObjectHandler handler) {
    this.handler = handler;
    this.bucket_table =
        new Bucket[(int) Math.ceil(Settings.SCREEN_HEIGHT / Settings.BUCKET_SIZE) + 1]
            [(int) Math.ceil(Settings.SCREEN_WIDTH / Settings.BUCKET_SIZE) + 1];
    for (int y = 0; y < bucket_table.length; y++) {
      for (int x = 0; x < bucket_table[0].length; x++) {
        bucket_table[y][x] = new Bucket(new Vector(x, y));
      }
    }
  }

  public void update() {
    updateList(handler.asteroids);
    updateList(handler.projectiles);
    updateList(handler.players);
    handleCollisions();
  }
  
  public void updateList(ArrayList<? extends GameObject> list) {
    for (int i = list.size(); i > 0; i--) {
      GameObject o = list.get(i - 1);
      Bucket b = findBucket(o);
      if (o.curr_bucket != b) {
        o.curr_bucket.remove(o);
        o.curr_bucket = b;
        b.add(o);
      }
    }
  }

  public Bucket findBucket(GameObject o) {
    int y = (int) Math.floor(o.position.y / Settings.BUCKET_SIZE);
    int x = (int) Math.floor(o.position.x / Settings.BUCKET_SIZE);
    y = (y > 0) ? y : 0;
    x = (x > 0) ? x : 0;
    y = (y < bucket_table.length - 1) ? y : bucket_table.length - 1;
    x = (x < bucket_table[0].length - 1) ? x : bucket_table[0].length - 1;
    return bucket_table[y][x];
  }

  public void reset() {
    for (Bucket[] bucket_list : bucket_table) {
      for (Bucket bucket: bucket_list) {
      	bucket.reset();
      }
    }
  }

  public void handleCollisions() {
    for (int y = 0; y < bucket_table.length - 1; y++) {
      for (int x = 0; x < bucket_table[0].length - 1; x++) {
        Bucket b = bucket_table[y][x];
        b.collide();
        b.collide(bucket_table[y + 1][x]);
        b.collide(bucket_table[y][x + 1]);
        b.collide(bucket_table[y + 1][x + 1]);
        bucket_table[y + 1][x].collide(bucket_table[y][x + 1]);
        b.updateColor();
        if(y == bucket_table.length - 2) {
        	bucket_table[y + 1][x].collide();
        	bucket_table[y + 1][x].updateColor();
        	if(x == bucket_table[0].length - 2) {
        		bucket_table[y + 1][x + 1].collide();
          	bucket_table[y + 1][x + 1].updateColor();
        	}
        }
      }
      bucket_table[y][bucket_table[0].length - 1].collide();
      bucket_table[y][bucket_table[0].length - 1].updateColor();
    }
  }

  public void paintBuckets(Graphics g) {
    for (Bucket[] bucket_list : bucket_table) {
      for (Bucket b : bucket_list) {
        b.paintComponent(g);
      }
    }
  }
}
