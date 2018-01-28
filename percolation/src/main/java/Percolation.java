import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int TOP = 0;

    private final int bottom;
    private final boolean[] open;
    //    private final boolean[] directlyConnectedToTop;
    private final int n;
    private final WeightedQuickUnionUF uf;
    private int openSites = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.n = n;
        this.open = new boolean[n * n];
//        this.directlyConnectedToTop = new boolean[n * n];
        this.bottom = n * n + 1;
    }

    public void open(int row, int col) {
        checkBounds(row, col);
        if (isOpen(row, col)) {
            return;
        }
        int cell = toCellNum(row, col);
        open[cell - 1] = true;
        openSites++;
        connectCells(cell, row - 1, col);
        connectCells(cell, row + 1, col);
        connectCells(cell, row, col - 1);
        connectCells(cell, row, col + 1);

    }

    private void connectCells(int cell, int r, int c) {
        if (r == 0) {
            uf.union(cell, TOP);
//            directlyConnectedToTop[cell - 1] = true;
        } else if (r == n + 1) {
            uf.union(cell, bottom);
        } else if (c > 0 && c <= n && isOpen(r, c)) {
            int otherCell = toCellNum(r, c);
            uf.union(cell, otherCell);
//            if (directlyConnectedToTop[otherCell - 1]) {
//                directlyConnectedToTop[cell - 1] = true;
//            } else if (directlyConnectedToTop[cell - 1]) {
//                directlyConnectedToTop[otherCell - 1] = true;
//            }
        }
    }

    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return open[toCellNum(row, col) - 1];
    }

    public boolean isFull(int row, int col) {
        checkBounds(row, col);
//        return directlyConnectedToTop[toCellNum(row, col) - 1];
        return uf.connected(toCellNum(row, col), TOP);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return uf.connected(TOP, bottom);
    }

    private void checkBounds(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    private int toCellNum(int r, int c) {
        return (r - 1) * n + c;
    }
}