package is.command;

import is.shapes.model.GraphicObject;

import java.awt.geom.Dimension2D;

public class ScaleCommand implements Command{
    private GraphicObject graphicObject;
    private Double scale;

    private Dimension2D oldDimension;

    public ScaleCommand(GraphicObject graphicObject, Double scale){
        this.graphicObject=graphicObject;
        this.scale=scale;
        this.oldDimension=graphicObject.getDimension();

    }
    @Override
    public boolean execute() {
        graphicObject.scale(scale);
        return true;
    }

    @Override
    public boolean undoIt() {
        graphicObject.scale(1/scale);
        return true;
    }
}
