import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node root;

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return node != null ? node.size : 0;
    }

    public void insert(Point2D p) {
        requireNotNull(p);
        root = insert(p, root, true);
    }

    private Node insert(Point2D p, Node node, boolean vertical) {
        if (node == null) {
            return new Node(p);
        }
        Point2D current = node.point;
        if (p.equals(current)) {
            return node;
        }
        int cmp = comparePoints(p, current, vertical);
        if (cmp < 0) {
            node.left = insert(p, node.left, !vertical);
        } else {
            node.right = insert(p, node.right, !vertical);
        }
        node.size = size(node.left) + 1 + size(node.right);
        return node;
    }

    private int comparePoints(Point2D p1, Point2D p2, boolean vertical) {
        return vertical ? Double.compare(p1.x(), p2.x()) : Double.compare(p1.y(), p2.y());
    }

    public boolean contains(Point2D p) {
        requireNotNull(p);
        return contains(p, root, true);
    }

    private boolean contains(Point2D p, Node node, boolean vertical) {
        if (node == null) {
            return false;
        }
        Point2D current = node.point;
        if (p.equals(current)) {
            return true;
        }
        int cmp = comparePoints(p, current, vertical);
        if (cmp < 0) {
            return contains(p, node.left, !vertical);
        } else {
            return contains(p, node.right, !vertical);
        }
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        requireNotNull(rect);
        return null;
    }

    public Point2D nearest(Point2D p) {
        requireNotNull(p);
        return null;
    }

    private void requireNotNull(Object p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    private static class Node {
        Point2D point;
        Node left;
        Node right;
        int size;

        Node(Point2D pt) {
            point = pt;
            size = 1;
        }
    }
}
