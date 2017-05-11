package hills.services.files;

import java.awt.image.BufferedImage;

/**
 * Created by corne on 5/10/2017.
 */
public interface IPictureFileService {

    /**
     * Creates a png file from a buffered image.
     * @param bufferedImage - buffered image to read from.
     * @param name - name of the created png file.
     */
    public void writeImage(BufferedImage bufferedImage, String name);

    /**
     * Reads a png file.
     * @param name - name of the file to be read.
     */
    public void readImage(String name);
}