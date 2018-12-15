import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;


public class Solver {

    private class Pair<A, B> {
        A key;
        B value;

        Pair(A key, B value) {
            this.key = key;
            this.value = value;
        }
    }

    private boolean solvable;

    private LinkedList<Board> solutionPath;

    private final int moves;

    // find a solution to the initial board (using the A* algorithm)
    private Solver(Board initial) {


        Comparator<Pair<Integer, Board>> boardComparator = Comparator.comparingInt(e -> e.key + e.value.manhattan());
        MinPQ<Pair<Integer, Board>> originWay = new MinPQ<>(boardComparator);
        MinPQ<Pair<Integer, Board>> twinWay = new MinPQ<>(boardComparator);

        originWay.insert(new Pair<>(0, initial));
        twinWay.insert(new Pair<>(0, initial.twin()));

        LinkedList<Board> originSolution = new LinkedList<>();

        Board originPredecessor = null;
        Board twinPredecessor = null;
        initial.manhattan();

        while (!originWay.min().value.isGoal() && !twinWay.min().value.isGoal()) {
            Pair<Integer, Board> min = originWay.delMin();
            Board origin = min.value;
            originSolution.add(origin);
            for (Board neighbor : origin.neighbors()) {
                if (!neighbor.equals(originPredecessor)) originWay.insert(new Pair<>(min.key + 1, neighbor));
            }
            originPredecessor = origin;

            min = twinWay.delMin();
            Board twin = min.value;
            for (Board neighbor : twin.neighbors()) {
                if (!neighbor.equals(twinPredecessor)) twinWay.insert(new Pair<>(min.key + 1, neighbor));
            }
            twinPredecessor = twin;
        }
        moves =  originWay.min().key;
        if (originWay.min().value.isGoal()) {
            solvable = true;
            solutionPath = originSolution;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? moves : -1;
    }

    // sequence of originWay in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionPath;
    }

    // solve a slider puzzle (given below)
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
}