package is.memento;

import is.shapes.model.GraphicObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GraphicObjectFactory {
    private static final Map<String, Supplier<GraphicObject>> registry = new HashMap<>();

    public static void register(String type, Supplier<GraphicObject> supplier) {
        registry.put(type, supplier);
    }

    public static GraphicObject createGraphicObject(String type) {
        Supplier<GraphicObject> supplier = registry.get(type);
        if (supplier != null) {
            return supplier.get();
        }
        throw new IllegalArgumentException("No registered supplier for type: " + type);
    }

    public static GraphicObject createGraphicObject(Memento memento) {
        if (memento instanceof TypeMemento) {
            String type = ((TypeMemento) memento).getType();
            return createGraphicObject(type);
        }
        throw new IllegalArgumentException("Memento must be a TypeMemento");
    }

    public static abstract class TypeMemento implements Memento {
        public abstract String getType();
    }
}
