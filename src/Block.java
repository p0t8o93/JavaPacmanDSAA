
import java.awt.Image;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author potato
 */
public class Block {

    Image texture;

    int start_x;
    int start_y;
    int x_position; // Pixel position
    int y_position; // Pixel position
    int width; // Usually tileSize
    int height; // Usually tileSize

    int X_GridPosition; // Grid coordinate
    int Y_GridPosition; // Grid coordinate

    int start_X_GridPosition; // Grid coordinate
    int start_Y_GridPosition; // Grid coordinate

    // --- Add Ghost/Movement Specific State ---
    boolean isMoving = false;
    Direction currentDirection = Direction.NONE;
    int targetPixelX;
    int targetPixelY;
    boolean isGhost = false; // Flag to easily identify ghosts

    // Constructor updated for clarity
    public Block(Image texture, int startGridX, int startGridY, int width, int height) {
        this.texture = texture;
        this.X_GridPosition = startGridX;
        this.Y_GridPosition = startGridY;
        this.start_X_GridPosition = startGridX;
        this.start_Y_GridPosition = startGridY;
        this.x_position = startGridX * width; // Calculate pixel position from grid
        this.y_position = startGridY * height; // Calculate pixel position from grid
        this.start_x = this.x_position;
        this.start_y = this.y_position;
        this.width = width;   // Store width
        this.height = height; // Store height
    }
}
