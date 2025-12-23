package com.group5.main;

import com.group5.entity.NPC;
import com.group5.object.Obj_Chest_Key;
import com.group5.object.Obj_Door;
import com.group5.object.Obj_Gift;
import com.group5.object.Obj_Myers_Key;
import com.group5.object.Obj_Chest;
import com.group5.monster.Enemy_Santa;
import com.group5.monster.Mon_Boss;
import com.group5.monster.MyersEnemy;


/**
 * The {@code AssetSetter} class is responsible for initializing and placing all game assets
 * such as objects, NPCs, and monsters onto their respective maps.
 * <p>
 * It handles positioning each entity in the game world based on tile coordinates,
 * which are multiplied by {@code gp.tileSize} to align properly with the grid-based map system.
 * </p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class AssetSetter {

      /** Reference to the main {@link GamePanel} instance controlling the game state. */
    GamePanel gp;

    /**
     * Constructs an {@code AssetSetter} for the specified {@link GamePanel}.
     *
     * @param gp the main game panel instance that holds game objects and entities
     */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Initializes and places all interactable objects (e.g., gifts, keys) into the game world.
     * <p>
     * Each object is positioned using tile coordinates multiplied by the game’s tile size.
     * </p>
     */
    public void setObject() {

        int mapNum = 0;
        int i = 0;

        gp.obj[mapNum][i] = new Obj_Chest_Key(gp);
        gp.obj[mapNum][i].worldx = 16 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 12 * gp.tileSize;

        /* 
        i++;
        gp.obj[mapNum][i] = new Obj_Chest_Key(gp);
        gp.obj[mapNum][i].worldx = 5 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 6 * gp.tileSize;
        */ 

        //myers map
        mapNum = 1;
        i = 0;

        //6 possible locations for the kye to spawn
        int[][] keySpots = {
            {4,4},
            {20,3},
            {34,3},
            {5,11},
            {19,11},
            {36,11}  
        };

        // picking a random location
        int rand = new java.util.Random().nextInt(keySpots.length);
        gp.obj[mapNum][i] = new Obj_Myers_Key(gp);
        gp.obj[mapNum][i].worldx = keySpots[rand][0]* gp.tileSize;
        gp.obj[mapNum][i].worldy = keySpots[rand][1]* gp.tileSize;


        // Christmas map 
        mapNum = 2;
        i = 0;
        
        // random key spots 
        int[][] christmasKeySpots = {
            {18,16},
            {25,15},
            {43,12}
        };

        int random = new java.util.Random().nextInt(christmasKeySpots.length);
        gp.obj[mapNum][i] = new Obj_Myers_Key(gp);
        gp.obj[mapNum][i].worldx = christmasKeySpots[random][0]* gp.tileSize;
        gp.obj[mapNum][i].worldy = christmasKeySpots[random][1]* gp.tileSize;


        // gift objects (GOOD)
        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 18 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 16 * gp.tileSize;

        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 25 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 15 * gp.tileSize;
        
        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 43 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 12 * gp.tileSize;

        // gift objects (BAD)
        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 17 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 13 * gp.tileSize;

        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 22 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 17 * gp.tileSize;

        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 34 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 18 * gp.tileSize;

        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 31 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 15 * gp.tileSize;

        i++;
        gp.obj[mapNum][i] = new Obj_Gift(gp);
        gp.obj[mapNum][i].worldx = 37 * gp.tileSize;
        gp.obj[mapNum][i].worldy = 9 * gp.tileSize;

    }


    /**
     * Initializes and places all NPCs (Non-Player Characters) into the game world.
     * <p>
     * Currently, this method spawns a single NPC in the main map to provide dialogue and interaction.
     * </p>
     */
    public void setNPC() {
        int mapNum = 0;
        gp.npc[mapNum][0] = new NPC(gp);
        gp.npc[mapNum][0].worldx = gp.tileSize * 12;
        gp.npc[mapNum][0].worldy = gp.tileSize * 6;
    }

    /**
     * Initializes and places all monsters into the game world.
     * <p>
     * This method currently spawns a single boss monster on the specified map.
     * </p>
     */
    public void setMonster() {
        //myers
        int mapNum = 1;
        int i = 0;
        gp.monster[mapNum][i] = new MyersEnemy(gp);
        gp.monster[mapNum][i].worldx = gp.tileSize * 8;
        gp.monster[mapNum][i].worldy = gp.tileSize * 12;

        mapNum = 2;
        i = 0;
        gp.monster[mapNum][i] = new Enemy_Santa(gp);
        gp.monster[mapNum][i].worldx = gp.tileSize * 35;
        gp.monster[mapNum][i].worldy = gp.tileSize * 13;

        mapNum = 3;
        i = 0;
        gp.monster[mapNum][i] = new Mon_Boss(gp);
        gp.monster[mapNum][i].worldx = gp.tileSize * 40;
        gp.monster[mapNum][i].worldy = gp.tileSize * 25;
        

    }
}
