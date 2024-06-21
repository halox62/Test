package is.test;

import static org.junit.Assert.assertEquals;

import is.shapes.model.RectangleObject;
import org.junit.Before;
import org.junit.Test;
import java.awt.Point;

public class GraphicObjectTest {
    private RectangleObject rectangle;

    @Before
    public void setUp() {
        rectangle = new RectangleObject(new Point(0, 0),1,2);
    }

    @Test
    public void testMoveObject() {
        rectangle.moveTo(new Point(10, 10));
        assertEquals(new Point(10, 10), rectangle.getPosition());
    }


}
