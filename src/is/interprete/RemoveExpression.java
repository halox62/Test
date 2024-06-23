package is.interprete;

import is.command.DeleteCommand;
import is.command.HistoryCommandHandler;
import is.shapes.model.GraphicObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RemoveExpression implements Expression {
    private String objId;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private DefaultComboBoxModel<Integer> dropdownModel;

    private String[] tokens;

    public RemoveExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList, DefaultComboBoxModel<Integer> dropdownModel) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
        this.dropdownModel=dropdownModel;
    }

    @Override
    public void interpret(Context context) {
        context.addOutput("Removing object " + objId);
        try{
            this.objId = tokens[1];
            boolean found = graphicList.stream()
                    .anyMatch(obj -> obj.getId() == Integer.parseInt(objId));
            if(!found){
                throw new IdNotFoundExceptio();
            }
            for(GraphicObject graphicObject:graphicList){
                if(graphicObject.getId()==Integer.parseInt(objId)){
                    DeleteCommand deleteCommand = new DeleteCommand(graphicList, dropdownModel, gpanel, graphicObject);
                    handler.handle(deleteCommand);
                    gpanel.repaint();
                    break;
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
}
