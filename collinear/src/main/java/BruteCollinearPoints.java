import java.util.Comparator;

public class BruteCollinearPoints {

    private int segNum;
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        int length = points.length;
        if (length < 4) {
            segments = new LineSegment[0];
            if (length == 1) {
                checkNonNull(points[0]);
            } else {
                for (int i = 0; i < length - 1; i++) {
                    checkNonNull(points[i]);
                    for (int j = i + 1; j < length; j++) {
                        checkNonNull(points[j]);
                        checkNotEqual(points[i], points[j]);
                    }
                }
            }
            return;
        }
        segments = new LineSegment[length / 2];
        for (int p = 0; p < length - 3; p++) {
            for (int q = p + 1; q < length - 2; q++) {
                Point pointP = points[p];
                Point pointQ = points[q];
                checkNonNull(pointP);
                checkNonNull(pointQ);
                checkNotEqual(pointP, pointQ);
                double pqSlope = pointP.slopeTo(pointQ);
                for (int r = q + 1; r < length - 1; r++) {
                    Point pointR = points[r];
                    checkNonNull(pointR);
                    checkNotEqual(pointP, pointR);
                    checkNotEqual(pointQ, pointR);
                    double prSlope = pointP.slopeTo(pointR);
                    if (Double.compare(pqSlope, prSlope) != 0) {
                        checkNonNull(points[r + 1]);
                        checkNotEqual(pointP, points[r + 1]);
                        checkNotEqual(pointQ, points[r + 1]);
                        checkNotEqual(pointR, points[r + 1]);
                        continue;
                    }
                    for (int s = r + 1; s < length; s++) {
                        Point pointS = points[s];
                        checkNonNull(pointS);
                        checkNotEqual(pointP, pointS);
                        checkNotEqual(pointQ, pointS);
                        checkNotEqual(pointR, pointS);
                        double psSlope = pointP.slopeTo(pointS);
                        if (Double.compare(pqSlope, psSlope) == 0) {
                            Point min = find(Comparator.naturalOrder(), pointP, pointQ, pointR, pointS);
                            Point max = find(Comparator.reverseOrder(), pointP, pointQ, pointR, pointS);
                            LineSegment ls = new LineSegment(min, max);
                            segments[segNum++] = ls;
                        }
                    }
                }
            }
        }
    }

    private Point find(Comparator<Point> cmp, Point... points) {
        Point res = points[0];
        for (int i = 1; i < points.length; i++) {
            if (cmp.compare(res, points[i]) > 0) {
                res = points[i];
            }
        }
        return res;
    }

    private void checkNonNull(Point p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotEqual(Point p, Point q) {
        if (p.compareTo(q) == 0) {
            throw new IllegalArgumentException();
        }
    }

    public int numberOfSegments() {
        return segNum;
    }

    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[segNum];
        System.arraycopy(segments, 0, res, 0, segNum);
        return res;
    }
}
