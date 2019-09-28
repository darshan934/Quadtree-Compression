/**
 * Factory for {@link QuadTree} instances. All implementors of this interface must have have a
 * public default no-args constructor.
 * <p/>
 * This factory allows callers to build your implementation of a {@link QuadTree} without depending
 * on your actual concrete implementation class.
 *
 * @author davix
 */
public interface QuadTreeFactory {

    /**
     * Builds a {@link QuadTree} instance from a 2D array.
     *
     * @param imageData a 2D array that contains the image to compress
     * @return a {@link QuadTree} representing the specified image data
     * @throws IllegalArgumentException if {@code imageData} is null
     * @throws IllegalArgumentException if {@code imageData} has a size of 0
     * @throws IllegalArgumentException if {@code imageData} has rows of different lengths
     * @throws IllegalArgumentException if the dimensions of {@code imageData} are not equal,
     * non-negative integer powers of 2.
     */
    QuadTree buildFromIntArray(int[][] imageData);
}
