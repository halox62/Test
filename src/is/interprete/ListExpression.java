package is.interprete;

import is.command.HistoryCommandHandler;
import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ListExpression implements Expression {
    private String option;
    private String objIdOrType;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private String[] tokens;

    public ListExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
    }

    @Override
    public void interpret(Context context) {
        try {
            this.option = tokens[1];
            this.objIdOrType = tokens[1];
            if (isNumeric(objIdOrType)) {
                System.out.println(objIdOrType);
                boolean found = graphicList.stream()
                        .anyMatch(obj -> obj.getId() == Integer.parseInt(objIdOrType));
                if (!found) {
                    throw new IdNotFoundExceptio();
                }
                for (GraphicObject graphicObject : graphicList) {
                    if (graphicObject.getId() == Integer.parseInt(objIdOrType)) {
                        if(graphicObject.getType().equals("Group")){
                            StringBuilder message = new StringBuilder();
                            GroupObject groupObject=(GroupObject)graphicObject;
                            List<GraphicObject> listObject=groupObject.getObjects();
                            for(GraphicObject graphicObject1:listObject){
                                message.append("Type: ").append(graphicObject1.getType()).append("\n");
                                message.append("ID: ").append(graphicObject1.getId()).append("\n");
                            }
                            JOptionPane.showMessageDialog(
                                    null,
                                    message.toString(),
                                    "Info",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                        JOptionPane.showMessageDialog(
                                null,
                                "Object= "+graphicObject.getType()+"\n"+
                                        "Id= "+graphicObject.getId()+"\n"+
                                        "Position= "+graphicObject.getPosition().getY()+","+graphicObject.getPosition().getY()+"\n"+
                                        "Dimension= "+graphicObject.getDimension().getWidth()+"x"+graphicObject.getDimension().getHeight(),
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }else {
                StringBuilder message;
                switch (option) {
                    case "all":
                        message = new StringBuilder();
                        for (GraphicObject graphicObject : graphicList) {
                            message.append("Type: ").append(graphicObject.getType()).append("\n");
                            message.append("ID: ").append(graphicObject.getId()).append("\n");
                        }
                        JOptionPane.showMessageDialog(
                                null,
                                message.toString(),
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        break;
                    case "group":
                        for (GraphicObject graphicObject : graphicList) {
                            if (graphicObject.getType()=="Group") {
                                GroupObject groupObject = (GroupObject) graphicObject;
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Gruppo con id= " + groupObject.getId() + "\n" +
                                                "Contiene id= " + groupObject.getIds(),
                                        "Info",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            }
                        }
                        break;
                    case "Circle":
                        message = new StringBuilder();
                        for (GraphicObject graphicObject : graphicList) {
                            if (graphicObject.getType().equals("Circle")) {
                                message.append("Type: ").append(graphicObject.getType()).append("\n");
                                message.append("ID: ").append(graphicObject.getId()).append("\n");
                            }
                        }
                        JOptionPane.showMessageDialog(
                                null,
                                message.toString(),
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        break;
                    case "Rectangle":
                        message = new StringBuilder();
                        for (GraphicObject graphicObject : graphicList) {
                            if (graphicObject.getType().equals("Rectangle")) {
                                message.append("Type: ").append(graphicObject.getType()).append("\n");
                                message.append("ID: ").append(graphicObject.getId()).append("\n");

                            }
                        }
                        JOptionPane.showMessageDialog(
                                null,
                                message.toString(),
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        break;

                    case "Image":
                        message = new StringBuilder();
                        for (GraphicObject graphicObject : graphicList) {
                            if (graphicObject.getType().equals("Image")) {
                                message.append("Type: ").append(graphicObject.getType()).append("\n");
                                message.append("ID: ").append(graphicObject.getId()).append("\n");
                            }
                        }
                        JOptionPane.showMessageDialog(
                                null,
                                message.toString(),
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        break;
                    default:
                        JOptionPane.showMessageDialog(
                                null,
                                "Errore nell'input",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );

                        break;
                }
            }
        }catch(IdNotFoundExceptio id){
            JOptionPane.showMessageDialog(
                    null,
                    "ID Non Esistente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }catch (Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    "Errore input\n"+e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }


}