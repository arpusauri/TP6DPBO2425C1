import java.awt.*;

public class Ground {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Image image;

    public Ground(int posX, int posY, int width, int height, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    // Getters
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Image getImage() { return image; }

    // Setters
    public void setPosX(int posX) { this.posX = posX; }
    public void setPosY(int posY) { this.posY = posY; }
}