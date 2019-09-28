/**
 * Interface definition of a {@link QuadTree}.
 * <p/>
 * A {@link QuadTree} represents a square image whose side lengths are non-negative powers of two.
 * It is implemented as a container of a tree built from {@link QuadNode}s. A {@link QuadTree} must
 * keep all nodes in a state consistent with the QuadTree invariants after calling any of its public
 * methods.
 * <p/>
 * The {@link QuadTree} is not thread-safe. You must implement your own synchronization if you wish
 * to use the {@link QuadTree} in a multi-threaded application.
 *
 * @author cquanze
 */
public interface QuadTree {

    /**
     * Sets the color at coordinates {@code (x, y)}.
     *
     * @param x the {@code x}-coordinate
     * @param y the {@code y}-coordinate
     * @param color the color
     * @throws IllegalArgumentException if {@code x} or {@code y} is out of bounds
     */
    void setColor(int x, int y, int color);

    /**
     * Gets the color at coordinates {@code (x, y)}.
     *
     * @param x the {@code x}-coordinate
     * @param y the {@code y}-coordinate
     * @return the color
     * @throws IllegalArgumentException if {@code x} or {@code y} is out of bounds
     */
    int getColor(int x, int y);

    /**
     * Gets the dimension of the image encoded by this quadtree. This is the size of the side of the
     * square of pixels covered by the root {@link QuadNode}.
     *
     * @return the length of the side of the square of the root {@link QuadNode}
     */
    int getDimension();

    /**
     * Decompresses the quadtree into a flat image. The returned array contains integers that
     * represent the color at each coordinate. The returned 2D array satisfies {@code
     * result[y][x] == getColor(x, y)} for each coordinate {@code (x, y)}.
     *
     * @return a newly initialized array storing the decompressed image data
     */
    int[][] decompress();

    /**
     * Gets the compression ratio of the current quadtree. The compression ratio is defined as the
     * number of {@link QuadNode}s contained in the tree, divided by the number of pixels in the
     * image.
     *
     * @return the compression ratio
     */
    double getCompressionRatio();

    /**
     * Returns the root node of the quadtree for testing.
     * <p/>
     * You may assume that outside code will not modify the {@link QuadNode} returned by this
     * method.
     *
     * @return root QuadNode
     */
    QuadNode getRoot();
}
