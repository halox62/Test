package is.interprete;

import is.command.HistoryCommandHandler;
import is.command.UngroupCommand;
import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UngroupExpression implements Expression {
    private String groupId;

    private HistoryCommandHandler handler;
    private GraphicObjectPanel gpanel;

    private List<GraphicObject> graphicList = new ArrayList<>();

    private DefaultComboBoxModel<Integer> dropdownModel;

    private String[] tokens;

    public UngroupExpression(String[] tokens, HistoryCommandHandler handler, GraphicObjectPanel gpanel, List<GraphicObject> graphicList, DefaultComboBoxModel<Integer> dropdownModel) {
        this.tokens=tokens;
        this.handler=handler;
        this.gpanel=gpanel;
        this.graphicList=graphicList;
        this.dropdownModel=dropdownModel;
    }

    @Override
    public void interpret(Context context) {
        context.addOutput("Ungrouping group " + groupId);
       try {
           this.groupId = tokens[1];
           boolean found = graphicList.stream()
                   .anyMatch(obj -> obj.getId() == Integer.parseInt(groupId));
           if (!found) {
               throw new IdNotFoundExceptio();
           }

           UngroupCommand ungroupCommand = new UngroupCommand(Integer.parseInt(groupId), graphicList, gpanel, dropdownModel);
           handler.handle(ungroupCommand);
           gpanel.repaint();
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