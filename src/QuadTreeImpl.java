/*
 * @author Darshan
 */
public class QuadTreeImpl implements QuadTree {
    
    private QuadNode root;
    
    public QuadTreeImpl(int[][] imageData) {
        int length = imageData.length;
        if (length == 1) {
            root = new QuadNodeImpl(length, imageData[0][0]);
        } else {
            root = segmentation(length / 2, length / 2, imageData, length);
            root = segmentation(length / 2, length / 2, imageData, length);
        }
        
    }
    
    @Override
    public void setColor(int x, int y, int color) throws IllegalArgumentException {

        if (x < 0 || x >= root.getDimension() || y < 0 || y >= root.getDimension()) {
            throw new IllegalArgumentException();
        }
        colorSet(root, root.getDimension() / 2, root.getDimension() / 2, x, y, color);
    }
    
    @Override
    public int getColor(int x, int y) throws IllegalArgumentException {
        if (x < 0 || x >= root.getDimension() || y < 0 || y >= root.getDimension()) {
            throw new IllegalArgumentException();
        }
        int color = colorGet(root, root.getDimension() / 2, root.getDimension() / 2, x, y);
        return color;
        
    }
    
    @Override
    public int getDimension() {
        return root.getDimension();
    }

    @Override
    public int[][] decompress() {

        int dimension = root.getDimension();
        int[][] temp = new int[dimension][dimension];
        populateImg(root, dimension / 2, dimension / 2, temp);
        return temp;
  
    }
    
    @Override
    public double getCompressionRatio() {
        return root.getSize() / (getDimension() * getDimension());
    }

    @Override
    public QuadNode getRoot() {
        return root;
    }
    
