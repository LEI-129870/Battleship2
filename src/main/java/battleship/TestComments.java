package battleship;

public class TestComments {
    private final char[][] grid = new char[10][10];

    public boolean checkShot(int r, int c) {
        return r >= 0 && r < 10 && c >= 0 && c < 10 && grid[r][c] == 'S';
    }
}