import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Simple paint program that uses the {@link QuadTree} as a backing implementation. Most of the code
 * is UI-related so it is not required that you understand it. This class is provided as a courtesy
 * for debugging.
 *
 * @author cquanze
 */
public class SimplePaint implements Runnable {

    public static void main(String[] argv) {
        SwingUtilities.invokeLater(new SimplePaint());
    }

    /**
     * Builds a {@link QuadTree} instance from a {@link BufferedImage}. Depends on {@link
     * QuadTreeFactoryImpl#buildFromIntArray}.
     * <p/>
     * This method guarantees that even images with non-square dimensions are able to be loaded by
     * loading them in the smallest {@link QuadTree} that can contain them.
     * <p/>
     * You do not need to edit this code.
     *
     * @param image the image to compress into a {@link QuadTree}
     * @return the compressed version of the image as a {@link QuadTree}.
     * @throws IllegalArgumentException if the specified {@link BufferedImage} is null
     */
    public static QuadTree buildFromImage(QuadTreeFactory quadTreeFactory, BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        int maxDimensions = Math.max(image.getHeight(), image.getWidth());
        int dimensions = 1;
        while (dimensions < maxDimensions) {
            dimensions *= 2;
        }
        int[][] imageData = new int[dimensions][dimensions];
        for (int i = image.getMinX(); i < image.getWidth(); i++) {
            for (int j = image.getMinY(); j < image.getHeight(); j++) {
                // Remove alpha channel if it exists
                imageData[j][i] = image.getRGB(i, j) % 0x1000000;
            }
        }
        return quadTreeFactory.buildFromIntArray(imageData);
    }

