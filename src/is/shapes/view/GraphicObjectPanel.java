package is.shapes.view;

import is.shapes.model.GraphicEvent;
import is.shapes.model.GraphicObject;
import is.shapes.model.GraphicObjectListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.*;

import javax.swing.JComponent;

public class GraphicObjectPanel extends JComponent implements GraphicObjectListener {

	private List<GraphicObject> graphicObjects=new ArrayList<>();
	private GraphicObject highlightedObject;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8993548105090978185L;

	/**
	 * @directed true
	 */

	private final List<GraphicObject> objects = new LinkedList<>();


	public GraphicObjectPanel() {
		this.graphicObjects = new ArrayList<>();
		setBackground(Color.WHITE);
	}

	@Override
	public void graphicChanged(GraphicEvent e) {
		repaint();
		revalidate();

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (graphicObjects != null) {
			for (GraphicObject go : graphicObjects) {
				go.draw(g2d, go.isHighlighted());
			}
		}
	}

	public void setGraphicObjects(List<GraphicObject> graphicObjects) {
		this.graphicObjects = graphicObjects;
	}

	public List<GraphicObject> getGraphicObjects() {
		return graphicObjects;
	}



	public GraphicObject getGraphicObjectAt(Point2D p) {
		for (GraphicObject g : objects) {
			if (g.contains(p))
				return g;
		}
		return null;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension ps = super.getPreferredSize();
		double x = ps.getWidth();
		double y = ps.getHeight();
		for (GraphicObject go : objects) {
			double nx = go.getPosition().getX() + go.getDimension().getWidth() / 2;
			double ny = go.getPosition().getY() + go.getDimension().getHeight() / 2;
			if (nx > x)
				x = nx;
			if (ny > y)
				y = ny;
		}
		return new Dimension((int) x, (int) y);
	}

	public void add(GraphicObject go) {
		objects.add(go);
		go.addGraphicObjectListener(this);
		repaint();
	}

	public void remove(GraphicObject go) {
		if (objects.remove(go)) {
			repaint();
			go.removeGraphicObjectListener(this);
		}

	}


	public List<Object> getGroups() {
		List<Object> res=new ArrayList<>();
		for(GraphicObject graphicObject:objects){
			if(graphicObject.getType().equals("Group")){
				res.add(graphicObject);
			}
		}
		return res;
	}
}
