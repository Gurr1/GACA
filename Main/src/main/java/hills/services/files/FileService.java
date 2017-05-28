package hills.services.files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Cornelis T Sjöbeck
 */
public class FileService implements IPictureFileService{

    final String path = getClass().getResource("").getPath();

    protected FileService(){

    }

    @Override
    public void writeImage(BufferedImage bufferedImage, String name){
        try {
            ImageIO.write(bufferedImage, "png", new File("src/main/resources/" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readImage(String name){
        try {
            ImageIO.read(new File("src/main/resources/" + name + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
