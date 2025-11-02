import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener, MouseListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int groundHeight = 90;

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

    Image groundImage;
    Ground ground1;
    Ground ground2;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -4;

    boolean gameStarted = false;
    boolean gameOver = false;
    boolean showFlash = false;
    boolean showGetReady = true; // Show "Get Ready" at start

    int score = 0;
    int bestScore = 0;

    SoundManager soundManager;

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        // Try to load ground image, fallback to null if not found
        try {
            groundImage = new ImageIcon(getClass().getResource("assets/ground.png")).getImage();
        } catch (Exception e) {
            groundImage = null;
            System.err.println("Ground image not found, using colored ground!");
        }

        // Create two ground segments for seamless scrolling
        ground1 = new Ground(0, frameHeight - groundHeight, frameWidth, groundHeight, groundImage);
        ground2 = new Ground(frameWidth, frameHeight - groundHeight, frameWidth, groundHeight, groundImage);

        // Initialize sound manager
        soundManager = new SoundManager();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (gameStarted) {
                    placePipes();
                }
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000 / 45, this);
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

    public Ground getGround1() {
        return ground1;
    }

    public Ground getGround2() {
        return ground2;
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean shouldShowFlash() {
        return showFlash;
    }

    public boolean shouldShowGetReady() {
        return showGetReady;
    }

    public void move() {
        if (!gameStarted || gameOver) {
            return;
        }

        // Move player with gravity
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        // Check if player hit bottom or top
        if (player.getPosY() >= frameHeight - playerHeight || player.getPosY() <= 0) {
            gameOver = true;
            showFlash = true;
            soundManager.playHit();
            System.out.println("Game Over! Hit screen boundary!");

            // Hide flash after short delay
            Timer flashTimer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showFlash = false;
                    ((Timer)e.getSource()).stop();
                }
            });
            flashTimer.setRepeats(false);
            flashTimer.start();
            return;
        }

        // Move pipes and check collisions
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // Check collision with pipe
            if (checkCollision(player, pipe)) {
                gameOver = true;
                showFlash = true;
                soundManager.playHit();
                System.out.println("Game Over! Hit pipe! Final Score: " + score);

                // Hide flash after short delay
                Timer flashTimer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showFlash = false;
                        ((Timer)e.getSource()).stop();
                    }
                });
                flashTimer.setRepeats(false);
                flashTimer.start();
                return;
            }
        }

        // Ground collision check (same as pipe collision)
        if (checkCollisionWithGround(player, ground1) || checkCollisionWithGround(player, ground2)) {
            gameOver = true;
            showFlash = true;
            soundManager.playHit();
            System.out.println("Game Over! Hit Ground! Final Score: " + score);

            // Hide flash after short delay
            Timer flashTimer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showFlash = false;
                    ((Timer)e.getSource()).stop();
                }
            });
            flashTimer.setRepeats(false);
            flashTimer.start();
            return;
        }

        // Check score - only count upper pipes (every other pipe starting from index 0)
        for (int i = 0; i < pipes.size(); i += 2) {
            Pipe pipe = pipes.get(i);
            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score++;
                soundManager.playPoint();
                System.out.println("Score: " + score);
            }
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
        // M key to go back to menu
        if (e.getKeyCode() == KeyEvent.VK_M && gameOver) {
            returnToMenu();
            return;
        }

        // R key to restart
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
            return;
        }

        // Accept SPACE, UP arrow, or W key to flap
        if (e.getKeyCode() == KeyEvent.VK_SPACE ||
                e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {

            if (gameOver) {
                return; // Don't flap when game is over
            }

            if (!gameStarted) {
                gameStarted = true;
                showGetReady = false; // Hide "Get Ready" message
                System.out.println("Game started!");
            }

            player.setVelocityY(-9);
            soundManager.playJump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // Mouse listener methods
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameOver) {
            return; // Don't flap when game is over
        }

        if (!gameStarted) {
            gameStarted = true;
            showGetReady = false; // Hide "Get Ready" message
            System.out.println("Game started!");
        }

        player.setVelocityY(-9);
        soundManager.playJump();
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingspace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingspace + pipeHeight),
                pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // Collision detection using rectangle intersection
    public boolean checkCollision(Player player, Pipe pipe) {
        return player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                player.getPosX() + player.getWidth() > pipe.getPosX() &&
                player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                player.getPosY() + player.getHeight() > pipe.getPosY();
    }

    // Collision detection for ground using rectangle intersection
    public boolean checkCollisionWithGround(Player player, Ground ground) {
        return player.getPosX() < ground.getPosX() + ground.getWidth() &&
                player.getPosX() + player.getWidth() > ground.getPosX() &&
                player.getPosY() < ground.getPosY() + ground.getHeight() &&
                player.getPosY() + player.getHeight() > ground.getPosY();
    }

    public void restartGame() {
        // Update best score if current score is higher
        if (score > bestScore) {
            bestScore = score;
            System.out.println("New Best Score: " + bestScore);
        }

        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = false;
        showFlash = false;
        showGetReady = true; // Show "Get Ready" again
        System.out.println("Game restarted!");
    }

    public void returnToMenu() {
        // Update best score if current score is higher
        if (score > bestScore) {
            bestScore = score;
            System.out.println("New Best Score: " + bestScore);
        }

        // Reset game state
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = false;
        showFlash = false;
        showGetReady = true; // Reset "Get Ready" for next time

        // Switch to menu
        if (view != null) {
            view.getCardLayout().show(view.getMainContainer(), "menu");
        }
        System.out.println("Returned to menu!");
    }
}