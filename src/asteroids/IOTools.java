package asteroids;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class IOTools {
  public static BufferedImage getImage(String path) {
    BufferedImage rImage;
    try {
      rImage = ImageIO.read(GameObject.class.getResource("/" + path));
    } catch (IOException ex) {
      rImage = null;
    }
    return rImage;
  }

  public static BufferedImage[] loadSprites(String path, int n) {
    BufferedImage[] sprites = new BufferedImage[n];
    for (int i = 0; i < n; i++) {
      sprites[i] = getImage(path + (i + 1) + ".png");
    }
    return sprites;
  }

  public static BufferedImage getImage(String path, double scalar) {
    BufferedImage img = getImage(path);
    int newW = (int) (img.getWidth() * scalar);
    int newH = (int) (img.getHeight() * scalar);
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();
    return dimg;
  }

  public static void exportResource(String resourceName) {
    InputStream stream = null;
    OutputStream resStreamOut = null;
    String jarFolder;
    try {
      stream = IOTools.class.getResourceAsStream(resourceName);
      if (stream == null) {
        System.out.println("Cannot get resource \"" + resourceName + "\" from Jar file.");
      }

      int readBytes;
      byte[] buffer = new byte[4096];
      jarFolder =
          new File(
                  IOTools.class
                      .getProtectionDomain()
                      .getCodeSource()
                      .getLocation()
                      .toURI()
                      .getPath())
              .getParentFile()
              .getPath()
              .replace('\\', '/');
      resStreamOut = new FileOutputStream(jarFolder + resourceName);
      while ((readBytes = stream.read(buffer)) > 0) {
        resStreamOut.write(buffer, 0, readBytes);
      }
      stream.close();
      resStreamOut.close();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}
