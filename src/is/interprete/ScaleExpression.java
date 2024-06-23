package is.interprete;

import is.command.HistoryCommandHandler;
import is.command.ScaleCommand;
import is.shapes.model.GraphicObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ScaleExpression implements Expression {
    private String objId;
    private String factor;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private String[] tokens;

    public ScaleExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
    }

    @Override
    public void interpret(Context context) {
        try{
            this.objId = tokens[1];
            this.factor = tokens[2];
            boolean found = graphicList.stream()
                    .anyMatch(obj -> obj.getId() == Integer.parseInt(objId));
            if(!found){
                throw new IdNotFoundExceptio();
            }
            context.addOutput("Scaling object " + objId + " by factor " + factor);
            double scaleFactor = Double.parseDouble(factor);
            for(GraphicObject graphicObject:graphicList){
                if(graphicObject.getId()==Integer.parseInt(objId)){
                    ScaleCommand scaleCommand = new ScaleCommand(graphicObject, scaleFactor);
                    handler.handle(scaleCommand);
                    gpanel.repaint();
                }
            }


        }catch (Exception e){
            JOptionPane.showMessageDialog(
                    null,
                    "Errore input"+e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }
}