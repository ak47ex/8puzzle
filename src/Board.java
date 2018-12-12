import java.util.*;

public class Board {

    private int[][] blocks;

    private int zx;
    private int zy;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        int n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                this.blocks[i][j] = blocks[i][j];
                if (this.blocks[i][j] == 0) {
                    zx = i;
                    zy = j;
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
        Board b = new Board(blocks);

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        while (x1 == zx) x1 = (int)(dimension() * Math.random());
        while (y1 == zy) y1 = (int)(dimension() * Math.random());
        while (x2 == zx) x2 = (int)(dimension() * Math.random());
        while (y2 == zy) y2 = (int)(dimension() * Math.random());

        swap(x1, y1, x2, y2);

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
        List<Board> neighbors = new ArrayList<>(4);

        if (canMoveDown()) {
            Board bro = new Board(blocks);
            bro.down();
            neighbors.add(bro);
        }

        if (canMoveLeft()) {
            Board bro = new Board(blocks);
            bro.left();
            neighbors.add(bro);
        }

        if (canMoveUp()) {
            Board bro = new Board(blocks);
            bro.up();
            neighbors.add(bro);
        }

        if (canMoveRight()) {
            Board bro = new Board(blocks);
            bro.right();
            neighbors.add(bro);
        }

        return neighbors;
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

        return blocks[i][j] == (i * dimension() + j + 1);
    }

    private boolean canMoveLeft() {
        return zx != dimension() - 1;
    }

    private boolean canMoveUp() {
        return zy != dimension() - 1;
    }

    private boolean canMoveRight() {
        return zx != 0;
    }

    private boolean canMoveDown() {
        return zy != 0;
    }

    private void left() {
        for (int i = zx; i < dimension() - 1; ++i) {
            blocks[i][zy] = blocks[i + 1][zy];
        }
        zx = dimension() - 1;
        blocks[zx][zy] = 0;
    }

    private void up() {
        for (int i = 0; i < dimension() - 1; ++i) {
            blocks[zx][i] = blocks[zx][i + 1];
        }
        zy = dimension() - 1;
        blocks[zx][zy] = 0;
    }

    private void right() {
        for (int i = zx; i > 0; --i) {
            blocks[i][zy] = blocks[i - 1][zy];
        }
        zx = 0;
        blocks[zx][zy] = 0;
    }

    private void down() {
        for (int i = zy; i > 0; --i) {
            blocks[zx][i] = blocks[zx][i - 1];
        }
        zy = 0;
        blocks[zx][zy] = 0;
    }

    private void swap(int x1, int y1, int x2, int y2) {
        int tmp = blocks[x1][y1];
        blocks[x1][y1] = blocks[x2][y2];
        blocks[x2][y2] = tmp;
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }

}
