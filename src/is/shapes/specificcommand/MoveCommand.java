package is.shapes.specificcommand;

import is.command.Command;
import is.shapes.model.GraphicObject;

import java.awt.geom.Point2D;

public class MoveCommand implements Command {

	Point2D oldPos;

	Point2D newPos;

	GraphicObject object;
	
	public MoveCommand(GraphicObject go, Point2D pos) {
		oldPos = go.getPosition();
		newPos = pos;
		this.object = go;
		
		
	}

	@Override
	public boolean doIt() {

		object.moveTo(newPos);

		return true;
	}

	@Override
	public boolean undoIt() {
		object.moveTo(oldPos);
		
		return true;
	}

}
