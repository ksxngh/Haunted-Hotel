package com.group5.main;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code UtilityTool} class provides helper methods for performing
 * common image processing tasks such as resizing or scaling images.
 * <p>
 * It is primarily used throughout the game to adjust sprite and tile
 * dimensions to match the desired resolution and tile size in the game world.
 * </p>
 *
 * <p>Example use case:</p>
 * <pre>{@code
 * BufferedImage scaled = utilityTool.scaleImage(originalImage, 64, 64);
 * }</pre>
 *
 * @author Group 5
 * @version 1.0
 */
public class UtilityTool {

    /**
     * Scales a given {@link BufferedImage} to a specified width and height.
     *
     * @param original the original image to scale.
     * @param width the desired width of the scaled image.
     * @param height the desired height of the scaled image.
     * @return a new {@link BufferedImage} that has been scaled to the specified size.
     */
    public BufferedImage scaleImage (BufferedImage original, int width, int height){
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

}