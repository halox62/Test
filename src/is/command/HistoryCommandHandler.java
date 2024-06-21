package is.command;

import is.shapes.view.GraphicObjectPanel;

import java.util.LinkedList;

public class HistoryCommandHandler implements CommandHandler {
	
	private int maxHistoryLength = 100;

	private final LinkedList<Command> history = new LinkedList<>();

	private final LinkedList<Command> redoList = new LinkedList<>();
	private GraphicObjectPanel gpanel;

	public HistoryCommandHandler() {
		this(null,100);
	}

	public HistoryCommandHandler(GraphicObjectPanel gpanel, int maxHistoryLength) {
		this.gpanel=gpanel;
		if (maxHistoryLength < 0)
			throw new IllegalArgumentException();
		this.maxHistoryLength = maxHistoryLength;
	}

	public void handle(Command cmd) {
		if (cmd.execute()) {
			// restituisce true: può essere annullato
			addToHistory(cmd);
		} else {
			// restituisce false: non può essere annullato
			history.clear();
		}
		if (redoList.size() > 0)
			redoList.clear();
		gpanel.repaint();
	}

	public void redo() {
		if (redoList.size() > 0) {
			Command redoCmd = redoList.removeFirst();
			redoCmd.execute();
			history.addFirst(redoCmd);
			gpanel.repaint();
		}
	}

	public void undo() {
		if (history.size() > 0) {
			Command undoCmd = history.removeFirst();
			undoCmd.undoIt();
			redoList.addFirst(undoCmd);
			gpanel.repaint();
		}
	}

	private void addToHistory(Command cmd) {
		history.addFirst(cmd);
		if (history.size() > maxHistoryLength) {
			history.removeLast();
		}

	}

}
