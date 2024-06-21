package is;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import is.command.HistoryCommandHandler;
import is.shapes.controller.GraphicObjectController;
import is.shapes.model.*;
import is.shapes.view.*;

public class TestGraphics2 {

	private DefaultComboBoxModel<Integer> dropdownModel;

	public TestGraphics2() {
		/*JFrame f = new JFrame();

		JToolBar toolbar = new JToolBar();

		JButton undoButt = new JButton("Undo");
		JButton redoButt = new JButton("Redo");

		final HistoryCommandHandler handler = new HistoryCommandHandler();

		undoButt.addActionListener(evt -> handler.undo());

		redoButt.addActionListener(evt -> handler.redo());

		toolbar.add(undoButt);
		toolbar.add(redoButt);

		final GraphicObjectPanel gpanel = new GraphicObjectPanel();

		gpanel.setPreferredSize(new Dimension(400, 400));

		GraphicObjectViewFactory.FACTORY.installView(RectangleObject.class, new RectangleObjectView());
		GraphicObjectViewFactory.FACTORY.installView(CircleObject.class, new CircleObjectView());
		GraphicObjectViewFactory.FACTORY.installView(ImageObject.class, new ImageObjectView());

		dropdownModel = new DefaultComboBoxModel<>();
		JComboBox<Integer> dropdown = new JComboBox<>(dropdownModel);
		dropdown.addActionListener(e -> {
			Integer selectedOption = (Integer) dropdown.getSelectedItem();
			System.out.println("Opzione selezionata: " + selectedOption);
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("graphic_list.ser"))) {
				List<GraphicObject> graphicList = (List<GraphicObject>) in.readObject();
				if (selectedOption != null) {
					for (GraphicObject go : graphicList) {
						go.setHighlighted(go.getId() == selectedOption);
					}
					gpanel.repaint();
				}
			gpanel.repaint();
			} catch (IOException | ClassNotFoundException ex) {
				System.out.println("No saved objects found or error loading file.");
			}
		});

		toolbar.add(dropdown);

		AbstractGraphicObject go = new RectangleObject(new Point(180, 80), 20, 50);
		JButton rectButton = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel));
		rectButton.setText(go.getType());
		toolbar.add(rectButton);

		go = new CircleObject(new Point(200, 100), 10);
		JButton circButton = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel));
		circButton.setText(go.getType());
		toolbar.add(circButton);

		go = new CircleObject(new Point(200, 100), 100);
		JButton circButton2 = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel));
		circButton2.setText("big " + go.getType());
		toolbar.add(circButton2);

		go = new ImageObject(new ImageIcon(TestGraphics2.class.getResource("shapes/model/NyaNya.gif")),
				new Point(240, 187));
		JButton imgButton = new JButton(new CreateObjectAction(go, gpanel, handler, dropdownModel));
		imgButton.setText(go.getType());
		toolbar.add(imgButton);

		final GraphicObjectController goc = new GraphicObjectController(handler);

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
		f.setVisible(true);*/
	}

	public static void main(String[] args) {
		new TestGraphics2();
	}

}
