package com.group5.main;

import com.group5.entity.Entity;
import com.group5.object.Obj_Chest_Key;
import com.group5.object.Obj_Myers_Key;
import com.group5.monster.MyersEnemy;
import com.group5.monster.Enemy_Santa;
import com.group5.monster.Mon_Boss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssetSetterTest {

    TestGamePanel gp;
    AssetSetter setter;

    @BeforeEach
    void setup() {
        gp = new TestGamePanel();
        setter = new AssetSetter(gp);
    }

    @Test
    void testSetObject_spawnsChestKeyOnMap0() {
        setter.setObject();

        Entity firstObj = gp.obj[0][0];

        assertNotNull(firstObj);
        assertEquals(Obj_Chest_Key.class, firstObj.getClass());
        assertEquals(16 * gp.tileSize, firstObj.worldx);
        assertEquals(12 * gp.tileSize, firstObj.worldy);
    }

    @Test
    void testSetObject_spawnsMyersKeyOnMap1() {
        setter.setObject();

        Entity key = gp.obj[1][0];

        assertNotNull(key);
        assertEquals(Obj_Myers_Key.class, key.getClass());
    }

    @Test
    void testSetNPC_spawnsNPCOnMap0() {
        setter.setNPC();

        assertNotNull(gp.npc[0][0]);
        assertEquals(12 * gp.tileSize, gp.npc[0][0].worldx);
        assertEquals(6 * gp.tileSize, gp.npc[0][0].worldy);
    }

    @Test
    void testSetMonster_spawnsMonstersOnCorrectMaps() {
        setter.setMonster();

        assertNotNull(gp.monster[1][0]);
        assertEquals(MyersEnemy.class, gp.monster[1][0].getClass());

        assertNotNull(gp.monster[2][0]);
        assertEquals(Enemy_Santa.class, gp.monster[2][0].getClass());

        assertNotNull(gp.monster[3][0]);
        assertEquals(Mon_Boss.class, gp.monster[3][0].getClass());
    }
}
