
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList; // Import ArrayList
import java.util.HashSet;
import java.util.List; // Import List
import java.util.Random;
import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class PacmanGame extends JPanel implements ActionListener, KeyListener {

    // ... (Keep existing variables: rowCount, columnCount, etc.) ...
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    Timer gameLoop;

    int level = 1;

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
    GhostPowers ghostPowers;

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
    int move_speed = 4;
    int ghost_move_speed = 2; // Give ghosts a slightly different speed if desired
    Random random = new Random();

    HashSet<Block> walls;
    HashSet<Block> foods;
    public HashSet<Block> ghosts;
    Block pacman;

    private Font PixelFont; // Renamed to follow convention

    // Levels (keep as is)
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
        "X  X     X     X  X",
        "X XX XXX X XXX XX X",
        "X    X       X    X",
        "X X XX X-X X XX X X",
        "X X    X P X    X X",
        "X XXXX XXXXX XXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX",};

    private String[] Level2 = {
        "XXXXXXXXXXXXXXXXXXX",
        "X                 X",
        "X X XX XXXXX XX X X",
        "X X  X   X   X  X X",
        "X XX X X X X X XX X",
        "X      X   X      X",
        "X XX X XXXXX X XX X",
        "X XX X       X XX X",
        "X XX X XXrXX X XX X",
        "       XbpoX      X",
        "X X XX XXXXX XX X X",
        "X X             X X",
        "X XX X XXXXX X XX X",
        "X X  X  -X   X  X X",
        "X X XX X X X XX X X",
        "X    X X P X X    X",
        "X XX X X X X X XX X",
        "X X      X      X X",
        "X X XX XXXXX XX X X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX",};

    private String[] Level3 = {
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
    

    public PacmanGame(App app) {
        this.app = app;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        addKeyListener(this);
        setFocusable(true);
        setBackground(Color.BLACK);

        // Font Loading (keep as is)
        // ... (font loading code) ...
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
        // ... (image loading code) ...
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

        loadLevel(Level1); // Load the map and create blocks
        gameLoop = new Timer(16, this); // ~60 FPS

    }

    public void startGame() {
        resetGamePositions(); // Ensure things start correctly
        gameLoop.start();
    }

    // Renamed for clarity, selects level array
    public void loadLevel(String[] mapData) {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        pacman = null; // Reset pacman before loading
        
        for (int r = 0; r < rowCount; r++) {
            String row = mapData[r];
            for (int c = 0; c < columnCount; c++) {
                char mapChar = row.charAt(c);

                if (mapChar == 'X') {
                    // Simplified wall creation for now, add textures back if needed
                    // Pass grid coordinates to constructor
                    //Block wall = new Block(null, c, r, tileSize, tileSize);
                    //walls.add(wall);

                    if (r == 0 && mapData[r + 1].charAt(c) != 'X' || r == rowCount - 1 && mapData[r - 1].charAt(c) != 'X'
                            || r > 0 && r < rowCount - 1 && mapData[r - 1].charAt(c) != 'X' && mapData[r + 1].charAt(c) != 'X') {
                        Block wall = new Block(wallTopBottom, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c == 0 && mapData[r].charAt(c + 1) != 'X' || c == columnCount - 1 && mapData[r].charAt(c - 1) != 'X'
                            || c > 0 && c < columnCount - 1 && mapData[r].charAt(c + 1) != 'X' && mapData[r].charAt(c - 1) != 'X') {
                        Block wall = new Block(wallSide, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && c < columnCount - 1 && mapData[r].charAt(c - 1) == 'X' && mapData[r].charAt(c + 1) == 'X' && mapData[r + 1].charAt(c) == 'X') {
                        Block wall = new Block(wallTUp, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && c < columnCount - 1 && mapData[r].charAt(c - 1) == 'X' && mapData[r].charAt(c + 1) == 'X' && mapData[r - 1].charAt(c) == 'X') {
                        Block wall = new Block(wallTDown, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && r < rowCount - 1 && mapData[r].charAt(c - 1) == 'X' && mapData[r + 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLDownLeft, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c < columnCount - 1 && r < rowCount - 1 && mapData[r].charAt(c + 1) == 'X' && mapData[r + 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLDownRight, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c > 0 && mapData[r].charAt(c - 1) == 'X' && mapData[r - 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLUpLeft, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else if (c < columnCount - 1 && mapData[r].charAt(c + 1) == 'X' && mapData[r - 1].charAt(c) == 'X') {
                        Block wall = new Block(wallLUpRight, c, r, tileSize, tileSize);
                        walls.add(wall);
                    } else {
                        Block wall = new Block(null, c, r, tileSize, tileSize); // Default/Inner wall
                        walls.add(wall);
                    }
                } else if (mapChar == 'P') {
                    pacman = new Block(pacmanRight, c, r, tileSize, tileSize);
                } else if (mapChar == 'o') {
                    Block ghost = new Block(orangeGhost, c, r, tileSize, tileSize);
                    ghost.isGhost = true; // Mark as ghost
                    ghosts.add(ghost);
                } else if (mapChar == 'p') {
                    Block ghost = new Block(pinkGhost, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                } else if (mapChar == 'r') {
                    Block ghost = new Block(redGhost, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                } else if (mapChar == 'b') {
                    Block ghost = new Block(blueGhost, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                } else if (mapChar == '.' || mapChar == '-') { // Handle both food chars
                    Block pellet = new Block(null, c, r, tileSize, tileSize);
                    foods.add(pellet);
                }
                // Ignore empty spaces ' '
            }
        }
        if (pacman == null) {
            System.err.println("Warning: Pacman start position 'P' not found in map!");
            // Optionally place Pacman at a default location or throw error
            pacman = new Block(pacmanRight, 1, 1, tileSize, tileSize); // Default fallback
        }
        ghostPowers = new GhostPowers(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g); // Use a separate method for drawing
    }

    // Separate drawing logic
    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw Food Pellets
        g.setColor(new Color(253, 245, 185));
        for (Block food : foods) {
            // Draw smaller dots centered in the tile
            g.fillOval(food.x_position + tileSize / 2 - 3, food.y_position + tileSize / 2 - 3, 6, 6);
        }

        // Draw Walls
        // g2d.setColor(Color.BLUE); // Keep blue for debugging if needed
        for (Block wall : walls) {
            if (wall.texture != null) {
                g2d.drawImage(wall.texture, wall.x_position, wall.y_position, tileSize, tileSize, this);
            } else {
                // Optionally draw a default color for walls without textures
                // g2d.fillRect(wall.x_position, wall.y_position, tileSize, tileSize);
            }
        }

        // Draw Ghosts
        for (Block ghost : ghosts) {
            if (ghost.texture != null) {
                g2d.drawImage(ghost.texture, ghost.x_position, ghost.y_position, tileSize, tileSize, this);
            }
        }

        // Draw Pacman (draw last so he's on top)
        if (pacman != null && pacman.texture != null) {
            g2d.drawImage(pacman.texture, pacman.x_position, pacman.y_position, tileSize, tileSize, this);
        }

        // Draw Score/Lives (keep as is)
        // ... (score/lives drawing code) ...
        g2d.setFont(PixelFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + String.valueOf(score), boardWidth / 2 - 60, boardHeight + 32);

        g2d.drawString("Lives: ", 10, boardHeight + 32);
        int hearts_gap = 60;
        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                g2d.drawImage(hearts, hearts_gap, boardHeight + 13, (int) (23 * 1.5), (int) (17 * 1.5), this);
            } else {
                g2d.drawImage(empty_hearts, hearts_gap, boardHeight + 13, (int) (23 * 1.5), (int) (17 * 1.5), this);
            }
            hearts_gap += 27;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        checkCollisions(); // Check collisions after updates
        repaint();
    }

    // --- KeyListener Methods (keep as is) ---
    // ... (keyTyped, keyPressed, keyReleased) ...
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

    // --- Game State (keep as is) ---
    private Direction pacmanNextDirection = Direction.NONE; // Input buffer

    // --- Input Handling (keep as is, but update state var names) ---
    private void handleKeyPress(int keyCode) {
        if (pacman == null) {
            return; // Safety check
        }
        Direction desiredDirection = Direction.NONE;
        switch (keyCode) {
            // ... (key switch cases remain the same) ...
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

        // Store the desired direction in the buffer
        pacmanNextDirection = desiredDirection;

        // If Pacman isn't currently moving between tiles,
        // or if the player wants to reverse direction immediately, try to move.
        // Use pacman's internal state now.
        if (!pacman.isMoving || pacmanNextDirection == getOpposite(pacman.currentDirection)) {
            attemptToStartPacmanMove();
        }
        // Otherwise, the nextDirection is buffered and will be checked
        // when the current tile move finishes in updateGame.
    }

    private Direction getOpposite(Direction dir) { // Keep this helper
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

    // --- Pacman Movement Logic (Rename methods/vars for clarity) ---
    private void attemptToStartPacmanMove() {
        if (pacman == null) {
            return; // Safety check
        }
        // Prioritize the buffered direction ('pacmanNextDirection')
        if (pacmanNextDirection != Direction.NONE && canMove(pacman, pacmanNextDirection)) {
            startPacmanMove(pacmanNextDirection);
            // Clear the buffer once the move starts based on it
            pacmanNextDirection = Direction.NONE;
        } // If buffered direction is blocked or none, try continuing current direction (if any)
        else if (pacman.currentDirection != Direction.NONE && canMove(pacman, pacman.currentDirection)) {
            // Only start if not already moving in this direction (avoids redundant calls)
            if (!pacman.isMoving) {
                startPacmanMove(pacman.currentDirection);
            }
            // Reset nextDirection if it was specifically blocked
            if (pacmanNextDirection != Direction.NONE && !canMove(pacman, pacmanNextDirection)) {
                pacmanNextDirection = Direction.NONE;
            }
        } else {
            // Cannot move in buffered or current direction.
            // Pacman might be stationary but facing a direction. Don't change state here.
            // If an invalid direction was buffered, clear it.
            if (pacmanNextDirection != Direction.NONE && !canMove(pacman, pacmanNextDirection)) {
                pacmanNextDirection = Direction.NONE;
            }
            // Ensure pacman is marked as stopped if he wasn't moving.
            // If he was moving, the arrival logic in updateGame will handle stopping him.
            // pacman.isMoving = false; // This might be set incorrectly if called while moving but blocked
        }
    }

    /**
     * Checks if a Block (Pacman or Ghost) can move into the tile in the given
     * direction. Uses the current level's map data.
     */
    private boolean canMove(Block block, Direction direction) {
        if (block == null || direction == Direction.NONE) {
            return false;
        }

        int targetGridX = block.X_GridPosition + direction.getDx();
        int targetGridY = block.Y_GridPosition + direction.getDy();

        // 1. Check Board Boundaries
        if (targetGridX < 0 || targetGridX >= columnCount
                || targetGridY < 0 || targetGridY >= rowCount) {
            // Handle wrap-around tunnels here later if needed
            return false;
        }

        // 2. Check for Walls at the target grid position using the appropriate Level map
        String[] currentLevelMap = getCurrentLevelMap(); // Helper to get current map
        if (currentLevelMap == null) {
            return false; // Safety check
        }
        try {
            // Allow ghosts to move through the ghost house door ('-') maybe? Not implemented here.
            char tileAtTarget = currentLevelMap[targetGridY].charAt(targetGridX);
            if (tileAtTarget == 'X') {
                return false; // It's a wall
            }
            // Ghosts might have special rules about moving into certain squares (like the ghost house)
            // Add those rules here if needed for specific ghost AI later.
            // For now, random ghosts just avoid 'X' walls.

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error checking map bounds: " + targetGridX + "," + targetGridY);
            return false;
        }

        return true; // Not out of bounds and not a wall
    }

    // Helper to get the current level map array
    private String[] getCurrentLevelMap() {
        switch (level) {
            case 1:
                return Level1;
            case 2:
                return Level2;
            case 3:
                return Level3;
            default:
                return null; // Or handle error/default case
        }
    }

    private void startPacmanMove(Direction direction) {
        // No need to check canMove again if attemptToStartPacmanMove already did,
        // but it's a safe redundancy.
        if (pacman == null || !canMove(pacman, direction)) {
            // Optional: Clear buffer if the intended move failed?
            // if (direction == pacmanNextDirection) pacmanNextDirection = Direction.NONE;
            return; // Don't start the move
        }

        // Set Pacman's internal state
        pacman.currentDirection = direction;
        pacman.isMoving = true; // <-- CRITICAL FIX: Set the block's moving flag

        // Calculate target pixel coordinates based on the *target grid* cell
        pacman.targetPixelX = (pacman.X_GridPosition + pacman.currentDirection.getDx()) * tileSize;
        pacman.targetPixelY = (pacman.Y_GridPosition + pacman.currentDirection.getDy()) * tileSize;

        // Update pacman's texture based on direction
        switch (direction) {
            case UP:
                pacman.texture = pacmanUp;
                break;
            case DOWN:
                pacman.texture = pacmanDown;
                break;
            case LEFT:
                pacman.texture = pacmanLeft;
                break;
            case RIGHT:
                pacman.texture = pacmanRight;
                break;
            default:
                break; // Should not happen
        }
    }

    // --- Main Update Logic ---
    private void updateGame() {
        if (pacman == null) {
            return; // Safety check
        }
        // --- Update Pacman ---
        if (pacman.isMoving) { // Check pacman's internal state
            moveBlock(pacman, move_speed); // Use generic moveBlock
        } else {
            // Pacman is stationary at a tile center.
            // Check for food *first* before attempting the next move.
            foodCollision(); // Eat food on the tile Pacman is currently on
            if (foods.isEmpty()) { // Check win condition after eating
                if (!gameLoop.isRunning()) {
                    return;
                }
                gameLoop.stop();
                nextLevel();
                return; // Exit update after level change attempt
            }
            // Now, attempt to start the next move based on buffer or continue intention
            attemptToStartPacmanMove();
        }

        // Check if Pacman *finished* moving to a tile in this frame
        // This must be checked AFTER moveBlock might have been called
        if (pacman.isMoving && pacman.x_position == pacman.targetPixelX && pacman.y_position == pacman.targetPixelY) {
            // Pacman arrived at the center of the target tile
            pacman.isMoving = false; // Stop moving for now
            // Update grid position *after* arriving
            pacman.X_GridPosition += pacman.currentDirection.getDx();
            pacman.Y_GridPosition += pacman.currentDirection.getDy();

            // Snap pixel position exactly to the new grid center (good practice)
            pacman.x_position = pacman.X_GridPosition * tileSize;
            pacman.y_position = pacman.Y_GridPosition * tileSize;

            // Check for food again *after* arriving at the new tile center
            foodCollision();
            if (foods.isEmpty()) { // Check win condition again
                if (!gameLoop.isRunning()) {
                    return;
                }
                gameLoop.stop();
                nextLevel();
                return;
            }

            // Immediately attempt the next move based on buffer/current direction
            // This allows for continuous movement if the path is clear or turning if buffered
            attemptToStartPacmanMove();
        }

        // --- Update Ghosts (remains the same) ---
        for (Block ghost : ghosts) {
            if (ghost.isMoving) {
                moveBlock(ghost, ghost_move_speed);
            } else {
                attemptToStartGhostMove(ghost);
            }

            if (ghost.isMoving && ghost.x_position == ghost.targetPixelX && ghost.y_position == ghost.targetPixelY) {
                ghost.isMoving = false;
                ghost.X_GridPosition += ghost.currentDirection.getDx();
                ghost.Y_GridPosition += ghost.currentDirection.getDy();
                // Snap position (optional but good)
                ghost.x_position = ghost.X_GridPosition * tileSize;
                ghost.y_position = ghost.Y_GridPosition * tileSize;

                attemptToStartGhostMove(ghost);
            }
        }
    }

    /**
     * Generic function to move a block (Pacman or Ghost) towards its target
     */
    private void moveBlock(Block block, int speed) {
        if (!block.isMoving || block.currentDirection == Direction.NONE) {
            return;
        }

        // Move pixel position towards target X
        if (block.x_position < block.targetPixelX) {
            block.x_position += speed;
            if (block.x_position >= block.targetPixelX) {
                block.x_position = block.targetPixelX; // Clamp/Snap
            }
        } else if (block.x_position > block.targetPixelX) {
            block.x_position -= speed;
            if (block.x_position <= block.targetPixelX) {
                block.x_position = block.targetPixelX; // Clamp/Snap
            }
        }

        // Move pixel position towards target Y
        if (block.y_position < block.targetPixelY) {
            block.y_position += speed;
            if (block.y_position >= block.targetPixelY) {
                block.y_position = block.targetPixelY; // Clamp/Snap
            }
        } else if (block.y_position > block.targetPixelY) {
            block.y_position -= speed;
            if (block.y_position <= block.targetPixelY) {
                block.y_position = block.targetPixelY; // Clamp/Snap
            }
        }

        // NOTE: Checking for arrival and updating grid position is now done
        // AFTER the moveBlock call in the main updateGame loop.
    }

    // --- Ghost Movement Logic ---
    private void attemptToStartGhostMove(Block ghost) {
        if (ghost.isMoving) {
            return; // Already moving
        }
        List<Direction> possibleDirections = getValidGhostDirections(ghost);
        Direction preferredDirection = Direction.NONE;

        if (!possibleDirections.isEmpty()) {
            // Select a random direction from the valid ones
            int randomIndex = random.nextInt(possibleDirections.size());
            preferredDirection = possibleDirections.get(randomIndex);
        } else {
            // Should only happen if trapped, maybe try reversing?
            Direction opposite = getOpposite(ghost.currentDirection);
            if (opposite != Direction.NONE && canMove(ghost, opposite)) {
                preferredDirection = opposite;
            }
        }

        if (preferredDirection != Direction.NONE) {
            startGhostMove(ghost, preferredDirection);
        } else {
            // Ghost is stuck, keep its current state (facing direction)
            ghost.isMoving = false; // Ensure it's marked as not moving
        }
    }

    private List<Direction> getValidGhostDirections(Block ghost) {
        List<Direction> validDirections = new ArrayList<>();
        Direction oppositeDirection = getOpposite(ghost.currentDirection);

        for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}) {
            // Basic AI: Don't allow immediate reversal unless it's the only option
            if (dir == oppositeDirection && countValidDirections(ghost) > 1) { // Check if more than one way to go
                continue; // Skip reversing if other paths exist
            }
            if (canMove(ghost, dir)) {
                validDirections.add(dir);
            }
        }
        return validDirections;
    }

    // Helper to count valid moves (excluding walls) from a ghost's position
    private int countValidDirections(Block ghost) {
        int count = 0;
        for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}) {
            if (canMove(ghost, dir)) {
                count++;
            }
        }
        return count;
    }

    private void startGhostMove(Block ghost, Direction direction) {
        // Redundant check, but safe:
        if (!canMove(ghost, direction)) {
            ghost.isMoving = false;
            return;
        }

        ghost.currentDirection = direction;
        ghost.isMoving = true;

        // Calculate target pixel coordinates
        ghost.targetPixelX = (ghost.X_GridPosition + direction.getDx()) * tileSize;
        ghost.targetPixelY = (ghost.Y_GridPosition + direction.getDy()) * tileSize;

        // Optional: Update ghost texture based on direction if you have sprites for it
    }

    // --- Collision Detection ---
    // Check collisions after movements are processed
    private void checkCollisions() {
        if (pacman == null) {
            return;
        }

        // Pacman vs Ghost Collision
        for (Block ghost : ghosts) {
            // Use a slightly smaller bounding box for collision to feel more forgiving
            if (collision(pacman, ghost)) {
                System.out.println("Ghost Collision!");
                handlePlayerCaught();
                return; // Only handle one collision per frame
            }
        }
        // Food collision is checked within pacman's movement logic when he enters a tile
        // foodCollision(); // Moved into updateGame at tile arrival
    }

    // Renamed from ghostCollision to be more general
    private void handlePlayerCaught() {
        lives--;
        System.out.println("Lives left: " + lives);
        if (lives <= 0) {
            gameOver();
        } else {
            resetGamePositions(); // Reset Pacman and Ghosts to start positions

            // Explicitly reset pacman's movement state after position reset
            if (pacman != null) {
                pacman.isMoving = false;
                pacman.currentDirection = Direction.RIGHT; // Or NONE, or keep last direction? Resetting is safer.
                pacman.texture = pacmanRight; // Ensure texture matches state
            }
            pacmanNextDirection = Direction.NONE; // Clear input buffer

            // Reset ghost states too (already done in resetGamePositions, but explicit here is ok)
            for (Block ghost : ghosts) {
                ghost.isMoving = false;
                ghost.currentDirection = Direction.NONE;
            }
        }
    }

    // Renamed from foodCollision - check if Pacman is on a food tile
    private void foodCollision() {
        if (pacman == null) {
            return;
        }

        Block foodEaten = null;
        for (Block food : foods) {
            // Check if Pacman's center is within the food tile
            if (pacman.X_GridPosition == food.X_GridPosition && pacman.Y_GridPosition == food.Y_GridPosition) {
                foodEaten = food;
                break; // Eat one food per check
            }
        }

        if (foodEaten != null) {
            foods.remove(foodEaten);
            score += 10;
            System.out.println("Score: " + score); // Debugging
            // Win condition check moved to updateGame after food check
        }
    }

    public void nextLevel() {
        if (!gameLoop.isRunning()) { // Prevent multiple dialogs if already stopped
            // If called externally while stopped, decide behavior (e.g., just load next level data)
            System.out.println("Attempting level advance while stopped.");
            // return; // Or proceed to load map data below
        } else {
            gameLoop.stop(); // Ensure timer is stopped
        }

        int response = JOptionPane.showConfirmDialog(
                this, // Parent component (the panel itself)
                "Level " + level + " Cleared!\nScore: " + score + "\nContinue to next level?", // Message
                "Level Complete", // Title
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            level++;
            System.out.println("Starting Level " + level);
            String[] nextMap = null;
            switch (level) {
                case 2:
                    nextMap = Level2;
                    break;
                case 3:
                    nextMap = Level3;
                    break;
                // Add more cases for more levels
                default:
                    System.out.println("All levels completed or level not found!");
                    gameOver(); // Or show a "You Win" screen
                    return; // Exit if no next level
            }
            loadLevel(nextMap); // Load the new map data
            resetGamePositions(); // Reset positions for the new level
            score = score; // Keep score across levels (reset if needed)
            // lives = lives; // Keep remaining lives (reset if needed)
            startGame(); // Restart the timer and game loop
        } else {
            // Player chose NO
            gameOver(); // Go back to menu or end game
        }
    }

    // Simple collision check between two Blocks (Axis-Aligned Bounding Box)
    public boolean collision(Block a, Block b) {
        // Add a small tolerance (e.g., 2 pixels) to make collisions less strict at corners
        int tolerance = 4;
        return a.x_position < b.x_position + b.width - tolerance
                && a.x_position + a.width > b.x_position + tolerance
                && a.y_position < b.y_position + b.height - tolerance
                && a.y_position + a.height > b.y_position + tolerance;
    }

    // Reset just the positions of Pacman and Ghosts
    // Reset just the positions of Pacman and Ghosts
    private void resetGamePositions() {
        if (pacman != null) {
            pacman.x_position = pacman.start_x;
            pacman.y_position = pacman.start_y;
            pacman.X_GridPosition = pacman.start_X_GridPosition;
            pacman.Y_GridPosition = pacman.start_Y_GridPosition;
            pacman.isMoving = false; // Correctly resets internal state
            pacman.currentDirection = Direction.NONE; // Correctly resets internal state (Set to RIGHT if you prefer)
            pacman.texture = pacmanRight; // Reset texture
        }
        
        for (Block ghost: ghosts){
            ghost.x_position = ghost.start_x;
            ghost.y_position = ghost.start_y;
            ghost.X_GridPosition = ghost.start_X_GridPosition;
            ghost.Y_GridPosition = ghost.start_Y_GridPosition;
        }

        // Clear Pacman's buffered input
        pacmanNextDirection = Direction.NONE;
    }

    // Reset game state completely (lives, score, positions) - called on game over or restart
    private void resetGame() {
        lives = 3;
        score = 0;
        level = 1; // Reset to level 1
        loadLevel(Level1); // Reload the first level map
        resetGamePositions(); // Reset positions
        gameLoop.stop(); // Ensure game loop is stopped before potentially restarting
        // Don't automatically restart the loop here, let App handle showing menu/starting game
    }

    // Game Over Mechanic
    private void gameOver() {
        System.out.println("Game Over!");
        gameLoop.stop(); // Stop the game loop firmly

    }

    // Method to give focus to the panel (usually called from App.java)
    public void requestPanelFocus() {
        requestFocusInWindow();
    }
}
