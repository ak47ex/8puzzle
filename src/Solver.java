import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {

    private MinPQ<Board> originWay;
    private MinPQ<Board> twinWay;

    private Board solution;
    private boolean solvable;

    private LinkedList<Board> solutionPath;

    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        Comparator<Board> boardComparator = new Comparator<Board>() {
            @Override
            public int compare(Board b1, Board b2) {
                return Integer.compare(b1.manhattan() + b1.hamming(), b2.manhattan() + b2.hamming());
            }
        };

        originWay = new MinPQ<>(boardComparator);
        twinWay = new MinPQ<>(boardComparator);

        originWay.insert(initial);
        twinWay.insert(initial.twin());

        LinkedList<Board> originSolution = new LinkedList<>();
        LinkedList<Board> twinSolution = new LinkedList<>();

        moves = 0;
        while (!originWay.min().isGoal() || !twinWay.min().isGoal()) {

            Board origin = originWay.delMin();
            originSolution.add(origin);
            for (Board neighbor : origin.neighbors()) {
                originWay.insert(neighbor);
            }

            Board twin = twinWay.delMin();
            twinSolution.add(twin);
            for (Board neighbor : twin.neighbors()) {
                twinWay.insert(neighbor);
            }
            moves++;
        }

        if (originWay.min().isGoal()) {
            solution = originWay.min();
            solvable = true;
            solutionPath = originSolution;
        } else {
            solution = twinWay.min();
            solutionPath = twinSolution;
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