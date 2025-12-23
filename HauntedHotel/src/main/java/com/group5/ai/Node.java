package com.group5.ai;

/**
 * The {@code Node} class represents a single tile or cell used in the pathfinding system.
 * Each node contains information about its position on the grid, whether it is solid (blocked),
 * and its cost values (G, H, and F) used for A* pathfinding.
 * 
 * <p>Nodes are connected via their {@code parent} field to allow reconstruction of the path
 * once the goal node has been reached.</p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class Node {

    /** Reference to the parent node (used for path reconstruction). */
    Node parent;

    /** The column index of the node in the grid. */
    public int col;

    /** The row index of the node in the grid. */
    public int row;

    /** The movement cost from the start node to this node (G cost). */
    int gCost;

    /** The estimated movement cost from this node to the goal node (H cost). */
    int hCost;

    /** The total cost (F cost), calculated as {@code gCost + hCost}. */
    int fCost;

    /** Indicates whether the node is solid (blocked or impassable). */
    boolean solid;

    /** Indicates whether the node has been added to the open list during search. */
    boolean open;

    /** Indicates whether the node has already been checked during search. */
    boolean checked;

    /**
     * Constructs a {@code Node} with the specified column and row position.
     *
     * @param col the column index of the node
     * @param row the row index of the node
     */
    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }
}