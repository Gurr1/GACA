package hills.controller.ModelInterfaceControllers;

import hills.controller.InputControllers.InputMediator;
import hills.controller.InputControllers.MouseButtonListener;
import hills.model.IAttack;

/**
 * Created by gustav on 2017-05-11.
 */
public class AttackController implements MouseButtonListener {
    IAttack player;
    public AttackController(){
        InputMediator.INSTANCE.subscribeToMouseButton(this);
    }
    public void setPlayer(IAttack player){
        this.player = player;
    }

    @Override
    public void mousePressed(int button, int mods) {
        player.setAttacking(true);
    }

    @Override
    public void mouseReleased(int button, int mods) {
        player.setAttacking(false);
    }
}
