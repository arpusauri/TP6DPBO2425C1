import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    View view;
    Image birdImage;
    Player player;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -4;

    boolean gameStarted = false;

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (gameStarted) {
                    placePipes();
                }
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public void move() {
        if (!gameStarted) {
            return;
        }

        // Move player with gravity
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        // Keep player within screen bounds
        player.setPosY(Math.max(player.getPosY(), 0)); // Top boundary
        player.setPosY(Math.min(player.getPosY(), frameHeight - playerHeight)); // Bottom boundary

        // Move pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);
        }

        // Remove pipes that are off screen
        for (int i = pipes.size() - 1; i >= 0; i--) {
            if (pipes.get(i).getPosX() + pipeWidth < 0) {
                pipes.remove(i);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // Accept SPACE, UP arrow, or W key to flap
        if (e.getKeyCode() == KeyEvent.VK_SPACE ||
                e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {

            if (!gameStarted) {
                gameStarted = true;
                System.out.println("Game dimulai!");
            }

            player.setVelocityY(-9);
            System.out.println("Aduh nabrak!: " + player.getVelocityY());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingspace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingspace + pipeHeight),
                pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }
}