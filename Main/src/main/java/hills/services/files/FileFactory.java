package hills.services.files;

/**
 * Created by corne on 5/24/2017.
 */
public class FileFactory {

    private FileFactory(){

    }

    private static FileService fileService = null;

    public static IPictureFileService getPictureFileServiceInstance(){
        if(fileService == null)
            fileService = new FileService();

        return fileService;
    }
}
