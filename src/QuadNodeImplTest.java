import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QuadNodeImplTest {

    QuadNode quadNode;
    @Before
    public void setUp() {
        quadNode = new QuadNodeImpl(4);
    }
    @Test
    public void testGetColor() {
        assertEquals(-1, quadNode.getColor());
        QuadNode temp = new QuadNodeImpl(4, 10);
        assertEquals(10, temp.getColor());
    }

    @Test
    public void testSetColor() {
        quadNode.setColor(165);
        assertEquals(165, quadNode.getColor());
    }

    @Test
    public void testGetQuadrant() {
        assertNull(quadNode.getQuadrant(QuadNode.QuadName.TOP_LEFT));
    }

    @Test
    public void testIsLeaf() {
        assertTrue(quadNode.isLeaf());
    }

    @Test
    public void testGetDimension() {
        //fix this later
        assertEquals(4, quadNode.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(1, quadNode.getSize());
    }

    @Test
    public void testSetQuadrant() {
        QuadNode temp = new QuadNodeImpl(2, 4);
        temp.setQuadrant(QuadNode.QuadName.TOP_LEFT, null);
        temp.setQuadrant(QuadNode.QuadName.TOP_RIGHT, null);
        temp.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, null);
        temp.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, null);
        assertEquals(quadNode.getQuadrant(QuadNode.QuadName.TOP_LEFT), 
                temp.getQuadrant(QuadNode.QuadName.TOP_LEFT));
        assertEquals(quadNode.getQuadrant(QuadNode.QuadName.TOP_RIGHT), 
                temp.getQuadrant(QuadNode.QuadName.TOP_RIGHT));
        assertEquals(quadNode.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT), 
                temp.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT));
        assertEquals(quadNode.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), 
                temp.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT));
        
    }

}
