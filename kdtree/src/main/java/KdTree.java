import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private static final RectHV WHOLE_RECT = new RectHV(0, 0, 1, 1);
    private static final boolean VERTICAL = true;
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
        root = insert(p, root, VERTICAL);
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
        return contains(p, root, VERTICAL);
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

    public Iterable<Point2D> range(RectHV rect) {
        requireNotNull(rect);
        Collection<Point2D> res = new LinkedList<>();
        range(rect, root, WHOLE_RECT, VERTICAL, res);
        return res;
    }

    private void range(RectHV query, Node node, RectHV current, boolean vertical, Collection<Point2D> res) {
        if (node == null) {
            return;
        }
        if (query.contains(node.point)) {
            res.add(node.point);
        }
        List<RectHV> rects = getChildRects(node.point, current, vertical);
        if (query.intersects(rects.get(0))) {
            range(query, node.left, rects.get(0), !vertical, res);
        }
        if (query.intersects(rects.get(1))) {
            range(query, node.right, rects.get(1), !vertical, res);
        }
    }

    private List<RectHV> getChildRects(Point2D point, RectHV current, boolean vertical) {
        List<RectHV> res = new LinkedList<>();
        if (vertical) {
            res.add(new RectHV(current.xmin(), current.ymin(), point.x(), current.ymax()));
            res.add(new RectHV(point.x(), current.ymin(), current.xmax(), current.ymax()));
        } else {
            res.add(new RectHV(current.xmin(), current.ymin(), current.xmax(), point.y()));
            res.add(new RectHV(current.xmin(), point.y(), current.xmax(), current.ymax()));
        }
        return res;
    }

    public Point2D nearest(Point2D p) {
        requireNotNull(p);
        if (isEmpty()) {
            return null;
        }
        Point2D[] res = {root.point};
        nearest(p, root, WHOLE_RECT, VERTICAL, res);
        return res[0];
    }

    private void nearest(Point2D query, Node node, RectHV curRect, boolean vertical, Point2D[] res) {
        if (node == null) {
            return;
        }
        double minSqDist = query.distanceSquaredTo(res[0]);
        if (minSqDist > curRect.distanceSquaredTo(query)) {
            double dst = query.distanceSquaredTo(node.point);
            if (dst < minSqDist) {
                res[0] = node.point;
                minSqDist = dst;
            }
        }
        List<RectHV> rects = getChildRects(node.point, curRect, vertical);
        RectHV leftRect = rects.get(0);
        RectHV rightRect = rects.get(1);
        double leftDist = leftRect.distanceSquaredTo(query);
        boolean lCheck = leftDist <= minSqDist;
        double rightDist = rightRect.distanceSquaredTo(query);
        boolean rCheck = rightDist <= minSqDist;
        if (lCheck && !rCheck) {
            nearest(query, node.left, leftRect, !vertical, res);
        } else if (!lCheck && rCheck) {
            nearest(query, node.right, rightRect, !vertical, res);
        } else if (lCheck) {
            if (leftDist < rightDist) {
                nearest(query, node.left, leftRect, !vertical, res);
                nearest(query, node.right, rightRect, !vertical, res);
            } else {
                nearest(query, node.right, rightRect, !vertical, res);
                nearest(query, node.left, leftRect, !vertical, res);
            }
        }
    }

    private void requireNotNull(Object p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    public void draw() {

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
