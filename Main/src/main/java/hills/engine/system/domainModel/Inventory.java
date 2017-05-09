package hills.engine.system.domainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corne on 5/3/2017.
 */
public class Inventory {

    private List<ICollectible> inventory = new ArrayList<ICollectible>();

    public void addCollectible(ICollectible collectible){
        inventory.add(collectible);
    }

    public ICollectible getCollectible(int i){
        return inventory.get(i);
    }

    public void emptyInventory(){
        for (int i = 0 ; i < inventory.size() ; i++){
            inventory.set(i, null);
        }
    }
}
