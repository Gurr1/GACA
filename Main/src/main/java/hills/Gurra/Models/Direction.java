package hills.Gurra.Models;

/**
 * Created by gustav on 2017-04-04.
 */
public enum Direction {

        FORWARD(1.0f), BACKWARD(-1.0f), LEFT(-1.0f), RIGHT(1.0f), NONE(0.0f);

        private final float multiplier;
        private Direction(float multiplier){
            this.multiplier = multiplier;
    }
}
