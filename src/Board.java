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

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        size = blocks.length;

        int item = 0;

        this.blocks = new int[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                this.blocks[i][j] = blocks[i][j];
                if (this.blocks[i][j] == 0) {
                    zx = i;
                    zy = j;
                }

                item++;
                int v = blocks[i][j];
                if (item == v || v == 0) continue;
                hamming++;

                manhattan += manhattan(i, j, v);
            }
        }
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int x1 = (zx == 0 ? 1 : 0) % size;
        int y1 = zy % size;
        int x2 = (zx == 0 ? 1 : 0) % size;
        int y2 = (zy + 1) % size;

        return swap(x1, y1, x2, y2);
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

        if (canMoveDown()) neighbors.add(down());

        if (canMoveLeft()) neighbors.add(left());

        if (canMoveUp()) neighbors.add(up());

        if (canMoveRight()) neighbors.add(right());

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

    private Board left() {
        Board b = new Board(blocks);

        int v = b.blocks[b.zx + 1][b.zy];

        int manhWas = manhattan(b.zx + 1, b.zy, v);
        int manhBecame = manhattan(b.zx, b.zy, v);
        int manh = manhWas - manhBecame;
        b.manhattan -= manh;

        if (manhBecame == 0) b.hamming -= 1;
        if (manhWas == 0) b.hamming += 1;

        b.blocks[b.zx][b.zy] = b.blocks[b.zx + 1][b.zy];
        b.zx = b.zx + 1;
        b.blocks[b.zx][b.zy] = 0;

        return b;
    }

    private Board up() {
        Board b = new Board(blocks);

        int v = b.blocks[b.zx][b.zy + 1];
        int manhWas = manhattan(b.zx, b.zy + 1, v);
        int manhBecame = manhattan(b.zx, b.zy, v);
        int manh = manhWas - manhBecame;
        b.manhattan -= manh;

        if (manhBecame == 0) b.hamming -= 1;
        if (manhWas == 0) b.hamming += 1;

        b.blocks[b.zx][b.zy] = b.blocks[b.zx][b.zy + 1];
        b.zy = b.zy + 1;
        b.blocks[b.zx][b.zy] = 0;

        return b;
    }

    private Board right() {
        Board b = new Board(blocks);

        int v = b.blocks[b.zx - 1][b.zy];
        int manhWas = manhattan(b.zx - 1, b.zy, v);
        int manhBecame = manhattan(b.zx, b.zy, v);
        int manh = manhWas - manhBecame;
        b.manhattan -= manh;

        if (manhBecame == 0) b.hamming -= 1;
        if (manhWas == 0) b.hamming += 1;

        b.blocks[b.zx][b.zy] = b.blocks[b.zx - 1][b.zy];
        b.zx = b.zx - 1;
        b.blocks[b.zx][b.zy] = 0;

        return b;
    }

    private Board down() {
        Board b = new Board(blocks);

        int v = b.blocks[b.zx][b.zy - 1];
        int manhWas = manhattan(b.zx, b.zy - 1, v);
        int manhBecame = manhattan(b.zx, b.zy, v);
        int manh = manhWas - manhBecame;
        b.manhattan -= manh;

        if (manhBecame == 0) b.hamming -= 1;
        if (manhWas == 0) b.hamming += 1;

        b.blocks[b.zx][b.zy] = b.blocks[b.zx][b.zy - 1];
        b.zy = b.zy - 1;
        b.blocks[b.zx][b.zy] = 0;

        return b;
    }

    private Board swap(int x1, int y1, int x2, int y2) {
        Board b = new Board(blocks);

        int manhWas = manhattan(x1, y1, b.blocks[x1][y1]) + manhattan(x2, y2, b.blocks[x2][y2]);
        if (manhattan(x1, y1, b.blocks[x1][y1]) == 0) b.hamming += 1;
        if (manhattan(x2, y2, b.blocks[x2][y2]) == 0) b.hamming += 1;

        b.blocks[x2][y2] = blocks[x1][y1];
        b.blocks[x1][y1] = blocks[x2][y2];

        if (manhattan(x1, y1, b.blocks[x1][y1]) == 0) b.hamming -= 1;
        if (manhattan(x2, y2, b.blocks[x2][y2]) == 0) b.hamming -= 1;

        int manhBecame = manhattan(x1, y1, b.blocks[x1][y1]) + manhattan(x2, y2, b.blocks[x2][y2]);
        int manh = manhWas - manhBecame;

        b.manhattan -= manh;


        return b;
    }

    private int manhattan(int row, int col, int val) {
        int i = (val - 1) / size;
        int j = (val - 1) % size;
        return Math.abs(i - row) + Math.abs(j - col);
    }

}
