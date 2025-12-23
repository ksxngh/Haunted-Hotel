package com.group5.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * The {@code Sound} class manages all sound effects and background music
 * within the game. It handles loading, playing, looping, and stopping audio clips.
 * <p>
 * Audio files are stored in the {@code /sound/} resource directory and loaded using
 * {@link java.net.URL} references. This class is used for both background music
 * and short sound effects like item pickups or interactions.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * Sound sound = new Sound();
 * sound.setFile(0); // Load background music
 * sound.play();     // Play it once
 * sound.loop();     // Loop continuously
 * </pre>
 * 
 * @author Group 5
 * @version 1.0
 */
public class Sound {

     /** The {@link Clip} object used to play audio. */
    Clip clip;

    /** Array holding up to 30 {@link URL} references to audio files. */
    URL soundURL[] = new URL[30];

    /**
     * Constructs a {@code Sound} object and initializes available sound files.
     * The default sounds include background music, key pickup, and chest open effects.
     */
    public Sound() {
        soundURL[0] = getClass().getResource("/sound/BackgroundB.wav");
        soundURL[1] = getClass().getResource("/sound/Key.wav");
        soundURL[2] = getClass().getResource("/sound/Chest.wav");
    }

    /**
     * Loads the specified sound file into memory, preparing it for playback.
     *
     * @param i the index of the sound file in {@link #soundURL}.
     */
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the currently loaded sound once.
     */
    public void play() {
        clip.start();
    }

    /**
     * Loops the currently loaded sound continuously until stopped.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

     /**
     * Stops the currently playing sound clip.
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Placeholder for sound effect playback functionality.
     * (Currently not implemented.)
     */
    public void playSE() {

    }

}