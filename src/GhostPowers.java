

import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.SwingUtilities;

public class GhostPowers {

    Block blueGhost; // Should be the specific Block instance for the blue ghost
    Block redGhost;
    Block pinkGhost;
    Block orangeGhost;

    boolean boolBlueInvis = false;
    boolean boolPinkClones = false;

    Block ghostClone1 = null;
    Block ghostClone2 = null;

    PacmanGame pacmanGame; // Changed name for clarity from 'pacman' to 'pacmanGame'

    // To identify ghosts, we need references to the Image objects used for their textures in PacmanGame
    private Image blueGhostTextureRef;
    private Image redGhostTextureRef;
    private Image pinkGhostTextureRef;
    private Image orangeGhostTextureRef;

    public GhostPowers(PacmanGame pacmanGameInstance) {
        this.pacmanGame = pacmanGameInstance;

        // Store references to the canonical ghost textures from PacmanGame
        this.blueGhostTextureRef = pacmanGameInstance.blueGhostLeft;
        this.redGhostTextureRef = pacmanGameInstance.redGhostLeft;
        this.pinkGhostTextureRef = pacmanGameInstance.pinkGhostLeft;
        this.orangeGhostTextureRef = pacmanGameInstance.orangeGhostLeft;

        // Identify ghosts by comparing their *originalTexture* with the Image references
        // This assumes Block.originalTexture holds their standard appearance.
        for (Block ghostBlock : pacmanGame.ghosts) {
            if (ghostBlock.originalTexture == null) {
                continue;
            }

            // Check originalTexture as .texture can change (frightened, eaten, blue's power)
            if (ghostBlock.originalTexture.equals(this.blueGhostTextureRef)) {
                this.blueGhost = ghostBlock;
            } else if (ghostBlock.originalTexture.equals(this.redGhostTextureRef)) {
                this.redGhost = ghostBlock;
            } else if (ghostBlock.originalTexture.equals(this.pinkGhostTextureRef)) {
                this.pinkGhost = ghostBlock;
            } else if (ghostBlock.originalTexture.equals(this.orangeGhostTextureRef)) {
                this.orangeGhost = ghostBlock;
            }
        }

        // Defensive check if ghosts were not found (e.g., if textures didn't match)
        if (this.blueGhost == null && pacmanGame.ghosts.stream().anyMatch(g -> g.originalTexture == this.blueGhostTextureRef)) {
            System.err.println("GhostPowers: Blue ghost Block instance not found but texture exists. Check identification logic.");
        }
        // Similar checks for other ghosts can be added.

        if (blueGhost != null) {
            blueTask = new GhostAbilityTask(this::activateBlueGhostPower, pacmanGameInstance);
            new Thread(blueTask, "BlueGhostAbilityThread").start();
        }
        if (pinkGhost != null) {
            pinkTask = new GhostAbilityTask(this::activatePinkGhostPower, pacmanGameInstance);
            new Thread(pinkTask, "PinkGhostAbilityThread").start();
        }
        if (redGhost != null) {
            redTask = new GhostAbilityTask(this::activateRedGhostPower, 7000, pacmanGameInstance);
            new Thread(redTask, "RedGhostAbilityThread").start();
        }
        if (orangeGhost != null) {
            orangeTask = new GhostAbilityTask(this::activateOrangeGhostPower, 10000, pacmanGameInstance);
            new Thread(orangeTask, "OrangeGhostAbilityThread").start();
        }
    }

    public void recalculateGhosts() {
        for (Block ghostBlock : pacmanGame.ghosts) {
            if (ghostBlock.originalTexture == null) {
                continue;
            }

            // Check originalTexture as .texture can change (frightened, eaten, blue's power)
            if (ghostBlock.originalTexture.equals(this.blueGhostTextureRef)) {
                this.blueGhost = ghostBlock;
            } else if (ghostBlock.originalTexture.equals(this.redGhostTextureRef)) {
                this.redGhost = ghostBlock;
            } else if (ghostBlock.originalTexture.equals(this.pinkGhostTextureRef)) {
                this.pinkGhost = ghostBlock;
            } else if (ghostBlock.originalTexture.equals(this.orangeGhostTextureRef)) {
                this.orangeGhost = ghostBlock;
            }
        }

        // Defensive check if ghosts were not found (e.g., if textures didn't match)
        if (this.blueGhost == null && pacmanGame.ghosts.stream().anyMatch(g -> g.originalTexture == this.blueGhostTextureRef)) {
            System.err.println("GhostPowers: Blue ghost Block instance not found but texture exists. Check identification logic.");
        }
    }

