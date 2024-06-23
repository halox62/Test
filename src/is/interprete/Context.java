package is.interprete;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private String input;
    private List<String> output = new ArrayList<>();

    public Context(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void addOutput(String output) {
        this.output.add(output);
    }

    public List<String> getOutput() {
        return output;
    }
}
