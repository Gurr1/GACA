package hills.controller;

/**
 * Created by gustav on 2017-05-05.
 */
public interface KeyboardSubscribe {
    /**
     * The objects that wants to subscribe.
     * @param listener The object that will listen to KeyboardChanges
     */
    void subscribe(KeyboardListener listener);
}
