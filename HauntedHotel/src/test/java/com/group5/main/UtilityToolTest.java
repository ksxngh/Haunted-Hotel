package com.group5.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class UtilityToolTest {

    UtilityTool util;

    @BeforeEach
    void setup() {
        util = new UtilityTool();
    }

    @Test
    void testScaleImageChangesDimensions() {
        BufferedImage original = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        int newWidth = 64;
        int newHeight = 64;

        BufferedImage scaled = util.scaleImage(original, newWidth, newHeight);

        assertNotNull(scaled, "Scaled image should not be null");
        assertEquals(newWidth, scaled.getWidth(), "Width should match requested width");
        assertEquals(newHeight, scaled.getHeight(), "Height should match requested height");
    }

    @Test
    void testScaleImageDoesNotAffectOriginal() {
        BufferedImage original = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        // Copy original pixels
        copy.setData(original.getData());

        util.scaleImage(original, 40, 40);

        assertEquals(copy.getWidth(), original.getWidth(), "Original width should not change");
        assertEquals(copy.getHeight(), original.getHeight(), "Original height should not change");
    }

    @Test
    void testScaleImageThrowsNoExceptionForValidInput() {
        BufferedImage original = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

        assertDoesNotThrow(() -> util.scaleImage(original, 25, 25), "Scaling should not throw an exception");
    }

    @Test
    void testScaleImageReturnsCorrectType() {
        BufferedImage original = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
        BufferedImage scaled = util.scaleImage(original, 60, 60);

        assertEquals(original.getType(), scaled.getType(), "Scaled image should have the same type as original");
    }
}
