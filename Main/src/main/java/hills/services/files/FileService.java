package hills.services.files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by corne on 5/10/2017.
 */
public class FileService implements IPictureFileService{

    public void writeImage(BufferedImage bufferedImage, String path){
        try {
            ImageIO.write(bufferedImage, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readImage(String path){
        try {
            ImageIO.read(new File(path));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
