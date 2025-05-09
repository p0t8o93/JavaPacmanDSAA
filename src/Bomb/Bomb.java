import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bomb {
    int x_position_pixel; // Pixel position
    int y_position_pixel; // Pixel position
    int X_GridPosition;
    int Y_GridPosition;
    int tileSize;
    Color color;
    long placedTime;
    long explosionDuration = 750; // ms for how long explosion effect lasts
    long fuseTime = 2000; // 1.5 seconds

    enum BombState { PLACED, EXPLODING, DONE }
    BombState state = BombState.PLACED;
    long explosionStartTime;

    public Bomb(int gridX, int gridY, int tileSize, Color color) {
        this.X_GridPosition = gridX;
        this.Y_GridPosition = gridY;
        this.tileSize = tileSize;
        this.x_position_pixel = gridX * tileSize;
        this.y_position_pixel = gridY * tileSize;
        this.color = color; // Color for PLACED state (e.g., Yellow)
        this.placedTime = System.currentTimeMillis();
    }

    public void update() {
        if (state == BombState.PLACED) {
            if (System.currentTimeMillis() - placedTime > fuseTime) {
                state = BombState.EXPLODING;
                explosionStartTime = System.currentTimeMillis();
                // System.out.println("Bomb at " + X_GridPosition + "," + Y_GridPosition + " exploding!");
            }
        } else if (state == BombState.EXPLODING) {
            if (System.currentTimeMillis() - explosionStartTime > explosionDuration) {
                state = BombState.DONE;
                // System.out.println("Bomb at " + X_GridPosition + "," + Y_GridPosition + " done exploding.");
            }
        }
    }

    public void draw(Graphics2D g2d, PacmanGame game) { // PacmanGame needed for isWallAtGrid
        if (state == BombState.PLACED) {
            g2d.setColor(this.color); // Use the stored color for placed bomb
            g2d.fillRect(x_position_pixel, y_position_pixel, tileSize, tileSize);
        } else if (state == BombState.EXPLODING) {
            g2d.setColor(Color.ORANGE); // Explosion color

            // Explosion pattern: Center + 1 tile in each cardinal direction if not blocked by wall
            int[][] DIRS = {{0, 0}, {0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // Center, Up, Down, Left, Right

            for (int[] d : DIRS) {
                int ex = X_GridPosition + d[0];
                int ey = Y_GridPosition + d[1];

                // Check bounds and if the tile is not a wall
                if (ex >= 0 && ex < game.columnCount && ey >= 0 && ey < game.rowCount) {
                    if (!game.isWallAtGrid(ex, ey)) {
                        g2d.fillRect(ex * game.tileSize, ey * game.tileSize, game.tileSize, game.tileSize);
                    }
                }
            }
        }
    }

    // Helper for collision detection against Pacman
    public Rectangle getExplosionCellBounds(int gridX, int gridY) {
        return new Rectangle(gridX * tileSize, gridY * tileSize, tileSize, tileSize);
    }
}