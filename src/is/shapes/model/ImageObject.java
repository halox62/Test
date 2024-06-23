package is.shapes.model;

import is.memento.GraphicObjectFactory;
import is.memento.Memento;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import javax.swing.ImageIcon;

public final class ImageObject extends AbstractGraphicObject implements Serializable {
	private double factor = 1.0;

	private  Image image;

	private Point2D position;

	private int idImage;

	public ImageObject() {

	}

	public Image getImage() {
		return image;
	}

	public ImageObject(ImageIcon img, Point2D pos) {
		position = new Point2D.Double(pos.getX(), pos.getY());
		image = img.getImage();
	}



	@Override
	public boolean contains(Point2D p) {
		double w = (factor * image.getWidth(null)) / 2;
		double h = (factor * image.getHeight(null)) / 2;
		double dx = Math.abs(p.getX() - position.getX());
		double dy = Math.abs(p.getY() - position.getY());
		return dx <= w && dy <= h;
	}

	public void setPosition(Point2D point2D){
		this.position=point2D;
	}

	@Override
	public void moveTo(Point2D p) {
		position.setLocation(p);
		notifyListeners(new GraphicEvent(this));
	}

	@Override
	public void draw(Graphics2D g, boolean highlight) {
		int width = (int) (factor * image.getWidth(null));
		int height = (int) (factor * image.getHeight(null));
		int x = (int) (position.getX() - width / 2);
		int y = (int) (position.getY() - height / 2);

		g.drawImage(image, x, y, width, height, null);

		if (highlight) {
			g.setColor(Color.RED);
			g.drawRect(x, y, width, height);
		}
	}

	@Override
	public ImageObject clone() {
		ImageObject cloned = (ImageObject) super.clone();
		cloned.position = (Point2D) position.clone();
		return cloned;
	}

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(position.getX(), position.getY());
	}

	@Override
	public double getArea() {
		Dimension2D dim = getDimension();
		double width = dim.getWidth();
		double height = dim.getHeight();

		double area = width * height;

		return area;
	}

	@Override
	public double getPerimetro() {
		Dimension2D dim = getDimension();
		double width = dim.getWidth();
		double height = dim.getHeight();

		double perimetro = 2 * (width + height);

		return perimetro;
	}

	@Override
	public void scale(double factor) {
		if (factor <= 0)
			throw new IllegalArgumentException();
		this.factor *= factor;
		notifyListeners(new GraphicEvent(this));
	}

	@Override
	public Dimension2D getDimension() {
		Dimension dim = new Dimension();
		dim.setSize(factor * image.getWidth(null), factor * image.getHeight(null));
		return dim;
	}

	@Override
	public String getType() {
		return "Image";
	}

	public int getId() {
		return this.idImage;
	}

	@Override
	public void setId(int id) {
		this.idImage = id;
	}


	@Override
	public Memento createMemento() {
		return new ImageObjectMemento(position, factor);
	}

	@Override
	public void restore(Memento memento) {
		if (memento instanceof ImageObjectMemento) {
			ImageObjectMemento imgMemento = (ImageObjectMemento) memento;
			this.position = imgMemento.position;
			this.factor = imgMemento.factor;
		}
	}

	public void setImage(Image image) {
		this.image=image;
	}

	private static class ImageObjectMemento extends GraphicObjectFactory.TypeMemento {
		private final Point2D position;
		private final double factor;

		public ImageObjectMemento(Point2D position, double factor) {
			this.position = position;
			this.factor = factor;
		}

		@Override
		public String getType() {
			return "Image";
		}
	}
}

