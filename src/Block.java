// Block.java

import java.awt.Image;

public class Block {

    Image texture;
    Image originalTexture; // To store normal texture when frightened

    int start_x;
    int start_y;
    int x_position; // Pixel position
    int y_position; // Pixel position
    int width; // Usually tileSize
    int height; // Usually tileSize

    int X_GridPosition; // Grid coordinate
    int Y_GridPosition; // Grid coordinate

    int start_X_GridPosition; // Grid coordinate for spawn/reset
    int start_Y_GridPosition; // Grid coordinate for spawn/reset

    boolean isMoving = false;
    Direction currentDirection = Direction.NONE;
    int targetPixelX;
    int targetPixelY;
    boolean isGhost = false;

    // New states for energizer effect
    boolean isFrightened = false;
    boolean isEaten = false;
    
    boolean isInPenWaiting = false;
    long penEntryTime = 0;


    public Block(Image texture, int startGridX, int startGridY, int width, int height) {
        this.texture = texture;
        this.originalTexture = texture; // Initially, original is the same
        this.X_GridPosition = startGridX;
        this.Y_GridPosition = startGridY;
        this.start_X_GridPosition = startGridX;
        this.start_Y_GridPosition = startGridY;
        this.x_position = startGridX * width;
        this.y_position = startGridY * height;
        this.start_x = this.x_position;
        this.start_y = this.y_position;
        this.width = width;
        this.height = height;
    }

    // Helper to reset ghost state after being eaten and returning to base
    public void resetFromEatenState() {
        this.isEaten = false;
        this.isFrightened = false; // Should also no longer be frightened
        this.texture = this.originalTexture;
        // Restore normal speed if it was changed (handled in PacmanGame)
    }
    
    public void finishPenWaitAndResetState() {
        this.isInPenWaiting = false; // No longer waiting
        this.isEaten = false;      // No longer considered "eaten" for movement/collision
        this.isFrightened = false; // Default to not frightened; PacmanGame will set if energizer is active
        // Texture will be set by PacmanGame (setGhostNormal or setGhostFrightened)
    }
    
    public void enterPenToWait(Image penTexture) { // penTexture could be ghostEatenTexture or a specific "in pen" look
        this.isEaten = false; // It's no longer "eaten and returning" but "waiting in pen"
        this.isFrightened = false;
        this.isInPenWaiting = true;
        this.isMoving = false; // Should be stationary in pen
        this.currentDirection = Direction.NONE; // No specific direction while waiting
        this.penEntryTime = System.currentTimeMillis();
        if (penTexture != null) { // Keep eyes texture or a specific "in pen" texture
            this.texture = penTexture;
        }
    }
}