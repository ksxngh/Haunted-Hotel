package com.group5.tile;

import java.awt.image.BufferedImage;

/**
 * Represents a single tile in the game world.
 * Stores the tile's image and whether it has collision (blocking) enabled.
 */
public class Tile {

    /** The visual image of the tile. */
    public BufferedImage image;

    /** Whether this tile blocks movement (true) or can be walked over (false). */
    public boolean collision = false;
}