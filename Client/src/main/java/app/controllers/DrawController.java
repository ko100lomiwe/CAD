package app.controllers;

import app.Controller;
import app.interfaces.Controllable;
import app.model.Bar;
import app.model.Model;
import app.model.Point;
import app.utils.CoordinateUtils;
import javafx.scene.input.MouseEvent;


/**
 * Обработчик событий при режиме "Рисование"
 * Created by APodshivalov on 29.03.2017.
 */
public class DrawController implements Controllable {
    private Controller controller;
    private CoordinateUtils utils;
    private Model model;
    private Point firstPoint;

    public DrawController(Controller controller) {
        this.controller = controller;
        utils = controller.getCoordinateUtils();
        firstPoint = null;
        model = controller.getModel();
    }

    /**
     * Обработка клика мышки по канвасу
     *
     * @param mouseEvent Эвент клика
     */
    public void onMouseClickedOverCanvas(MouseEvent mouseEvent) {
        if (firstPoint == null) {
            firstPoint = new Point(utils.toRealX(mouseEvent.getX()), utils.toRealY(mouseEvent.getY()));
        } else {
            Point newPoint = new Point(utils.toRealX(mouseEvent.getX()), utils.toRealY(mouseEvent.getY()));
            model.addBar(new Bar(firstPoint, newPoint));
            firstPoint = newPoint;
            controller.getCanvas().redraw();
        }
    }

    @Override
    public void onMouseMoved(MouseEvent mouseEvent) {
        if (firstPoint != null) {
            controller.getCanvas().redraw();
            controller.getCanvas().getGraphicsContext2D().strokeLine(
                    utils.fromRealX(firstPoint.getX()), utils.fromRealY(firstPoint.getY()),
                    mouseEvent.getX(), mouseEvent.getY());
        }
    }

    @Override
    public void disable() {
        controller.getCanvas().redraw();
        controller.getPivot().setVisible(false);
        controller.getOrto().setVisible(false);
        controller.getNet().setVisible(false);
    }

    @Override
    public void enable() {
        controller.getPivot().setVisible(true);
        controller.getOrto().setVisible(true);
        controller.getNet().setVisible(true);
    }
}
