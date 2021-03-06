package app.interfaces;

import javafx.scene.input.MouseEvent;


/**
 * Created by APodshivalov on 29.03.2017.
 */
public interface Controllable {
    void onMouseClickedOverCanvas(MouseEvent mouseEvent);
    void onMouseMoved(MouseEvent mouseEvent);
    void redraw();
    void disable();
    void enable();
}
