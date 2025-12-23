package com.group5.main;

import javax.swing.*;

/**
 * The {@code Main} class serves as the entry point for the game.
 * It creates the main application window, initializes the {@link GamePanel},
 * and starts the main game loop.
 * <p>
 * The window title, size, and close behavior are configured here.
 * Once initialized, the {@code GamePanel} handles rendering, updates,
 * and user input for the entire game.
 * </p>
 * 
 * <p><b>Game Title:</b> Haunted Hotel</p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class Main {

     /**
     * The main method that launches the game.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Haunted Hotel");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}