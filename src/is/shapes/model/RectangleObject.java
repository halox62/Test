package is.shapes.model;

import is.memento.GraphicObjectFactory;
import is.memento.Memento;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public final class RectangleObject extends AbstractGraphicObject implements Serializable {

	private Point2D position;
	private Dimension2D dim;
	private int idRectangle;
	private boolean highlighted;

	public RectangleObject(Point2D pos, double w, double h) {
		if (w <= 0 || h <= 0)
			throw new IllegalArgumentException();
		dim = new Dimension();
		dim.setSize(w, h);
		position = new Point2D.Double(pos.getX(), pos.getY());
	}

	public RectangleObject() {

	}

	public void setPosition(Point2D point2D){
		this.position=point2D;
	}

	public void setDimensione(double w,double h){
		this.dim = new Dimension();
		this.dim.setSize(w, h);
	}

	@Override
	public boolean contains(Point2D p) {
		double w = dim.getWidth() / 2;
		double h = dim.getHeight() / 2;
		double dx = Math.abs(p.getX() - position.getX());
		double dy = Math.abs(p.getY() - position.getY());
		return dx <= w && dy <= h;
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
		return this.dim.getHeight()*this.dim.getWidth();
	}

	@Override
	public double getPerimetro() {
		return (this.dim.getHeight()+this.dim.getWidth())*2;
	}

	@Override
	public void scale(double factor) {
		if (factor <= 0)
			throw new IllegalArgumentException();
		dim.setSize(dim.getWidth() * factor, dim.getHeight() * factor);
		notifyListeners(new GraphicEvent(this));
	}

	@Override
	public Dimension2D getDimension() {
		Dimension2D d = new Dimension();
		d.setSize(dim);
		return d;
	}

	@Override
	public void draw(Graphics2D g, boolean highlight) {
		double x = position.getX() - dim.getWidth() / 2;
		double y = position.getY() - dim.getHeight() / 2;
		double w = dim.getWidth();
		double h = dim.getHeight();
		if (highlight) {
			g.setColor(Color.RED); // Color for highlighting
			g.setStroke(new BasicStroke(3)); // Thicker stroke for highlighting
		} else {
			g.setColor(Color.BLACK); // Normal color
			g.setStroke(new BasicStroke(1)); // Normal stroke
		}
		g.drawRect((int) x, (int) y, (int) w, (int) h);
	}

	@Override
	public RectangleObject clone() {
		RectangleObject cloned = (RectangleObject) super.clone();
		cloned.position = (Point2D) position.clone();
		cloned.dim = (Dimension2D) dim.clone();
		return cloned;
	}

	@Override
	public String getType() {
		return "Rectangle";
	}

	@Override
	public int getId() {
		return this.idRectangle;
	}

	@Override
	public void setId(int id) {
		this.idRectangle = id;
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
	public Memento createMemento() {
		return new RectangleObjectMemento(position, dim);
	}

	@Override
	public void restore(Memento memento) {
		if (memento instanceof RectangleObjectMemento) {
			RectangleObjectMemento rectMemento = (RectangleObjectMemento) memento;
			this.position = rectMemento.position;
			this.dim=rectMemento.dim;
		}
	}

	private static class RectangleObjectMemento extends GraphicObjectFactory.TypeMemento {
		private final Point2D position;
		private Dimension2D dim;

		public RectangleObjectMemento(Point2D position, Dimension2D dim) {
			this.position = position;
			this.dim=dim;
		}

		@Override
		public String getType() {
			return "Rectangle";
		}
	}
}

