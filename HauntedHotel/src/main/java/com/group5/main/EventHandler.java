package com.group5.main;

import com.group5.entity.Player;

/**
 * The {@code EventHandler} class manages special in-game events such as damage zones and teleportation.
 * It monitors the player's position and triggers appropriate responses when specific tiles or event
 * areas are interacted with. Events can include traps, transitions between maps, or scripted interactions.
 * <p>
 * This class uses a 3D array of {@link EventRect} objects to track event zones for each map tile and
 * ensures that repeated triggering of the same event is prevented until the player moves away.
 * </p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class EventHandler {

    /** Reference to the main {@link GamePanel} instance controlling game state and entities. */
    GamePanel gp;

    /** 3D array representing all event trigger areas across different maps. */
    EventRect[][][] eventRect;

    /** Tracks the previous X and Y position where an event occurred. */
    int previousEventX, previousEventY;

     /** Indicates whether the player can currently trigger an event. */
    boolean canTouchEvent = true;

    /**
     * Constructs a new {@code EventHandler} and initializes event rectangles for each map tile.
     *
     * @param gp the main {@link GamePanel} instance containing the game world data
     */
    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;

        int col = 0;
        int row = 0;
        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {

            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 20;
            eventRect[map][col][row].height = 20;
            eventRect[map][col][row].eventRectDefultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;

                if (row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

    }

    /** Used to check if the player can trigger an event related to taking damage or moving between levels. */
    private boolean canTriggerEvent() {
        int xDistance = Math.abs(gp.player.worldx - previousEventX);
        int yDistance = Math.abs(gp.player.worldy - previousEventY);
        int distance = Math.max(xDistance, yDistance);

        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }

        return canTouchEvent;
    }

    /** Used to check if the player can trigger an event related to taking damage or moving between levels. */
    private boolean handleFireEvents() {
        int mapNum = 3;
        int[][] fireTiles = {
            {25, 19}, {44, 3}, {65, 2}, {55, 7}, {39, 10},
            {65, 13}, {65, 26}, {44, 15}, {43, 27}, {33, 21},
            {29, 27}, {55, 20}, {25, 5}
        };

        for (int[] tile : fireTiles) {
            if (isEventTriggered(mapNum, tile[0], tile[1])) {
                firePunishment(gp.dialogueState);
                return true;
            }
        }
        return false;
    }

    /** Used to check if the player can trigger an event related to taking damage or moving between levels. */
    private boolean handleBombEvents() {
        int mapNum = 2;
        int[][] bombTiles = {
            {17, 13}, {22, 17}, {31, 15}, {34, 18}, {37, 9}
        };

        for (int[] tile : bombTiles) {
            if (isEventTriggered(mapNum, tile[0], tile[1])) {
                bombPunishment(gp.dialogueState);
                return true;
            }
        }
        return false;
    }
    
    /** Used to check if the player can trigger an event related to taking damage or moving between levels. */
    private void handleTeleportEvents() {
        int lobbyMap = 0;
        int myersMap = 1;
        int santaMap = 2;
        int basementMap = 3;
        int teleportTiles[][] = {
            {40, 7}, {1, 7}, {16, 10}, {18, 7}, {1, 15}
        };

    if (isEventTriggered(0, 1, 7) && (gp.player.hasKey >= 1 && gp.player.hasKey <= 2)) {
        teleport(myersMap, teleportTiles[0][0], teleportTiles[0][1]);
    } else if (isEventTriggered(1, 40, 7) && (gp.player.hasKey >= 2 && gp.player.hasKey <= 3)) {
        teleport(lobbyMap, teleportTiles[1][0], teleportTiles[1][1]);
    } else if (isEventTriggered(0, 18, 7) && (gp.player.hasKey >= 1 && gp.player.hasKey <= 2)) {
        teleport(santaMap, teleportTiles[2][0], teleportTiles[2][1]);
    } else if (isEventTriggered(2, 16, 10) && (gp.player.hasKey >= 2 && gp.player.hasKey <= 3)) {
        teleport(lobbyMap, teleportTiles[3][0], teleportTiles[3][1]);
    } else if ((isEventTriggered(0, 10, 1) || isEventTriggered(0, 9, 1)) && gp.player.hasKey == 3) {
        teleport(basementMap, teleportTiles[4][0], teleportTiles[4][1]);
    }
}





    /**
     * Checks if the player is currently standing in or near any event area and triggers appropriate
     * event responses such as teleportation or environmental hazards.
     * <p>
     * This method ensures that players cannot repeatedly trigger the same event without moving away.
     * </p>
     */


    public void checkEvent() {
        if (!canTriggerEvent()) return;

        // Handle different event types
        if (handleFireEvents()) return;
        if (handleBombEvents()) return;
        handleTeleportEvents();
    }

    

    /**
     * Determines whether the player is currently colliding with a specific event rectangle
     * on the given map and tile coordinates.
     *
     * @param map the map number to check for events
     * @param col the column coordinate of the event
     * @param row the row coordinate of the event
     * @return {@code true} if the player has collided with the event rectangle; {@code false} otherwise
     */
    public boolean isEventTriggered(int map,int col, int row) {
        boolean triggerEvent = false;
        if (map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldx + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldy + gp.player.solidArea.y;
            eventRect[map][col][row].x = col*gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row*gp.tileSize + eventRect[map][col][row].y;

            if(gp.player.solidArea.intersects(eventRect[map][col][row])) {
                triggerEvent = true;

                previousEventX = gp.player.worldx;
                previousEventY = gp.player.worldy;
            }
            // reset values
            gp.player.solidArea.x = gp.player.solidAreaDefultX;
            gp.player.solidArea.y = gp.player.solidAreaDefultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefultY;
    }
        return triggerEvent;
    }

    /**
     * Triggers a fire-related punishment event when the player touches a hazardous tile.
     * <p>
     * This reduces the player's life, displays a message, and prevents immediate retriggering
     * until the player moves away from the event zone.
     * </p>
     *
     * @param gameState the state to switch the game into (e.g., dialogue state)
     */
    public void firePunishment(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You burned yourself";
        gp.player.life -= 1;
        canTouchEvent = false;
    }


    public void bombPunishment(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You got a bomb for christmas";
        gp.player.life -= 1;
        canTouchEvent = false;
    }


    /**
     * Teleports the player to a specified map and tile location.
     * <p>
     * This method updates the player’s world coordinates and prevents immediate retriggering
     * of the teleport event.
     * </p>
     *
     * @param map the destination map index
     * @param col the destination column coordinate
     * @param row the destination row coordinate
     */
    public void teleport(int map, int col, int row) {

        gp.currentMap = map;
        gp.player.worldx = gp.tileSize * col;
        gp.player.worldy = gp.tileSize * row;
        previousEventX = gp.player.worldx;
        previousEventY = gp.player.worldy;
        canTouchEvent = false;
    }
}
