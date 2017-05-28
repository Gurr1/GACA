package hills.services.files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by corne on 5/10/2017.
 */
public class FileService implements IPictureFileService{

    @Override
    public void writeImage(BufferedImage bufferedImage, String name){
        try {
            ImageIO.write(bufferedImage, "png", new File("Main/src/main/resources/" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readImage(String name){
        try {
            ImageIO.read(new File("Main/src/main/resources/" + name + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
