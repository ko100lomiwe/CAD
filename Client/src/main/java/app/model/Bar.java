package app.model;

import app.Controller;
import app.utils.CoordinateUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.UUID;

/**
 * Created by podsh on 08.04.2017.
 */
public class Bar {
    private String id;
    private Point firstPoint;
    private Point secondPoint;
    private Material material;
    private Cut cut;
    private boolean isSelected;

    public Bar(Point first, Point second) {
        this.id = UUID.randomUUID().toString();
        this.firstPoint = first;
        this.secondPoint = second;
        isSelected = false;
    }

    public Bar() {
    }

    public void draw(Controller controller) {
        CoordinateUtils utils = controller.getCoordinateUtils();
        GraphicsContext gc = controller.getCanvas().getGraphicsContext2D();
        if (isSelected) {
            gc.setStroke(Color.RED);
            draw(gc, utils);
        } else {
            if (controller.getMaterialView().isSelected()) {
                drawWithColors(gc, utils);
            } else {
                drawWithoutColors(gc, utils);
            }
        }
        if (controller.getCutView().isSelected() && cut != null) {
            drawCutName(gc, utils);
        }
    }

    private void drawCutName(GraphicsContext gc, CoordinateUtils utils) {
        double x1 = utils.fromRealX(firstPoint.getX());
        double y1 = utils.fromRealY(firstPoint.getY());
        double x2 = utils.fromRealX(secondPoint.getX());
        double y2 = utils.fromRealY(secondPoint.getY());
        String shortName = cut.getShortName();
        float width = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(shortName, gc.getFont());
        double xx = x1 + (x2 - x1) / 2;
        double yy = y1 + (y2 - y1) / 2;
        gc.setFill(Color.BLACK);
        gc.fillRect(xx, yy, width + 8, 28);
        gc.setFill(Color.WHITE);
        gc.fillRect(xx + 1, yy + 1, width + 6, 26);
        gc.setStroke(Color.BLACK);
        gc.strokeText(shortName, xx + 4, yy + 18);
        gc.setFill(Color.BLACK);
    }

    private void drawWithoutColors(GraphicsContext gc, CoordinateUtils utils) {
        gc.setStroke(Color.BLACK);
        draw(gc, utils);
    }

    private void draw(GraphicsContext gc, CoordinateUtils utils) {
        double x1 = utils.fromRealX(firstPoint.getX());
        double y1 = utils.fromRealY(firstPoint.getY());
        double x2 = utils.fromRealX(secondPoint.getX());
        double y2 = utils.fromRealY(secondPoint.getY());
        gc.strokeLine(x1, y1, x2, y2);
    }

    private void drawWithColors(GraphicsContext gc, CoordinateUtils utils) {
        if (material == null) {
            gc.setStroke(Color.BLACK);
        } else {
            gc.setStroke(material.getColor());
        }
        draw(gc, utils);
    }

    public boolean between(double x1, double y1, double x2, double y2) {
        return firstPoint.between(x1, y1, x2, y2) && secondPoint.between(x1, y1, x2, y2);
    }

    public boolean equals(Bar obj) {
        return ((obj.secondPoint == secondPoint) && (obj.firstPoint == firstPoint)) ||
                ((obj.secondPoint == firstPoint) && (obj.firstPoint == secondPoint));
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
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

    public boolean isSelected() {
        return isSelected;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Cut getCut() {
        return cut;
    }

    public void setCut(Cut cut) {
        this.cut = cut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(firstPoint.getX() - secondPoint.getX(), 2) + Math.pow(firstPoint.getY() - secondPoint.getY(), 2));
    }
}
