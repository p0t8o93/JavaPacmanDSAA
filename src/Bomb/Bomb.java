
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
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

    public Bomb(int gridX, int gridY, int tileSize, Color color, boolean sound) {
        this.X_GridPosition = gridX;
        this.Y_GridPosition = gridY;
        this.tileSize = tileSize;
        this.x_position_pixel = gridX * tileSize;
        this.y_position_pixel = gridY * tileSize;
        this.color = color; // Color for PLACED state (e.g., Yellow)
        this.placedTime = System.currentTimeMillis();
        this.sound= sound;

        this.bombTexture = new ImageIcon(getClass().getResource("./assets/game_textures/bomb/bomb.GIF")).getImage();
        this.explosionTexture = new ImageIcon(getClass().getResource("./assets/game_textures/bomb/explosion.gif")).getImage();
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
                orangeP("assets/game_sounds/bomb.wav");
                state = BombState.DONE;

                // System.out.println("Bomb at " + X_GridPosition + "," + Y_GridPosition + " done exploding.");
            }
        }
    }

    public void draw(Graphics2D g2d, PacmanGame game) { // PacmanGame needed for isWallAtGrid
        if (state == BombState.PLACED) {

            g2d.drawImage(bombTexture, x_position_pixel, y_position_pixel, tileSize, tileSize, game);
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
                        //g2d.fillRect(ex * game.tileSize, ey * game.tileSize, game.tileSize, game.tileSize);
                        g2d.drawImage(explosionTexture, ex * game.tileSize, ey * game.tileSize, game.tileSize, game.tileSize, game);
                    }
                }
            }
        }
    }

    // Helper for collision detection against Pacman
    public Rectangle getExplosionCellBounds(int gridX, int gridY) {
        return new Rectangle(gridX * tileSize, gridY * tileSize, tileSize, tileSize);
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
