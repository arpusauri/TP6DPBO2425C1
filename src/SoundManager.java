import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    private Clip jumpSound;
    private Clip hitSound;
    private Clip pointSound;
    private boolean muted = false;

    public SoundManager() {
        try {
            // Load jump sound
            jumpSound = loadSound("assets/jump.wav");

            // Load hit/death sound
            hitSound = loadSound("assets/hit.wav");

            // Load point/score sound
            pointSound = loadSound("assets/point.wav");

        } catch (Exception e) {
            System.err.println("Could not load sound files! Game will run without sound!");
        }
    }

    private Clip loadSound(String path) {
        try {
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

    public void playJump() {
        if (!muted && jumpSound != null) {
            if (jumpSound.isRunning()) {
                jumpSound.stop();
            }
            jumpSound.setFramePosition(0);
            jumpSound.start();
        }
    }

    public void playHit() {
        if (!muted && hitSound != null) {
            if (hitSound.isRunning()) {
                hitSound.stop();
            }
            hitSound.setFramePosition(0);
            hitSound.start();
        }
    }

    public void playPoint() {
        if (!muted && pointSound != null) {
            if (pointSound.isRunning()) {
                pointSound.stop();
            }
            pointSound.setFramePosition(0);
            pointSound.start();
        }
    }

    public void toggleMute() {
        muted = !muted;
        System.out.println("Sound " + (muted ? "muted" : "unmuted"));
    }

    public boolean isMuted() {
        return muted;
    }

    public void cleanup() {
        if (jumpSound != null) jumpSound.close();
        if (hitSound != null) hitSound.close();
        if (pointSound != null) pointSound.close();
    }
}