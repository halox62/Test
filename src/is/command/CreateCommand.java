package is.command;
import is.shapes.model.*;
import is.shapes.view.CreateObjectAction;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;

public class CreateCommand implements Command {
    private final String type;
    private final String[] params;
    private final GraphicObjectPanel gpanel;
    private final List<GraphicObject> graphicList;
    private final DefaultComboBoxModel<Integer> dropdownModel;
    private final HistoryCommandHandler handler;
    private final Map<String, GraphicObject> prototypes;

    private GraphicObject newObject = null;

    public CreateCommand(String type, String[] params, GraphicObjectPanel gpanel, List<GraphicObject> graphicList,
                         DefaultComboBoxModel<Integer> dropdownModel, HistoryCommandHandler handler, Map<String, GraphicObject> prototypes) {
        this.type = type;
        this.params = params;
        this.gpanel = gpanel;
        this.graphicList = graphicList;
        this.dropdownModel = dropdownModel;
        this.handler = handler;
        this.prototypes = prototypes;
    }

    @Override
    public boolean execute() {
        GraphicObject prototype = prototypes.get(type.toLowerCase());
        if (prototype == null) {
            throw new IllegalArgumentException("Tipo non supportato: " + type);
        }

        switch (type.toLowerCase()) {
            case "circle":
                double radius = Double.parseDouble(params[0]);
                String[] positionTokens = params[1].split(",");
                double x = Double.parseDouble(positionTokens[0]);
                double y = Double.parseDouble(positionTokens[1]);
                if(radius<=0 || x<0 || y<0){
                    throw new IllegalArgumentException("Errore creazione tipo "+type+"Valori passati non corretti");
                }
                CircleObject circle = (CircleObject) prototype;
                newObject = circle.clone();
                ((CircleObject) newObject).setPosition(new Point((int) x, (int) y));
                ((CircleObject) newObject).setRadius(radius);
                break;

            case "image":
                String[] imagePathTokens = params[0].split("\"");
                String imagePath = imagePathTokens[1].trim();

                String positionString = params[1].trim();
                String[] positionTokensImage = positionString.split(",");
                double xImage = Double.parseDouble(positionTokensImage[0].trim());
                double yImage = Double.parseDouble(positionTokensImage[1].trim());
                if(xImage<0 || yImage<0){
                    throw new IllegalArgumentException("Errore creazione tipo "+type+"Valori passati non corretti");
                }
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    throw new IllegalArgumentException("Errore: Il file immagine non esiste.");
                }

                ImageObject imagePrototype = (ImageObject) prototype;
                newObject = imagePrototype.clone();
                ((ImageObject) newObject).setPosition(new Point2D.Double(xImage, yImage));
                ((ImageObject) newObject).setImage(new ImageIcon(imagePath).getImage());
                break;

            case "rectangle":
                double width = Double.parseDouble(params[0]);
                double height = Double.parseDouble(params[1]);
                positionTokens = params[2].split(",");
                x = Double.parseDouble(positionTokens[0]);
                y = Double.parseDouble(positionTokens[1]);
                if(width<=0 || height<=0 || x<0 || y<0){
                    throw new IllegalArgumentException("Errore creazione tipo "+type+"Valori passati non corretti");
                }
                RectangleObject rectangle = (RectangleObject) prototype;
                newObject = rectangle.clone();
                ((RectangleObject) newObject).setPosition(new Point2D.Double(x, y));
                ((RectangleObject) newObject).setDimensione(width, height);
                break;

            default:
                throw new IllegalArgumentException("Tipo non supportato: " + type);
        }

        if (newObject != null) {
            CreateObjectAction action = new CreateObjectAction((AbstractGraphicObject) newObject, gpanel, handler, dropdownModel, graphicList);
            action.actionPerformed(null);
        }
        return true;
    }

    @Override
    public boolean undoIt() {
        GraphicObject graphicObject=graphicList.get(graphicList.size()-1);
        if (graphicObject != null) {
            System.out.println(graphicObject.getId());
            graphicList.remove(graphicObject);
            dropdownModel.removeElement(graphicObject.getId());

            gpanel.remove(graphicObject);
            gpanel.repaint();

            return true;
        }

        return false;
    }
}