    @Override
    public void run() {
        QuadTreeFactory quadTreeFactory = new QuadTreeFactoryImpl();
        final JFrame window = new JFrame("JPaint");
        // Build the paint component
        final PaintPanel paint =
                new PaintPanel(quadTreeFactory.buildFromIntArray(new int[512][512]));
        window.add(paint, BorderLayout.CENTER);

        // Build the menu bar
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menuFile = new JMenu("File");
        final JMenuItem openMenuItem = new JMenuItem("Open");
        final JMenu menuEdit = new JMenu("Edit");
        final JMenuItem pruneMenuItem = new JMenuItem("Pruning Compression");
        final JMenu menuInfo = new JMenu("Info");
        final JMenuItem infoMenuItem = new JMenuItem("Show Info");
        final JCheckBoxMenuItem showBordersMenuItem =
                new JCheckBoxMenuItem("Show QuadNode borders");
        final JFileChooser fc = new JFileChooser();

        openMenuItem.addActionListener(e -> {
            int returnVal = fc.showOpenDialog(window);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    paint.setQuadTree(buildFromImage(quadTreeFactory, ImageIO.read(f)));
                    window.pack();
                    window.repaint();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(window, "IOException: " + e);
                } catch (IllegalArgumentException n) {
                    JOptionPane.showMessageDialog(window, "ImageIO: Error reading image file.");
                }
            }
        });
        menuFile.add(openMenuItem);
        pruneMenuItem.addActionListener(e -> {
            String depth =
                    JOptionPane.showInputDialog("Select prune depth (1 ~ MAX_INTEGER):", "8");
            if (depth != null) {
                try {
                    int depthInt = Integer.parseInt(depth);
                    TreePruner.pruneTree(paint.getQuadTree().getRoot(), Math.max(1, depthInt));
                    paint.repaint();
                } catch (NumberFormatException ignored) {
                }
            }
        });
        menuEdit.add(pruneMenuItem);
        infoMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(window,
                "Info:\n" + "Image Size: " + paint.getQuadTree().getDimension()
                        + "x" + paint.getQuadTree().getDimension() + "\n" + "Bytes: "
                        + paint.getQuadTree().getDimension() * paint.getQuadTree().getDimension()
                        + "\n"
                        + "Nodes Used: " + paint.getQuadTree().getRoot().getSize() + "\n"
                        + "Compression Ratio: "
                        + String.format("%1.4f", paint.getQuadTree().getCompressionRatio())));
        showBordersMenuItem.setState(false);
        showBordersMenuItem
                .addActionListener(e -> paint.setShowNodeBorders(showBordersMenuItem.getState()));
        menuInfo.add(infoMenuItem);
        menuInfo.add(showBordersMenuItem);
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuInfo);
        window.setJMenuBar(menuBar);

        // Configure the window
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        window.repaint();
    }

    private static class TreePruner {
        private static int averageColor(int[] colors) {
            int r = 0, g = 0, b = 0;
            for (int color : colors) {
                r += color / 65536;
                g += (color % 65536) / 256;
                b += color % 256;
            }
            return (int) Math.round((double) r / colors.length) * 65536
                    + (int) Math.round((double) g / colors.length) * 256 + (int) Math
                    .round((double) b / colors.length);
        }

        private static boolean isCombinable(QuadNode node) {
            if (node == null) {
                return false;
            }
            for (QuadNode.QuadName quad : QuadNode.QuadName.values()) {
                if (node.getQuadrant(quad) == null || !node.getQuadrant(quad).isLeaf()) {
                    return false;
                }
            }
            return true;
        }

        public static void pruneTree(QuadNode root, int depth) {
            if (root == null || root.isLeaf()) {
                return;
            }
            if (depth <= 0 && isCombinable(root)) {
                int tl = root.getQuadrant(QuadNode.QuadName.TOP_LEFT).getColor();
                int tr = root.getQuadrant(QuadNode.QuadName.TOP_RIGHT).getColor();
                int bl = root.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT).getColor();
                int br = root.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT).getColor();
                root.setQuadrant(QuadNode.QuadName.TOP_LEFT, null);
                root.setQuadrant(QuadNode.QuadName.TOP_RIGHT, null);
                root.setQuadrant(QuadNode.QuadName.BOTTOM_LEFT, null);
                root.setQuadrant(QuadNode.QuadName.BOTTOM_RIGHT, null);
                root.setColor(averageColor(new int[]{tl, tr, bl, br}));
            } else {
                pruneTree(root.getQuadrant(QuadNode.QuadName.TOP_LEFT), depth - 1);
                pruneTree(root.getQuadrant(QuadNode.QuadName.TOP_RIGHT), depth - 1);
                pruneTree(root.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), depth - 1);
                pruneTree(root.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT), depth - 1);
                if (depth <= 0) {
                    pruneTree(root, depth);
                }
            }
        }
    }

    private static class PaintPanel extends Component
            implements MouseListener, MouseMotionListener {
        /**
         * Serial version UID
         */
        private static final long serialVersionUID = -2271950113761629787L;

        private static final Color BACKGROUND = Color.BLACK;
        private final int currentColor = 0xffffff;
        private QuadTree tree;
        private int width;
        private int height;
        private boolean showNodeBorders = false;
        private int prevX = -1, prevY = -1;

        public PaintPanel(QuadTree backingInstance) {
            this.width = backingInstance.getDimension();
            this.height = backingInstance.getDimension();
            this.tree = backingInstance;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }

        public void setShowNodeBorders(boolean show) {
            this.showNodeBorders = show;
            this.repaint();
        }

        public QuadTree getQuadTree() {
            return this.tree;
        }

        public void setQuadTree(QuadTree qt) {
            this.tree = qt;
            this.width = qt.getDimension();
            this.height = qt.getDimension();
            this.repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(this.width, this.height);
        }

        @Override
        public void update(Graphics g) {
            paint(g);
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(BACKGROUND);
            g.fillRect(0, 0, width, height);
            this.recursivePaint(g, this.tree.getRoot(), 0, 0);
        }

        private void recursivePaint(Graphics g, QuadNode n, int x, int y) {
            if (n.isLeaf()) {
                g.setColor(new Color(n.getColor()));
                g.fillRect(x, y, n.getDimension(), n.getDimension());
                if (showNodeBorders) {
                    g.setColor(Color.RED);
                    g.drawRect(x, y, n.getDimension(), n.getDimension());
                }
            } else {
                int midPoint = n.getDimension() / 2;
                recursivePaint(g, n.getQuadrant(QuadNode.QuadName.TOP_LEFT), x, y);
                recursivePaint(g, n.getQuadrant(QuadNode.QuadName.TOP_RIGHT), x + midPoint, y);
                recursivePaint(g, n.getQuadrant(QuadNode.QuadName.BOTTOM_LEFT), x, y + midPoint);
                recursivePaint(g, n.getQuadrant(QuadNode.QuadName.BOTTOM_RIGHT),
                        x + midPoint, y + midPoint);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            prevX = -1;
            prevY = -1;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (prevX < 0 || prevY < 0) {
                prevX = e.getX();
                prevY = e.getY();
                return;
            }
            this.drawLine(prevX, prevY, e.getX(), e.getY());
            this.repaint();
            prevX = e.getX();
            prevY = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        private void drawLine(int fromX, int fromY, int toX, int toY) {
            fromX = Math.min(Math.max(0, fromX), this.tree.getDimension() - 1);
            fromY = Math.min(Math.max(0, fromY), this.tree.getDimension() - 1);
            toX = Math.min(Math.max(0, toX), this.tree.getDimension() - 1);
            toY = Math.min(Math.max(0, toY), this.tree.getDimension() - 1);
            int deltaX = toX - fromX;
            int deltaY = toY - fromY;
            if (deltaX == 0) {
                for (int y = fromY; toY > fromY ? y <= toY : y >= toY; ) {
                    this.tree.setColor(fromX, y, this.currentColor);
                    if (toY > fromY) {
                        y++;
                    } else {
                        y--;
                    }
                }
                return;
            }
            float error = 0;
            float deltaError = Math.abs((float) deltaY / deltaX);
            int y = fromY;
            for (int x = fromX; toX > fromX ? x <= toX : x >= toX; ) {
                this.tree.setColor(x, y, this.currentColor);
                error += deltaError;
                while (error >= 0.5) {
                    this.tree.setColor(x, y, this.currentColor);
                    y = Math.min(Math.max(y + (toY > fromY ? 1 : -1), 0),
                            this.tree.getDimension() - 1);
                    error -= 1.0;
                }
                if (toX > fromX) {
                    x++;
                } else {
                    x--;
                }
            }
        }
    }
}
