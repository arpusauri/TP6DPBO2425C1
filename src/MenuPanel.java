import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private Image background;
    private CardLayout cardLayout;
    private JPanel mainContainer;

    public MenuPanel(CardLayout cardLayout, JPanel mainContainer) {
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;

        setPreferredSize(new Dimension(360, 640));
        setLayout(null); // Use absolute positioning for custom layout

        // Load background
        background = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        // Title Label
        JLabel titleLabel = new JLabel("FLAPPY BIRD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 150, 360, 60);
        add(titleLabel);

        // Play Button
        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 24));
        playButton.setBounds(105, 300, 150, 50);
        playButton.setFocusPainted(false);
        playButton.setBackground(new Color(76, 175, 80));
        playButton.setForeground(Color.WHITE);
        playButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContainer, "game");
                // Request focus for the game panel
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

        // Exit Button
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setBounds(105, 370, 150, 50);
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(244, 67, 54));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);

        // Instructions Label
        JLabel instructionsLabel = new JLabel("<html><center>Click or Press SPACE/UP/W to Jump<br>Press R to Restart</center></html>", SwingConstants.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsLabel.setForeground(Color.WHITE);
        instructionsLabel.setBounds(0, 480, 360, 50);
        add(instructionsLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
    }
}