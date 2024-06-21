package is.command;

import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.List;

public class GroupCommand implements Command{

    private List<Integer> selectedIds;
    private int nextGroupId;
    private List<GraphicObject> graphicList;

    private DefaultComboBoxModel<Integer> dropdownModel;

    private GroupObject group;
    private GraphicObjectPanel gpanel;

    private final CommandHandler cmdHandler;

    public GroupCommand(int nextGroupId,List<Integer>selectedIds,List<GraphicObject>graphicList,GraphicObjectPanel gpanel,
                        DefaultComboBoxModel<Integer> dropdownModel,CommandHandler cmdHandler){
        this.nextGroupId=nextGroupId;
        this.selectedIds=selectedIds;
        this.graphicList=graphicList;
        this.gpanel=gpanel;
        this.dropdownModel=dropdownModel;
        this.cmdHandler=cmdHandler;
    }

    @Override
    public boolean execute() {
        group = new GroupObject(cmdHandler);
        group.setId(nextGroupId++);
        for (Integer id : selectedIds) {
            GraphicObject go = graphicList.stream()
                    .filter(g -> g.getId() == id)
                    .findFirst()
                    .orElse(null);
            if (go != null) {
                group.add(go);
            }
        }
        graphicList.add(group);
        dropdownModel.addElement(group.getId());
        gpanel.add(group);
        gpanel.repaint();
        return true;
    }

    @Override
    public boolean undoIt() {
        if (group != null) {
            graphicList.remove(group);
            dropdownModel.removeElement(group.getId());
            gpanel.remove(group);

            gpanel.repaint();
            return true;
        }
        return false;
    }
}
