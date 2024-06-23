package is.interprete;

import is.command.HistoryCommandHandler;
import is.shapes.model.GraphicObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PerimeterExpression implements Expression {
    private String option;
    private String objIdOrType;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private List<Integer> intId=new ArrayList<>();

    private String[] tokens;

    public PerimeterExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList) {
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
                double totalPerimetro = 0;
                for (Integer id :intId) {
                    GraphicObject go = graphicList.stream()
                            .filter(obj -> obj.getId() == id)
                            .findFirst()
                            .orElse(null);
                    if (go != null) {
                        totalPerimetro += go.getArea();
                    }
                }
                JOptionPane.showMessageDialog(
                        null,
                        "Total Perimetro of selected objects: " + totalPerimetro,
                        "Visualizza Area",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                context.addOutput("Calculating area for " + option + " with " + objIdOrType);
                if(isNumeric(objIdOrType)){
                    boolean found = graphicList.stream()
                            .anyMatch(obj -> obj.getId() == Integer.parseInt(objIdOrType));
                    if(!found){
                        throw new IdNotFoundExceptio();
                    }
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getId()==Integer.parseInt(objIdOrType)){
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Perimetro of selected object: " + graphicObject.getPerimetro(),
                                    "Visualizza Perimetro",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }

                    }
                }
                if(objIdOrType.equals("Circle")){
                    int perimetro=0;
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getType().equals("Circle")){
                            perimetro+=graphicObject.getPerimetro();
                        }
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Perimetro of Circle: " + perimetro,
                            "Visualizza perimetro",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                if(objIdOrType.equals("Rectangle")){
                    int perimetro=0;
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getType().equals("Rectangle")){
                            perimetro+=graphicObject.getPerimetro();
                        }
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Perimetro of Rectangle: " + perimetro,
                            "Visualizza perimetro",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                }
                if(objIdOrType.equals("Image")){
                    int perimetro=0;
                    for(GraphicObject graphicObject:graphicList){
                        if(graphicObject.getType().equals("Image")){
                            perimetro+=graphicObject.getPerimetro();
                        }
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Perimetro of Image: " + perimetro,
                            "Visualizza perimetro",
                            JOptionPane.INFORMATION_MESSAGE
                    );

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
