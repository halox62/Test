package is.command;

import is.shapes.model.GraphicObject;

import java.awt.*;
import java.awt.geom.Point2D;

public class MoveCommand implements Command {
    private GraphicObject graphicObject;
    private Point2D oldPosition;
    private Point2D newPosition;

    public MoveCommand(GraphicObject graphicObject, Point2D newPosition) {
        this.graphicObject = graphicObject;
        this.oldPosition = (Point2D) graphicObject.getPosition().clone();
        this.newPosition = newPosition;
    }

    @Override
    public boolean execute() {
        graphicObject.moveTo(newPosition);
        return true;
    }

    @Override
    public boolean undoIt() {
        graphicObject.moveTo(oldPosition);
        return true;
    }

}
