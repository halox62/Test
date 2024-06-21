package is;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import is.command.*;
import is.memento.GraphicObjectFactory;
import is.shapes.controller.GraphicObjectController;
import is.shapes.model.*;
import is.shapes.view.*;

public class TestGraphics3 {

    private DefaultComboBoxModel<Integer> dropdownModel;
    private List<GraphicObject> graphicList = new ArrayList<>();

    private int nextGroupId = 1000;

    public TestGraphics3() {
        JFrame f = new JFrame();

        JToolBar toolbar = new JToolBar();

        JButton undoButt = new JButton("Undo");
        JButton redoButt = new JButton("Redo");

        GraphicObjectFactory.register("Circle", CircleObject::new);
        GraphicObjectFactory.register("Image", ImageObject::new);
        GraphicObjectFactory.register("Rectangle", RectangleObject::new);
        GraphicObjectFactory.register("Group", GroupObject::new);

        final GraphicObjectPanel gpanel = new GraphicObjectPanel();

        final HistoryCommandHandler handler = new HistoryCommandHandler(gpanel, 100);
        final GraphicObjectController goc = new GraphicObjectController(handler);

        undoButt.addActionListener(evt -> handler.undo());

        redoButt.addActionListener(evt -> handler.redo());

        toolbar.add(undoButt);
        toolbar.add(redoButt);


        gpanel.setPreferredSize(new Dimension(400, 400));

        GraphicObjectViewFactory.FACTORY.installView(RectangleObject.class, new RectangleObjectView());
        GraphicObjectViewFactory.FACTORY.installView(CircleObject.class, new CircleObjectView());
        GraphicObjectViewFactory.FACTORY.installView(ImageObject.class, new ImageObjectView());

        //graphicList = loadGraphicObjects("graphic_list.ser");
        //gpanel.setGraphicObjects(graphicList);

        dropdownModel = new DefaultComboBoxModel<>();
        //for (GraphicObject go : graphicList) {
        //  dropdownModel.addElement(go.getId());
        //}

        // Menu a tendina per le operazioni
        String[] operations = {"MoveCommand", "Delete", "Move", "Move Offset", "Scale", "List", "Area", "Perimeter", "Create Group", "Ungroup"};
        JComboBox<String> operationDropdown = new JComboBox<>(operations);
        toolbar.add(operationDropdown);


        operationDropdown.addActionListener(e -> {
            String selectedOperation = (String) operationDropdown.getSelectedItem();

            List<Integer> availableIds = new ArrayList<>();
            for (GraphicObject go : graphicList) {
               availableIds.add(go.getId());
            }

            if (!availableIds.isEmpty()) {
                List<Integer> selectedIds = showIdSelectionDialog(availableIds,
                        selectedOperation.equals("Create Group"),
                        selectedOperation.equals("Area")||selectedOperation.equals("Perimeter")||selectedOperation.equals("List"),
                        selectedOperation.equals("List")
                        ,gpanel);
                if (!selectedIds.isEmpty()) {
                        GraphicObject selectedObject = graphicList.stream()
                                .filter(go -> go.getId() == selectedIds.get(0))
                                .findFirst()
                                .orElse(null);

                        if (selectedObject != null) {
                            if(selectedObject.getId()==selectedIds.get(0)){
                                selectedObject.setHighlighted(true);
                            }else{
                                selectedObject.setHighlighted(false);
                            }
                            gpanel.repaint();

                            switch (selectedOperation) {
                                case "MoveCommand":
                                    goc.setControlledObject(selectedObject);
                                    break;
                                case "Delete":
                                    int response = JOptionPane.showConfirmDialog(
                                            null,
                                            "Sei sicuro di voler eliminare l'oggetto selezionato?",
                                            "Conferma eliminazione",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE
                                    );

                                    if (response == JOptionPane.YES_OPTION) {
                                        DeleteCommand deleteCommand = new DeleteCommand(graphicList, dropdownModel, gpanel, selectedObject);
                                        handler.handle(deleteCommand);
                                        gpanel.repaint();
                                    }
                                    break;
                                case "Move":// TODO
                                    String newPosition = JOptionPane.showInputDialog(
                                            null,
                                            "Enter new position (x,y):",
                                            "Move Object",
                                            JOptionPane.PLAIN_MESSAGE
                                    );
                                    if (newPosition != null && newPosition.matches("\\d+,\\d+")) {
                                        String[] coords = newPosition.split(",");
                                        Point newPoint = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                                        MoveCommand moveCommand = new MoveCommand(selectedObject, newPoint);
                                        handler.handle(moveCommand);
                                        gpanel.repaint();
                                    }
                                    break;
                                case "Move Offset":// TODO
                                    String offset = JOptionPane.showInputDialog(
                                            null,
                                            "Enter offset (dx,dy):",
                                            "Move Object Offset",
                                            JOptionPane.PLAIN_MESSAGE
                                    );
                                    if (offset != null && offset.matches("\\d+,\\d+")) {
                                        String[] offsets = offset.split(",");
                                        MoveOffsetCommand moveOffsetCommand = new MoveOffsetCommand(selectedObject, offsets);
                                        handler.handle(moveOffsetCommand);
                                        gpanel.repaint();
                                    }
                                    break;
                                case "Scale":
                                    String scaleFactor = JOptionPane.showInputDialog(
                                            null,
                                            "Enter scale factor:",
                                            "Scale Object",
                                            JOptionPane.PLAIN_MESSAGE
                                    );
                                    if (scaleFactor != null && scaleFactor.matches("\\d+(\\.\\d+)?")) {
                                        double factor = Double.parseDouble(scaleFactor);
                                        ScaleCommand scaleCommand = new ScaleCommand(selectedObject, factor);
                                        handler.handle(scaleCommand);
                                        gpanel.repaint();
                                    }
                                    break;
                                case "Area":
                                    if (selectedIds.size() > 1) {
                                        double totalArea = 0;
                                        for (Integer id : selectedIds) {
                                            GraphicObject go = graphicList.stream()
                                                    .filter(obj -> obj.getId() == id)
                                                    .findFirst()
                                                    .orElse(null);
                                            if (go != null) {
                                                totalArea += go.getArea();
                                            }
                                        }
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Total Area of selected objects: " + totalArea,
                                                "Visualizza Area",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    } else if (selectedObject != null) {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Area of selected object: " + selectedObject.getArea(),
                                                "Visualizza Area",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    }
                                    break;
                                case "Perimeter":
                                    if (selectedIds.size() > 1) {
                                        double totalPerimetro = 0;
                                        for (Integer id : selectedIds) {
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
                                                "Visualizza Perimetro",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    } else if (selectedObject != null) {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Perimetro of selected object: " + selectedObject.getPerimetro(),
                                                "Visualizza Perimetro",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    }
                                    break;
                                case "Create Group":
                                        GroupCommand groupCommand = new GroupCommand(nextGroupId, selectedIds, graphicList, gpanel, dropdownModel, handler);
                                        handler.handle(groupCommand);
                                        gpanel.repaint();
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Group created with ID: " + nextGroupId++,
                                                "Group Created",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    break;
                                case "Ungroup":
                                    if (selectedObject instanceof GroupObject) {
                                        ((GroupObject) selectedObject).getObjects().forEach(go -> {
                                            if (!graphicList.contains(go)) {
                                                graphicList.add(go);
                                                dropdownModel.addElement(go.getId());
                                            }
                                        });
                                        graphicList.remove(selectedObject);
                                        dropdownModel.removeElement(selectedIds.get(0));
                                        gpanel.remove(selectedObject);
                                        gpanel.repaint();
                                    }
                                    break;
                                case "List":
                                    if(selectedObject.getType()=="Group"){
                                        GroupObject groupObject=(GroupObject) selectedObject;
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Gruppo con id= "+groupObject.getId()+"\n"+
                                                        "Contiene id= "+groupObject.getIds(),
                                                "Info",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                        break;
                                    }
                                    if(selectedIds.size()>1){
                                        ArrayList<GraphicObject> selectedObjectList=new ArrayList<>();
                                        for(GraphicObject graphicObject:graphicList){
                                            if(selectedIds.contains(graphicObject.getId())){
                                                selectedObjectList.add(graphicObject);
                                            }
                                        }
                                        StringBuilder message = new StringBuilder();
                                        for (GraphicObject graphicObject : selectedObjectList) {
                                            message.append("Type: ").append(graphicObject.getType()).append("\n");
                                            message.append("ID: ").append(graphicObject.getId()).append("\n");
                                        }


                                        JOptionPane.showMessageDialog(
                                                null,
                                                message.toString(),
                                                "Info",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    }else{
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Object= "+selectedObject.getType()+"\n"+
                                                        "Id= "+selectedObject.getId()+"\n"+
                                                        "Position= "+selectedObject.getPosition().getY()+","+selectedObject.getPosition().getY()+"\n"+
                                                        "Dimension= "+selectedObject.getDimension().getWidth()+"x"+selectedObject.getDimension().getHeight(),
                                                "Info",
                                                JOptionPane.INFORMATION_MESSAGE
                                        );
                                    }
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Scelta non valida",
                                            "ERRORE",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    break;
                            }
                        }
                    }else{
                    JOptionPane.showMessageDialog(
                            null,
                            "Non ci sono oggetti con il tipo selezionato",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                }else{
                JOptionPane.showMessageDialog(
                        null,
                        "Non ci sono oggetti disponibili",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        AbstractGraphicObject go = new RectangleObject(new Point(180, 80), 20, 50);
        JButton rectButton = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel, graphicList));
        rectButton.setText(go.getType());
        toolbar.add(rectButton);

        go = new CircleObject(new Point(200, 100), 10);
        JButton circButton = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel, graphicList));
        circButton.setText(go.getType());
        toolbar.add(circButton);

        go = new CircleObject(new Point(200, 100), 100);
        JButton circButton2 = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel, graphicList));
        circButton2.setText("big " + go.getType());
        toolbar.add(circButton2);

        go = new ImageObject(new ImageIcon(TestGraphics2.class.getResource("shapes/model/NyaNya.gif")),
                new Point(240, 187));
        JButton imgButton = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel, graphicList));
        imgButton.setText(go.getType());
        toolbar.add(imgButton);


        gpanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goc.setControlledObject(gpanel.getGraphicObjectAt(e.getPoint()));
            }
        });

        f.add(toolbar, BorderLayout.NORTH);
        f.add(new JScrollPane(gpanel), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());

        controlPanel.add(goc);
        f.setTitle("Shapes");
        f.getContentPane().add(controlPanel, BorderLayout.SOUTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    private List<GraphicObject> loadGraphicObjects(String filename) {
        List<GraphicObject> objects = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            objects = (List<GraphicObject>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("No saved objects found or error loading file.");
        }
        return objects;
    }



    private List<Integer> showIdSelectionDialog(List<Integer> availableIds, boolean multipleSelection, boolean areaPerimetro, boolean list,GraphicObjectPanel gpanel) {
        JCheckBox checkBoxR = null;
        JCheckBox checkBoxC=null;
        JCheckBox checkBoxI=null;
        JCheckBox checkBoxA=null;
        JCheckBox checkBoxL=null;

        for (GraphicObject go : graphicList) {
            go.setHighlighted(false);
        }
        gpanel.repaint();

        List<Integer> selectedIds = new ArrayList<>();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<JCheckBox> checkBoxes = new ArrayList<>();
        if(list){
            checkBoxL=new JCheckBox("Group");
            panel.add(checkBoxL);
        }
        if (areaPerimetro) {
            checkBoxR = new JCheckBox("Rectangle");
            checkBoxC = new JCheckBox("Circle");
            checkBoxI = new JCheckBox("Image");
            checkBoxA = new JCheckBox("All");
            panel.add(checkBoxR);
            panel.add(checkBoxC);
            panel.add(checkBoxI);
            panel.add(checkBoxA);

            checkBoxR.addItemListener(e -> highlightObjectsByType(RectangleObject.class, e.getStateChange() == ItemEvent.SELECTED,gpanel));
            checkBoxC.addItemListener(e -> highlightObjectsByType(CircleObject.class, e.getStateChange() == ItemEvent.SELECTED,gpanel));
            checkBoxI.addItemListener(e -> highlightObjectsByType(ImageObject.class, e.getStateChange() == ItemEvent.SELECTED,gpanel));
            checkBoxA.addItemListener(e -> {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                for (GraphicObject go : graphicList) {
                    go.setHighlighted(selected);
                }
                gpanel.repaint();
            });
        }
        for (Integer id : availableIds) {
            JCheckBox checkBox = new JCheckBox("ID: " + id);
            checkBoxes.add(checkBox);
            panel.add(checkBox);

            checkBox.addItemListener(e -> {
                GraphicObject go = graphicList.stream()
                        .filter(obj -> obj.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (go != null) {
                    go.setHighlighted(e.getStateChange() == ItemEvent.SELECTED);
                    gpanel.repaint();
                }

                if (!multipleSelection && e.getStateChange() == ItemEvent.SELECTED) {
                    for (JCheckBox otherCheckBox : checkBoxes) {
                        if (otherCheckBox != checkBox) {
                            otherCheckBox.setSelected(false);
                        }
                    }
                }
            });
        }

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                multipleSelection ? "Select IDs to group" : "Select ID",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if(checkBoxL!=null&&checkBoxL.isSelected()){
                selectedIds.addAll(getIdsByType(GroupObject.class));
            }
            if (checkBoxR!=null&&checkBoxR.isSelected()) {
                selectedIds.addAll(getIdsByType(RectangleObject.class));
            }
            if (checkBoxC!=null&&checkBoxC.isSelected()) {
                selectedIds.addAll(getIdsByType(CircleObject.class));
            }
            if (checkBoxI!=null&&checkBoxI.isSelected()) {
                selectedIds.addAll(getIdsByType(ImageObject.class));
            }
            if (checkBoxA!=null&&checkBoxA.isSelected()) {
                selectedIds.addAll(availableIds);
            }
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selectedIds.add(availableIds.get(i));
                }
            }
        }
        return selectedIds;
    }

    private void highlightObjectsByType(Class<?> type, boolean highlight,GraphicObjectPanel gpanel) {
        for (GraphicObject go : graphicList) {
            if (type.isInstance(go)) {
                go.setHighlighted(highlight);
            }
        }
        gpanel.repaint();
    }

    private List<Integer> getIdsByType(Class<?> type) {
        List<Integer> ids = new ArrayList<>();
        for (GraphicObject go : graphicList) {
            if (type.isInstance(go)) {
                ids.add(go.getId());
            }
        }
        return ids;
    }


    public static void main(String[] args) {
        new TestGraphics3();
    }

}

