package is.interprete;

import is.command.GroupCommand;
import is.command.HistoryCommandHandler;
import is.shapes.model.GraphicObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GroupExpression implements Expression {
    private String[] objIds;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();
    private List<Integer> intId = new ArrayList<>();

    private DefaultComboBoxModel<Integer> dropdownModel;
    private int nextGroupId=1000;

    private String[] tokens;

    public GroupExpression(String[] tokens,HistoryCommandHandler handler,GraphicObjectPanel gpanel,List<GraphicObject> graphicList,DefaultComboBoxModel<Integer> dropdownModel) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
        this.dropdownModel=dropdownModel;
    }

    @Override
    public void interpret(Context context) {
        try{
            for(GraphicObject graphicObject:graphicList){
                if(graphicObject.getType()=="Group"){
                    nextGroupId++;
                }
            }
            this.objIds = tokens[1].split(",");
            if(objIds.length>1){
                for(String id:objIds){
                    intId.add(Integer.parseInt(id));
                    boolean found = graphicList.stream()
                            .anyMatch(obj -> obj.getId() == Integer.parseInt(id));
                    if(!found){
                        throw new IdNotFoundExceptio();
                    }

                }
                GroupCommand groupCommand = new GroupCommand(nextGroupId, intId, graphicList, gpanel, dropdownModel, handler);
                handler.handle(groupCommand);
                gpanel.repaint();
                JOptionPane.showMessageDialog(
                        null,
                        "Group created with ID: " + nextGroupId,
                        "Group Created",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }else{
                JOptionPane.showMessageDialog(
                        null,
                        "Non è possibile creare un gruppo con un solo elemento",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
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