import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board {

    private final int[][] blocks;
    private final int hamming;
    private final int manhattan;
    private List<Board> neighbours;

    public Board(int[][] blocks) {
        this.blocks = copy(blocks);
        this.manhattan = calcManhattan();
        this.hamming = calcHamming();
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    private int calcHamming() {
        int res = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if ((blocks[i][j] != 0) && (blocks[i][j] != blockInPlace(i, j))) {
                    res++;
                }
            }
        }
        return res;
    }

    private int blockInPlace(int row, int col) {
        int lastIdx = dimension() - 1;
        return (row == lastIdx && col == lastIdx) ? 0 : row * dimension() + col + 1;
    }

    private int calcManhattan() {
        int res = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int block = blocks[i][j];
                if (block != 0) {
                    res += Math.abs(i - correctRow(block));
                    res += Math.abs(j - correctCol(block));
                }
            }
        }
        return res;
    }

    private int correctRow(int block) {
        return (block % dimension() == 0) ? block / dimension() - 1 : block / dimension();
    }

    private int correctCol(int block) {
        int m = block % dimension() - 1;
        return m < 0 ? dimension() + m : m;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        int[][] newBlocks = copy(blocks);
        int i = 0;
        int j = 0;
        int k = 0;
        int m = 1;
        if (newBlocks[i][j] == 0 || newBlocks[k][m] == 0) {
            i++;
            k++;
        }
        swap(i, j, newBlocks, k, m);
        return new Board(newBlocks);
    }

    private int[][] copy(int[][] src) {
        int[][] newBlocks = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            newBlocks[i] = new int[src[i].length];
            System.arraycopy(src[i], 0, newBlocks[i], 0, src[i].length);
        }
        return newBlocks;
    }

    public Iterable<Board> neighbors() {
        if (neighbours == null) {
            calcNeighbours();
        }
        return neighbours;
    }

    private void calcNeighbours() {
        neighbours = new LinkedList<>();
        int blankRow = -1;
        int blankCol = -1;
        outer:
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break outer;
                }
            }
        }
        createNeighbour(blankRow, blankCol, blankRow, blankCol - 1); // left
        createNeighbour(blankRow, blankCol, blankRow, blankCol + 1); // right
        createNeighbour(blankRow, blankCol, blankRow - 1, blankCol); // top
        createNeighbour(blankRow, blankCol, blankRow + 1, blankCol); // bottom
    }

    private void createNeighbour(int srcRow, int srcCol, int swapRow, int swapCol) {
        if (isInBounds(swapRow) && isInBounds(swapCol)) {
            int[][] newBlocks = copy(blocks);
            swap(srcRow, srcCol, newBlocks, swapRow, swapCol);
            neighbours.add(new Board(newBlocks));
        }
    }

    private boolean isInBounds(int idx) {
        return idx >= 0 && idx < dimension();
    }

    private void swap(int i, int j, int[][] arr, int k, int m) {
        int tmp = arr[i][j];
        arr[i][j] = arr[k][m];
        arr[k][m] = tmp;
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null || y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension()).append("\n");
        for (int[] row : blocks) {
            sb.append(" ");
            for (int elem : row) {
                sb.append(elem).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Board a = new Board(new int[][]{{1, 0}, {3, 2}});
        System.out.println(a);
        System.out.println(a.twin());
    }

}
