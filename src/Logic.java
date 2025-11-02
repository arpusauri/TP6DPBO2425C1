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
    // set frame size
    int frameWidth = 360;
    int frameHeight = 640;

    // set ground height
    int groundHeight = 90;

    // set player position & size
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // set pipe position & size
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    // player
    View view;
    Image birdImage;
    Player player;

    // pipes
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    // ground
    Image groundImage;
    Ground ground1;
    Ground ground2;

    // game loop
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    int pipeVelocityX = -4;

    // game status & condition
    boolean gameStarted = false;
    boolean gameOver = false;
    boolean showFlash = false;
    boolean showGetReady = true; // Show "Get Ready" at start

    // score
    int score = 0;
    int bestScore = 0;

    // sound effect
    SoundManager soundManager;

    public Logic() {
        // load bird (player)
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        // load pipes
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        // load ground
        try {
            groundImage = new ImageIcon(getClass().getResource("assets/ground.png")).getImage();
        } catch (Exception e) { // set ground with bg-color when image not found
            groundImage = null;
            System.err.println("Ground image not found, using colored ground!");
        }

        // create ground segments
        ground1 = new Ground(0, frameHeight - groundHeight, frameWidth, groundHeight, groundImage);
        ground2 = new Ground(frameWidth, frameHeight - groundHeight, frameWidth, groundHeight, groundImage);

        // initialize sound manager
        soundManager = new SoundManager();

        // place pipes when game starts
        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (gameStarted) {
                    placePipes();
                }
            }
        });
        pipesCooldown.start();

        // start game & fps
        gameLoop = new Timer(1000 / 45, this);
        gameLoop.start();
    }

    // getter & setter
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

    // game over boolean
    public boolean isGameOver() {
        return gameOver;
    }

    // show flash boolean
    public boolean shouldShowFlash() {
        return showFlash;
    }

    // show get ready boolean
    public boolean shouldShowGetReady() {
        return showGetReady;
    }

    // move function
    public void move() {
        // pause when game not started / game over
        if (!gameStarted || gameOver) {
            return;
        }

        // move player using gravity
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        // check if player out of bounds
        if (player.getPosY() >= frameHeight - playerHeight || player.getPosY() <= 0) {
            // end game
            gameOver = true;
            // show flash vfx
            showFlash = true;
            // play hit sfx
            soundManager.playHit();
            System.out.println("Game Over! Hit screen boundary!");

            // hide flash after delay (showing)
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

        // move pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // check collision with pipes
            if (checkCollision(player, pipe)) {
                // end game
                gameOver = true;
                showFlash = true;
                soundManager.playHit();
                System.out.println("Game Over! Hit pipe! Final Score: " + score);

                // hit sfx
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

        // check collision with pipe
        if (checkCollisionWithGround(player, ground1) || checkCollisionWithGround(player, ground2)) {
            // end game
            gameOver = true;
            showFlash = true;
            soundManager.playHit();
            System.out.println("Game Over! Hit Ground! Final Score: " + score);

            // show sfx
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

        // check and count score when passing pipes
        for (int i = 0; i < pipes.size(); i += 2) {
            Pipe pipe = pipes.get(i);
            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score++;
                soundManager.playPoint();
                System.out.println("Score: " + score);
            }
        }

        // limit pipes off screen
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

    // key listener
    @Override
    public void keyPressed(KeyEvent e) {
        // press m to go to menu (when game is over)
        if (e.getKeyCode() == KeyEvent.VK_M && gameOver) {
            returnToMenu();
            return;
        }

        // press r to go to restart game (when game is over)
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
            return;
        }

        // player control (W / UP ARROW / SPACE)
        if (e.getKeyCode() == KeyEvent.VK_SPACE ||
                e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W) {

            // prevent input when game is over
            if (gameOver) {
                return;
            }

            // when game not started
            if (!gameStarted) {
                // set game to start
                gameStarted = true;
                // and hide get ready
                showGetReady = false;
                System.out.println("Game started!");
            }

            // set player y velocity
            player.setVelocityY(-9);
            // jump sfx when pressing keys
            soundManager.playJump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // mouse listener
    @Override
    public void mouseClicked(MouseEvent e) {
        // only accept LMB
        if (e.getButton() == MouseEvent.BUTTON1) {
            // prevent input when game is over
            if (gameOver) {
                return;
            }

            // set game to start
            if (!gameStarted) {
                gameStarted = true;
                showGetReady = false;
                System.out.println("Game started!");
            }

            // set player velocity & play sfx
            player.setVelocityY(-9);
            soundManager.playJump();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // function to place pipes randomly
    public void placePipes() {
        // set y position
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        // set pipes opening
        int openingspace = frameHeight / 4;

        // create upper pipe based on random pos y
        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        // create lower pipe
        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingspace + pipeHeight),
                pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // pipe collision detection
    public boolean checkCollision(Player player, Pipe pipe) {
        return player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                player.getPosX() + player.getWidth() > pipe.getPosX() &&
                player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                player.getPosY() + player.getHeight() > pipe.getPosY();
    }

    // ground collision detection
    public boolean checkCollisionWithGround(Player player, Ground ground) {
        return player.getPosX() < ground.getPosX() + ground.getWidth() &&
                player.getPosX() + player.getWidth() > ground.getPosX() &&
                player.getPosY() < ground.getPosY() + ground.getHeight() &&
                player.getPosY() + player.getHeight() > ground.getPosY();
    }

    // restart game
    public void restartGame() {
        // update best score when current score is higher
        if (score > bestScore) {
            bestScore = score;
            System.out.println("New Best Score: " + bestScore);
        }

        // start over game
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = false;
        showFlash = false;
        showGetReady = true;
        System.out.println("Game restarted!");
    }

    // return to menu
    public void returnToMenu() {
        // update best score when current score is higher
        if (score > bestScore) {
            bestScore = score;
            System.out.println("New Best Score: " + bestScore);
        }

        // start over game (reset game state)
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = false;
        showFlash = false;
        showGetReady = true; // Reset "Get Ready" for next time

        // switch to menu layout
        if (view != null) {
            view.getCardLayout().show(view.getMainContainer(), "menu");
        }
        System.out.println("Returned to menu!");
    }
}