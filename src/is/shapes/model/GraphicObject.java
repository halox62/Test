package is.shapes.model;

import is.memento.Memento;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface GraphicObject {

	void addGraphicObjectListener(GraphicObjectListener l);

	void removeGraphicObjectListener(GraphicObjectListener l);

	void moveTo(Point2D p);

	default void moveTo(double x, double y){
		moveTo(new Point2D.Double(x, y));
	}

	Point2D getPosition();

	double getArea();

	double getPerimetro();

	Dimension2D getDimension();

	void scale(double factor);


	boolean contains(Point2D p);

	String getType();

	int getId();

	void setId(int id);

	void draw(Graphics2D g, boolean highlight);

	boolean isHighlighted();

	void setHighlighted(boolean highlighted);

	Memento createMemento();

	void restore(Memento memento);

}
