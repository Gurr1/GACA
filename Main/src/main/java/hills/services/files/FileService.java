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
            ImageIO.write(bufferedImage, "png", new File("Main/src/main/resources/textures/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage readImage(String name){
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(new File("Main/src/main/resources/textures/" + name));
        } catch (IOException e){
            e.printStackTrace();
        }
        return bf;
    }
}
