package com.group5.main;

import java.awt.*;
/**
 * The {@code EventRect} class represents a rectangular area on the game map
 * where an event can be triggered. It extends {@link Rectangle} to define
 * its position and size, and adds properties to manage event state and default positioning.
 * <p>
 * Each {@code EventRect} is typically associated with a specific tile and used by
 * the {@link EventHandler} to detect collisions with the player or other entities.
 * </p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class EventRect extends Rectangle {

    /** The default X and Y position of the event rectangle (before being offset to world coordinates). */
    int eventRectDefultX, eventRectDefultY;

    /** Indicates whether the event associated with this rectangle has already been completed. */
    boolean eventDone = false;
}