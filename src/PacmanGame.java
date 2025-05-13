
// PacmanGame.java

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList; // Import ArrayList
import java.util.HashSet;
import java.util.Iterator;
import java.util.List; // Import List
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import javax.swing.*;

public class PacmanGame extends JPanel implements ActionListener, KeyListener, MouseListener {

    int rowCount = 21;
    int columnCount = 19;
    int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    Timer gameLoop;

    int level = 1;
    App app;

    public List<Projectile> activeProjectiles;
    public List<Bomb> activeBombs;

    // Pacman Textures
    Image hearts;
    Image empty_hearts;
    Image pacmanRight;
    Image pacmanLeft;
    Image pacmanUp;
    Image pacmanDown;
    Image backButtonImage;
    Rectangle backButtonBounds;
    Image playButtonImage;

    // Ghost Textures
    Image blueGhostLeft; // Renamed to avoid conflict with Block instance
    Image blueGhostRight;
    Image blueGhostUp;
    Image blueGhostDown;

    Image redGhostLeft;
    Image redGhostRight;
    Image redGhostUp;
    Image redGhostDown;

    Image pinkGhostLeft;
    Image pinkGhostRight;
    Image pinkGhostUp;
    Image pinkGhostDown;

    Image orangeGhostLeft;
    Image orangeGhostRight;
    Image orangeGhostUp;
    Image orangeGhostDown;

    Image ghostFrightenedTexture; // For blue vulnerable state
    Image ghostEatenTexture;      // For "eyes" returning state

    Image blueGhost_FRIGHTENED;
    Image redGhost_FRIGHTENED;
    Image pinkGhost_FRIGHTENED;
    Image orangeGhost_FRIGHTENED;
    // If all frightened ghosts look the same, you only need one:
    // Image commonFrightenedTexture;

    // --- NEW: Specific Eaten (Eyes) Textures ---
    Image blueGhost_EATEN;
    Image redGhost_EATEN;
    Image pinkGhost_EATEN;
    Image orangeGhost_EATEN;

    GhostPowers ghostPowers;
    PacmanAbilities pacmanPowers;

    // Wall Textures
    private Image bldg9Wall;
    private Image bldg5Wall;
    private Image technocoreWall;

    int score = 0;
    int lives = 3;
    int move_speed = 5; // Pacman's speed
    int ghost_move_speed = 3; // Normal ghost speed
    int ghost_frightened_speed = 2; // Slower speed when frightened
    int ghost_eaten_speed = 9; // Faster speed when returning as eyes
    Random random = new Random();

    HashSet<Block> walls;
    HashSet<Block> foods;
    public HashSet<Block> ghosts;
    HashSet<Block> powerPellets; // New set for power pellets
    Block pacman;

    private Font PixelFont;

    // Energizer state
    boolean energizerActive = false;
    Timer energizerTimer;
    final int GHOST_ENERGIZED_DURATION = 7000; // 7 seconds in milliseconds
    int eatenGhostScoreMultiplier = 1; // For 200, 400, 800, 1600 points

    // Ghost spawn/pen location (approximate center, adjust as needed for your map)
    // This is where eaten ghosts should return.
    // For simplicity, let's use the red ghost's initial spawn from Level 1 as a general pen.
    // Ideally, each ghost might have its own specific "home" tile inside the ghost house.
    // For Level1, 'r' is at (10, 8) if we find it.
    int ghostPenGridX = 10; // Default, will be updated if 'r' is found
    int ghostPenGridY = 8;

    // Levels (keep as is)
    private String[] Level1 = {
        "XXXXXXXXXXXXXXXXXXX",
        "X0     X 0 X     0X",
        "X XX X X X X X XX X",
        "X    X   X   X    X",
        "X XX XXX X XXX XX X",
        "X X      X      X X",
        "X X XX XXXXX XX X X",
        "X   0          0  X",
        "X XX X XXrXX X XX X",
        "X    X XbpoX X    X",
        "X XX X XXXXX X XX X",
        "X  X X       X X  X",
        "XX X X XXXXX X X XX",
        "X  X     X     X  X",
        "X XX0XXX X XXX0XX X",
        "X    X       X    X",
        "X X XX XlX X XX X X",
        "X X    X P X    X X",
        "X XXXX XXXXX XXXX X",
        "X0               0X",
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
        "X X  X   X   X  X X",
        "X X XX X X X XX X X",
        "X    X X P X X    X",
        "X XX X X X X X XX X",
        "X X     lX      X X",
        "X X XX XXXXX XX X X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX",};

