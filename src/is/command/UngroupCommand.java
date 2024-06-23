package is.command;

import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.view.GraphicObjectPanel;

import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class UngroupCommand implements Command {
    private int groupId;
    private List<GraphicObject> graphicList;
    private GraphicObjectPanel gpanel;
    private DefaultComboBoxModel<Integer> dropdownModel;
    private GroupObject group;

    private List<GraphicObject> ungroupedObjects;

    public UngroupCommand(int groupId, List<GraphicObject> graphicList, GraphicObjectPanel gpanel, DefaultComboBoxModel<Integer> dropdownModel) {
        this.groupId = groupId;
        this.graphicList = graphicList;
        this.gpanel = gpanel;
        this.dropdownModel = dropdownModel;
    }

    @Override
    public boolean execute() {
        group = getGroupById(groupId);
        if (group != null) {
            ungroupedObjects = group.getObjects();

            for (GraphicObject go : ungroupedObjects) {
                if (!graphicList.contains(go)) {
                    graphicList.add(go);
                    dropdownModel.addElement(go.getId());
                    gpanel.add(go);
                }
            }

            graphicList.remove(group);
            dropdownModel.removeElement(groupId);
            gpanel.remove(group);
            gpanel.repaint();
            return true;
        }
        return false;
    }

    @Override
    public boolean undoIt() {
        if (group != null) {

            graphicList.add(group);
            dropdownModel.addElement(groupId);
            gpanel.add(group);
            gpanel.repaint();
            return true;
        }
        return false;
    }


    private GroupObject getGroupById(int id) {
        for (GraphicObject obj : graphicList) {
            if (obj instanceof GroupObject && obj.getId() == id) {
                return (GroupObject) obj;
            }
        }
        return null;
    }
}