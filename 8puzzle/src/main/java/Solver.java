import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {

    private final int moves;
    private final LinkedList<Board> solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        twinPq.insert(new SearchNode(initial.twin(), 0, null));
        while (!pq.isEmpty() || !twinPq.isEmpty()) {
            SearchNode cur = pq.delMin();
            SearchNode curTwin = twinPq.delMin();
            if (cur.board.isGoal()) {
                moves = cur.moves;
                solution = new LinkedList<>();
                do {
                    solution.addFirst(cur.board);
                    cur = cur.prev;
                } while (cur != null);
                return;
            }
            if (curTwin.board.isGoal()) {
                break;
            }
            for (Board neighbor : cur.board.neighbors()) {
                if (cur.prev == null) {
                    pq.insert(new SearchNode(neighbor, cur.moves + 1, cur));
                } else if (!neighbor.equals(cur.prev.board)) {
                    pq.insert(new SearchNode(neighbor, cur.moves + 1, cur));
                }
            }
            for (Board neighbor : curTwin.board.neighbors()) {
                if (curTwin.prev == null) {
                    twinPq.insert(new SearchNode(neighbor, curTwin.moves + 1, curTwin));
                } else if (!neighbor.equals(curTwin.prev.board)) {
                    twinPq.insert(new SearchNode(neighbor, curTwin.moves + 1, curTwin));
                }
            }
        }
        moves = -1;
        solution = null;
    }

    public boolean isSolvable() {
        return moves != -1;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhattan;

        private SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattan = board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            int thisRes = manhattan + moves;
            int thatRes = that.moves + that.manhattan;
            return thisRes - thatRes;
        }

    }
}
