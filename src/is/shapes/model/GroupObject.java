package is.shapes.model;

import is.command.CommandHandler;
import is.command.MoveCommand;
import is.memento.GraphicObjectFactory;
import is.memento.Memento;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GroupObject extends AbstractGraphicObject {
    private List<GraphicObject> objects;
    private int idGroup;

    private double area;
    private double perimetro;

    private Point2D position;

    private CommandHandler cmdHandler;

    public GroupObject(CommandHandler cmdHandler) {
        this.cmdHandler = cmdHandler;
        this.objects = new ArrayList<>();
        this.position = new Point2D.Double(0, 0);
    }

    public GroupObject() {

    }
    public void add(GraphicObject go) {
        objects.add(go);
    }

    public void remove(GraphicObject go) {
        objects.remove(go);
    }

    public List<GraphicObject> getObjects() {
        return objects;
    }

    @Override
    public boolean contains(Point2D p) {
        for (GraphicObject go : objects) {
            if (go.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsObject(GraphicObject graphicObject){
        return objects.contains(graphicObject);
    }

    @Override
    public void moveTo(Point2D newPosition) {
        double deltaX = newPosition.getX() - position.getX();
        double deltaY = newPosition.getY() - position.getY();
        for (GraphicObject go : objects) {
            Point2D originalPos = go.getPosition();
            go.moveTo(new Point2D.Double(originalPos.getX() + deltaX, originalPos.getY() + deltaY));
        }
        position.setLocation(newPosition);
        notifyListeners(new GraphicEvent(this));
    }



    @Override
    public void scale(double factor) {
        for (GraphicObject go : objects) {
            go.scale(factor);
        }
        notifyListeners(new GraphicEvent(this));
    }

    @Override
    public Dimension2D getDimension() {
        double maxWidth = 0;
        double maxHeight = 0;
        for (GraphicObject go : objects) {
            Dimension2D dim = go.getDimension();
            if (dim.getWidth() > maxWidth) {
                maxWidth = dim.getWidth();
            }
            if (dim.getHeight() > maxHeight) {
                maxHeight = dim.getHeight();
            }
        }
        return new Dimension((int) maxWidth, (int) maxHeight);
    }

    @Override
    public void draw(Graphics2D g, boolean highlight) {
        for (GraphicObject go : objects) {
            go.draw(g, highlight);
        }
    }

    public ArrayList<Point2D> getPositionArray() {
        ArrayList<Point2D> positionArray=new ArrayList<>();
        if (objects.isEmpty()) {
            positionArray.add(new Point2D.Double(0, 0));
            return positionArray;
        }
        for(GraphicObject graphicObject:objects){
            positionArray.add(graphicObject.getPosition());
        }
       return positionArray;
    }


    @Override
    public Point2D getPosition(){
        return position;
    }


    @Override
    public double getArea() {
       for(GraphicObject graphicObject:objects){
           this.area+=graphicObject.getArea();
       }
       return area;
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    @Override
    public double getPerimetro() {
        for(GraphicObject graphicObject:objects){
            this.perimetro+=graphicObject.getPerimetro();
        }
        return perimetro;
    }

    @Override
    public String getType() {
        return "Group";
    }

    public int getId() {
        return idGroup;
    }

    public String getIds() {
        String res="";
        for (GraphicObject go : objects) {
           res+=go.getId();
           res+=" ";
        }
        return res;
    }

    @Override
    public void setId(int id) {
        this.idGroup = id;
    }

    @Override
    public Memento createMemento() {
        List<Memento> objectMementos = new ArrayList<>();
        for (GraphicObject go : objects) {
            objectMementos.add(go.createMemento());
        }
        return new GroupObjectMemento(objectMementos);
    }

    @Override
    public void restore(Memento memento) {
        if (memento instanceof GroupObjectMemento) {
            GroupObjectMemento groupMemento = (GroupObjectMemento) memento;
            List<Memento> objectMementos = groupMemento.objectMementos;
            objects.clear();
            for (Memento objMemento : objectMementos) {
                GraphicObject go = GraphicObjectFactory.createGraphicObject(objMemento);
                go.restore(objMemento);
                objects.add(go);
            }
        }
    }


    private static class GroupObjectMemento implements Memento {
        private final List<Memento> objectMementos;

        public GroupObjectMemento(List<Memento> objectMementos) {
            this.objectMementos = objectMementos;
        }
    }
}
