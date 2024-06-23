package is.interprete;

import is.TestGraphics2;
import is.command.HistoryCommandHandler;
import is.shapes.model.CircleObject;
import is.shapes.model.GraphicObject;
import is.shapes.model.ImageObject;
import is.shapes.model.RectangleObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandExpression implements Expression {
    private Expression command;
    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;
    private Context context=null;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private DefaultComboBoxModel<Integer> dropdownModel;

    private int id=0;

    private int nextGroupId=1000;

    private static final Map<String, GraphicObject> prototypes = new HashMap<>();

    static {
        prototypes.put("circle", new CircleObject(new Point(0,0),1));
        prototypes.put("image", new ImageObject(new ImageIcon(TestGraphics2.class.getResource("shapes/model/NyaNya.gif")),
                new Point(240, 187)));
        prototypes.put("rectangle", new RectangleObject(new Point(0,0),1,2));
    }


    public CommandExpression(String input,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList,DefaultComboBoxModel<Integer> dropdownModel) {
        context=new Context(input);
        String[] tokens = input.split(" ");
        switch (tokens[0].toLowerCase()) {
            case "new":
                command = new CreateExpression(tokens,handler,gpanel,graphicList,dropdownModel,prototypes);
                break;
            case "del":
                command = new RemoveExpression(tokens,handler,gpanel,graphicList,dropdownModel);
                break;
            case "move":
            case "moveoff":
                command = new MoveExpression(tokens,handler,gpanel,graphicList);
                break;
            case "scale":
                command = new ScaleExpression(tokens,handler,gpanel,graphicList);
                break;
            case "ls":
                command = new ListExpression(tokens,handler,gpanel,graphicList);
                break;
            case "grp":
                command = new GroupExpression(tokens,handler,gpanel,graphicList,dropdownModel);
                nextGroupId++;
                break;
            case "ungrp":
                command = new UngroupExpression(tokens,handler,gpanel,graphicList,dropdownModel);
                break;
            case "area":
                command = new AreaExpression(tokens,handler,gpanel,graphicList);
                break;
            case "perimeter":
                command = new PerimeterExpression(tokens,handler,gpanel,graphicList);
                break;
            default:
                command = new UnknownExpression(tokens);
                break;
        }
    }

    @Override
    public void interpret(Context context) {
        if (command != null) {
            command.interpret(context);
        }
    }
}