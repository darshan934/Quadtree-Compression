import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QuadTreeFactoryImplTest {

    int[][] img;
    QuadTreeFactory quadFactory;
    @Before
    public void setup() {
        quadFactory = new QuadTreeFactoryImpl();
    }
    @Test (expected = IllegalArgumentException.class)
    public void testBuildFromIntArrayNullCheck() {
        img = null;
        quadFactory.buildFromIntArray(img);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testBuildFromIntArrayRowCheck() {
        img = new int[][] {{1, 2,}};
        quadFactory.buildFromIntArray(img);
    }
    @Test (expected = IllegalArgumentException.class)
    public void testBuildFromIntArrayDimensionCheck() {
        img = new int[2][1];
        quadFactory.buildFromIntArray(img);
    }
}
