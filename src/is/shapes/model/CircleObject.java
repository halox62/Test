package is.shapes.model;

import is.memento.GraphicObjectFactory;
import is.memento.Memento;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public final  class CircleObject extends AbstractGraphicObject implements Serializable {

	private Point2D position;
	private boolean highlighted;

	private double radius;

	private int idCircle=0;


	public CircleObject(Point2D pos, double r) {
		if (r <= 0)
			throw new IllegalArgumentException();
		this.position = new Point2D.Double(pos.getX(), pos.getY());
		this.radius = r;
	}

	public CircleObject() {

	}


	public void setId(int id) {
		this.idCircle = id;
	}


	public int getId() {
		return idCircle;
	}

	@Override
	public void moveTo(Point2D p) {
		position.setLocation(p);
		notifyListeners(new GraphicEvent(this));
	}

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(position.getX(), position.getY());
	}

	@Override
	public double getArea() {
		return Math.PI*(radius*radius);
	}

	@Override
	public double getPerimetro() {
		return 2*Math.PI*radius;
	}

	@Override
	public void scale(double factor) {
		if (factor <= 0)
			throw new IllegalArgumentException();
		radius *= factor;
		notifyListeners(new GraphicEvent(this));
	}


	@Override
	public Dimension2D getDimension() {
		Dimension d = new Dimension();
		d.setSize(2 * radius, 2 * radius);

		return d;
	}

	@Override
	public boolean contains(Point2D p) {
		return (position.distance(p) <= radius);

	}

	@Override
	public boolean isHighlighted() {
		return highlighted;
	}

	@Override
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	@Override
	public void draw(Graphics2D g, boolean highlight) {
		if (highlight) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		g.drawOval((int) position.getX(), (int) position.getY(), (int) radius * 2, (int) radius * 2);
	}

	@Override
	public CircleObject clone() {
		CircleObject cloned = (CircleObject) super.clone();
		cloned.position = (Point2D) position.clone();
		return cloned;
	}

	@Override
	public String getType() {
		return "Circle";
	}

	public double getRadius() {
		return radius;
	}


	@Override
	public Memento createMemento() {
		return new CircleObjectMemento(position, radius);
	}

	@Override
	public void restore(Memento memento) {
		if (memento instanceof CircleObjectMemento) {
			CircleObjectMemento circleMemento = (CircleObjectMemento) memento;
			this.position = circleMemento.position;
			this.radius = circleMemento.radius;
		}
	}

	private static class CircleObjectMemento extends GraphicObjectFactory.TypeMemento{
		private final Point2D position;
		private final double radius;

		public CircleObjectMemento(Point2D position, double radius) {
			this.position = position;
			this.radius = radius;
		}

		@Override
		public String getType() {
			return "Circle";
		}
	}

}
