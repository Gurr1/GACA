import hills.Unused_Usable_Code.ObjectPlacer;
import hills.services.ServiceLocator;
import hills.services.files.FileService;
import hills.services.files.IPictureFileService;
import hills.util.math.Vec3;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Anders on 2017-05-10.
 */
public class ObjectPlacerTest {
    ObjectPlacer objectPlacer;
    IPictureFileService fileService;
    @Before
    public void init(){objectPlacer = new ObjectPlacer();
   // fileService = ServiceLocator.INSTANCE.getFileService();
    }

    @Test
    public void testPlacement(){
        List<Vec3> objects = objectPlacer.placeObjects();
        fileService.writeImage(objectPlacer.getOBJECT_MAP(),"testPlacer");
    }
}
