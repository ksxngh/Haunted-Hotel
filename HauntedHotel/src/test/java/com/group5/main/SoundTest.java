package com.group5.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class SoundTest {

    Sound sound;

    @BeforeEach
    void setup() {
        sound = new Sound();
    }

    @Test
    void testConstructorInitializesURLs() {
        // Check that the first 3 sound URLs are not null
        assertNotNull(sound.soundURL[0], "BackgroundB.wav should be initialized");
        assertNotNull(sound.soundURL[1], "Key.wav should be initialized");
        assertNotNull(sound.soundURL[2], "Chest.wav should be initialized");

        // Other indices should be null
        for (int i = 3; i < sound.soundURL.length; i++) {
            assertNull(sound.soundURL[i], "Sound URL at index " + i + " should be null");
        }
    }

    @Test
    void testSetFileDoesNotThrow() {
        // For safety, only test indexes 0-2 because others are null
        for (int i = 0; i < 3; i++) {
            final int index = i; // make a final copy for lambda
            assertDoesNotThrow(() -> sound.setFile(index), "setFile should not throw for index " + index);
            assertNotNull(sound.clip, "Clip should be initialized after setFile(" + index + ")");
        }

    }

    @Test
    void testPlayLoopStopDoNotThrow() {
        // Load a sound first
        sound.setFile(1);

        assertDoesNotThrow(() -> sound.play(), "play() should not throw");
        assertDoesNotThrow(() -> sound.loop(), "loop() should not throw");
        assertDoesNotThrow(() -> sound.stop(), "stop() should not throw");
    }

    @Test
    void testPlaySEDoesNotThrow() {
        assertDoesNotThrow(() -> sound.playSE(), "playSE() should not throw");
    }

    @Test
    void testSetFileException() {
        assertDoesNotThrow(() -> sound.setFile(99), "setFile should handle invalid index without throwing");
    }


}
