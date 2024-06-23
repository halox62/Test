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
		//GraphicObject go = prototype.clone();
		//ch.handle(new NewObjectCmd(panel, prototype));
		int maxId = 0;
		for (GraphicObject goS : graphicList) {
			int id = goS.getId();
			if (id > maxId) {
				maxId = id;
			}
		}
		prototype.setId(maxId+1);
		graphicList.add(prototype);
		dropdownModel.addElement(prototype.getId());
		panel.setGraphicObjects(graphicList);
		panel.repaint();
	}
}
