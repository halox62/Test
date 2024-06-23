package is.interprete;

import is.command.HistoryCommandHandler;
import is.shapes.model.GraphicObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AreaExpression implements Expression {
    private String option;
    private String objIdOrType;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private List<Integer> intId = new ArrayList<>();

    private String[] tokens;

    public AreaExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
    }

    @Override
    public void interpret(Context context) {
        try{
            this.option = tokens[1];
            this.objIdOrType = tokens[1];
            if ("all".equals(option)) {
                for(GraphicObject graphicObject:graphicList){
                    intId.add(graphicObject.getId());
                }
                double totalArea = 0;
                for (Integer id :intId) {
                    GraphicObject go = graphicList.stream()
                            .filter(obj -> obj.getId() == id)
                            .findFirst()
                            .orElse(null);
                    if (go != null) {
                        totalArea += go.getArea();
                    }
                }
                JOptionPane.showMessageDialog(
                        null,
                        "Total Area of selected objects: " + totalArea,
                        "Visualizza Area",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                if(objIdOrType.equals("Circle")){
                    int area=0;
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getType().equals("Circle")){
                            area+=graphicObject.getArea();
                        }
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Area of Circle: " + area,
                            "Visualizza Area",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                if(objIdOrType.equals("Rectangle")){
                    int area=0;
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getType().equals("Rectangle")){
                            area+=graphicObject.getArea();
                        }
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Area of Rectangle: " + area,
                            "Visualizza Area",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                }
                if(objIdOrType.equals("Image")){
                    int area=0;
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getType().equals("Image")){
                            area+=graphicObject.getArea();
                        }
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Area of Image: " + area,
                            "Visualizza Area",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                }

            }
            if(isNumeric(objIdOrType)){
            context.addOutput("Calculating area for " + option + " with " + objIdOrType);
            boolean found = graphicList.stream()
                    .anyMatch(obj -> obj.getId() == Integer.parseInt(objIdOrType));
            if(!found){
                throw new IdNotFoundExceptio();
            }
            for(GraphicObject graphicObject:graphicList){
                if(graphicObject.getId()==Integer.parseInt(objIdOrType)){
                    JOptionPane.showMessageDialog(
                            null,
                            "Area of selected object: " + graphicObject.getArea(),
                            "Visualizza Area",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            }
            }

        }catch (IdNotFoundExceptio id){
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
