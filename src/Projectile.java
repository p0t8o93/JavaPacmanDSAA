import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Projectile {
    int x_position; // Pixel position
    int y_position; // Pixel position
    int width;
    int height;
    Direction direction;
    int speed;
    Color color;
    boolean isActive = true;
    Image fireballTexture;
    PacmanGame game;

    public Projectile(int startX, int startY, int size, Direction dir, int speed, Color color, PacmanGame game) {
        this.x_position = startX;
        this.y_position = startY;
        this.width = size;
        this.height = size;
        this.direction = dir;
        this.speed = speed;
        this.color = color;
        this.game = game;
        
        switch (this.direction){
            case Direction.UP:
                fireballTexture = new ImageIcon(getClass().getResource("/assets/game_textures/fireball/forieball_UP.gif")).getImage();
                break;
            case Direction.DOWN:
                fireballTexture = new ImageIcon(getClass().getResource("/assets/game_textures/fireball/forieball_DOWN.gif")).getImage();
                break;
            case Direction.LEFT:
                fireballTexture = new ImageIcon(getClass().getResource("/assets/game_textures/fireball/forieball_LEFT.gif")).getImage();
                break;
            case Direction.RIGHT:
                fireballTexture = new ImageIcon(getClass().getResource("/assets/game_textures/fireball/forieball_RIGHT.gif")).getImage();
                break;
        }
    }

    public void move() {
        if (!isActive || direction == Direction.NONE) return;
        this.x_position += direction.getDx() * speed;
        this.y_position += direction.getDy() * speed;
    }

    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        g2d.setColor(color);
        //g2d.fillRect(x_position, y_position, width, height);
        g2d.drawImage(fireballTexture, this.x_position, this.y_position, 32,32, game);
        //g2d.drawImage(ghost.texture, ghost.x_position, ghost.y_position, tileSize, tileSize, this);
    }

    public Rectangle getBounds() {
        return new Rectangle(x_position, y_position, width, height);
    }
}