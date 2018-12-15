import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int size;

    private int[][] blocks;

    private int zx;
    private int zy;

    private int hamming;
    private int manhattan;

    private boolean changed;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        size = blocks.length;
        this.blocks = new int[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                this.blocks[i][j] = blocks[i][j];
                if (this.blocks[i][j] == 0) {
                    zx = i;
                    zy = j;
                }
            }
        }
        changed = true;
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of blocks out of place
    public int hamming() {
        if (changed) recalculate();

        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (changed) recalculate();
        return manhattan;
    }

    private void recalculate() {
        int m = 0;
        int h = 0;

        int item = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                item++;
                int v = blocks[i][j];
                if (item == v || v == 0) continue;
                h++;

                int k = (v - 1) / size;
                int b = (v - 1) % size;
                m += Math.abs(k - i) + Math.abs(b - j);
            }
        }
        manhattan = m;
        hamming = h;
        changed = false;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board b = new Board(blocks);

        int x1 = (zx == 0 ? 1 : 0) % size;
        int y1 = zy % size;
        int x2 = (zx == 0 ? 1 : 0) % size;
        int y2 = (zy + 1) % size;

        b.swap(x1, y1, x2, y2);

        return b;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (size != that.size) return false;

        return Arrays.deepEquals(blocks, ((Board) y).blocks);
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
        StringBuilder s = new StringBuilder(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private boolean canMoveLeft() {
        return zx != size - 1;
    }

    private boolean canMoveUp() {
        return zy != size - 1;
    }

    private boolean canMoveRight() {
        return zx != 0;
    }

    private boolean canMoveDown() {
        return zy != 0;
    }

    private void left() {

        blocks[zx][zy] = blocks[zx + 1][zy];
        zx = zx + 1;
        blocks[zx][zy] = 0;

        changed = true;
    }

    private void up() {
        blocks[zx][zy] = blocks[zx][zy + 1];
        zy = zy + 1;
        blocks[zx][zy] = 0;

        changed = true;
    }

    private void right() {
        blocks[zx][zy] = blocks[zx - 1][zy];
        zx = zx - 1;
        blocks[zx][zy] = 0;

        changed = true;
    }

    private void down() {
        blocks[zx][zy] = blocks[zx][zy - 1];
        zy = zy - 1;
        blocks[zx][zy] = 0;

        changed = true;
    }

    private void swap(int x1, int y1, int x2, int y2) {
        int tmp = blocks[x1][y1];
        blocks[x1][y1] = blocks[x2][y2];
        blocks[x2][y2] = tmp;

        changed = true;
    }

}
