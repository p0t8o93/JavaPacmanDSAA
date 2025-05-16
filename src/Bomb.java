
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

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
    boolean sound;

    enum BombState {
        PLACED, EXPLODING, DONE
    }
    BombState state = BombState.PLACED;
    long explosionStartTime;

    Image bombTexture;
    Image explosionTexture;
    
    private HashSet<Point> affectedCells = new HashSet<>();
    private boolean explosionCalculated = false;
    private final int EXPLOSION_RANGE = 4;

    public Bomb(int gridX, int gridY, int tileSize, Color color, boolean sound) {
        this.X_GridPosition = gridX;
        this.Y_GridPosition = gridY;
        this.tileSize = tileSize;
        this.x_position_pixel = gridX * tileSize;
        this.y_position_pixel = gridY * tileSize;
        this.color = color; // Color for PLACED state (e.g., Yellow)
        this.placedTime = System.currentTimeMillis();
        this.sound= sound;

        try {
            this.bombTexture = new ImageIcon(getClass().getResource("/assets/game_textures/bomb/bomb.gif")).getImage();
            this.explosionTexture = new ImageIcon(getClass().getResource("/assets/game_textures/bomb/explosion.GIF")).getImage();
        } catch (NullPointerException e) {
            System.err.println("Error loading bomb textures! Check paths.");
            // Assign placeholder colors/shapes if loading fails
            this.bombTexture = null; // Or create a placeholder image
            this.explosionTexture = null;
        }
    }

    public void update() {
        if (state == BombState.PLACED) {
            if (System.currentTimeMillis() - placedTime > fuseTime) {
                state = BombState.EXPLODING;
                explosionStartTime = System.currentTimeMillis();
                explosionCalculated = false; // Reset calculation flag
                affectedCells.clear();      // Clear previous cells if any
                // Note: Calculation now happens lazily in draw/getAffectedCells
            }
        } else if (state == BombState.EXPLODING) {
            if (System.currentTimeMillis() - explosionStartTime > explosionDuration) {
                orangeP("assets/game_sounds/bomb.wav");
                state = BombState.DONE;
            }
        }
    }

    public void draw(Graphics2D g2d, PacmanGame game) { // PacmanGame needed for isWallAtGrid and dimensions
        if (state == BombState.PLACED) {
            if (bombTexture != null) {
                g2d.drawImage(bombTexture, x_position_pixel, y_position_pixel, tileSize, tileSize, game);
            } else {
                // Fallback drawing if texture failed
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(x_position_pixel + 2, y_position_pixel + 2, tileSize - 4, tileSize - 4);
            }
        } else if (state == BombState.EXPLODING) {
            // Calculate cells if not already done
            if (!explosionCalculated) {
                calculateAffectedCells(game);
            }

            // Draw explosion texture on all affected cells
            for (Point cell : affectedCells) {
                 if (explosionTexture != null) {
                     g2d.drawImage(explosionTexture, cell.x * game.tileSize, cell.y * game.tileSize, game.tileSize, game.tileSize, game);
                 } else {
                     // Fallback drawing if texture failed
                     g2d.setColor(Color.ORANGE);
                     g2d.fillRect(cell.x * game.tileSize, cell.y * game.tileSize, game.tileSize, game.tileSize);
                 }
            }
        }
        // No drawing needed for DONE state
    }
    
    private void calculateAffectedCells(PacmanGame game) {
        affectedCells.clear(); // Start fresh

        // 1. Add Center Tile (if not a wall itself, though unlikely for bomb placement)
        if (!game.isWallAtGrid(X_GridPosition, Y_GridPosition)) {
            affectedCells.add(new Point(X_GridPosition, Y_GridPosition));
        } else {
            return; // Should not explode if placed inside a wall
        }


        // 2. Expand outwards in each direction
        // UP
        for (int i = 1; i <= EXPLOSION_RANGE; i++) {
            int ex = X_GridPosition;
            int ey = Y_GridPosition - i;
            if (ex < 0 || ex >= game.columnCount || ey < 0 || ey >= game.rowCount) break; // Out of bounds
            if (game.isWallAtGrid(ex, ey)) break; // Stop at wall
            affectedCells.add(new Point(ex, ey));
        }
        // DOWN
        for (int i = 1; i <= EXPLOSION_RANGE; i++) {
            int ex = X_GridPosition;
            int ey = Y_GridPosition + i;
            if (ex < 0 || ex >= game.columnCount || ey < 0 || ey >= game.rowCount) break; // Out of bounds
            if (game.isWallAtGrid(ex, ey)) break; // Stop at wall
            affectedCells.add(new Point(ex, ey));
        }
        // LEFT
        for (int i = 1; i <= EXPLOSION_RANGE; i++) {
            int ex = X_GridPosition - i;
            int ey = Y_GridPosition;
            if (ex < 0 || ex >= game.columnCount || ey < 0 || ey >= game.rowCount) break; // Out of bounds
            if (game.isWallAtGrid(ex, ey)) break; // Stop at wall
            affectedCells.add(new Point(ex, ey));
        }
        // RIGHT
        for (int i = 1; i <= EXPLOSION_RANGE; i++) {
            int ex = X_GridPosition + i;
            int ey = Y_GridPosition;
             if (ex < 0 || ex >= game.columnCount || ey < 0 || ey >= game.rowCount) break; // Out of bounds
            if (game.isWallAtGrid(ex, ey)) break; // Stop at wall
            affectedCells.add(new Point(ex, ey));
        }

        explosionCalculated = true;
         // System.out.println("Calculated explosion cells: " + affectedCells.size());
    }

    // Helper for collision detection against Pacman
    public Set<Point> getAffectedCells(PacmanGame game) {
        // Ensure calculation happens if needed (e.g., if accessed before first draw)
        if (state == BombState.EXPLODING && !explosionCalculated) {
             calculateAffectedCells(game);
        }
        // Return a copy to prevent external modification? Or the direct set?
        // Returning direct set is fine if PacmanGame doesn't modify it.
        return affectedCells;
    }


    // Helper for collision detection against Pacman (kept for consistency, but use getAffectedCells now)
    // This specific method might become less useful if PacmanGame uses getAffectedCells directly.
    public Rectangle getExplosionCellBounds(int gridX, int gridY) {
        return new Rectangle(gridX * tileSize, gridY * tileSize, tileSize, tileSize);
    }

     // --- NEW: Check if a specific grid point is part of this bomb's explosion ---
     // Needs recalculation if called before draw/getAffectedCells triggered it
     public boolean isCellInExplosion(int gridX, int gridY, PacmanGame game) {
         if (state != BombState.EXPLODING) {
             return false;
         }
         // Ensure calculation has run
         if (!explosionCalculated) {
             calculateAffectedCells(game);
         }
         return affectedCells.contains(new Point(gridX, gridY));
     }
    
    
    private Clip orangeP;

    public void orangeP(String relativePath) {
        try {
            if (sound == false) {
            return;
        }
            if (orangeP != null && orangeP.isRunning()) {
                return; // Avoid restarting
            }
            URL soundURL = getClass().getClassLoader().getResource(relativePath);
            if (soundURL == null) {
                System.out.println("Could not find file: " + relativePath);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            orangeP = AudioSystem.getClip();
            orangeP.open(audioInput);
            orangeP.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
