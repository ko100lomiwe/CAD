package app.model;

import app.Controller;
import app.utils.CoordinateUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by podsh on 08.04.2017.
 */
public class Bar {
    private Point firstPoint;
    private Point secondPoint;
    private boolean isSelected;

    public Bar(Point first, Point second){
        this.firstPoint = first;
        this.secondPoint = second;
        isSelected = false;
    }

    public void draw(Controller controller) {
        CoordinateUtils utils = controller.getCoordinateUtils();
        GraphicsContext gc = controller.getCanvas().getGraphicsContext2D();
        if (isSelected) {
            gc.setStroke(Color.RED);
        }
        gc.strokeLine(utils.fromRealX(firstPoint.getX()), utils.fromRealY(firstPoint.getY()),
                utils.fromRealX(secondPoint.getX()), utils.fromRealY(secondPoint.getY()));
        gc.setStroke(Color.BLACK);
    }

    public boolean between(double x1, double y1, double x2, double y2) {
        return firstPoint.between(x1,y1,x2,y2) && secondPoint.between(x1,y1,x2,y2);
    }

    public boolean equals(Bar obj) {
        return ((obj.secondPoint == secondPoint) && (obj.firstPoint == firstPoint)) ||
                ((obj.secondPoint == firstPoint) && (obj.firstPoint == secondPoint));
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public Point getSecondPoint() {
        return secondPoint;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
