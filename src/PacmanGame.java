
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import javax.management.StringValueExp;
import javax.swing.*;

public class PacmanGame extends JPanel implements ActionListener, KeyListener {

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    Timer gameLoop;
    
    App app;

    //Pacman Textures
    Image hearts;
    Image empty_hearts;
    Image pacmanRight; // Consider adding Left, Up, Down later
    Image pacmanLeft;
    Image pacmanUp;
    Image pacmanDown;

    Image blueGhost;
    Image redGhost;
    Image pinkGhost;
    Image orangeGhost;

    Font pixelFont;

    private Image wallSide;
    private Image wallTopBottom;
    private Image wallLDownLeft;
    private Image wallLDownRight;
    private Image wallLUpLeft;
    private Image wallLUpRight;
    private Image wallTUp;
    private Image wallTDown;

    int score = 0;
    int lives = 3;
    // Make move_speed an int if possible for simpler pixel math, or keep double if needed
    // Ensure tileSize is divisible by move_speed if int, or handle potential floating point issues
    int move_speed = 4; // Example: Changed to int, ensure tileSize (32) % move_speed (4) == 0
    int ghost_move_speed = 3;
    Random random = new Random();

    HashSet<Block> walls;
    HashSet<Block> foods; // Not used yet
    HashSet<Block> ghosts; // Not used yet
    Block pacman;

    private Font PixelFont;

    // Keep Level1 definition as is
    private String[] Level1 = {
        "XXXXXXXXXXXXXXXXXXX",
        "X      X   X      X",
        "X XX X X X X X XX X",
        "X    X   X   X    X",
        "X XX XXX X XXX XX X",
        "X X      X      X X",
        "X X XX XXXXX XX X X",
        "X                 X",
        "X XX X XXrXX X XX X",
        "X    X XbpoX X    X",
        "X XX X XXXXX X XX X",
        "X  X X       X X  X",
        "XX X X XXXXX X X XX",
        "O  X     X     X  O",
        "X XX XXX X XXX XX X",
        "X    X       X    X",
        "X X XX X X X XX X X",
        "X X    X P X    X X",
        "X XXXX XXXXX XXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX",};

    // The Block inner class represents the walls, ghosts, portals, and pacman
    class Block {

        Image texture;

        int start_x;
        int start_y;
        int x_position; // Pixel position
        int y_position; // Pixel position
        // int velocity; // Not used currently in Block
        int width; // Usually tileSize
        int height; // Usually tileSize

        int X_GridPosition; // Grid coordinate
        int Y_GridPosition; // Grid coordinate

        int start_X_GridPosition; // Grid coordinate
        int start_Y_GridPosition; // Grid coordinate

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

