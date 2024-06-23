package is.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import is.command.CommandHandler;
import is.command.GroupCommand;
import is.command.HistoryCommandHandler;
import is.command.MoveCommand;
import is.shapes.model.CircleObject;
import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.model.RectangleObject;
import is.shapes.view.GraphicObjectPanel;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UndoRedoTest {
    private GraphicObjectPanel gpanel;
    private List<GraphicObject> graphicList;
    private HistoryCommandHandler cmdHandler;
    private DefaultComboBoxModel<Integer> dropdownModel;

    private List<Object> groups;

    private GroupCommand groupCommand;

    @Before
    public void setUp() {
        gpanel = new GraphicObjectPanel();
        graphicList = new ArrayList<>();
        cmdHandler = new HistoryCommandHandler(gpanel,10);
        dropdownModel = new DefaultComboBoxModel<>();

        RectangleObject rectangle1 = new RectangleObject();
        CircleObject circle1 = new CircleObject();

        rectangle1.setId(1);
        circle1.setId(2);

        graphicList.add(rectangle1);
        graphicList.add(circle1);
    }

    @Test
    public void testUndoRedo() {
        List<Integer> selectedIds = Arrays.asList(1, 2);
        groupCommand = new GroupCommand(1000,selectedIds, graphicList, gpanel, dropdownModel, cmdHandler);
        cmdHandler.handle(groupCommand);

        groups = gpanel.getGroups();
        assertEquals(1, groups.size());
        assertTrue(graphicList.get(2) instanceof GroupObject);


        cmdHandler.undo();
        assertEquals(2, graphicList.size());
        assertTrue(graphicList.get(0) instanceof RectangleObject);
        assertTrue(graphicList.get(1) instanceof CircleObject);


        cmdHandler.redo();
        groups = gpanel.getGroups();
        assertEquals(1, groups.size());
        assertTrue(graphicList.get(2) instanceof GroupObject);

    }
}
