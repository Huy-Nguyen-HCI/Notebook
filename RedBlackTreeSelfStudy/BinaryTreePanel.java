import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

/**
 * A panel that maintains a picture of a binary tree.
 * 
 * @author http://cs.lmu.edu/~ray/notes/binarytrees/
 */
public class BinaryTreePanel extends JPanel {
    private Node tree;
    private int gridwidth;
    private int gridheight;

    /**
     * Stores the pixel values for each node in the tree.
     */
    private Map<Node, Point> coordinates =
        new HashMap<Node, Point>();

    /**
     * Constructs a panel, saving the tree and drawing parameters.
     */
    public BinaryTreePanel(Node tree, int gridwidth, int gridheight) {
        this.tree = tree;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    /**
     * Changes the tree rendered by this panel.
     */
    public void setTree(Node root) {
        tree = root;
        repaint();
    }

    /**
     * Draws the tree in the panel.  First it computes the coordinates
     * of all the nodes with an inorder traversal, then draws them
     * with a postorder traversal.
     */
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (tree == null) {
            return;
        }

        tree.traverseInorder(new Node.Visitor() {
            private int x = gridwidth;
            public void visit(Node node) {
                coordinates.put(node, new Point(x, gridheight * (depth(node)+1)));
                x += gridwidth;
            }
        });

        tree.traversePostorder(new Node.Visitor() {
            public void visit(Node node) {
                String data = "" + node.value() + " " + (node.color() ? "RED" : "BLACK");
                Point center = (Point)coordinates.get(node);
                if (node.parent() != null) {
                    Point parentPoint = (Point)coordinates.get(node.parent());
                    g.setColor(Color.black);
                    g.drawLine(center.x, center.y, parentPoint.x, parentPoint.y);
                }
                FontMetrics fm = g.getFontMetrics();
                Rectangle r = fm.getStringBounds(data, g).getBounds();
                r.setLocation(center.x - r.width/2, center.y - r.height/2);
                Color color = getNodeColor(node);
                Color textColor =
                    (color.getRed() + color.getBlue() + color.getGreen() < 382)
                    ? Color.white
                    : Color.black;
                g.setColor(color);
                g.fillRect(r.x - 2 , r.y - 2, r.width + 4, r.height + 4);
                g.setColor(textColor);
                g.drawString(data, r.x, r.y + r.height);
            }
        });
    }

    
    Color getNodeColor(Node node) {
        return Color.white;
    }

    private int depth(Node node) {
        return (node.parent() == null) ? 0 : 1 + depth(node.parent());
    }
}