    public PacmanGame(App app) {
        this.app = app;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        addKeyListener(this);
        setFocusable(true);
        setBackground(Color.BLACK);

        // Font Loading (keep as is)
        try {
            InputStream fontStream = getClass().getResourceAsStream("./assets/game_font/PixelGame.otf");
            if (fontStream != null) {
                PixelFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
                fontStream.close();
            } else {
                System.err.println("Font file not found!");
                PixelFont = new Font("SansSerif", Font.PLAIN, 12);
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            PixelFont = new Font("SansSerif", Font.PLAIN, 12);
        }

        // Image Loading (keep as is)
        hearts = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/heart.gif")).getImage();
        empty_hearts = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/empty_heart.gif")).getImage();
        pacmanRight = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/RIGHT.gif")).getImage();
        pacmanLeft = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/LEFT.gif")).getImage();
        pacmanUp = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/UP.gif")).getImage();
        pacmanDown = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/DOWN.gif")).getImage();

        redGhost = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/redGhost.gif")).getImage();
        blueGhost = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/blueGhost.gif")).getImage();
        pinkGhost = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/pinkGhost.gif")).getImage();
        orangeGhost = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/orangeGhost.gif")).getImage();

        wallSide = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_Sides.png")).getImage();
        wallTopBottom = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_TopBottom.png")).getImage();
        wallLDownLeft = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_LDownLeft.png")).getImage();
        wallLDownRight = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_LDownRight.png")).getImage();
        wallLUpLeft = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_LUpLeft.png")).getImage();
        wallLUpRight = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_LUpRight.png")).getImage();
        wallTUp = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_TUp.png")).getImage();
        wallTDown = new ImageIcon(getClass().getResource("./assets/game_textures/walls/Wall_TDown.png")).getImage();

        loadMap(Level1); // Load the map and create blocks
        gameLoop = new Timer(16, this); // ~60 FPS (1000/16) - adjust if needed

    }

    public void startGame() {
        gameLoop.start();
    }

    // Load map - Creates Blocks based on Level1
    public void loadMap(String[] Map) {
        walls = new HashSet<Block>();
        // Initialize foods and ghosts if needed here
        foods = new HashSet<>();
        ghosts = new HashSet<Block>();

        int ghostSpeed = 3;

        for (int r = 0; r < rowCount; r++) {
            String row = Map[r];
            for (int c = 0; c < columnCount; c++) {
                char MapChar = row.charAt(c);

                // int x = c * tileSize; // Pixel position - calculate in Block constructor
                // int y = r * tileSize; // Pixel position - calculate in Block constructor
                if (MapChar == 'X') {
                    // Pass grid coordinates to constructor
                    //Block wall = new Block(null, c, r, tileSize, tileSize);
                    //walls.add(wall);

                    if (r == 0 && Map[r + 1].charAt(c) != 'X' || r == rowCount - 1 && Map[r - 1].charAt(c) != 'X'
                            || r > 0 && r < rowCount - 1 && Map[r - 1].charAt(c) != 'X' && Map[r + 1].charAt(c) != 'X') {
                        Block wall = new Block(wallTopBottom, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c == 0 && Map[r].charAt(c + 1) != 'X' || c == columnCount - 1 && Map[r].charAt(c - 1) != 'X'
                            || c > 0 && c < columnCount - 1 && Map[r].charAt(c + 1) != 'X' && Map[r].charAt(c - 1) != 'X') {
                        Block wall = new Block(wallSide, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && c < columnCount - 1 && Map[r].charAt(c - 1) == 'X' && Map[r].charAt(c + 1) == 'X' && Map[r + 1].charAt(c) == 'X') {
                        Block wall = new Block(wallTUp, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && c < columnCount - 1 && Map[r].charAt(c - 1) == 'X' && Map[r].charAt(c + 1) == 'X' && Map[r - 1].charAt(c) == 'X') {
                        Block wall = new Block(wallTDown, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && r < rowCount - 1 && Map[r].charAt(c - 1) == 'X' && Map[r + 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLDownLeft, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c < columnCount - 1 && r < rowCount - 1 && Map[r].charAt(c + 1) == 'X' && Map[r + 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLDownRight, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && Map[r].charAt(c - 1) == 'X' && Map[r - 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLUpLeft, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c < columnCount - 1 && Map[r].charAt(c + 1) == 'X' && Map[r - 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLUpRight, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else {
                        Block wall = new Block(null, c, r, tileSize, tileSize);
                        walls.add(wall);
                    }
                } else if (MapChar == 'P') {
                    // Pass grid coordinates to constructor
                    pacman = new Block(pacmanRight, c, r, tileSize, tileSize); // Assign texture
                } else if (MapChar == 'o') {
                    // Pass grid coordinates to constructor
                    Block ghost = new Block(orangeGhost, c, r, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (MapChar == 'p') {
                    // Pass grid coordinates to constructor
                    Block ghost = new Block(pinkGhost, c, r, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (MapChar == 'r') {
                    // Pass grid coordinates to constructor
                    Block ghost = new Block(redGhost, c, r, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (MapChar == 'b') {
                    // Pass grid coordinates to constructor
                    Block ghost = new Block(blueGhost, c, r, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (MapChar == ' '){
                    Block pellet = new Block(null, c, r, tileSize, tileSize);
                    foods.add(pellet);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g
    ) {
        super.paintComponent(g);
        drawBoard(g); // Use a separate method for drawing
    }
    // Separate drawing logic

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(new Color(253, 245, 185));
        for (Block food : foods) {
            g.fillRect(food.x_position+14, food.y_position+14, 5, 5);
        }

        // Draw Pacman
        if (pacman != null) {
            // Use pacman's texture (update texture based on direction later)
            g2d.drawImage(pacman.texture, pacman.x_position, pacman.y_position, tileSize, tileSize, this);
        }

        // Draw Walls (optional - could use textures later)
        g2d.setColor(Color.BLUE); // Make walls visible
        for (Block wall : walls) {
            g2d.drawImage(wall.texture, wall.x_position, wall.y_position, tileSize, tileSize, this);
        }

        for (Block ghost : ghosts) {
            g2d.drawImage(ghost.texture, ghost.x_position, ghost.y_position, tileSize, tileSize, this);
        }
        
        
        
        // Draw Score/Lives
        g2d.setFont(PixelFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + String.valueOf(score), boardWidth/2-60, boardHeight+32);
        
        g2d.drawString("Lives: ", 10, boardHeight+32);
        int hearts_gap = 60;
        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                g2d.drawImage(hearts, hearts_gap, boardHeight+13, (int)(23*1.5), (int) (17*1.5), this);
            } else {
                g2d.drawImage(empty_hearts, hearts_gap, boardHeight+13, (int)(23*1.5), (int) (17*1.5), this);
            }
            hearts_gap += 27;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    // --- KeyListener Methods ---
    @Override
    public void keyTyped(KeyEvent e) {
        /* Not used */ }

    @Override
    public void keyPressed(KeyEvent e) {
        handleKeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        /* Not used */ }

    // --- Game State ---
    private boolean isMoving = false;
    private Direction currentDirection = Direction.NONE;
    private Direction nextDirection = Direction.NONE; // Input buffer

    // --- Input Handling ---
    private void handleKeyPress(int keyCode) {
        // System.out.println(keyCode); // Optional debugging
        Direction desiredDirection = Direction.NONE;
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                desiredDirection = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                desiredDirection = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                desiredDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                desiredDirection = Direction.RIGHT;
                break;
            default:
                return; // Ignore other keys
        }

        // Store the desired direction
        nextDirection = desiredDirection;

        // If Pacman isn't currently moving between tiles,
        // or if the player wants to reverse direction, try to move immediately.
        if (!isMoving || nextDirection == getOpposite(currentDirection)) {
            attemptToStartMove();
        }
        // Otherwise, the nextDirection is buffered and will be checked
        // when the current tile move finishes.
    }

    private Direction getOpposite(Direction dir) {
        switch (dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return Direction.NONE;
        }
    }

    // --- Movement Logic ---
    // Tries to initiate a move based on 'nextDirection' or continue 'currentDirection'
    private void attemptToStartMove() {
        // Prioritize the buffered direction ('nextDirection')
        if (nextDirection != Direction.NONE && canMove(nextDirection)) {
            startMove(nextDirection);
        } // If buffered direction is blocked or none, try continuing current direction
        else if (currentDirection != Direction.NONE && canMove(currentDirection)) {
            // This case handles continuous movement when no new input is given
            // or the new input is blocked.
            startMove(currentDirection);
            // Reset nextDirection if it was blocked
            if (nextDirection != Direction.NONE && !canMove(nextDirection)) {
                nextDirection = Direction.NONE;
            }
        } else {
            // Cannot move in buffered or current direction, so stop.
            isMoving = false;
            currentDirection = Direction.NONE;
            // No valid move possible from buffer, clear it.
            nextDirection = Direction.NONE;
        }
    }

    /**
     * Checks if Pacman can move into the tile in the given direction. This now
     * includes checking for walls based on the Level1 map.
     *
     * @param direction The direction to check.
     * @return true if the move is valid (within bounds and not a wall), false
     * otherwise.
     */
    private boolean canMove(Direction direction) {
        if (pacman == null || direction == Direction.NONE) {
            return false; // Cannot move if pacman doesn't exist or direction is none
        }

        // Calculate the potential next grid position
        int targetGridX = pacman.X_GridPosition + direction.getDx();
        int targetGridY = pacman.Y_GridPosition + direction.getDy();

        // 1. Check Board Boundaries (using grid counts)
        if (targetGridX < 0 || targetGridX >= columnCount
                || targetGridY < 0 || targetGridY >= rowCount) {
            // Optional: Handle wrap-around tunnels here if needed later
            // For now, moving off the edge is blocked.
            // System.out.println("Boundary Block: " + targetGridX + "," + targetGridY); // Debug
            return false; // Out of bounds
        }

        // 2. Check for Walls at the target grid position using Level1 map
        try {
            char tileAtTarget = Level1[targetGridY].charAt(targetGridX);
            if (tileAtTarget == 'X') {
                // System.out.println("Wall Block at: " + targetGridX + "," + targetGridY); // Debug
                return false; // It's a wall, cannot move there
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Should be caught by boundary checks above, but good failsafe.
            System.err.println("Error checking map bounds: " + targetGridX + "," + targetGridY);
            return false;
        }

        // If not out of bounds and not a wall, the move is valid
        // System.out.println("Can Move to: " + targetGridX + "," + targetGridY); // Debug
        return true;
    }

    // Target pixel coordinates for the current move
    private int targetPixelX;
    private int targetPixelY;

    // Starts moving Pacman one tile in the specified direction
    private void startMove(Direction direction) {
        // Redundant check, but safe:
        if (!canMove(direction)) {
            isMoving = false; // Ensure state is correct if called erroneously
            currentDirection = Direction.NONE;
            return;
        }

        currentDirection = direction;
        // Don't reset nextDirection here immediately, allows buffering.
        // nextDirection = direction; // Removed: Let nextDirection persist from input

        isMoving = true;

        // Calculate target pixel coordinates based on the *target grid* cell
        targetPixelX = (pacman.X_GridPosition + currentDirection.getDx()) * tileSize;
        targetPixelY = (pacman.Y_GridPosition + currentDirection.getDy()) * tileSize;

        // TODO: Update pacman's texture based on direction
        // if (direction == Direction.LEFT) pacman.texture = pacmanLeft; else ...
        // Make sure to load other direction images first.
        switch (direction) {
            case Direction.UP:
                pacman.texture = pacmanUp;
                break;
            case Direction.DOWN:
                pacman.texture = pacmanDown;
                break;
            case Direction.LEFT:
                pacman.texture = pacmanLeft;
                break;
            case Direction.RIGHT:
                pacman.texture = pacmanRight;
                break;
        }
    }

    // Main game loop update logic
    private void updateGame() {
        if (isMoving) {
            movePlayer();
        } else {
            // If not moving, check if we *should* start moving based on buffered input
            // or continue in the current direction if the key is held (implicitly handled)
            attemptToStartMove();
        }

        // Add other game updates here later (ghost movement, pellet eating, etc.)
    }

    // Updates Pacman's pixel position during a move
    private void movePlayer() {
        if (!isMoving || currentDirection == Direction.NONE) {
            return; // Should not happen if logic is right, but safe check
        }
        boolean movedX = false;
        boolean movedY = false;

        // Move pixel position towards target X
        if (pacman.x_position < targetPixelX) {
            pacman.x_position += move_speed;
            if (pacman.x_position >= targetPixelX) { // Use >= to snap correctly
                pacman.x_position = targetPixelX; // Clamp/Snap
            }
            movedX = true;
        } else if (pacman.x_position > targetPixelX) {
            pacman.x_position -= move_speed;
            if (pacman.x_position <= targetPixelX) { // Use <= to snap correctly
                pacman.x_position = targetPixelX; // Clamp/Snap
            }
            movedX = true;
        }

        // Move pixel position towards target Y
        if (pacman.y_position < targetPixelY) {
            pacman.y_position += move_speed;
            if (pacman.y_position >= targetPixelY) { // Use >= to snap correctly
                pacman.y_position = targetPixelY; // Clamp/Snap
            }
            movedY = true;
        } else if (pacman.y_position > targetPixelY) {
            pacman.y_position -= move_speed;
            if (pacman.y_position <= targetPixelY) { // Use <= to snap correctly
                pacman.y_position = targetPixelY; // Clamp/Snap
            }
            movedY = true;
        }

        // Check if target pixel reached
        if (pacman.x_position == targetPixelX && pacman.y_position == targetPixelY) {
            // Update logical grid position *after* reaching the destination pixel
            pacman.X_GridPosition += currentDirection.getDx();
            pacman.Y_GridPosition += currentDirection.getDy();

            // Snap pixel position exactly to the new grid position (redundant but safe)
            // pacman.x_position = pacman.X_GridPosition * tileSize;
            // pacman.y_position = pacman.Y_GridPosition * tileSize;
            // Finished this tile move
            isMoving = false;
            ghostCollision(nextDirection);
            foodCollision(nextDirection);

            // The updateGame loop will call attemptToStartMove() next frame
            // to check the buffered 'nextDirection' or continue 'currentDirection'.
            // No need to call it directly here.
            // Clear currentDirection IF the next move isn't simply continuing.
            // attemptToStartMove will set the new currentDirection if a valid move exists.
            // This prevents getting stuck if the *next* move is blocked.
            // Let attemptToStartMove handle the next direction logic fully.
            // currentDirection = Direction.NONE; // Removed: Let attemptToStartMove manage this transition
        }
    }

    // Method to give focus to the panel (usually called from App.java)
    public void requestPanelFocus() {
        requestFocusInWindow();
    }

    private void ghostCollision(Direction direction) {
        // Calculate the potential next grid position
        int targetGridX = pacman.X_GridPosition + direction.getDx();
        int targetGridY = pacman.Y_GridPosition + direction.getDy();

        // 2. Check for Walls at the target grid position using Level1 map
        try {
            char tileAtTarget = Level1[targetGridY].charAt(targetGridX);
            if (tileAtTarget == 'b' || tileAtTarget == 'p' || tileAtTarget == 'r' || tileAtTarget == 'o') {
                System.out.println("Ghost Collision");
                resetGame();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Should be caught by boundary checks above, but good failsafe.
            System.err.println("Error checking map bounds: " + targetGridX + "," + targetGridY);
        }
    }
    
    private void foodCollision(Direction direction) {
        // Calculate the potential next grid position
        int targetGridX = pacman.X_GridPosition;
        int targetGridY = pacman.Y_GridPosition;
        
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
        
        // Move to next level
        if (foods.isEmpty()){
            gameLoop.stop();
        }
    }
    
    public boolean collision(Block a, Block b) {
        return a.x_position < b.x_position + b.width
                && a.x_position + a.width > b.x_position
                && a.y_position < b.y_position + b.height
                && a.y_position + a.height > b.y_position;
    }

    private void resetGame() {
        pacman.x_position = pacman.start_x;
        pacman.y_position = pacman.start_y;
        pacman.X_GridPosition = pacman.start_X_GridPosition;
        pacman.Y_GridPosition = pacman.start_Y_GridPosition;

        currentDirection = Direction.NONE;
        nextDirection = Direction.NONE;

        lives -= 1;
        if (lives == 0) {
            gameOver();
        }

    }
    
    // Game Over Mechanic
    private void gameOver() {
        app.cardLayout.show(app.MainPanel, "instructions");
    }
}
