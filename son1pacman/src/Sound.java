package src;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("sounds/pacmanAcilisSesi.wav");
        soundURL[1] = getClass().getResource("sounds/pacmanYemekYemeSesi.wav");
        soundURL[2] = getClass().getResource("sounds/pacmanOlmeSesi.wav");
        soundURL[3] = getClass().getResource("sounds/level-complete.wav");
        soundURL[4] = getClass().getResource("sounds/game-over.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void play() {
        if (clip != null)
            clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public Clip getClip() {
        return clip;
    }
}
