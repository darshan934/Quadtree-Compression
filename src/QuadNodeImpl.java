/*
 * @author Darshan
 */
public class QuadNodeImpl implements QuadNode {
    
    private int color, dimension;
    private QuadNode tl, tr, br, bl;
    

    QuadNodeImpl(int dimension, int color) {
            
        this.color = color;
            
        tl = null;
        tr = null;
        bl = null;
        br = null;
            
        this.dimension = dimension;
    }
    
    QuadNodeImpl(int dimension) {
        
        this(dimension, -1);
    }
    
    @Override
    public int getColor() throws IllegalStateException {
        
        if (!this.isLeaf()) {
            throw new IllegalStateException("Node is not a leaf");
        }
        return color;
    }

    @Override
    public void setColor(int color) throws IllegalStateException {
        
        if (!this.isLeaf()) {
            throw new IllegalStateException();
        }
        this.color = color;  
    }

    @Override
    public QuadNode getQuadrant(QuadName quadrant) {
        
        if (this.isLeaf()) {
            return null;
        }
        if (QuadNode.QuadName.TOP_LEFT == quadrant) {
            return tl;
        } else if (QuadNode.QuadName.BOTTOM_LEFT == quadrant) {
            return bl;
        } else if (QuadNode.QuadName.BOTTOM_RIGHT == quadrant) {
            return br;
        } else {
            return tr;
        } 
        
    }

    @Override
    public boolean isLeaf() {
        if (this.getSize() == 1) {
            return true;
        }
        return false;
        
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    
    /**
     * Recursive method to find the size of the node and its children
     */
    @Override
    public int getSize() {
        int size = 1;
        if (this.tr != null) {
            size = size + this.tr.getSize(); 
        }
        if (this.br != null) {
            size = size + this.br.getSize();
        }
        if (this.tl != null) {
            size = size + this.tl.getSize();
        }
        if (this.bl != null) {
            size = size + this.bl.getSize();
        }
        return size;
    }

    @Override
    public void setQuadrant(QuadName quadrant, QuadNode value) {
        if (QuadNode.QuadName.TOP_LEFT == quadrant) {
            this.tl = value;
        } else if (QuadNode.QuadName.BOTTOM_LEFT == quadrant) {
            this.bl = value;
        } else if (QuadNode.QuadName.BOTTOM_RIGHT == quadrant) {
            this.br = value;
        } else {
            this.tr = value;
        } 
    }
}