package hills.services.files;

import java.awt.image.BufferedImage;

/**
 * Created by corne on 5/10/2017.
 */
public interface IPictureFileService {
    public void writeImage(BufferedImage bufferedImage, String path);

    public void readImage(String path);
}
