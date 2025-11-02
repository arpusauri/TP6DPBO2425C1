import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    // sfx list
    private Clip jumpSound;
    private Clip hitSound;
    private Clip pointSound;

    public SoundManager() {
        try {
            // loard jump sound
            jumpSound = loadSound("assets/jump.wav");

            // load hit/death sound
            hitSound = loadSound("assets/hit.wav");

            // load point/score sound
            pointSound = loadSound("assets/point.wav");

        } catch (Exception e) { // sound files not found
            System.err.println("Could not load sound files! Game will run without sound!");
        }
    }

    // load sound
    private Clip loadSound(String path) {
        try {
            // get sound url
            URL soundURL = getClass().getClassLoader().getResource(path);
            if (soundURL == null) {
                System.err.println("Could not find: " + path);
                return null;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + path);
            return null;
        }
    }

    // play jump sound
    public void playJump() {
        if (jumpSound != null) {
            // stop previous jump sound when playing new jump sound (overlay)
            if (jumpSound.isRunning()) {
                jumpSound.stop();
            }
            jumpSound.setFramePosition(0);
            jumpSound.start();
        }
    }

    // play hit sound
    public void playHit() {
        if (hitSound != null) {
            if (hitSound.isRunning()) {
                // stop previous hit sound when playing new hit sound (overlay)
                hitSound.stop();
            }
            hitSound.setFramePosition(0);
            hitSound.start();
        }
    }

    // play point sound
    public void playPoint() {
        if (pointSound != null) {
            if (pointSound.isRunning()) {
                // stop previous hit sound when playing new hit sound (overlay)
                pointSound.stop();
            }
            pointSound.setFramePosition(0);
            pointSound.start();
        }
    }
}