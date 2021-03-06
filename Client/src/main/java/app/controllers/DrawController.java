package app.controllers;

import app.Controller;
import app.interfaces.Controllable;
import app.model.Bar;
import app.model.Model;
import app.model.Point;
import app.utils.CoordinateUtils;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


/**
 * Обработчик событий при режиме "Рисование"
 * Created by APodshivalov on 29.03.2017.
 */
public class DrawController implements Controllable {

    private Controller controller;
    private GraphicsContext gc;
    private CoordinateUtils utils;
    private Model model;
    private Point firstPoint;

    DrawController(Controller controller) {
        this.controller = controller;
        gc = controller.getCanvas().getGraphicsContext2D();
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
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            drawNewElements();
        }
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            firstPoint = null;
            controller.getCanvas().redraw();
        }
    }

    private void drawNewElements() {
        Point pointAtThisCoordinate = model.findPoint(utils.toRealX(), utils.toRealY());
        if (firstPoint == null) {
            if (pointAtThisCoordinate == null) {
                firstPoint = new Point(utils.toRealX(), utils.toRealY());
                model.addPoint(firstPoint);
            } else {
                firstPoint = pointAtThisCoordinate;
            }
        } else {
            Point newPoint;
            if (pointAtThisCoordinate == null) {
                newPoint = new Point(utils.toRealX(), utils.toRealY());
                model.addPoint(newPoint);
            } else {
                newPoint = pointAtThisCoordinate;
            }
            model.addBar(new Bar(firstPoint, newPoint));
            firstPoint = newPoint;
            controller.getCanvas().redraw();
        }
    }

    @Override
    public void onMouseMoved(MouseEvent mouseEvent) {
        // Привязка курсора к точкам
        if (controller.getPivot().isSelected()) {
            Point p = model.findNearbyPoint(mouseEvent.getX(), mouseEvent.getY());
            if (p != null) {
                utils.setY(utils.fromRealY(p.getY()));
                utils.setX(utils.fromRealX(p.getX()));
            }
        }
        if (controller.getOrto().isSelected() && firstPoint != null) {
            drawOrto(mouseEvent);
        }
        if (controller.getNet().isSelected()) {
            drawNet();
        }
        // Перерисовка модели
        controller.getCanvas().redraw();
        if (firstPoint != null) {
            gc.strokeLine(utils.fromRealX(firstPoint.getX()), utils.fromRealY(firstPoint.getY()),
                    utils.getX(), utils.getY());
        }
        drawCursor();
    }

    @Override
    public void redraw() {
            if (controller.getNet().isSelected()) {
                this.drawDots();
            }
            this.drawCursor();
    }

    private void drawNet() {
        double x = utils.toRealX() % 100;
        double y = utils.toRealY() % 100;
        utils.setX(utils.fromRealX(utils.toRealX(utils.getX()) - x));
        utils.setY(utils.fromRealY(utils.toRealY(utils.getY()) - y));
    }

    private void drawDots() {
        gc.setStroke(Color.BLACK);
        double x = utils.toRealX(0);
        x = utils.fromRealX(x - x%100);
        while (x < controller.getCanvas().getWidth()){
            double y = utils.toRealY(0);
            y = utils.fromRealY(y - y%100);
            while (y < controller.getCanvas().getHeight()){
                controller.getCanvas().getGraphicsContext2D().strokeLine(x,y,x,y);
                y = utils.fromRealY(utils.toRealY(y) - 100);
            }
            x = utils.fromRealX(utils.toRealX(x) + 100);
        }
    }

    private void drawOrto(MouseEvent mouseEvent) {
        if (Math.abs(mouseEvent.getX() - utils.fromRealX(firstPoint.getX())) > Math.abs(mouseEvent.getY() - utils.fromRealY(firstPoint.getY()))) {
            utils.setY(utils.fromRealY(firstPoint.getY()));
        } else {
            utils.setX(utils.fromRealX(firstPoint.getX()));
        }
    }

    private void drawCursor() {
        double x = utils.getX();
        double y = utils.getY();
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x, y + 3, x, y + 8);
        gc.strokeLine(x + 3, y, x + 8, y);
        gc.strokeLine(x, y - 3, x, y - 8);
        gc.strokeLine(x - 3, y, x - 8, y);
    }

    @Override
    public void disable() {
        controller.getCanvas().redraw();
        controller.getPivot().setVisible(false);
        controller.getOrto().setVisible(false);
        controller.getNet().setVisible(false);
        controller.getCanvas().setCursor(Cursor.DEFAULT);
        firstPoint = null;
    }

    @Override
    public void enable() {
        controller.getPivot().setVisible(true);
        controller.getOrto().setVisible(true);
        controller.getNet().setVisible(true);
        controller.getCanvas().setCursor(Cursor.NONE);
    }
}
