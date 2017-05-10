import hills.Unused_Usable_Code.ObjectPlacer;
import hills.services.ServiceLocator;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.util.math.Vec3;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Anders on 2017-05-10.
 */
public class ObjectPlacerTest {
    ObjectPlacer objectPlacer;
    IPictureFileService fileService;
    double radius = 1;
    @Before
    public void init(){
        objectPlacer = new ObjectPlacer(255,0.5,radius,0.3);
        fileService = ServiceLocator.INSTANCE.getFileService();
    }

    @Test
    public void testPlacement(){
        List<Vec3> objects = objectPlacer.placeObjects();
        fileService.writeImage(objectPlacer.getOBJECT_MAP(),"testPlacer");
        radius = objectPlacer.getRADIUS();
        for (int i = 0; i < objects.size(); i++) {
            Vec3 temp = objects.get(i);
            for (int j = 0; j <i ; j++) {
                Vec3 temp2 = objects.get(j);
                float tempX = temp.getX()-temp2.getX();
                float tempZ = temp.getZ()-temp2.getZ();
                float length = (tempX*tempX)+(tempZ*tempZ);
                Assert.assertTrue(length>(radius*radius));
            }
        }
    }
}