    // Blue's power
    public void activateBlueGhostPower() {
        if (blueGhost == null || !pacmanGame.gameLoop.isRunning() || pacmanGame.isPaused) {
            return;
        }
        pacmanGame.app.settings.blueP("assets/game_sounds/blueP.wav");

        // ADD CHECK: Do not activate if frightened or eaten
        if (blueGhost.isFrightened || blueGhost.isEaten) {
            // If blue was invisible and becomes frightened/eaten, ensure it's visible (handled by energizer logic)
            // If it was invisible and energizer wears off, it should become invisible again if boolBlueInvis is true.
            // For this power, we just prevent toggling invisibility if vulnerable.
            if (boolBlueInvis) { // If it was supposed to be invisible but became frightened/eaten
                // The energizer logic in PacmanGame should set its texture.
                // If energizer ends, PacmanGame.setGhostNormal might need to check boolBlueInvis.
            }
            return;
        }

        boolBlueInvis = !boolBlueInvis;
        if (boolBlueInvis) {
            blueGhost.texture = null; // Becomes "invisible"
        } else {
            blueGhost.texture = blueGhost.originalTexture; // Reassign original texture
        }
    }

    // Pink's power
    public void removePinkClones() {
        if (boolPinkClones || ghostClone1 != null || ghostClone2 != null) { // If clones might exist
            System.out.println("GhostPowers: Removing pink clones.");
            final Block clone1ToRemove = this.ghostClone1;
            final Block clone2ToRemove = this.ghostClone2;

            // Remove from PacmanGame's list on the EDT
            if (clone1ToRemove != null || clone2ToRemove != null) {
                SwingUtilities.invokeLater(() -> {
                    if (clone1ToRemove != null && pacmanGame != null && pacmanGame.ghosts != null) {
                        pacmanGame.ghosts.remove(clone1ToRemove);
                    }
                    if (clone2ToRemove != null && pacmanGame != null && pacmanGame.ghosts != null) {
                        pacmanGame.ghosts.remove(clone2ToRemove);
                    }
                });
            }

            this.ghostClone1 = null;
            this.ghostClone2 = null;
            this.boolPinkClones = false; // Ensure the state flag is also reset
        }
    }

    // Modify activatePinkGhostPower to use this method when toggling off
    public void activatePinkGhostPower() {
        if (pinkGhost == null || !pacmanGame.gameLoop.isRunning() || pacmanGame.isPaused) {
            return;
        }
         pacmanGame.app.settings.pinkP("assets/game_sounds/pinkP.wav");
        // ... (defensive check and isFrightened/isEaten/isInPenWaiting checks) ...

        // If power is toggling from ON to OFF
        if (this.boolPinkClones) { // If clones are currently active and we are turning them off
            removePinkClones(); // Use the new method
        } else { // Clones are not active, and we are turning them ON
            // ... (existing logic to spawn clones) ...
            // Ensure boolPinkClones is set to true here
            // this.boolPinkClones = true; // This should be part of your existing spawn logic
            Image texture = pinkGhost.originalTexture;
            this.boolPinkClones = true; // Set the flag since we are activating

            ghostClone1 = new Block(texture, pinkGhost.X_GridPosition, pinkGhost.Y_GridPosition, pacmanGame.tileSize, pacmanGame.tileSize);
            ghostClone2 = new Block(texture, pinkGhost.X_GridPosition, pinkGhost.Y_GridPosition, pacmanGame.tileSize, pacmanGame.tileSize);
            ghostClone1.isGhost = true;
            ghostClone2.isGhost = true;

            // Add to PacmanGame's list on the EDT
            final Block c1 = ghostClone1; // Final for lambda
            final Block c2 = ghostClone2;
            SwingUtilities.invokeLater(() -> {
                if (c1 != null && pacmanGame != null && pacmanGame.ghosts != null) {
                    pacmanGame.ghosts.add(c1);
                }
                if (c2 != null && pacmanGame != null && pacmanGame.ghosts != null) {
                    pacmanGame.ghosts.add(c2);
                }
            });
        }
        // Note: The original direct toggle `boolPinkClones = !boolPinkClones;` needs to be removed
        // if you adopt this if/else structure for clarity. Or, keep the toggle and then use an if/else
        // based on the new value of boolPinkClones. The if/else above is clearer.
    }

