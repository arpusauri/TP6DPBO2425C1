import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MenuPanel extends JPanel {
    private Image background;
    private Image titleImage;
    private Font customFont;
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // menu panel
    public MenuPanel(CardLayout cardLayout, JPanel mainContainer) {
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;

        setPreferredSize(new Dimension(360, 640));
        setLayout(null);

        // load background image
        background = new ImageIcon(getClass().getResource("assets/background-menu.png")).getImage();

        // load title image
        try {
            URL messageURL = getClass().getResource("assets/message.png");
            if (messageURL == null) {
                System.err.println("Title sprite resource 'assets/message.png' not found.");
                throw new IOException("Resource not found.");
            }
            BufferedImage messageSprite = ImageIO.read(messageURL);
            titleImage = messageSprite.getSubimage(0, 0, 184, 89);
        } catch (Exception e) {
            titleImage = null;
            System.err.println("Could not load Flappy Bird title image: " + e.getMessage());
        }

        // load custom font
        try {
            InputStream fontStream = getClass().getResourceAsStream("assets/Minecraft.ttf"); // yes pake font minecraft wkwkwk
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

        // exit & instruction y position
        int exitY = 350;
        int instructionsY = 520;

        // start button (JButton)
        JButton playButton = new JButton("START");
        playButton.setFont(customFont.deriveFont(Font.BOLD, 24f));
        playButton.setBounds(105, 280, 150, 50);
        playButton.setFocusPainted(false);
        playButton.setBackground(new Color(50, 205, 50));
        playButton.setForeground(Color.WHITE);
        playButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        playButton.setHorizontalAlignment(SwingConstants.CENTER);
        playButton.setMargin(new Insets(0, 0, 0, 0));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContainer, "game");
                Component[] components = mainContainer.getComponents();
                for (Component comp : components) {
                    if (comp instanceof View) {
                        comp.requestFocus();
                        break;
                    }
                }
            }
        });
        add(playButton);


        // exit button (JButton)
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(customFont.deriveFont(Font.BOLD, 24f));
        exitButton.setBounds(105, exitY, 150, 50);
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setMargin(new Insets(5, 10, 5, 10));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);

        // instruction label (JLabel)
        JLabel instructionsLabel = new JLabel("<html><center>Click or Press SPACE/UP/W/LMB to Jump<br>Press R to Restart<br>Press R to Open Menu</center></html>", SwingConstants.CENTER);
        instructionsLabel.setFont(customFont.deriveFont(14f));
        instructionsLabel.setForeground(new Color(255, 100, 0));
        instructionsLabel.setBounds(0, instructionsY, 360, 50);
        add(instructionsLabel);
    }

    // paint background & title images
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
        if (titleImage != null) {
            int titleWidth = titleImage.getWidth(null);
            int titleHeight = titleImage.getHeight(null);
            int imgX = (getWidth() - titleWidth) / 2;
            int imgY = 120;
            g.drawImage(titleImage, imgX, imgY, titleWidth, titleHeight, null);
        }
    }
}
