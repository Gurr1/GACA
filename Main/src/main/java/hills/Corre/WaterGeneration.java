package hills.Corre;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javafx.scene.input.KeyCode.F;

/**
 * Created by gustav on 2017-03-21.
 */
public class WaterGeneration {

    BufferedImage bufferedImage;

    public WaterGeneration() {
        try {
            //bufferedImage = ImageIO.read(this.getClass().getResource("/noiseG.png"));
            bufferedImage = ImageIO.read(new File("Main/src/main/resources/noiseGtest.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.bufferedImage = bufferedImage;   (sköter i main sen)



    }

    public void genRiver(){

        for(int i = 0 ; i < bufferedImage.getHeight() ; i++){
            for(int j = 0 ; j < bufferedImage.getWidth() ; j++){
                Point startPoint = genStartPoints(i, j);

                System.out.println(startPoint);

                if(startPoint != null) {

                    int x = (int) startPoint.getX();
                    int y = (int) startPoint.getY();
                    /*
                    Color Pelel = new Color(90, 0, 0);
                    bufferedImage.setRGB(x, y, (Pelel).getRGB());
                    System.out.println(bufferedImage.getRGB(x, y));
                    System.out.println(Pelel.getRGB());
                    */

                    bufferedImage.setRGB(x, y, (new Color(90, 89, 150)).getRGB());





                    if (!hasReachedSea(startPoint)) {
                        //bufferedImage.setRGB(x, y, (0xff00));
                    }
                }

            }
        }


        try {
            ImageIO.write(bufferedImage, "png", new File("Main/src/main/resources/startingPoints5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public Point genStartPoints(int i, int j){
        Point point = null;

        Color temp = new Color(bufferedImage.getRGB(i, j));

        //if(((bufferedImage.getRGB(i, j) >> 8) & 0xFF) >= 0x63)
        if(temp.getGreen() >= 0x63)
        return new Point(i, j);

        else return point;
    }

    public void drawRiver(Point point){
        Point lowestAmpPoint;
        //kolla alla runt
    }

    public boolean hasReachedSea(Point point){
        int x = point.x;
        int y = point.y;
        //kolla blå (fixa annan blå för hav)
        return false;
    }


}
