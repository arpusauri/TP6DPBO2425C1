import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    private Logic logic;
    int width = 360;
    int height = 640;
    private Image background;

    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.white);
        background = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        setFocusable(true);
        addKeyListener(logic);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw background
        if (background != null) {
            g.drawImage(background, 0, 0, width, height, null);
        }

        // Draw player
        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
                    player.getWidth(), player.getHeight(), null);
        }

        // Draw pipes
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(),
                        pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));

        if (logic.isGameOver()) {
            // Game Over screen
            g.drawString("Game Over", width / 2 - 80, height / 2 - 50);
            g.drawString("Score: " + (int)logic.getScore(), width / 2 - 70, height / 2);
            g.drawString("Best: " + logic.getBestScore(), width / 2 - 60, height / 2 + 50);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press SPACE to restart", width / 2 - 100, height / 2 + 100);
        } else {
            // Current score during gameplay
            g.drawString(String.valueOf((int)logic.getScore()), 10, 35);

            // Best score in top right
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Best: " + logic.getBestScore(), width - 100, 30);
        }
    }
}