import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;


public class Solver {

    private boolean solvable;

    private Iterable<Board> solutionPath;

    private final int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Comparator<SearchNode> boardComparator = Comparator.comparingInt(node -> node.priority);
        MinPQ<SearchNode> originWay = new MinPQ<>(boardComparator);
        MinPQ<SearchNode> twinWay = new MinPQ<>(boardComparator);

        originWay.insert(new SearchNode(initial));
        twinWay.insert(new SearchNode(initial.twin()));

        initial.manhattan();

        while (!originWay.min().board.isGoal() && !twinWay.min().board.isGoal()) {
            SearchNode min = originWay.delMin();
            Board origin = min.board;

            for (Board neighbor : origin.neighbors()) {
                if (min.predecessor == null || !neighbor.equals(min.predecessor.board)) originWay.insert(new SearchNode(neighbor, min));
            }

            min = twinWay.delMin();
            Board twin = min.board;
            for (Board neighbor : twin.neighbors()) {
                if (min.predecessor == null || !neighbor.equals(min.predecessor.board)) twinWay.insert(new SearchNode(neighbor, min));
            }
        }
        moves =  originWay.min().moves;
        if (originWay.min().board.isGoal()) {
            solvable = true;

            SearchNode tail = originWay.min();
            LinkedList<Board> path = new LinkedList<>();
            while (tail != null) {
                path.push(tail.board);
                tail = tail.predecessor;
            }
            solutionPath = path;

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

    private class SearchNode {
        final SearchNode predecessor;
        final Board board;
        final int moves;
        final int priority;


        SearchNode(Board board) {
            this.board = board;
            predecessor = null;
            moves = 0;
            priority = board.manhattan();
        }

        SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            predecessor = prevNode;
            moves = prevNode.moves + 1;
            priority = moves + board.manhattan();
        }

    }

}