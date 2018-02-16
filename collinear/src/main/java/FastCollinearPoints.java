import java.util.Arrays;

public class FastCollinearPoints {

    private int segNum;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] pts) {
        if (pts == null) {
            throw new IllegalArgumentException();
        }
        Point[] points = Arrays.copyOf(pts, pts.length);
        int length = points.length;
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
        segments = new LineSegment[length * 4];
        try {
            Arrays.sort(points);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
        Point[] cp = Arrays.copyOf(points, length);
        for (int i = 0; i < length - 3; i++) {
            Point p = points[i];
            checkNonNull(p);
            try {
                Arrays.sort(cp);
                Arrays.sort(cp, p.slopeOrder());
            } catch (NullPointerException e) {
                throw new IllegalArgumentException();
            }
            if (p.compareTo(cp[1]) == 0) {
                throw new IllegalArgumentException();
            }
            for (int j = 1; j < cp.length; ) {
                int apc = maxAdjacentIdx(cp, p, j);
                if (apc > 1 && p.compareTo(cp[j]) < 1) {
                    addSeg(new LineSegment(p, cp[j + apc]));
                }
                j += apc + 1;
            }
        }
    }

    private void addSeg(LineSegment ls) {
        if (segments.length == segNum) {
            segments = Arrays.copyOf(segments, segNum * 2);
        }
        segments[segNum++] = ls;
    }

    private static int maxAdjacentIdx(Point[] points, Point p, int start) {
        double slope = p.slopeTo(points[start]);
        int adjacentCount = 0;
        for (int i = start + 1; i < points.length; i++) {
            if (Double.compare(slope, p.slopeTo(points[i])) == 0) {
                adjacentCount++;
            } else {
                break;
            }
        }
        return adjacentCount;
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
        return Arrays.copyOf(segments, segNum);
    }

}
