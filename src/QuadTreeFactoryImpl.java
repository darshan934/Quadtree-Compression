/*
 * @author Darshan
 */
public class QuadTreeFactoryImpl implements QuadTreeFactory {
    
    @Override
    public QuadTree buildFromIntArray(int[][] imageData) {
        if (imageData == null) {
            throw new IllegalArgumentException();
        }
        if (imageData.length == 0) {
            throw new IllegalArgumentException();
        }
        int rowLength = imageData[0].length;
        for (int i = 0; i < imageData.length; i++) {
            if (imageData[i].length != rowLength) {
                throw new IllegalArgumentException();
            }
        }
        if (imageData.length != rowLength) {
            throw new IllegalArgumentException();
        }
        QuadTree quadTree = new QuadTreeImpl(imageData);
        return quadTree;        
    }
}
