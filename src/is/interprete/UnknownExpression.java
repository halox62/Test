package is.interprete;

import is.shapes.view.GraphicObjectPanel;

import javax.swing.*;

public class UnknownExpression implements Expression {
    private String command;


    public UnknownExpression(String[] tokens) {
        this.command = String.join(" ", tokens);
    }

    @Override
    public void interpret(Context context) {
        context.addOutput("Unknown command: " + command);
        JOptionPane.showMessageDialog(
                null,
                "Comando non riconosciuto",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
