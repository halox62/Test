package is.shapes.model;


import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGraphicObject implements GraphicObject, Cloneable {

	private boolean highlighted = false;

	private int id;

	private  List<GraphicObjectListener> listeners = new LinkedList<>();


	@Override
	public void addGraphicObjectListener(GraphicObjectListener l) {
		if (listeners.contains(l))
			return;
		listeners.add(l);
	}

	@Override
	public void removeGraphicObjectListener(GraphicObjectListener l) {
		listeners.remove(l);

	}

	protected void notifyListeners(GraphicEvent e) {

		for (GraphicObjectListener gol : listeners)

			gol.graphicChanged(e);

	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	// Metodo astratto per disegnare l'oggetto
	public abstract void draw(Graphics2D g, boolean highlight);



	@Override
	public GraphicObject clone() {
		try {
			AbstractGraphicObject go = (AbstractGraphicObject) super.clone();
			go.listeners = new LinkedList<>();
			return go;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

}
