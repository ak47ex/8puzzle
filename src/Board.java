import java.util.Arrays;
import java.util.Collections;

public class Board {

    private int[][] blocks;

    int zeroI;
    int zeroJ;
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        int n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                this.blocks[i][j] = blocks[i][j];
            }
        }

        updateZeroPointers();
    }

    private void updateZeroPointers() {
        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (blocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                    return;
                }
            }
        }
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
            int h = 0;
            for (int i = 0; i < dimension(); ++i) {
                for (int j = 0; j < dimension(); ++j) {
                    if (blocks[i][j] == 0) continue;
                    if (!isOnPosition(i, j)) h++;
                }
            }
            return h;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int m = 0;

        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (isOnPosition(i, j)) continue;

                int v = blocks[i][j];
                int k = (v - 1) / dimension();
                int l = (v - 1) / dimension();
                m += Math.abs(k - i) + Math.abs(l - j);
            }
        }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i = zeroI;
        int j = zeroJ;
        int k = zeroI;
        int l = zeroJ;
        while ((i != zeroI && j != zeroJ) && (k != zeroI && l != zeroJ)) {
            i = (int) (Math.random() * dimension());
            j = (int) (Math.random() * dimension());
            k = (int) (Math.random() * dimension());
            l = (int) (Math.random() * dimension());
        }
        Board b = new Board(blocks);
        int temp = blocks[i][j];
        b.blocks[i][j] = b.blocks[k][l];
        b.blocks[k][l] = temp;

        return b;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) return super.equals(y);

        Board that = (Board)y;
        if (dimension() != that.dimension()) return false;

        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return Collections.emptyList();
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private boolean isOnPosition(int i, int j) {
        if (blocks[i][j] == 0) return i == j && i == dimension() - 1;

        return blocks[i][j] == (i * j + j + 1);
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }
}
