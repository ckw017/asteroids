package Asteroids;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Tools{
    public static BufferedImage getImage(String path){
        BufferedImage rImage;
        try{rImage = ImageIO.read(Tools.class.getResource("/" + path));}
        catch(IOException ex){rImage = null;}
        return rImage;
    }
}
