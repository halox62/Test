package is.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import is.command.CommandHandler;
import is.command.GroupCommand;
import is.command.HistoryCommandHandler;
import is.shapes.model.CircleObject;
import is.shapes.model.GraphicObject;
import is.shapes.model.GroupObject;
import is.shapes.model.RectangleObject;
import is.shapes.view.GraphicObjectPanel;
import org.junit.Before;
import org.junit.Test;

public class GroupCommandTest {
    private List<GraphicObject> graphicList;
    private GraphicObjectPanel gpanel;
    private DefaultComboBoxModel<Integer> dropdownModel;
    private HistoryCommandHandler cmdHandler;
    private RectangleObject rectangle1;

    private GroupObject groupObject;
    private CircleObject circle1;
    private GroupCommand groupCommand;

    @Before
    public void setUp() {
        rectangle1 = new RectangleObject();
        circle1 = new CircleObject();

        rectangle1.setId(1);
        circle1.setId(2);

        graphicList = new ArrayList<>(Arrays.asList(rectangle1, circle1));

        gpanel = new GraphicObjectPanel();
        dropdownModel = new DefaultComboBoxModel<>();
        cmdHandler = new HistoryCommandHandler();
    }

    @Test
    public void testGroupObjects() {
        List<Integer> selectedIds = Arrays.asList(1, 2);

        GroupCommand groupCommand = new GroupCommand(100, selectedIds, graphicList, gpanel, dropdownModel, cmdHandler);

        groupCommand.execute();

        List<Object> groups = gpanel.getGroups();
        assertEquals(1, groups.size());

        GroupObject group = (GroupObject) groups.get(0);
        assertTrue(group.containsObject(rectangle1));
        assertTrue(group.containsObject(circle1));
    }
}