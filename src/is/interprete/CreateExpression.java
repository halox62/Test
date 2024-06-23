package is.interprete;

import is.TestGraphics2;
import is.command.CreateCommand;
import is.command.HistoryCommandHandler;
import is.shapes.model.*;
import is.shapes.view.CreateObjectAction;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class CreateExpression implements Expression {
    private String type;
    private String[] params;

    private static Map<String, GraphicObject> prototypes = null;

    private Point2D point2D;

    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();
    private List<Integer> intId = new ArrayList<>();

    private DefaultComboBoxModel<Integer> dropdownModel;

    private HistoryCommandHandler handler;

    private String[] tokens;




    public CreateExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList,DefaultComboBoxModel<Integer> dropdownModel,
        Map<String, GraphicObject> prototypes) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
        this.dropdownModel=dropdownModel;
        this.prototypes=prototypes;
    }

    @Override
    public void interpret(Context context) {
        try{
            this.type = tokens[1];
            this.params = new String[tokens.length - 2];
            System.arraycopy(tokens, 2, this.params, 0, tokens.length - 2);
            CreateCommand createCommand=new CreateCommand(type,params,gpanel,graphicList,dropdownModel,handler,prototypes);
            handler.handle(createCommand);
            gpanel.repaint();
        }catch(Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    "Errore input:\n"+e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}