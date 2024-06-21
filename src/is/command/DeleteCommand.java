package is.command;

import is.memento.Memento;
import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteCommand implements Command {
    private final List<GraphicObject> graphicList;
    private final DefaultComboBoxModel<Integer> dropdownModel;
    private final GraphicObjectPanel gpanel;
    private final GraphicObject objectToDelete;
    private Memento memento;
    private List<Memento> affectedMementos;
    private List<GraphicObject> affectedObjects;

    public DeleteCommand(List<GraphicObject> graphicList,
                         DefaultComboBoxModel<Integer> dropdownModel, GraphicObjectPanel gpanel,GraphicObject objectToDelete) {
        this.objectToDelete = objectToDelete;
        this.graphicList = graphicList;
        this.dropdownModel = dropdownModel;
        this.gpanel = gpanel;
        this.affectedMementos = new ArrayList<>();
        this.affectedObjects = new ArrayList<>();
    }

    @Override
    public boolean execute() {
        memento = objectToDelete.createMemento();

        if (objectToDelete instanceof GroupObject) {
            GroupObject group = (GroupObject) objectToDelete;
            for (GraphicObject go : group.getObjects()) {
                affectedObjects.add(go);
                affectedMementos.add(go.createMemento());
                graphicList.remove(go);
                dropdownModel.removeElement(go.getId());
                gpanel.remove(go);
            }
        }

        graphicList.remove(objectToDelete);
        dropdownModel.removeElement(objectToDelete.getId());
        gpanel.remove(objectToDelete);

        // Rimuovere dagli eventuali gruppi
        List<GraphicObject> groups = graphicList.stream()
                .filter(go -> go instanceof GroupObject)
                .collect(Collectors.toList());
        for (GraphicObject group : groups) {
            GroupObject groupObject = (GroupObject) group;
            if (groupObject.getObjects().contains(objectToDelete)) {
                affectedObjects.add(groupObject);
                affectedMementos.add(groupObject.createMemento());
                groupObject.remove(objectToDelete);
                if (groupObject.getObjects().isEmpty()) {
                    graphicList.remove(groupObject);
                    dropdownModel.removeElement(groupObject.getId());
                }
            }
        }

        gpanel.repaint();
        return true;
    }

    @Override
    public boolean undoIt() {
        objectToDelete.restore(memento);
        graphicList.add(objectToDelete);
        dropdownModel.addElement(objectToDelete.getId());
        gpanel.add(objectToDelete);

        if (objectToDelete instanceof GroupObject) {
            GroupObject group = (GroupObject) objectToDelete;
            for (int i = 0; i < affectedObjects.size(); i++) {
                GraphicObject go = affectedObjects.get(i);
                go.restore(affectedMementos.get(i));
                graphicList.add(go);
                dropdownModel.addElement(go.getId());
                gpanel.add(go);
                group.add(go);
            }
        }

        // Ripristina gli eventuali gruppi
        for (int i = 0; i < affectedObjects.size(); i++) {
            GraphicObject go = affectedObjects.get(i);
            if (go instanceof GroupObject) {
                GroupObject group = (GroupObject) go;
                group.restore(affectedMementos.get(i));
                if (!graphicList.contains(group)) {
                    graphicList.add(group);
                    dropdownModel.addElement(group.getId());
                }
            }
        }

        gpanel.repaint();
        return true;
    }
}

