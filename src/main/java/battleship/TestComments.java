
package battleship;

public class TestComments {
    private final char[][] grid = new char[10][10];

    // Check if the shot hit a ship at coordinates r and c
    public boolean checkShot(int r, int c) {

        // int targetRow = r;
        // int targetCol = c;
        // System.out.println("Processing shot at " + targetRow + ", " + targetCol);

        if (r >= 0 && r < 10 && c >= 0 && c < 10 && grid[r][c] == 'S') {
            return true;
        }
        return false;
    }
}
