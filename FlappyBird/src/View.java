import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class View extends JPanel {
    private Logic logic;
    int width = 360;
    int height = 640;
    private Image background;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private Font customFont;

    // initialize image sprite
    private Image getReadyImage;
    private Image gameOverImage;

    public View(Logic logic, CardLayout cardLayout, JPanel mainContainer) {
        this.logic = logic;
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.white);

        // get image
        background = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("assets/gameover.png")).getImage();

        // load and extract "get ready" from image sprite sheet
        try {
            // get image
            Image messageSprite = new ImageIcon(getClass().getResource("assets/message.png")).getImage();
            // convert to buffered image
            BufferedImage buffered = new BufferedImage(184, 267, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffered.createGraphics();
            g2d.drawImage(messageSprite, 0, 0, null);
            g2d.dispose();

            // extract "get ready" image
            getReadyImage = buffered.getSubimage(0, 89, 184, 80);
        } catch (Exception e) {
            getReadyImage = null;
            System.err.println("Could not load Get Ready image: " + e.getMessage());
        }

        // load custom font
        try {
            InputStream fontStream = getClass().getResourceAsStream("assets/Minecraft.ttf"); // yes masih tetep pake font minecraft wkwkw
            if (fontStream == null) {
                System.err.println("Custom font resource not found.");
                customFont = new Font("Arial", Font.PLAIN, 24);
            } else {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
            }
        } catch (Exception e) {
            customFont = new Font("Arial", Font.PLAIN, 24);
            System.err.println("Could not load custom font: " + e.getMessage());
        }

        setFocusable(true);
        addKeyListener(logic);
        addMouseListener(logic);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // draw background
    public void draw(Graphics g) {
        if (background != null) {
            g.drawImage(background, 0, 0, width, height, null);
        }

        // draw pipes
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(),
                        pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // draw ground
        Ground ground1 = logic.getGround1();
        Ground ground2 = logic.getGround2();

        if (ground1 != null && ground2 != null) {
            if (ground1.getImage() != null) {
                // draw ground image if available
                g.drawImage(ground1.getImage(), ground1.getPosX(), ground1.getPosY(),
                        ground1.getWidth(), ground1.getHeight(), null);
                g.drawImage(ground2.getImage(), ground2.getPosX(), ground2.getPosY(),
                        ground2.getWidth(), ground2.getHeight(), null);
            } else {
                // draw colored ground if the images are not found
                // tan color (ground 2)
                g.setColor(new Color(222, 216, 149));
                g.fillRect(ground1.getPosX(), ground1.getPosY(), ground1.getWidth(), ground1.getHeight());
                g.fillRect(ground2.getPosX(), ground2.getPosY(), ground2.getWidth(), ground2.getHeight());

                // green color (ground 1)
                g.setColor(new Color(87, 158, 35));
                g.fillRect(ground1.getPosX(), ground1.getPosY(), ground1.getWidth(), 20);
                g.fillRect(ground2.getPosX(), ground2.getPosY(), ground2.getWidth(), 20);
            }
        }

        // draw player
        Player player = logic.getPlayer();
        if (player != null) {
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(),
                    player.getWidth(), player.getHeight(), null);
        }

        // draw score
        g.setColor(new Color(255, 100, 0));
        g.setFont(customFont.deriveFont(Font.BOLD, 32));

        // if game is over
        if (logic.isGameOver()) {
            // show game stats & additional info
            int imgX = (width - 184) / 2;
            int imgY = height / 3;
            g.drawImage(gameOverImage, imgX, imgY, null);
            g.drawString("Score: " + logic.getScore(), width / 2 - 70, height / 2);
            g.drawString("Best: " + logic.getBestScore(), width / 2 - 60, height / 2 + 50);
            g.setFont(customFont.deriveFont(Font.BOLD, 18));
            g.drawString("Press R to restart", width / 2 - 85, height / 2 + 100);
            g.drawString("Press M to go back", width / 2 - 85, height / 2 + 125);

        // if game not started (shouldShowGetReady = true)
        } else if (logic.shouldShowGetReady()) {
            // and getReadyImage is available
            if (getReadyImage != null) {
                // draw "get ready"
                int imgX = (width - 184) / 2;
                int imgY = height / 4;
                g.drawImage(getReadyImage, imgX, imgY, 184, 80, null);
            } else {
                g.drawString("GET READY!", width / 2 - 100, height / 3);
            }

        } else {
            // live score during gameplay
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(logic.getScore()), 10, 35);

        }

        // draw white flash on hit/death
        if (logic.shouldShowFlash()) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(0, 0, width, height);
        }
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }
}