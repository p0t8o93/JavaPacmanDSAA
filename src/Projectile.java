import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile {
    int x_position; // Pixel position
    int y_position; // Pixel position
    int width;
    int height;
    Direction direction;
    int speed;
    Color color;
    boolean isActive = true;

    public Projectile(int startX, int startY, int size, Direction dir, int speed, Color color) {
        this.x_position = startX;
        this.y_position = startY;
        this.width = size;
        this.height = size;
        this.direction = dir;
        this.speed = speed;
        this.color = color;
    }

    public void move() {
        if (!isActive || direction == Direction.NONE) return;
        this.x_position += direction.getDx() * speed;
        this.y_position += direction.getDy() * speed;
    }

    public void draw(Graphics2D g2d) {
        if (!isActive) return;
        g2d.setColor(color);
        g2d.fillRect(x_position, y_position, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x_position, y_position, width, height);
    }
}