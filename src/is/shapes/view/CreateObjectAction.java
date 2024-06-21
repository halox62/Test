package is.shapes.view;

import is.command.CommandHandler;
import is.shapes.model.AbstractGraphicObject;
import is.shapes.model.GraphicObject;
import is.shapes.specificcommand.NewObjectCmd;

import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class CreateObjectAction extends AbstractAction {
	private static final long serialVersionUID = 5399493440620423134L;
	private final AbstractGraphicObject prototype;
	private final GraphicObjectPanel panel;
	private final CommandHandler ch;
	private final DefaultComboBoxModel<Integer> dropdownModel;

	private final List<GraphicObject> graphicList;

	public CreateObjectAction(AbstractGraphicObject prototype, GraphicObjectPanel panel, CommandHandler ch, DefaultComboBoxModel<Integer> dropdownModel, List<GraphicObject> graphicList) {
		this.prototype = prototype;
		this.panel = panel;
		this.ch = ch;
		this.dropdownModel = dropdownModel;
		this.graphicList = graphicList;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		GraphicObject go = prototype.clone();
		ch.handle(new NewObjectCmd(panel, go));
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("graphic_list.ser"))) {
			List<GraphicObject> graphicObjects = (List<GraphicObject>) in.readObject();
			int maxId = 0;
			for (GraphicObject goS : graphicObjects) {
				int id = goS.getId();
				if (id > maxId) {
					maxId = id;
				}
			}
			in.close();
			go.setId(maxId+1);
			graphicList.add(go);
			dropdownModel.addElement(go.getId());
			panel.setGraphicObjects(graphicList);
			panel.repaint();
		} catch (IOException | ClassNotFoundException ex) {
			System.out.println("No saved objects found or error loading file.");
		}
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("graphic_list.ser"))){
			out.writeObject(graphicList);
		} catch (IOException ex) {
			System.out.println("No saved objects found or error loading file.");
		}
	}
}
