package com.group5.main;

import com.group5.entity.Entity;

public class TestGamePanel extends GamePanel {

    public TestGamePanel() {
        super(); // initializes everything from real GamePanel

        // Override arrays so we can control them in tests
        obj = new Entity[maxMap][20];
        npc = new Entity[maxMap][20];
        monster = new Entity[maxMap][20];
    }
}
