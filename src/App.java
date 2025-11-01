import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Create CardLayout and main container
        CardLayout cardLayout = new CardLayout();
        JPanel mainContainer = new JPanel(cardLayout);

        // Create menu panel
        MenuPanel menuPanel = new MenuPanel(cardLayout, mainContainer);

        // Create game components
        Logic logika = new Logic();
        View tampilan = new View(logika, cardLayout, mainContainer);
        logika.setView(tampilan);

        // Add panels to card layout
        mainContainer.add(menuPanel, "menu");
        mainContainer.add(tampilan, "game");

        // Show menu first
        cardLayout.show(mainContainer, "menu");

        frame.add(mainContainer);
        frame.pack();
        frame.setVisible(true);
    }
}