    private String[] Level3 = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X   X   X    X",
        "XXXX XX  X lXX XXXX",
        "---X X       X X---",
        "---X   XXrXX   X---",
        "---X X XbpoX X X---",
        "---X X XXXXX X X---",
        "---X X       X X---",
        "---X   X X X   X---", // Assuming 'O' is a typo and meant to be food or empty space as it's not handled.
        // If 'O' is special, its handling should be added in loadLevel.
        // For now, it will be treated as an empty space.
        "---X XXX X XXX X---",
        "---X     P     X---",
        "---X X XXXXX X X---",
        "---X X   X   X X---",
        "---XXXXX X XXXXX---",
        "---X           X---",
        "---XXXXXXXXXXXXX---",};

    final int GHOST_PEN_WAIT_DURATION = 2000;

    // Pause Button
    private Image pauseButtonImage;
    private Rectangle pauseButtonBounds;
    boolean isPaused = false;

    // Add field to track remaining energizer time when paused
    private int remainingEnergizerTime = 0;

    // Add field to track when energizer started
    private long energizerStartTime;

    public PacmanGame(App app) {
        activeProjectiles = new ArrayList<>();
        activeBombs = new ArrayList<>();
        powerPellets = new HashSet<>(); // Initialize powerPellets

        energizerTimer = new Timer(GHOST_ENERGIZED_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateEnergizer();
            }
        });
        energizerTimer.setRepeats(false);

        this.app = app;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setBackground(Color.BLACK);

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

        hearts = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/heart.gif")).getImage();
        empty_hearts = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/empty_heart.gif")).getImage();
        pacmanRight = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/RIGHT.gif")).getImage();
        pacmanLeft = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/LEFT.gif")).getImage();
        pacmanUp = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/UP.gif")).getImage();
        pacmanDown = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/DOWN.gif")).getImage();

        redGhostLeft = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/redGhost.gif")).getImage();
        blueGhostLeft = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/blueGhost.gif")).getImage();
        pinkGhostLeft = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/pinkGhost/Ghost-Eithan_LEFT.gif")).getImage();
        orangeGhostLeft = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/orangeGhost.gif")).getImage();

        redGhost_FRIGHTENED = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/redGhost_vulnerable.GIF")).getImage();
        blueGhost_FRIGHTENED = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/blueGhost_vulnerable.GIF")).getImage();
        pinkGhost_FRIGHTENED = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/pinkGhost_vulnerable.GIF")).getImage();
        orangeGhost_FRIGHTENED = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/orangeGhost_vulnerable.GIF")).getImage();

        redGhost_EATEN = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/redGhost_EATEN.gif")).getImage();
        blueGhost_EATEN = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/blueGhost_EATEN.png")).getImage();
        pinkGhost_EATEN = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/pinkGhost_EATEN.gif")).getImage();
        orangeGhost_EATEN = new ImageIcon(getClass().getResource("./assets/game_textures/ghosts/orangeGhost_EATEN.gif")).getImage();

        bldg9Wall = new ImageIcon(getClass().getResource("./assets/game_textures/walls/bldg9.png")).getImage();
        bldg5Wall = new ImageIcon(getClass().getResource("./assets/game_textures/walls/bldg5.png")).getImage();
        technocoreWall = new ImageIcon(getClass().getResource("./assets/game_textures/walls/technocore.png")).getImage();

        activeProjectiles = new ArrayList<>();
        activeBombs = new ArrayList<>();

        // Load pause button image
        pauseButtonImage = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/Pause_Button.png")).getImage();
        playButtonImage = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/play.png")).getImage();
        pauseButtonBounds = new Rectangle(boardWidth - 60, boardHeight + 15, 30, 30); // Moved below board height

        // Load back button image
        backButtonImage = new ImageIcon(getClass().getResource("./assets/ui_graphics/Back.png")).getImage();
        backButtonBounds = new Rectangle(boardWidth - 90, 10, 70, 40);

        loadLevel(Level1, bldg9Wall);
        gameLoop = new Timer(32, this);
        
        
    }

    private Image createSolidColorImage(int width, int height, Color color) {
        Image img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return img;
    }

    private Image createGhostEyesImage(int width, int height, Color eyeColor, Color bgColor) {
        Image img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        if (bgColor != null) { // Transparent if bgColor is null
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, width, height);
        }

        g2d.setColor(eyeColor);
        // Simple rectangular eyes
        int eyeWidth = width / 4;
        int eyeHeight = height / 3;
        int eyeSpacing = width / 12;
        int eyeY = height / 3;

        g2d.fillRect(width / 2 - eyeWidth - eyeSpacing / 2, eyeY, eyeWidth, eyeHeight);
        g2d.fillRect(width / 2 + eyeSpacing / 2, eyeY, eyeWidth, eyeHeight);

        g2d.dispose();
        return img;
    }

    public void startGame() {
        resetGamePositions();
        activeProjectiles.clear();
        activeBombs.clear();
        deactivateEnergizer(); // Ensure energizer is off at the start
        
        // Only create new ghost powers if they don't exist or game is not paused
        if (this.ghostPowers == null || !isPaused) {
            if (this.ghostPowers != null) {
                System.out.println("Stopping existing GhostPowers threads...");
                this.ghostPowers.stopAllAbilityThreads();
            }
            this.ghostPowers = new GhostPowers(this);
        } else {
            // If game is paused, just recalculate ghost references
            this.ghostPowers.recalculateGhosts();
        }
        
        gameLoop.start();
    }

    public void loadLevel(String[] mapData, Image wallTexture) {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        powerPellets = new HashSet<>(); // Clear and re-initialize for new level
        pacman = null;

        boolean redGhostSpawnSet = false;

        for (int r = 0; r < rowCount; r++) {
            String row = mapData[r];
            for (int c = 0; c < columnCount; c++) {
                char mapChar = row.charAt(c);

                if (mapChar == 'X') {

                    Block wall = new Block(wallTexture, c, r, tileSize, tileSize);
                    walls.add(wall);
                } else if (mapChar == 'P') {
                    pacman = new Block(pacmanRight, c, r, tileSize, tileSize);
                } else if (mapChar == 'o') {
                    Block ghost = new Block(orangeGhostLeft, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                } else if (mapChar == 'p') {
                    Block ghost = new Block(pinkGhostLeft, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                } else if (mapChar == 'r') {
                    Block ghost = new Block(redGhostLeft, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                    if (!redGhostSpawnSet) { // Set general ghost pen to red ghost's spawn
                        ghostPenGridX = c;
                        ghostPenGridY = r;
                        redGhostSpawnSet = true;
                    }
                } else if (mapChar == 'b') {
                    Block ghost = new Block(blueGhostLeft, c, r, tileSize, tileSize);
                    ghost.isGhost = true;
                    ghosts.add(ghost);
                } else if (mapChar == 'l') {
                    Block pellet = new Block(null, c, r, tileSize, tileSize);
                    foods.add(pellet);
                } else if (mapChar == '0') { // Power Pellet
                    Block powerPellet = new Block(null, c, r, tileSize, tileSize); // Texture is null, drawn as circle
                    powerPellets.add(powerPellet);
                }
            }
        }
        if (pacman == null) {
            System.err.println("Warning: Pacman start position 'P' not found in map!");
            pacman = new Block(pacmanRight, 1, 1, tileSize, tileSize);
        }
        // Ensure ghost pen is somewhat reasonable if 'r' wasn't in the map
        if (!redGhostSpawnSet && !ghosts.isEmpty()) {
            Block firstGhost = ghosts.iterator().next();
            ghostPenGridX = firstGhost.start_X_GridPosition;
            ghostPenGridY = firstGhost.start_Y_GridPosition;
        } else if (!redGhostSpawnSet && ghosts.isEmpty()) {
            // Fallback if no ghosts are defined
            ghostPenGridX = columnCount / 2;
            ghostPenGridY = rowCount / 2;
        }
        pacmanPowers = new PacmanAbilities(this, pacman);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw Food Pellets
        g.setColor(new Color(253, 245, 185));
        for (Block food : foods) {
            g.fillOval(food.x_position + tileSize / 2 - 3, food.y_position + tileSize / 2 - 3, 6, 6);
        }

        // Draw Power Pellets
        g.setColor(new Color(255, 184, 174)); // A light pink/peach color for power pellets
        for (Block pp : powerPellets) {
            g.fillOval(pp.x_position + tileSize / 2 - 6, pp.y_position + tileSize / 2 - 6, 12, 12); // Larger than food
        }

        // Draw Walls
        for (Block wall : walls) {
            if (wall.texture != null) {
                g2d.drawImage(wall.texture, wall.x_position, wall.y_position, tileSize, tileSize, this);
            }
        }

        // Draw Ghosts
        for (Block ghost : ghosts) {
            if (ghost.texture != null) { // Texture might be null if blue ghost is invisible AND energizer not active
                g2d.drawImage(ghost.texture, ghost.x_position, ghost.y_position, tileSize, tileSize, this);
            } else if (ghost.isFrightened || ghost.isEaten) { // Frightened/Eaten ghosts always have a texture
                g2d.drawImage(ghost.texture, ghost.x_position, ghost.y_position, tileSize, tileSize, this);
            }
            // If blue ghost is invisible due to its own power AND energizer is not active, it remains invisible.
            // If energizer is active, blue ghost should show frightened texture.
        }

        // Draw Pacman
        if (pacman != null && pacman.texture != null) {
            g2d.drawImage(pacman.texture, pacman.x_position, pacman.y_position, tileSize, tileSize, this);
        }

        // Draw Bombs & Projectiles
        for (Bomb bomb : activeBombs) {
            bomb.draw(g2d, this);
        }
        for (Projectile projectile : activeProjectiles) {
            projectile.draw(g2d);
        }

        // Draw pause/play button
        if (isPaused) {
            g2d.drawImage(playButtonImage, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height, this);
            
            // Dim background
            Composite original = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(original);

            // Draw "PAUSED" text
            g2d.setColor(Color.WHITE);
            Font pauseFont = new Font("Arial", Font.BOLD, 48);
            g2d.setFont(pauseFont);
            FontMetrics fm = g2d.getFontMetrics();
            String pausedText = "PAUSED";
            int textX = (getWidth() - fm.stringWidth(pausedText)) / 2;
            g2d.drawString(pausedText, textX, getHeight() / 2);

            // Draw back button
            g2d.drawImage(backButtonImage, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height, this);
        } else {
            g2d.drawImage(pauseButtonImage, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height, this);
        }

        // Draw Score/Lives UI
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
        checkCollisions();
        repaint();
    }

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

    private Direction pacmanNextDirection = Direction.NONE;

    private void handleKeyPress(int keyCode) {
        if (pacman == null) {
            return;
        }
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
            case KeyEvent.VK_SPACE:
                pacmanPowers.activateDash();
            default:
                return;
        }

        pacmanNextDirection = desiredDirection;

        if (!pacman.isMoving || pacmanNextDirection == getOpposite(pacman.currentDirection)) {
            attemptToStartPacmanMove();
        }
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

    private void attemptToStartPacmanMove() {
        if (pacman == null) {
            return;
        }
        if (pacmanNextDirection != Direction.NONE && canMove(pacman, pacmanNextDirection)) {
            startPacmanMove(pacmanNextDirection);
            pacmanNextDirection = Direction.NONE;
        } else if (pacman.currentDirection != Direction.NONE && canMove(pacman, pacman.currentDirection)) {
            if (!pacman.isMoving) {
                startPacmanMove(pacman.currentDirection);
            }
            if (pacmanNextDirection != Direction.NONE && !canMove(pacman, pacmanNextDirection)) {
                pacmanNextDirection = Direction.NONE;
            }
        } else {
            if (pacmanNextDirection != Direction.NONE && !canMove(pacman, pacmanNextDirection)) {
                pacmanNextDirection = Direction.NONE;
            }
        }
    }

    private boolean canMove(Block block, Direction direction) {
        if (block == null || direction == Direction.NONE) {
            return false;
        }

        int targetGridX = block.X_GridPosition + direction.getDx();
        int targetGridY = block.Y_GridPosition + direction.getDy();

        if (targetGridX < 0 || targetGridX >= columnCount
                || targetGridY < 0 || targetGridY >= rowCount) {
            return false;
        }

        String[] currentLevelMap = getCurrentLevelMap();
        if (currentLevelMap == null) {
            return false;
        }
        try {
            char tileAtTarget = currentLevelMap[targetGridY].charAt(targetGridX);
            if (tileAtTarget == 'X') {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error checking map bounds: " + targetGridX + "," + targetGridY);
            return false;
        }
        return true;
    }

    private String[] getCurrentLevelMap() {
        switch (level) {
            case 1:
                return Level1;
            case 2:
                return Level2;
            case 3:
                return Level3;
            default:
                return null;
        }
    }

    private void startPacmanMove(Direction direction) {
        if (pacman == null || !canMove(pacman, direction)) {
            return;
        }

        pacman.currentDirection = direction;
        pacman.isMoving = true;

        pacman.targetPixelX = (pacman.X_GridPosition + pacman.currentDirection.getDx()) * tileSize;
        pacman.targetPixelY = (pacman.Y_GridPosition + pacman.currentDirection.getDy()) * tileSize;

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
                break;
        }
    }

    private void updateGame() {
        if (pacman == null || isPaused) {
            return;
        }

        // --- Update Pacman ---
        if (pacman.isMoving) {
            moveBlock(pacman, move_speed);
        } else { // Pacman is stationary at a tile center
            foodCollision(); // Check for regular food
            powerPelletCollision(); // Check for power pellets
            if (foods.isEmpty() && powerPellets.isEmpty()) { // Win condition
                if (!gameLoop.isRunning()) {
                    return;
                }
                gameLoop.stop();
                nextLevel();
                return;
            }
            attemptToStartPacmanMove(); // Try to move again
        }

        if (pacman.isMoving && pacman.x_position == pacman.targetPixelX && pacman.y_position == pacman.targetPixelY) {
            // Pacman arrived at the center of the target tile
            pacman.isMoving = false;
            pacman.X_GridPosition += pacman.currentDirection.getDx();
            pacman.Y_GridPosition += pacman.currentDirection.getDy();
            pacman.x_position = pacman.X_GridPosition * tileSize;
            pacman.y_position = pacman.Y_GridPosition * tileSize;

            foodCollision();
            powerPelletCollision();
            if (foods.isEmpty() && powerPellets.isEmpty()) {
                if (!gameLoop.isRunning()) {
                    return;
                }
                gameLoop.stop();
                nextLevel();
                return;
            }
            attemptToStartPacmanMove();
        }

        Iterator<Block> ghostIterator = ghosts.iterator();
        while (ghostIterator.hasNext()) {
            Block currentGhost = ghostIterator.next();
            if (currentGhost == null) {
                ghostIterator.remove();
                continue;
            }

            // State 1: Ghost was eaten and just reached its starting pen position
            if (currentGhost.isEaten && !currentGhost.isInPenWaiting
                    && currentGhost.X_GridPosition == currentGhost.start_X_GridPosition
                    && currentGhost.Y_GridPosition == currentGhost.start_Y_GridPosition) {

                // System.out.println("Ghost reached pen, starting wait: " + currentGhost.originalTexture);
                currentGhost.enterPenToWait(this.ghostEatenTexture); // Pass the "eyes" texture
            } // State 2: Ghost is in pen, waiting
            else if (currentGhost.isInPenWaiting) {
                if (System.currentTimeMillis() - currentGhost.penEntryTime > GHOST_PEN_WAIT_DURATION) {
                    // Wait time is over
                    // System.out.println("Ghost finished pen wait: " + currentGhost.originalTexture);
                    currentGhost.finishPenWaitAndResetState(); // Resets isInPenWaiting, isEaten, isFrightened

                    // Now determine its actual state (normal or frightened)
                    if (energizerActive) {
                        setGhostFrightened(currentGhost); // It becomes frightened immediately
                    } else {
                        setGhostNormal(currentGhost, true); // Becomes normal, true to reverse direction out of pen
                    }
                    // attemptToStartGhostMove will be called below if not moving
                }
                // While isInPenWaiting, it should not move, so skip movement logic below.
                // Ensure it's marked as not moving:
                currentGhost.isMoving = false;
            }

            // State 3: Ghost is active (not waiting in pen) - standard movement logic
            if (!currentGhost.isInPenWaiting) { // Only move if not waiting in pen
                if (currentGhost.isMoving) {
                    int currentSpeed = ghost_move_speed;
                    if (currentGhost.isFrightened) {
                        currentSpeed = ghost_frightened_speed;
                    } else if (currentGhost.isEaten) {
                        currentSpeed = ghost_eaten_speed; // isEaten is for "returning to pen"
                    }
                    moveBlock(currentGhost, currentSpeed);
                } else {
                    attemptToStartGhostMove(currentGhost);
                }

                if (currentGhost.isMoving && currentGhost.x_position == currentGhost.targetPixelX && currentGhost.y_position == currentGhost.targetPixelY) {
                    currentGhost.isMoving = false;
                    currentGhost.X_GridPosition += currentGhost.currentDirection.getDx();
                    currentGhost.Y_GridPosition += currentGhost.currentDirection.getDy();
                    currentGhost.x_position = currentGhost.X_GridPosition * tileSize;
                    currentGhost.y_position = currentGhost.Y_GridPosition * tileSize;

                    // If it just arrived at a tile AND is not now waiting in pen (e.g. just finished waiting)
                    if (!currentGhost.isInPenWaiting) {
                        attemptToStartGhostMove(currentGhost);
                    }
                }
            }
        } // End of ghost iteration loop

        // --- Update Projectiles & Bombs --- (Moved OUTSIDE and AFTER ghost loop)
        // ... (projectile and bomb update logic - unchanged from your last version) ...
        Iterator<Projectile> projIterator = activeProjectiles.iterator();
        while (projIterator.hasNext()) {
            Projectile p = projIterator.next();
            p.move();
            int projCenterX = p.x_position + p.width / 2;
            int projCenterY = p.y_position + p.height / 2;
            int projGridX = projCenterX / tileSize;
            int projGridY = projCenterY / tileSize;
            if (p.x_position < 0 || p.x_position + p.width > boardWidth
                    || p.y_position < 0 || p.y_position + p.height > boardHeight
                    || isWallAtGrid(projGridX, projGridY)) {
                p.isActive = false;
            }
            if (!p.isActive) {
                projIterator.remove();
            }
        }

        Iterator<Bomb> bombIterator = activeBombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb b = bombIterator.next();
            b.update();
            if (b.state == Bomb.BombState.DONE) {
                bombIterator.remove();
            }
        }
    }

    private void moveBlock(Block block, int speed) {
        if (!block.isMoving || block.currentDirection == Direction.NONE) {
            return;
        }

        if (block.x_position < block.targetPixelX) {
            block.x_position += speed;
            if (block.x_position >= block.targetPixelX) {
                block.x_position = block.targetPixelX;
            }
        } else if (block.x_position > block.targetPixelX) {
            block.x_position -= speed;
            if (block.x_position <= block.targetPixelX) {
                block.x_position = block.targetPixelX;
            }
        }

        if (block.y_position < block.targetPixelY) {
            block.y_position += speed;
            if (block.y_position >= block.targetPixelY) {
                block.y_position = block.targetPixelY;
            }
        } else if (block.y_position > block.targetPixelY) {
            block.y_position -= speed;
            if (block.y_position <= block.targetPixelY) {
                block.y_position = block.targetPixelY;
            }
        }
    }

    private void attemptToStartGhostMove(Block ghost) {
        if (ghost.isMoving) {
            return;
        }

        Direction preferredDirection;

        if (ghost.isEaten) {
            // Pathfind back to pen (start_X_GridPosition, start_Y_GridPosition)
            preferredDirection = getDirectionTowardsTarget(ghost, ghost.start_X_GridPosition, ghost.start_Y_GridPosition);
        } else if (ghost.isFrightened) {
            // Move randomly, but try not to reverse immediately
            List<Direction> possibleDirections = getValidGhostDirections(ghost, true); // true = allow immediate reverse if only option
            if (!possibleDirections.isEmpty()) {
                preferredDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
            } else { // Should be rare if allow immediate reverse is true
                preferredDirection = getOpposite(ghost.currentDirection); // Last resort
            }
        } else {
            // Normal ghost AI (random, but don't reverse unless necessary)
            List<Direction> possibleDirections = getValidGhostDirections(ghost, false); // false = don't prefer immediate reverse
            if (!possibleDirections.isEmpty()) {
                preferredDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
            } else { // Only one way to go (reverse) or completely trapped
                preferredDirection = getOpposite(ghost.currentDirection); // Try reversing
                if (!canMove(ghost, preferredDirection)) {
                    preferredDirection = Direction.NONE; // Truly stuck
                }
            }
        }

        if (preferredDirection != Direction.NONE && canMove(ghost, preferredDirection)) {
            startGhostMove(ghost, preferredDirection);
        } else {
            // If no valid move, ghost remains stationary for this tick.
            // Ensure it's marked as not moving if a move couldn't be started.
            ghost.isMoving = false;
            // If it has a direction but can't move, it just faces that way.
            // If preferredDirection was NONE, its currentDirection remains.
        }
    }

    private List<Direction> getValidGhostDirections(Block ghost, boolean allowImmediateReverse) {
        List<Direction> validDirections = new ArrayList<>();
        Direction oppositeDirection = getOpposite(ghost.currentDirection);

        for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}) {
            if (!allowImmediateReverse && dir == oppositeDirection && countValidDirections(ghost) > 1) {
                continue;
            }
            if (canMove(ghost, dir)) {
                validDirections.add(dir);
            }
        }
        // If no directions found and immediate reverse was disallowed, but it's the only option:
        if (validDirections.isEmpty() && !allowImmediateReverse && oppositeDirection != Direction.NONE && canMove(ghost, oppositeDirection)) {
            validDirections.add(oppositeDirection);
        }
        return validDirections;
    }

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
        // ... (startGhostMove - largely unchanged, texture update is now handled by energizer state) ...
        if (ghost == null || direction == Direction.NONE || !canMove(ghost, direction)) {
            if (ghost != null) {
                ghost.isMoving = false;
            }
            return;
        }
        ghost.currentDirection = direction;
        ghost.isMoving = true;
        ghost.targetPixelX = (ghost.X_GridPosition + direction.getDx()) * tileSize;
        ghost.targetPixelY = (ghost.Y_GridPosition + direction.getDy()) * tileSize;
        // Ghost texture (normal, frightened, eaten) is managed by activate/deactivateEnergizer and when eaten
    }

    private void checkCollisions() {
    if (pacman == null) {
        return;
    }
    Rectangle pacmanBounds = new Rectangle(pacman.x_position, pacman.y_position, pacman.width, pacman.height);

    // ... (Pacman vs Ghost collision - keep as is) ...
     Iterator<Block> ghostIterator = ghosts.iterator();
     while (ghostIterator.hasNext()) {
         Block ghost = ghostIterator.next();
         // ... (ghost collision logic) ...
         if (collision(pacman, ghost)) {
             if (energizerActive && ghost.isFrightened && !ghost.isEaten) {
                 eatGhost(ghost);
             } else if (!ghost.isEaten && !ghost.isInPenWaiting) { // Check not waiting too
                 // Check if blue ghost is invisible and not frightened
                 boolean blueIsEffectivelyInvisible = false;
                 if (ghostPowers != null && ghost == ghostPowers.blueGhost && ghostPowers.boolBlueInvis && !ghost.isFrightened) {
                     blueIsEffectivelyInvisible = true;
                 }

                 // Only get caught if the ghost is visible or it's the invisible blue ghost
                 if (ghost.texture != null || blueIsEffectivelyInvisible) {
                    handlePlayerCaught();
                    app.settings.pacmandeath("assets/game_sounds/death.wav");
                    return; // Exit collision check immediately
                 }
             }
         }
     }


    // ... (Pacman vs Projectile collision - keep as is) ...
    Iterator<Projectile> projIterator = activeProjectiles.iterator();
    while (projIterator.hasNext()) {
        Projectile p = projIterator.next();
        if (p.isActive && pacmanBounds.intersects(p.getBounds())) {
            p.isActive = false; // Mark projectile as inactive
            projIterator.remove(); // Remove projectile
            handlePlayerCaught();
            return; // Exit collision check immediately
        }
    }

    // --- Pacman vs Bomb Explosion Collision (UPDATED) ---
    for (Bomb bomb : activeBombs) {
        if (bomb.state == Bomb.BombState.EXPLODING) {
            // Get the set of cells affected by this bomb's explosion
            // Pass 'this' (the PacmanGame instance) for wall checking
            Set<Point> explosionCells = bomb.getAffectedCells(this);

            // Check if Pacman intersects with any of the explosion cells
            for (Point cell : explosionCells) {
                // Get the bounds for the current explosion cell
                Rectangle explosionCellBounds = bomb.getExplosionCellBounds(cell.x, cell.y); // Use existing helper

                // Check for intersection
                if (pacmanBounds.intersects(explosionCellBounds)) {
                    handlePlayerCaught();
                    return; // Pacman caught, exit collision check immediately
                }
            }
        }
    }
}

    private void eatGhost(Block ghost) {
        if (!ghost.isFrightened || ghost.isEaten || ghost.isInPenWaiting) {
            return;
        }

        score += (200 * eatenGhostScoreMultiplier); // Adjusted base score
        eatenGhostScoreMultiplier *= 2;
        app.settings.playghostSFX();
       app.settings. playGoBackSFX();
        if (eatenGhostScoreMultiplier > 8) {
            eatenGhostScoreMultiplier = 8; // Max 1600
        }
        ghost.isFrightened = false;
        ghost.isEaten = true; // Now means "returning to pen"
        ghost.isInPenWaiting = false;

        // Determine which specific eaten texture to use
        if (ghost.originalTexture == redGhostLeft) {
            ghost.texture = redGhost_EATEN;
        } else if (ghost.originalTexture == blueGhostLeft) {
            ghost.texture = blueGhost_EATEN;
        } else if (ghost.originalTexture == pinkGhostLeft) {
            ghost.texture = pinkGhost_EATEN;
        } else if (ghost.originalTexture == orangeGhostLeft) {
            ghost.texture = orangeGhost_EATEN;
        } else {
            // Fallback to a common eaten texture
            ghost.texture = this.ghostEatenTexture; // Your existing common one
        }
        // If you only have one common eaten texture, this simplifies to:
        // ghost.texture = commonEatenTexture; // (or this.ghostEatenTexture)

        ghost.isMoving = false; // Force re-evaluation of path to pen
    }

    private void handlePlayerCaught() {
        lives--;
        deactivateEnergizer();
        activeProjectiles.clear();
        activeBombs.clear();

        // --- ADD CLONE CLEANUP ---
        if (ghostPowers != null) {
            ghostPowers.removePinkClones(); // New method in GhostPowers
        }
        // --- END CLONE CLEANUP ---

        if (lives <= 0) {
            gameOver();
        } else {
            // --- Display Death Animation ---
            // Clear ghost textures (if you want them to disappear during the animation)
            for (Block ghosts : ghosts) { // Assuming 'ghosts' is your collection of ghost objects
                ghosts.texture = null;
            }

            // Set Pac-Man's texture to the death GIF
            pacman.texture = new ImageIcon(getClass().getResource("./assets/game_textures/pacman/death.gif")).getImage();
            gameLoop.stop();

            // --- Schedule the Reset After the Animation ---
            // Remove the Thread.sleep() and the code that follows it from here
            // Create a Timer that will execute the reset logic after 1500ms
            Timer resetTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // This code runs AFTER the 1500ms delay
                    resetGamePositions(); // This will now operate on a list potentially cleared of clones
                    gameLoop.start();
                    if (pacman != null) {
                        pacman.currentDirection = Direction.RIGHT;
                        // Assuming pacmanRight is the standard texture
                        pacman.texture = pacmanRight;
                    }
                    pacmanNextDirection = Direction.NONE;

                    // Stop and dispose of the timer after it fires once
                    ((Timer) e.getSource()).stop();
                }
            });

            // Set the timer to only fire once
            resetTimer.setRepeats(false);

            // Start the timer
            resetTimer.start();

            // The handlePlayerCaught method finishes executing *immediately* after starting the timer.
            // The game loop (or EDT) can continue to run, painting the GIF, until the timer fires.
        }
    }

    private void foodCollision() {
        if (pacman == null || pacman.isMoving) {
            return; // Only check when pacman is centered on a tile
        }
        Block foodEaten = null;
        for (Block food : foods) {
            if (pacman.X_GridPosition == food.X_GridPosition && pacman.Y_GridPosition == food.Y_GridPosition) {
                foodEaten = food;
                break;
            }
        }
        if (foodEaten != null) {
            foods.remove(foodEaten);
            app.settings.playPelletSFX();
            score += 10;
            // System.out.println("Score: " + score); 
        }
    }

    private void powerPelletCollision() {
        if (pacman == null || pacman.isMoving) {
            return; // Only check when pacman is centered on a tile
        }
        Block ppEaten = null;
        for (Block pp : powerPellets) {
            if (pacman.X_GridPosition == pp.X_GridPosition && pacman.Y_GridPosition == pp.Y_GridPosition) {
                ppEaten = pp;
                break;
            }
        }
        if (ppEaten != null) {
            powerPellets.remove(ppEaten);
            app.settings.playPpelletSFX();
            app.settings.playvulnerableSFX();
            score += 50; // Score for power pellet
            activateEnergizer();
            System.out.println("Power Pellet Eaten! Score: " + score);
        }
    }

    private void activateEnergizer() {
        energizerActive = true;
        eatenGhostScoreMultiplier = 1;
        for (Block ghost : ghosts) {
            if (!ghost.isEaten) {
                setGhostFrightened(ghost);
            }
        }
        energizerTimer.restart();
        energizerStartTime = System.currentTimeMillis(); // Record start time
        System.out.println("Energizer ACTIVE!");
    }

    private void setGhostFrightened(Block ghost) {
        ghost.isFrightened = true;
        ghost.isEaten = false;
        ghost.isInPenWaiting = false;

        // Determine which specific frightened texture to use
        if (ghost.originalTexture == redGhostLeft) {
            ghost.texture = redGhost_FRIGHTENED;
        } else if (ghost.originalTexture == blueGhostLeft) {
            ghost.texture = blueGhost_FRIGHTENED;
        } else if (ghost.originalTexture == pinkGhostLeft) {
            ghost.texture = pinkGhost_FRIGHTENED;
        } else if (ghost.originalTexture == orangeGhostLeft) {
            ghost.texture = orangeGhost_FRIGHTENED;
        } else {
            // Fallback to a common frightened texture if specific one not found or not defined
            ghost.texture = this.ghostFrightenedTexture; // Your existing common one
        }
        // If you only have one common frightened texture, this simplifies to:
        // ghost.texture = commonFrightenedTexture; // (or this.ghostFrightenedTexture)

        if (ghost.currentDirection != Direction.NONE) {
            Direction newDir = getOpposite(ghost.currentDirection);
            if (canMove(ghost, newDir)) {
                ghost.currentDirection = newDir;
            }
        }
        ghost.isMoving = false;
    }

    private void deactivateEnergizer() {
        energizerActive = false;
        eatenGhostScoreMultiplier = 1;
        remainingEnergizerTime = 0;
        for (Block ghost : ghosts) {
            if (!ghost.isEaten) {
                setGhostNormal(ghost, false);
            }
        }
        if (energizerTimer.isRunning()) {
            energizerTimer.stop();
        }
        System.out.println("Energizer Wore Off.");
    }

    private void setGhostNormal(Block ghost, boolean reverseDirection) {
        ghost.isFrightened = false;
        ghost.isEaten = false; // Should already be false unless it just respawned
        ghost.texture = ghost.originalTexture; // Restore original look

        if (ghost.texture == null && ghost == ghostPowers.blueGhost && ghostPowers.boolBlueInvis) {
            // If blue ghost's power is to be invisible, it remains invisible.
            // This logic depends on how GhostPowers interacts.
            // For now, assume energizer overrides blue's invisibility.
            // If blue should revert to invisible:
            // ghost.texture = null; 
        } else {
            ghost.texture = ghost.originalTexture;
        }

        if (reverseDirection) {
            ghost.currentDirection = getOpposite(ghost.currentDirection);
            ghost.isMoving = false; // Force re-evaluation of move
        }
    }

    public void nextLevel() {
        // ... (nextLevel logic - unchanged, but ensure energizer is reset) ...
        if (gameLoop.isRunning()) {
            gameLoop.stop();
        }
        deactivateEnergizer(); // Stop energizer effects before showing dialog / loading next

        int response = JOptionPane.showConfirmDialog(this, "Level " + level + " Cleared!\nScore: " + score + "\nContinue to next level?", "Level Complete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            level++;
            System.out.println("Starting Level " + level);
            String[] nextMap = null;
            switch (level) {
                case 2:
                    nextMap = Level2;
                    loadLevel(nextMap, bldg5Wall);
                    break;
                case 3:
                    nextMap = Level3;
                    loadLevel(nextMap, technocoreWall);
                    break;
                default:
                    System.out.println("All levels completed or level not found!");
                    gameOver();
                    return;
            }

            startGame(); // This will reset positions, clear projectiles/bombs, re-init GhostPowers, and start loop
        } else {
            gameOver();
        }
    }

    public boolean collision(Block a, Block b) {
        int tolerance = 4;
        return a.x_position < b.x_position + b.width - tolerance
                && a.x_position + a.width > b.x_position + tolerance
                && a.y_position < b.y_position + b.height - tolerance
                && a.y_position + a.height > b.y_position + tolerance;
    }

    private void resetGamePositions() {
        // ... (resetGamePositions - ensure energizer effects are reset on ghosts) ...
        if (pacman != null) {
            pacman.x_position = pacman.start_x;
            pacman.y_position = pacman.start_y;
            pacman.X_GridPosition = pacman.start_X_GridPosition;
            pacman.Y_GridPosition = pacman.start_Y_GridPosition;
            pacman.isMoving = false;
            pacman.currentDirection = Direction.NONE;
            pacman.texture = pacmanRight;
        }

        for (Block ghost : ghosts) {
            ghost.x_position = ghost.start_x;
            ghost.y_position = ghost.start_y;
            ghost.X_GridPosition = ghost.start_X_GridPosition;
            ghost.Y_GridPosition = ghost.start_Y_GridPosition;
            ghost.isMoving = false;
            ghost.currentDirection = Direction.NONE;
            setGhostNormal(ghost, false); // Reset to normal state
        }
        pacmanNextDirection = Direction.NONE;
        deactivateEnergizer(); // Make sure energizer is off
    }

    public void resetGame() {
        // ... (resetGame - full game reset, ensures energizer off) ...
        lives = 3;
        score = 0;
        level = 1;
        deactivateEnergizer();
        if (gameLoop.isRunning()) {
            gameLoop.stop();
        }
        loadLevel(Level1, bldg9Wall);
        // startGame() will be called by App to show menu first.
    }

    private void gameOver() {
        // ... (gameOver - ensure energizer off) ...
        if (gameLoop.isRunning()) {
            gameLoop.stop();
        }
        deactivateEnergizer();
        app.MainFrame.setSize(624, 692);
        app.MainFrame.setLocationRelativeTo(null);
        app.gameOverPanel.setScore(score);
        app.cardLayout.show(app.MainPanel, "gameover");
        app.settings.playGameOver("assets/game_sounds/gameover.wav");
    }

    public boolean isWallAtGrid(int gridX, int gridY) {
        if (gridX < 0 || gridX >= columnCount || gridY < 0 || gridY >= rowCount) {
            return true;
        }
        String[] currentMap = getCurrentLevelMap();
        if (currentMap != null && gridY >= 0 && gridY < currentMap.length
                && gridX >= 0 && gridX < currentMap[gridY].length()) { // Added bounds check for safety
            return currentMap[gridY].charAt(gridX) == 'X';
        }
        return true;
    }

    public boolean isBombAtGrid(int gridX, int gridY) {
        for (Bomb bomb : activeBombs) {
            if (bomb.X_GridPosition == gridX && bomb.Y_GridPosition == gridY && bomb.state != Bomb.BombState.DONE) {
                return true;
            }
        }
        return false;
    }

    public void addProjectile(Projectile p) {
        SwingUtilities.invokeLater(() -> {
            activeProjectiles.add(p);
        });
    }

    public void addBomb(Bomb b) {
        SwingUtilities.invokeLater(() -> {
            activeBombs.add(b);
        });
    }

    public void requestPanelFocus() {
        requestFocusInWindow();
    }

    // Inside PacmanGame class, maybe at the very end or as a private static inner class
    private static class PathNode implements Comparable<PathNode> {

        int x, y; // Grid coordinates
        int gCost; // Cost from start to this node
        int hCost; // Heuristic cost from this node to target
        int fCost; // gCost + hCost
        PathNode parent;

        public PathNode(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void calculateFCost() {
            fCost = gCost + hCost;
        }

        @Override
        public int compareTo(PathNode other) {
            // For priority queue: lower fCost is higher priority
            // If fCosts are equal, use hCost as a tie-breaker (helps find more direct paths)
            if (this.fCost == other.fCost) {
                return Integer.compare(this.hCost, other.hCost);
            }
            return Integer.compare(this.fCost, other.fCost);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PathNode pathNode = (PathNode) obj;
            return x == pathNode.x && y == pathNode.y;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(x, y); // Simple hash based on coordinates
        }
    }

    private Direction getDirectionTowardsTarget(Block mover, int targetGridX, int targetGridY) {
        PathNode startNode = new PathNode(mover.X_GridPosition, mover.Y_GridPosition);
        PathNode targetNode = new PathNode(targetGridX, targetGridY);

        // A* open list (nodes to be evaluated), implemented as a PriorityQueue
        PriorityQueue<PathNode> openList = new PriorityQueue<>();
        // A* closed list (nodes already evaluated), using HashSet for fast lookups
        HashSet<PathNode> closedList = new HashSet<>();

        startNode.gCost = 0;
        startNode.hCost = calculateManhattanDistance(startNode, targetNode);
        startNode.calculateFCost();
        openList.add(startNode);

        while (!openList.isEmpty()) {
            PathNode currentNode = openList.poll(); // Get node with lowest fCost

            if (currentNode.equals(targetNode)) {
                // Path found, reconstruct and return the first step
                return getFirstStepFromPath(startNode, currentNode);
            }

            closedList.add(currentNode);

            // Check neighbors (Up, Down, Left, Right)
            for (Direction dir : Direction.values()) {
                if (dir == Direction.NONE) {
                    continue;
                }

                int neighborX = currentNode.x + dir.getDx();
                int neighborY = currentNode.y + dir.getDy();

                // Check if neighbor is valid (within bounds and not a wall)
                // Eaten ghosts can often pass through the ghost house door ('-'),
                // so canMove might need adjustment or we make a special check here.
                // For now, assume canMove checks for walls ('X').
                // For eaten ghosts, we might want a special `canEatenGhostMove`
                // if they have different movement rules (e.g., through the ghost door).
                // Let's make a simple check: allow movement if not a wall ('X')
                if (neighborX < 0 || neighborX >= columnCount || neighborY < 0 || neighborY >= rowCount
                        || isWallAtGrid(neighborX, neighborY)) { // Basic wall check
                    continue;
                }
                // If you have a specific ghost door tile (e.g., '-') and eaten ghosts can pass:
                // String[] currentMap = getCurrentLevelMap();
                // char tileAtNeighbor = (currentMap != null && neighborY < currentMap.length && neighborX < currentMap[neighborY].length()) ?
                //                       currentMap[neighborY].charAt(neighborX) : 'X';
                // if (tileAtNeighbor == 'X') { // Strict wall check
                //    continue;
                // }
                // Or, if mover.isEaten, allow passing through '-' even if canMove would normally block it for non-eaten ghosts.
                // For simplicity now, isWallAtGrid should be sufficient if '-' is not 'X'.

                PathNode neighborNode = new PathNode(neighborX, neighborY);
                if (closedList.contains(neighborNode)) {
                    continue; // Already evaluated
                }

                // Cost to move to neighbor is current gCost + 1 (assuming uniform cost)
                int tentativeGCost = currentNode.gCost + 1;

                boolean inOpenList = openList.contains(neighborNode);
                if (!inOpenList || tentativeGCost < neighborNode.gCost) {
                    neighborNode.parent = currentNode;
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.hCost = calculateManhattanDistance(neighborNode, targetNode);
                    neighborNode.calculateFCost();

                    if (!inOpenList) {
                        openList.add(neighborNode);
                    } else {
                        // If already in openList, remove and re-add to update priority
                        // (PriorityQueue doesn't auto-reorder on element change)
                        openList.remove(neighborNode); // Requires PathNode.equals() and hashCode()
                        openList.add(neighborNode);
                    }
                }
            }
        }

        // No path found
        // Fallback: try any valid random move if A* fails (should be rare with good map design)
        System.err.println("A* pathfinding failed for ghost at (" + mover.X_GridPosition + "," + mover.Y_GridPosition + ") to (" + targetGridX + "," + targetGridY + "). Falling back.");
        List<Direction> allValid = getValidGhostDirections(mover, true); // true = allow reverse
        if (!allValid.isEmpty()) {
            return allValid.get(random.nextInt(allValid.size()));
        }
        return Direction.NONE;
    }

// Helper for A*: Manhattan distance heuristic
    private int calculateManhattanDistance(PathNode a, PathNode b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

// Helper for A*: Reconstruct path and get the first direction
    private Direction getFirstStepFromPath(PathNode startNode, PathNode targetNode) {
        PathNode current = targetNode;
        PathNode previous = null;
        while (current.parent != null && !current.parent.equals(startNode)) {
            previous = current;
            current = current.parent;
        }
        // If current.parent is startNode, then 'current' is the first step.
        // If targetNode is adjacent to startNode, current will be targetNode and its parent is startNode.
        // Or if the loop finished and previous is not null, previous was the one before target, so current is the step from start.

        // The node 'current' is the first actual step from startNode
        int dx = current.x - startNode.x;
        int dy = current.y - startNode.y;

        if (dx == 1) {
            return Direction.RIGHT;
        }
        if (dx == -1) {
            return Direction.LEFT;
        }
        if (dy == 1) {
            return Direction.DOWN;
        }
        if (dy == -1) {
            return Direction.UP;
        }

        return Direction.NONE; // Should not happen if path is valid
    }

    // Add mouse listener methods
    @Override
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if (pauseButtonBounds.contains(p)) {
            isPaused = !isPaused; // Toggle pause state
            if (isPaused) {
                // Pause music
                if (app.settings.getClip() != null && app.settings.getClip().isRunning()) {
                    app.settings.getClip().stop();
                }
                // Pause energizer timer if active
                if (energizerTimer != null && energizerTimer.isRunning()) {
                    remainingEnergizerTime = energizerTimer.getDelay() - (int)(System.currentTimeMillis() - energizerStartTime);
                    energizerTimer.stop();
                }
            } else {
                // Resume music
                if (app.settings.getClip() != null && !app.settings.getClip().isRunning() && app.settings.getMusic()) {
                    app.settings.getClip().start();
                }
                // Resume energizer timer if it was active
                if (energizerActive && remainingEnergizerTime > 0) {
                    energizerTimer.setInitialDelay(remainingEnergizerTime);
                    energizerTimer.restart();
                    energizerStartTime = System.currentTimeMillis(); // Reset the start time
                }
            }
        } else if (isPaused && backButtonBounds.contains(p)) {
            isPaused = false;
            remainingEnergizerTime = 0; // Reset energizer time
            deactivateEnergizer(); // Make sure to deactivate energizer when going back
            if (ghostPowers != null) {
                ghostPowers.stopAllAbilityThreads(); // Stop ghost power threads before resetting
            }
            resetGame();
            app.MainFrame.setSize(680, 747);
            app.MainFrame.setLocationRelativeTo(null);
            app.cardLayout.show(app.MainPanel, "frontinterface");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // Add method to unpause the game
    public void unpause() {
        isPaused = false;
        repaint();
    }
}