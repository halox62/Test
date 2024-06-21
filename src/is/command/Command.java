package is.command;

public interface Command {
	boolean execute();

	boolean undoIt();
}
