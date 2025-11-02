import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Custom Window Icon
        try {
            // get url
            URL iconURL = App.class.getResource("/assets/winicon.png");

            // load image
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                Image image = icon.getImage();

                // set window icon
                frame.setIconImage(image);
            } else {
                // warning: file nout found
                System.err.println("Icon resource not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}