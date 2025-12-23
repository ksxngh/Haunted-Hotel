package com.group5.main;

import com.group5.entity.Entity;


/**
 * The {@code CollisionChecker} class is responsible for handling all collision detection logic
 * within the game world. It checks for interactions between entities, tiles, objects, monsters,
 * NPCs, and the player. 
 * <p>
 * Collision detection ensures that entities do not move through solid tiles or objects
 * and helps manage interactions such as picking up items or taking damage.
 * </p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class CollisionChecker {

    /** Reference to the main {@link GamePanel} instance controlling the game state. */
    GamePanel gp;

    /**
     * Constructs a new {@code CollisionChecker} with the specified {@link GamePanel}.
     *
     * @param gp the main game panel instance containing tiles, objects, and entities
     */
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Checks if the specified entity collides with solid tiles in the world.
     * <p>
     * This method calculates the entity’s current and potential tile positions
     * based on its speed and direction. If a collision tile is detected,
     * the entity’s {@code collisionOn} flag is set to {@code true}.
     * </p>
     *
     * @param entity the entity to check for tile collisions
     */
    public void checkTile(Entity entity) {

        int entityLeftWorldx = entity.worldx + entity.solidArea.x;
        int entityRightWorldx = entity.worldx + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldy = entity.worldy + entity.solidArea.y;
        int entityBottomWorldy = entity.worldy + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldx/gp.tileSize;
        int entityRightCol = entityRightWorldx/gp.tileSize;
        int entityTopRow = entityTopWorldy/gp.tileSize;
        int entityBottomRow = entityBottomWorldy/gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldy - entity.speed) / gp.tileSize;
                if (entityTopRow < 0 || entityLeftCol < 0 || 
                    entityRightCol >= gp.maxWorldCol || entityTopRow >= gp.maxWorldRow) {
                    entity.collisionOn = true; // or just break safely
                    break;
                }
                tileNum1 = gp.tileM.mapTilenum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTilenum[gp.currentMap][entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldy + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTilenum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTilenum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldx - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTilenum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTilenum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldx + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTilenum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTilenum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    /**
     * Checks for collisions between an entity and any objects in the game world.
     * <p>
     * If the {@code player} parameter is {@code true}, the method returns the index of
     * the object being collided with for pickup or interaction.
     * </p>
     *
     * @param entity the entity to check for collisions
     * @param player {@code true} if the entity is the player; {@code false} otherwise
     * @return the index of the collided object, or {@code 999} if no collision occurred
     */
    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for(int i = 0; i < gp.obj[1].length; i++){

            if(gp.obj[gp.currentMap][i] != null) {
                //Get entity solid area pos and onj solid area pos
                entity.solidArea.x = entity.worldx + entity.solidArea.x;
                entity.solidArea.y = entity.worldy + entity.solidArea.y;

                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldx + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldy + gp.obj[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.y += entity.speed;
                        break;
                }
                if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                    if(gp.obj[gp.currentMap][i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if(player == true) {
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefultX;
                entity.solidArea.y = entity.solidAreaDefultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefultY;
            }
        }
        return index;
    }

    /**
     * Checks for collisions between an entity and another entity (e.g., monsters or NPCs).
     *
     * @param entity the entity being checked
     * @param target the target entity array (e.g., {@code gp.monster} or {@code gp.npc})
     * @return the index of the collided entity, or {@code 999} if no collision occurred
     */
    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;

        for(int i = 0; i < target[1].length; i++){

            if(target[gp.currentMap][i] != null) {
                //Get entity solid area pos and onj solid area pos
                entity.solidArea.x = entity.worldx + entity.solidArea.x;
                entity.solidArea.y = entity.worldy + entity.solidArea.y;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldx + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldy + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.y += entity.speed;
                        break;
                }
                if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if(target[gp.currentMap][i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefultX;
                entity.solidArea.y = entity.solidAreaDefultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefultY;
            }
        }
        return index;
    }

    /**
     * Checks for collisions between a given entity (such as a monster or projectile) and the player.
     * <p>
     * If contact is detected, the entity’s {@code collisionOn} flag is set to {@code true},
     * and {@code contactPlayer} is returned as {@code true}.
     * </p>
     *
     * @param entity the entity to check for collision with the player
     * @return {@code true} if contact with the player occurs; {@code false} otherwise
     */
    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

            //Get entity solid area pos and onj solid area pos
        entity.solidArea.x = entity.worldx + entity.solidArea.x;
        entity.solidArea.y = entity.worldy + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldx + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldy + gp.player.solidArea.y;

            switch (entity.direction) {
                case "up":
                    entity.solidArea.y -= entity.speed;
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    break;
                case "right":
                    entity.solidArea.y += entity.speed;
                    break;
            }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefultX;
        entity.solidArea.y = entity.solidAreaDefultY;
        gp.player.solidArea.x = gp.player.solidAreaDefultX;
        gp.player.solidArea.y = gp.player.solidAreaDefultY;

        return contactPlayer;
    }


}