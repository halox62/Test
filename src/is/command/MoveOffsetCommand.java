package is.command;

import is.shapes.model.GraphicObject;

import java.awt.geom.Point2D;

public class MoveOffsetCommand implements Command{
    private GraphicObject graphicObject;
    private Point2D oldPosition;
    private String[] addPosition;

    public MoveOffsetCommand(GraphicObject graphicObject, String[] addPosition) {
        this.graphicObject = graphicObject;
        this.oldPosition = (Point2D) graphicObject.getPosition().clone();
        this.addPosition =addPosition;
    }

    @Override
    public boolean execute() {
        graphicObject.moveTo(oldPosition.getX()+Double.parseDouble(addPosition[0]),
                oldPosition.getY()+Double.parseDouble(addPosition[1]));
        return true;
    }

    @Override
    public boolean undoIt() {
        graphicObject.moveTo(oldPosition);
        return true;
    }
}
