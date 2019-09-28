/**
 * Interface definition of a {@link QuadNode}.
 * <p/>
 * A {@link QuadNode} represents some node in the {@link QuadTree}. It represents a square region of
 * color with sides being length of a power of two. Each {@link QuadNode} contains either four child
 * nodes or a single color represented by an {@code int} when it is a leaf.
 * <p/>
 * A single {@link QuadNode} does NOT need to guarantee the above invariant by itself - rather the
 * invariants will be maintained by the {@link QuadTree} that created the given {@link QuadNode}.
 *
 * @author cquanze
 */
public interface QuadNode {

    /**
     * Returns the color that this quadnode represents.
     * <p/>
     * If this node is a leaf, then the color is an integer value representing the color of the
     * region that this node represents. Otherwise, the node does not have a color and this method
     * should throw an exception.
     *
     * @return the integer representation of the color
     * @throws IllegalStateException if the node is not a leaf
     */
    int getColor();

    /**
     * Sets the color of the current quadrant. This method can only be called on a leaf
     * as internal {@link QuadNode}s do not store colors.
     * <p/>
     * Colors of all integers are theoretically allowed.
     *
     * @param color the color
     * @throws IllegalStateException if this quadnode is not a leaf
     */
    void setColor(int color);

    /**
     * Returns the {@link QuadNode} in the specified quadrant. If this quadnode is a leaf, then
     * this method returns {@code null} for all quadrants. Otherwise this method returns a
     * non-{@code null} value.
     *
     * @param quadrant the quadrant to retrieve
     * @return quadrant instance or {@code null}
     */
    QuadNode getQuadrant(QuadName quadrant);

    /**
     * Returns {@code true} if the {@link QuadNode} is a leaf.
     *
     * @return {@code true} if the quadnode is a leaf
     */
    boolean isLeaf();

    /**
     * Returns the dimensions that this quadnode represents.
     * <p/>
     * For example, a quadnode representing a 4x4 area will return {@code 4}. This value will always
     * be a non-negative integer power of 2.
     *
     * @return the size of the square's side length represented by this quadnode.
     */
    int getDimension();

    /**
     * Returns the number of quadnode children this current node contains (including itself).
     * <p/>
     * Example: A leaf is size {@code 1}.
     * Example: A quadnode with 4 children and no grandchildren is size {@code 5}
     *
     * @return the number of quadnodes contained by this quadnode
     */
    int getSize();

    /**
     * Sets a certain quadrant of the current quadnode to a different {@link QuadNode}.
     * <p/>
     * The value can be {@code null}. Note that calling this method an incorrect number of times may
     * leave the quadnode in an inconsistent state. You do not need to check for this in the
     * quadnode implementation, but you must guarantee that operations on the {@link QuadTree} do
     * not leave any of its child nodes in an inconsistent state.
     *
     * @param quadrant the quadrant to update
     * @param value the new node to set the quadrant to
     */
    void setQuadrant(QuadName quadrant, QuadNode value);

    /**
     * Enumeration for representing the location of a quadrant.
     */
    enum QuadName {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
