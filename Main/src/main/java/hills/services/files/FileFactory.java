package hills.services.files;

/**
 * @author Cornelis T Sjöbeck
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
