package is;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import is.command.*;
import is.interprete.CommandExpression;
import is.interprete.Context;
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

        dropdownModel = new DefaultComboBoxModel<>();
        for (GraphicObject go : graphicList) {
            dropdownModel.addElement(go.getId());
        }
        JComboBox<Integer> dropdown = new JComboBox<>(dropdownModel);
        dropdown.addActionListener(e -> {
            Integer selectedOption = (Integer) dropdown.getSelectedItem();
            if (selectedOption != null) {
                for (GraphicObject go : graphicList) {
                    if(go.getId()==selectedOption){
                        go.setHighlighted(true);
                        goc.setControlledObject(go);
                    }else{
                        go.setHighlighted(false);
                    }
                }
                gpanel.repaint();
            }
            gpanel.repaint();
        });



        toolbar.add(dropdown);
        JTextField textField = new JTextField(20);
        JButton okButton = new JButton("OK");

        toolbar.add(textField);
        toolbar.add(okButton);

        //commands
        String[] commands = {
                "Command",
                "<createCircle>::= new Circle raggio x,y",
                "<createRectangle>::= new Rectangle h w x,y",
                "<createImage>::= new Image (\"path\") x,y",
                "<remove>::= del objId",
                "<move>::= move objId x,y | moveoff objId xOff,yOff ",
                "<scale>::= scale objId posfloat",
                "<list>::= ls objID| ls type | ls all | ls groups",
                "<group>::= grp listID>",
                "<ungroup>::= ungrp objID",
                "<area>::= area objID| area type | area all",
                "<perimeter>::= perimeter objID | perimeter type | perimeter all"
        };
        JComboBox<String> commandDropdown = new JComboBox<>(commands);

        toolbar.add(commandDropdown);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textField.getText();
                CommandExpression commandExpression = new CommandExpression(inputText,handler,gpanel,graphicList,dropdownModel);
                Context context = new Context(inputText);
                commandExpression.interpret(context);
            }
        });


        gpanel.setPreferredSize(new Dimension(400, 400));

        GraphicObjectViewFactory.FACTORY.installView(RectangleObject.class, new RectangleObjectView());
        GraphicObjectViewFactory.FACTORY.installView(CircleObject.class, new CircleObjectView());
        GraphicObjectViewFactory.FACTORY.installView(ImageObject.class, new ImageObjectView());


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

    public static void main(String[] args) {
        new TestGraphics3();
    }

}

