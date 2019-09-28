import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QuadTreeImplTest {

    QuadTree quadTree1, quadTree2;
    int[][] img1, img2;
    @Before
    public void setUp() {
        img1 = new int[][] {{1,1}, {1,1}};
        img2 = new int[][] {
            {1, 1, 2, 2},
            {1, 1, 2, 2},
            {2, 3, 2, 3},
            {3, 3, 1, 1}
        };
        quadTree1 = new QuadTreeImpl(img1);
        quadTree2 = new QuadTreeImpl(img2);
        
    }
    @Test
    public void testQuadTreeImpl() {
        assertEquals(2, quadTree1.getDimension());
        
    }

    @Test
    public void testSetColor() {
        quadTree2.setColor(1, 1, 1);
        assertEquals(1, quadTree1.getColor(1, 1));
    }

    @Test
    public void testGetColor() {
        quadTree1.setColor(1, 1, 11);
        quadTree1.setColor(1, 0, 11);
        quadTree1.setColor(0, 1, 11);

        assertEquals(11, quadTree1.getColor(1, 1));
        assertEquals(11, quadTree1.getColor(0, 1));
    }

    @Test
    public void testGetDimension() {
        assertEquals(4, quadTree2.getDimension());
    }

    @Test
    public void testDecompress() {
        int[][] temp1 = {{1, 1}, {1, 1}};
        int[][] temp2 = quadTree1.decompress();
        assertArrayEquals(temp1, temp2);
        
    }

    @Test
    public void testGetCompressionRatio() {
        assertEquals(0.25, quadTree1.getCompressionRatio(), 6);
    }

    @Test
    public void testGetRoot() {
        assertNotNull(quadTree1.getRoot());
        assertNotNull(quadTree2.getRoot());
    }

}
