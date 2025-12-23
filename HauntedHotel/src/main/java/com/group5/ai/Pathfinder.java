package com.group5.ai;

import com.group5.entity.Entity;
import com.group5.main.GamePanel;

import java.util.ArrayList;

/**
 * The {@code Pathfinder} class implements an A* pathfinding system used for entity navigation within the game world.
 * It calculates the optimal path between a start and goal position, avoiding solid tiles and other obstacles.
 * 
 * <p>The pathfinder works by using a grid of {@link Node} objects that represent the world map. Each node
 * stores cost values (G, H, and F) to determine the most efficient path.</p>
 * 
 * <p>This class interacts closely with the {@link GamePanel} and tile management system to determine which
 * nodes are solid and cannot be traversed.</p>
 * 
 * @author Group 5
 * @version 1.0
 */
public class Pathfinder {

    /** Reference to the main {@link GamePanel} containing world and tile information. */
    GamePanel gp;

    /** 2D array of {@link Node} objects representing the game world grid. */
    Node[][] node;

    /** List of nodes that are being considered for expansion during the search (open list). */
    ArrayList<Node> openList = new ArrayList<>();

    /** Final list of nodes that make up the calculated path from start to goal. */
    public ArrayList<Node> pathList = new ArrayList<>();

    /** The starting node in the search. */
    Node startNode;

    /** The goal node the search is attempting to reach. */
    Node goalNode;

    /** The node currently being evaluated during the search. */
    Node currentNode;

    /** Flag indicating whether the goal has been successfully reached. */
    public boolean goalReached = false;

    /** Step counter used to prevent infinite loops during pathfinding. */
    int step = 0;

    /**
     * Constructs a {@code Pathfinder} for the given {@link GamePanel}.
     * Initializes the grid of nodes using the world dimensions.
     *
     * @param gp the main game panel reference
     */
    public Pathfinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    /**
     * Creates and initializes all nodes in the grid based on the world column and row limits.
     * Each node is assigned a column and row coordinate.
     */
    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row] = new Node(col, row);
            col++;

            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Resets all nodes in the grid to their default state by clearing their
     * open, checked, and solid flags. It also resets pathfinding data such as
     * open and path lists, step count, and goal status.
     */
    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;

            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    /**
     * Configures the pathfinding nodes by setting the start and goal positions
     * and marking solid tiles based on the current map’s tile data.
     *
     * @param startCol the column index of the start node
     * @param startRow the row index of the start node
     * @param goalCol  the column index of the goal node
     * @param goalRow  the row index of the goal node
     */
    public void setNode(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        // Ensure the goal node is never marked solid
        node[goalCol][goalRow].solid = false;

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            int tileNum = gp.tileM.mapTilenum[gp.currentMap][col][row];
            if (gp.tileM.tile[tileNum].collision == true) {
                node[col][row].solid = true;
            }
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Calculates the cost values for the specified node used in A* pathfinding.
     * <ul>
     *     <li>G Cost: Distance from the start node.</li>
     *     <li>H Cost: Estimated distance to the goal node.</li>
     *     <li>F Cost: Total cost (G + H).</li>
     * </ul>
     *
     * @param node the node for which to calculate the cost
     */
    public void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    /**
     * Executes the A* pathfinding search algorithm.
     * Expands nodes until the goal is reached or the step limit (500) is exceeded.
     *
     * @return {@code true} if the goal is reached, {@code false} otherwise
     */
    public boolean search() {
        while ((goalReached == false && step < 500)) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            if (row - 1 >= 0) openNode(node[col][row - 1]);
            if (col - 1 >= 0) openNode(node[col - 1][row]);
            if (row + 1 < gp.maxWorldRow) openNode(node[col][row + 1]);
            if (col + 1 < gp.maxWorldCol) openNode(node[col + 1][row]);

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if (openList.size() == 0) break;

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    /**
     * Adds a node to the open list if it is not solid, open, or checked.
     * Also assigns its parent node for path reconstruction.
     *
     * @param node the node to be opened
     */
    public void openNode(Node node) {
        if (node.open == false && node.checked == false && node.solid == false) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    /**
     * Traces back from the goal node to the start node using parent links
     * to reconstruct the final path and store it in {@code pathList}.
     */
    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
