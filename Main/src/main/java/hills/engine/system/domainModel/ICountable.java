package hills.engine.system.domainModel;

/**
 * Created by Anders on 2017-04-03.
 */
public interface ICountable extends ICollectible {

    /**
     * gets the name of the object that can be collected
     * @return the name of the object
     */
    String getNameOfCollectible();
}
