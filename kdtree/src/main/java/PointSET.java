import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class PointSET {

    private final NavigableSet<Point2D> pointSet;

    public PointSET() {
        pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        requireNotNull(p);
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        requireNotNull(p);
        return pointSet.contains(p);
    }

    public void draw() {
        pointSet.forEach(p -> StdDraw.point(p.x(), p.y()));
    }

    public Iterable<Point2D> range(RectHV rect) {
        requireNotNull(rect);
        List<Point2D> res = new LinkedList<>();
        for (Point2D pt : pointSet) {
            if (rect.contains(pt)) {
                res.add(pt);
            }
        }
        return res;
    }

    public Point2D nearest(Point2D p) {
        requireNotNull(p);
        if (isEmpty()) {
            return null;
        }
        Point2D res = pointSet.first();
        double minDistance = p.distanceSquaredTo(res);
        for (Point2D pt : pointSet) {
            double curDistance = p.distanceSquaredTo(pt);
            if (curDistance < minDistance) {
                minDistance = curDistance;
                res = pt;
            }
        }
        return res;
    }

    private void requireNotNull(Object p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }
}