    // Red's power: Shoot projectile
    public void activateRedGhostPower() {
        if (redGhost == null || redGhost.currentDirection == Direction.NONE || 
            !pacmanGame.gameLoop.isRunning() || pacmanGame.isPaused) {
            return;
        }
         pacmanGame.app.settings.redP("assets/game_sounds/redP.wav");

        // ADD CHECK: Do not activate if frightened or eaten
        if (redGhost.isFrightened || redGhost.isEaten) {
            return;
        }

        int projSize = pacmanGame.tileSize / 2;
        int projStartX = redGhost.x_position;
        int projStartY = redGhost.y_position;
        projStartX += redGhost.currentDirection.getDx() * (redGhost.width / 2 + projSize / 4);
        projStartY += redGhost.currentDirection.getDy() * (redGhost.height / 2 + projSize / 4);

        Projectile projectile = new Projectile(
                projStartX, projStartY, projSize,
                redGhost.currentDirection, 6, Color.RED, pacmanGame
        );
        pacmanGame.addProjectile(projectile);
    }

    // Orange's power: Drop bomb
    public void activateOrangeGhostPower() {
        if (orangeGhost == null || orangeGhost.currentDirection == Direction.NONE || 
            !pacmanGame.gameLoop.isRunning() || pacmanGame.isPaused) {
            return;
        }

        // ADD CHECK: Do not activate if frightened or eaten
        if (orangeGhost.isFrightened || orangeGhost.isEaten) {
            return;
        }

        int bombGridX = orangeGhost.X_GridPosition + orangeGhost.currentDirection.getDx();
        int bombGridY = orangeGhost.Y_GridPosition + orangeGhost.currentDirection.getDy();

        if (bombGridX >= 0 && bombGridX < pacmanGame.columnCount
                && bombGridY >= 0 && bombGridY < pacmanGame.rowCount
                && !pacmanGame.isWallAtGrid(bombGridX, bombGridY)
                && !pacmanGame.isBombAtGrid(bombGridX, bombGridY)) {

            Bomb bomb = new Bomb(bombGridX, bombGridY, pacmanGame.tileSize, Color.YELLOW,pacmanGame.app.settings.getSound());
            pacmanGame.addBomb(bomb);
        }
    }

    private static class GhostAbilityTask implements Runnable {
        private final Runnable abilityAction;
        private final long interval;
        private volatile boolean running = true;
        private final PacmanGame pacmanGame;
        private long remainingTime;
        private long lastTickTime;

        public GhostAbilityTask(Runnable action, long intervalMillis, PacmanGame game) {
            this.abilityAction = action;
            this.interval = intervalMillis;
            this.pacmanGame = game;
            this.remainingTime = intervalMillis;
        }

        public GhostAbilityTask(Runnable action, PacmanGame game) {
            this(action, 5000, game);
        }

        @Override
        public void run() {
            while (running) {
                try {
                    lastTickTime = System.currentTimeMillis();
                    Thread.sleep(100); // Check every 100ms
                    
                    if (!running) break;
                    
                    if (!pacmanGame.isPaused) {
                        // Only decrease remaining time if not paused
                        long now = System.currentTimeMillis();
                        long elapsed = now - lastTickTime;
                        remainingTime -= elapsed;
                        
                        if (remainingTime <= 0) {
                            if (!pacmanGame.isPaused) {
                                abilityAction.run();
                            }
                            remainingTime = interval; // Reset timer
                        }
                    }
                    lastTickTime = System.currentTimeMillis(); // Update lastTickTime even when paused
                    
                } catch (InterruptedException e) {
                    System.err.println("Ghost ability task interrupted: " + Thread.currentThread().getName());
                    running = false;
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.err.println("Exception in Ghost ability task: " + Thread.currentThread().getName());
                    e.printStackTrace();
                }
            }
            System.out.println("Ghost ability task stopped: " + Thread.currentThread().getName());
        }

        public void stopTask() {
            running = false;
        }
    }

    private GhostAbilityTask blueTask, redTask, pinkTask, orangeTask;

    public void stopAllAbilityThreads() {
        System.out.println("GhostPowers: Signaling ability threads to stop.");
        if (blueTask != null) {
            blueTask.stopTask();
        }
        if (pinkTask != null) {
            pinkTask.stopTask();
        }
        if (redTask != null) {
            redTask.stopTask();
        }
        if (orangeTask != null) {
            orangeTask.stopTask();
        }
        // You might also want to interrupt the threads if they are sleeping
        // Thread.currentThread().getThreadGroup().interrupt(); // This is too broad
        // Better to store Thread references and interrupt them individually if needed.
        // For now, relying on the 'running' flag is the primary mechanism.
    }

}