package hills.controller.ModelInterfaceControllers;

import hills.controller.InputControllers.MouseListener;
import hills.model.IAttack;

/**
 * Created by gustav on 2017-05-11.
 */
public class AttackController implements MouseListener {
    IAttack player;
    public void setPlayer(IAttack player){
        this.player = player;
    }
    @Override
    public void mouseMoved(float dXMovement, float dYMovement) {

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
