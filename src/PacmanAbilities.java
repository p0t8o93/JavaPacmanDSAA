
public class PacmanAbilities {

    private PacmanGame game;
    private Block pacman;

    // Dash Ability
    private static final long DASH_COOLDOWN_MS = 5000; // 5 seconds cooldown
    private long lastDashTime = 0;
    private boolean isDashing = false; // To prevent multiple dash calls while one is "active"

    public PacmanAbilities(PacmanGame game, Block pacman) {
        this.game = game;
        this.pacman = pacman;
    }

    public boolean canDash() {
        return System.currentTimeMillis() - lastDashTime >= DASH_COOLDOWN_MS && !isDashing;
    }

    public void activateDash() {

     
        System.out.println("Attempting Dash in direction: " + pacman.currentDirection);
        isDashing = true; // Mark as dashing to prevent re-triggering while processing

        int dashDistance = 0;
        int targetGridX = pacman.X_GridPosition;
        int targetGridY = pacman.Y_GridPosition;

        // Check path for 2 tiles
        for (int i = 1; i <= 3; i++) {
            int nextGridX = pacman.X_GridPosition + (pacman.currentDirection.getDx() * i);
            int nextGridY = pacman.Y_GridPosition + (pacman.currentDirection.getDy() * i);

            if (game.isWallAtGrid(nextGridX, nextGridY)) {
                break; // Stop if a wall is encountered
            }
            // If clear, this tile is part of the dash
            dashDistance = i;
            targetGridX = nextGridX;
            targetGridY = nextGridY;
        }

        if (dashDistance > 0) {
            System.out.println("Dashing " + dashDistance + " tiles.");
            // Collect food along the path before moving Pacman
            for (int i = 1; i <= dashDistance; i++) {
                int intermediateGridX = pacman.X_GridPosition + (pacman.currentDirection.getDx() * i);
                int intermediateGridY = pacman.Y_GridPosition + (pacman.currentDirection.getDy() * i);
                //game.collectFoodAt(intermediateGridX, intermediateGridY);
            }

            // Instantly move Pacman to the target grid position
            pacman.X_GridPosition = targetGridX;
            pacman.Y_GridPosition = targetGridY;
            pacman.x_position = targetGridX * game.tileSize;
            pacman.y_position = targetGridY * game.tileSize;

            // Pacman should stop any current tile-to-tile movement
            pacman.isMoving = false;
            // Optional: You might want to briefly show a dash animation or effect here
            // For now, it's an instant teleport

            lastDashTime = System.currentTimeMillis();
            //game.foodCollision(); // Final check for food at the destination tile
            if (game.foods.isEmpty() && game.powerPellets.isEmpty()) { // Win condition
                if (!game.gameLoop.isRunning()) {
                    return;
                }
                game.gameLoop.stop();
                game.nextLevel();
                return;
            }

        } else {
            System.out.println("Dash blocked or no space to dash.");
        }
        isDashing = false; // Reset dashing flag
        game.repaint(); // Ensure the screen updates after the dash
    }

    public long getDashCooldownRemaining() {
        long timeSinceLastDash = System.currentTimeMillis() - lastDashTime;
        if (timeSinceLastDash >= DASH_COOLDOWN_MS) {
            return 0;
        }
        return DASH_COOLDOWN_MS - timeSinceLastDash;
    }

    public boolean isDashActive() {
        return isDashing;
    }
}