    //helper methods
    private QuadNode segmentation(int x, int y, int[][] img, int dimension) {
        QuadNode node = new QuadNodeImpl(dimension);      
        if (dimension == 2) {
            int color = colorCheck(x, y, img);
            //if color is not -1 it means that the node is a leaf
            if (color != -1) {
                node.setColor(color);
            } else {
                //return node and its children
                node.setQuadrant(QuadNode.QuadName.TOP_LEFT, 
                        new QuadNodeImpl(dimension / 2, img[y - 1][x - 1]));
                node.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, 
                        new QuadNodeImpl(dimension / 2, img[y][x - 1]));
                node.setQuadrant(QuadNode.QuadName.TOP_RIGHT, 
                        new QuadNodeImpl(dimension / 2, img[y - 1][x]));
                node.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, 
                        new QuadNodeImpl(dimension / 2, img[y][x]));
            }
        // if the dimension is not 2 it means that the current node has children that
        // need to be explored hence we recursively search through the children
        } else {
            int xCoordinate = dimension / 4;
            int yCoordinate = dimension / 4;
            //offsetting centre according to the centrepoint of the nodes
            QuadNode tlTemp = segmentation(x - xCoordinate, y - yCoordinate, img, dimension / 2);
            QuadNode trTemp = segmentation(x + xCoordinate, y - yCoordinate, img, dimension / 2);
            QuadNode brTemp = segmentation(x + xCoordinate, y + yCoordinate, img, dimension / 2);
            QuadNode blTemp = segmentation(x - xCoordinate, y + yCoordinate, img, dimension / 2);
            
            if (tlTemp.isLeaf() && trTemp.isLeaf() && brTemp.isLeaf() && blTemp.isLeaf()) {
                int tlColor = tlTemp.getColor();
                int trColor = trTemp.getColor();
                int blColor = blTemp.getColor();
                int brColor = brTemp.getColor();
                if (tlColor == trColor && trColor == brColor && brColor == blColor) {
                    node.setColor(tlColor);
                    return node;
                }
            }
            node.setQuadrant(QuadNode.QuadName.TOP_LEFT, tlTemp);
            node.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, blTemp);
            node.setQuadrant(QuadNode.QuadName.TOP_RIGHT, trTemp);
            node.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, brTemp);
        }
        return node;
    }
    
    private int colorCheck(int x, int y, int[][] img) {
        
        int col = img[y][x];
        if (img[y][x - 1] != col || img[y - 1][x - 1] != col || img[y - 1][x] != col) {
            return -1;
        }
        return col;
    }
    
    private void colorSet(QuadNode parentNode, int parentX, int parentY, int x, int y, int color) {
        
        if (parentNode.isLeaf()) {
            int oldColor = parentNode.getColor();
            if (oldColor != color) {
                if (parentNode.getDimension() == 2) {
                    int dimension = parentNode.getDimension();
                    QuadNode tl = new QuadNodeImpl(dimension / 2, oldColor);
                    QuadNode tr = new QuadNodeImpl(dimension / 2, oldColor);
                    QuadNode br = new QuadNodeImpl(dimension / 2, oldColor);
                    QuadNode bl = new QuadNodeImpl(dimension / 2, oldColor);
                    if (x == parentX) {
                        if (parentY > y) {
                            tr.setColor(color);
                        } else if (parentY == y) {
                            br.setColor(color);
                        }
                    } else if (x < parentX) {
                        if (parentY > y) {
                            tl.setColor(color);
                        } else if (parentY == y) {
                            bl.setColor(color);
                        }
                    }
                    parentNode.setQuadrant(QuadNode.QuadName.TOP_LEFT, tl);
                    parentNode.setQuadrant(QuadNode.QuadName.TOP_RIGHT, tr);
                    parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, br);
                    parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, bl);
                }
                int dimension = parentNode.getDimension();
                QuadNode tl = new QuadNodeImpl(dimension / 2, oldColor);
                QuadNode tr = new QuadNodeImpl(dimension / 2, oldColor);
                QuadNode br = new QuadNodeImpl(dimension / 2, oldColor);
                QuadNode bl = new QuadNodeImpl(dimension / 2, oldColor);
                
                parentNode.setQuadrant(QuadNode.QuadName.TOP_LEFT, tl);
                parentNode.setQuadrant(QuadNode.QuadName.TOP_RIGHT, tr);
                parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, br);
                parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, bl);
                colorSet(parentNode, parentX, parentY, x, y, color);
            }
        }
        int parentDimension = parentNode.getDimension();
        if (parentDimension == 2) {
            QuadNode tl = parentNode.getQuadrant(QuadNode.QuadName.TOP_LEFT);
            QuadNode tr = parentNode.getQuadrant(QuadNode.QuadName.TOP_RIGHT);
            QuadNode br = parentNode.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT);
            QuadNode bl = parentNode.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT);
            
            if (parentX == x) {
                if (parentY > y) {
                    tr.setColor(color);
                } else if (parentY == y) {
                    br.setColor(color);
                }
            } else if (parentX > x) {
                if (parentY > y) {
                    tl.setColor(color);
                } else if (parentY == y) {
                    bl.setColor(color);
                }
            }
            
            int tlColor = tl.getColor();
            int trColor = tr.getColor();
            int brColor = br.getColor();
            int blColor = bl.getColor();
            if (tlColor == trColor && trColor == brColor && brColor == blColor) {
                parentNode.setQuadrant(QuadNode.QuadName.TOP_LEFT, null);
                parentNode.setQuadrant(QuadNode.QuadName.TOP_RIGHT, null);
                parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, null);
                parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, null);
                parentNode.setColor(tlColor);
            }
            return;
        }
        if (x >= parentX) {
            if (y < parentY) {
                int childX = parentX + (parentDimension / 4);
                int childY = parentY - (parentDimension / 4);
                colorSet(parentNode.getQuadrant(QuadNode.QuadName.TOP_RIGHT), 
                        childX, childY, x, y, color);
            } else {
                int childX = parentX + (parentDimension / 4);
                int childY = parentY + (parentDimension / 4);
                colorSet(parentNode.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT), 
                        childX, childY, x, y, color);
            }
        } else {
            if (y < parentY) {
                int childX = parentX - (parentDimension / 4);
                int childY = parentY - (parentDimension / 4);
                colorSet(parentNode.getQuadrant(QuadNode.QuadName.TOP_LEFT), 
                        childX, childY, x, y, color);
            } else {
                int childX = parentX - (parentDimension / 4);
                int childY = parentY + (parentDimension / 4);
                colorSet(parentNode.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), 
                        childX, childY, x, y, color);
            }
        }

        QuadNode tl = parentNode.getQuadrant(QuadNode.QuadName.TOP_LEFT);
        QuadNode tr = parentNode.getQuadrant(QuadNode.QuadName.TOP_RIGHT);
        QuadNode br = parentNode.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT);
        QuadNode bl = parentNode.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT);
        if (tl.isLeaf() && tr.isLeaf() && br.isLeaf() && bl.isLeaf()) {
            int tlColor = tl.getColor();
            int trColor = tr.getColor();
            int brColor = br.getColor();
            int blColor = bl.getColor();
            if (tlColor == trColor && trColor == brColor && brColor == blColor) {
                parentNode.setQuadrant(QuadNode.QuadName.TOP_LEFT, null);
                parentNode.setQuadrant(QuadNode.QuadName.TOP_RIGHT, null);
                parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, null);
                parentNode.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, null);
                
                parentNode.setColor(tlColor);
            }
        }

    }
    
    private int colorGet(QuadNode parent, int parentX, int parentY, int x, int y) {
        
        if (parent.isLeaf()) { 
            return parent.getColor();
        }
        int parentDimension = parent.getDimension();
        if (parentX < x) {
            if (parentY > y) {
                int childX = parentX + (parentDimension / 4);
                int childY = parentY - (parentDimension / 4);
                return colorGet(parent.getQuadrant(QuadNode.QuadName.TOP_RIGHT), 
                        childX, childY, x, y);
            } else {
                int childX = parentX + (parentDimension / 4);
                int childY = parentY + (parentDimension / 4);
                return colorGet(parent.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT), 
                        childX, childY, x, y);
            }
            
        } else {
            if (parentY > y) {
                int childX = parentX - (parentDimension / 4);
                int childY = parentY - (parentDimension / 4);
                return colorGet(parent.getQuadrant(QuadNode.QuadName.TOP_LEFT), 
                        childX, childY, x, y);
            } else {
                int childX = parentX - (parentDimension / 4);
                int childY = parentY + (parentDimension / 4);
                return colorGet(parent.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), 
                        childX, childY, x, y);
            }
        }
    }

    private void populateImg(QuadNode parent, int parentX, int parentY, int[][] arr) {
        
        if (!parent.isLeaf()) {
            int parentDimension = parent.getDimension();
            int center = parentDimension / 4;
            if (parentDimension == 2) {
                center = 1;
                populateImg(parent.getQuadrant(QuadNode.QuadName.TOP_LEFT), 
                        parentX - center, parentY - center, arr);
                populateImg(parent.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), 
                        parentX - center, parentY, arr);
                populateImg(parent.getQuadrant(QuadNode.QuadName.TOP_RIGHT), 
                        parentX, parentY - center, arr);
                populateImg(parent.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT), 
                        parentX, parentY, arr);
            } else {
                populateImg(parent.getQuadrant(QuadNode.QuadName.TOP_LEFT), 
                        parentX - center, parentY - center, arr);
                populateImg(parent.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), 
                        parentX - center, parentY + center, arr); 
                populateImg(parent.getQuadrant(QuadNode.QuadName.TOP_RIGHT), 
                        parentX + center, parentY - center, arr);
                populateImg(parent.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT), 
                        parentX + center, parentY + center, arr);                
            }
            
        } else {
            int dimension = parent.getDimension();
            int center = dimension / 2;
            int color = parent.getColor();
            if (dimension == 1) {
                arr[parentY][parentX] = color;
            } else {
                for (int y = parentY - center; y < parentY + center; y++) {
                    for (int x = parentX - center; x < parentX + center; x++) {
                        arr[y][x] = color;
                    }
                }
            }
        }
    }
}
