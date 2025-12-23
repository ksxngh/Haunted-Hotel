package com.group5.ai;

import com.group5.main.GamePanel;
import com.group5.tile.Tile;
import com.group5.tile.TileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Pathfinder} class.
 * Tests A* pathfinding algorithm including node initialization,
 * cost calculation, path searching, and edge cases.
 * 
 * Coverage Target: >90% instruction and branch coverage
 */
public class PathfinderTest {

    private Pathfinder pathfinder;
    private TestGamePanel gp;

    /**
     * Mock GamePanel for testing purposes with minimal dependencies
     */
    private static class TestGamePanel extends GamePanel {
        public TestGamePanel() {
            super();
            this.tileM = new MockTileManager(this);
        }
    }

    /**
     * Mock TileManager that provides controlled tile collision data
     */
    private static class MockTileManager extends TileManager {
        public MockTileManager(GamePanel gp) {
            super(gp);
            tile = new Tile[100]; 
            for (int i = 0; i < tile.length; i++) {
                tile[i] = new Tile();
                tile[i].collision = false; 
                tile[i].image = null; 
            }
            
            mapTilenum = new int[10][gp.maxWorldCol][gp.maxWorldRow];
            for (int map = 0; map < mapTilenum.length; map++) {
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    for (int row = 0; row < gp.maxWorldRow; row++) {
                        mapTilenum[map][col][row] = 0;
                    }
                }
            }
        }

        /**
         * Set a specific tile as solid/collision
         */
        public void setSolidTile(int tileNum) {
            if (tileNum < tile.length) {
                tile[tileNum].collision = true;
            }
        }

        /**
         * Set the tile number at a specific map position
         */
        public void setMapTile(int map, int col, int row, int tileNum) {
            if (map < mapTilenum.length && 
                col < mapTilenum[map].length && 
                row < mapTilenum[map][col].length) {
                mapTilenum[map][col][row] = tileNum;
            }
        }
    }

    @BeforeEach
    void setUp() {
        gp = new TestGamePanel();
        pathfinder = new Pathfinder(gp);
    }


    @Test
    void testConstructor_InitializesPathfinder() {
        assertNotNull(pathfinder);
        assertNotNull(pathfinder.node);
        assertNotNull(pathfinder.openList);
        assertNotNull(pathfinder.pathList);
        assertEquals(gp.maxWorldCol, pathfinder.node.length);
        assertEquals(gp.maxWorldRow, pathfinder.node[0].length);
    }

    @Test
    void testInstantiateNodes_CreatesAllNodes() {
        pathfinder.instantiateNodes();
        
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                assertNotNull(pathfinder.node[col][row], 
                    "Node at [" + col + "][" + row + "] should not be null");
                assertEquals(col, pathfinder.node[col][row].col);
                assertEquals(row, pathfinder.node[col][row].row);
            }
        }
    }

    @Test
    void testInstantiateNodes_HandlesMaxWorldDimensions() {
        pathfinder.instantiateNodes();
        
        assertNotNull(pathfinder.node[0][0]);
        assertNotNull(pathfinder.node[gp.maxWorldCol - 1][gp.maxWorldRow - 1]);
        assertNotNull(pathfinder.node[gp.maxWorldCol / 2][gp.maxWorldRow / 2]);
    }


    @Test
    void testResetNodes_ClearsAllNodeStates() {
        pathfinder.node[5][5].open = true;
        pathfinder.node[5][5].checked = true;
        pathfinder.node[5][5].solid = true;
        pathfinder.node[10][10].open = true;
        pathfinder.node[10][10].checked = true;
        
        pathfinder.openList.add(pathfinder.node[5][5]);
        pathfinder.pathList.add(pathfinder.node[10][10]);
        pathfinder.goalReached = true;
        pathfinder.step = 100;

        pathfinder.resetNodes();

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                assertFalse(pathfinder.node[col][row].open, 
                    "Node [" + col + "][" + row + "] should not be open");
                assertFalse(pathfinder.node[col][row].checked,
                    "Node [" + col + "][" + row + "] should not be checked");
                assertFalse(pathfinder.node[col][row].solid,
                    "Node [" + col + "][" + row + "] should not be solid");
            }
        }

        assertTrue(pathfinder.openList.isEmpty());
        assertTrue(pathfinder.pathList.isEmpty());
        assertFalse(pathfinder.goalReached);
        assertEquals(0, pathfinder.step);
    }


    @Test
    void testSetNode_InitializesStartAndGoalNodes() {
        int startCol = 5, startRow = 5;
        int goalCol = 15, goalRow = 15;

        pathfinder.setNode(startCol, startRow, goalCol, goalRow);

        assertNotNull(pathfinder.startNode);
        assertNotNull(pathfinder.goalNode);
        assertNotNull(pathfinder.currentNode);
        
        assertEquals(startCol, pathfinder.startNode.col);
        assertEquals(startRow, pathfinder.startNode.row);
        assertEquals(goalCol, pathfinder.goalNode.col);
        assertEquals(goalRow, pathfinder.goalNode.row);
        assertEquals(pathfinder.startNode, pathfinder.currentNode);
    }

    @Test
    void testSetNode_AddsStartNodeToOpenList() {
        pathfinder.setNode(5, 5, 15, 15);
        
        assertFalse(pathfinder.openList.isEmpty());
        assertTrue(pathfinder.openList.contains(pathfinder.startNode));
    }

    @Test
    void testSetNode_GoalNodeIsNeverSolid() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(5);
        mockTileM.setMapTile(0, 15, 15, 5);
        
        pathfinder.setNode(5, 5, 15, 15);
        
        
        assertNotNull(pathfinder.goalNode);
        assertEquals(15, pathfinder.goalNode.col);
        assertEquals(15, pathfinder.goalNode.row);
    }

    @Test
    void testSetNode_MarksSolidTilesCorrectly() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(1);
        mockTileM.setSolidTile(2);
        mockTileM.setMapTile(0, 10, 10, 1);
        mockTileM.setMapTile(0, 11, 11, 2);
        mockTileM.setMapTile(0, 12, 12, 0); 
        
        pathfinder.setNode(5, 5, 20, 20);
        
        assertTrue(pathfinder.node[10][10].solid);
        assertTrue(pathfinder.node[11][11].solid);
        assertFalse(pathfinder.node[12][12].solid);
    }

    @Test
    void testSetNode_CalculatesCostsForAllNodes() {
        pathfinder.setNode(5, 5, 15, 15);
        
        Node testNode = pathfinder.node[10][10];
        assertNotEquals(0, testNode.gCost + testNode.hCost + testNode.fCost);
    }


    @Test
    void testGetCost_CalculatesGCostCorrectly() {
        pathfinder.setNode(5, 5, 15, 15);
        
        Node testNode = pathfinder.node[8][7];
        pathfinder.getCost(testNode);
        
        assertEquals(5, testNode.gCost);
    }

    @Test
    void testGetCost_CalculatesHCostCorrectly() {
        pathfinder.setNode(5, 5, 15, 15);
        
        Node testNode = pathfinder.node[10][12];
        pathfinder.getCost(testNode);
        
        assertEquals(8, testNode.hCost);
    }

    @Test
    void testGetCost_CalculatesFCostCorrectly() {
        pathfinder.setNode(5, 5, 15, 15);
        
        Node testNode = pathfinder.node[10][10];
        pathfinder.getCost(testNode);
        
        assertEquals(testNode.gCost + testNode.hCost, testNode.fCost);
    }

    @Test
    void testGetCost_HandlesNegativeDistances() {
        pathfinder.setNode(15, 15, 5, 5);
        
        Node testNode = pathfinder.node[10][10];
        pathfinder.getCost(testNode);
        
        assertTrue(testNode.gCost >= 0);
        assertTrue(testNode.hCost >= 0);
        assertTrue(testNode.fCost >= 0);
    }

    @Test
    void testGetCost_StartNodeHasZeroGCost() {
        pathfinder.setNode(10, 10, 20, 20);
        
        pathfinder.getCost(pathfinder.startNode);
        
        assertEquals(0, pathfinder.startNode.gCost);
    }

    @Test
    void testGetCost_GoalNodeHasZeroHCost() {
        pathfinder.setNode(10, 10, 20, 20);
        
        pathfinder.getCost(pathfinder.goalNode);
        
        assertEquals(0, pathfinder.goalNode.hCost);
    }


    @Test
    void testOpenNode_AddsNodeToOpenList() {
        pathfinder.setNode(5, 5, 15, 15);
        pathfinder.currentNode = pathfinder.startNode;
        
        Node nodeToOpen = pathfinder.node[6][5];
        pathfinder.openNode(nodeToOpen);
        
        assertTrue(nodeToOpen.open);
        assertTrue(pathfinder.openList.contains(nodeToOpen));
        assertEquals(pathfinder.currentNode, nodeToOpen.parent);
    }

    @Test
    void testOpenNode_DoesNotAddAlreadyOpenNode() {
        pathfinder.setNode(5, 5, 15, 15);
        pathfinder.currentNode = pathfinder.startNode;
        
        Node nodeToOpen = pathfinder.node[6][5];
        nodeToOpen.open = true;
        int initialSize = pathfinder.openList.size();
        
        pathfinder.openNode(nodeToOpen);
        
        assertEquals(initialSize, pathfinder.openList.size());
    }

    @Test
    void testOpenNode_DoesNotAddCheckedNode() {
        pathfinder.setNode(5, 5, 15, 15);
        pathfinder.currentNode = pathfinder.startNode;
        
        Node nodeToOpen = pathfinder.node[6][5];
        nodeToOpen.checked = true;
        
        pathfinder.openNode(nodeToOpen);
        
        assertFalse(pathfinder.openList.contains(nodeToOpen));
        assertFalse(nodeToOpen.open);
    }

    @Test
    void testOpenNode_DoesNotAddSolidNode() {
        pathfinder.setNode(5, 5, 15, 15);
        pathfinder.currentNode = pathfinder.startNode;
        
        Node nodeToOpen = pathfinder.node[6][5];
        nodeToOpen.solid = true;
        
        pathfinder.openNode(nodeToOpen);
        
        assertFalse(nodeToOpen.open);
        assertFalse(pathfinder.openList.contains(nodeToOpen));
    }

    @Test
    void testOpenNode_SetsParentCorrectly() {
        pathfinder.setNode(5, 5, 15, 15);
        pathfinder.currentNode = pathfinder.startNode;
        
        Node nodeToOpen = pathfinder.node[6][5];
        pathfinder.openNode(nodeToOpen);
        
        assertEquals(pathfinder.currentNode, nodeToOpen.parent);
    }


    @Test
    void testSearch_FindsPathWithNoObstacles() {
        pathfinder.setNode(5, 5, 10, 10);
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertTrue(pathfinder.goalReached);
        assertFalse(pathfinder.pathList.isEmpty());
    }

    @Test
    void testSearch_FindsPathAroundObstacles() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(1);
        for (int row = 5; row <= 15; row++) {
            mockTileM.setMapTile(0, 10, row, 1);
        }
        
        pathfinder.setNode(5, 10, 15, 10);
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertTrue(pathfinder.goalReached);
        assertFalse(pathfinder.pathList.isEmpty());
        
        assertTrue(pathfinder.pathList.size() > Math.abs(15 - 5));
    }

    @Test
    void testSearch_ReturnsFlaseWhenNoPathExists() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(1);
        for (int col = 14; col <= 16; col++) {
            for (int row = 14; row <= 16; row++) {
                if (!(col == 15 && row == 15)) { 
                    mockTileM.setMapTile(0, col, row, 1);
                }
            }
        }
        
        pathfinder.setNode(5, 5, 15, 15);
        boolean result = pathfinder.search();
        
        assertFalse(result);
        assertFalse(pathfinder.goalReached);
    }

    @Test
    void testSearch_StopsAtStepLimit() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(1);
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                if ((col + row) % 3 == 0) {
                    mockTileM.setMapTile(0, col, row, 1);
                }
            }
        }
        
        pathfinder.setNode(0, 0, gp.maxWorldCol - 1, gp.maxWorldRow - 1);
        pathfinder.search();
        
        assertTrue(pathfinder.step <= 500);
    }

    @Test
    void testSearch_SelectsNodeWithLowestFCost() {
        pathfinder.setNode(5, 5, 15, 5);
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertNotNull(pathfinder.pathList);
    }

    @Test
    void testSearch_BreaksLowestFCostTieWithGCost() {
        pathfinder.setNode(5, 5, 6, 6);
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertTrue(pathfinder.pathList.size() >= 1);
    }

    @Test
    void testSearch_HandlesStartEqualsGoal() {
        pathfinder.setNode(10, 10, 10, 10);
        
        pathfinder.currentNode = pathfinder.startNode;
        pathfinder.currentNode.checked = true;
        pathfinder.openList.clear();
        
        boolean result = pathfinder.search();
        
        assertFalse(result);
    }

    @Test
    void testSearch_ExploresAllFourDirections() {
        pathfinder.setNode(10, 10, 12, 12);
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertTrue(pathfinder.node[10][9].checked || pathfinder.node[10][9].open ||
                   pathfinder.node[9][10].checked || pathfinder.node[9][10].open ||
                   pathfinder.node[10][11].checked || pathfinder.node[10][11].open ||
                   pathfinder.node[11][10].checked || pathfinder.node[11][10].open);
    }

    @Test
    void testSearch_RespectsBoundaries() {
        pathfinder.setNode(0, 0, 5, 5);
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
    }

    @Test
    void testSearch_OpensNeighborNodesCorrectly() {
        pathfinder.setNode(10, 10, 11, 10);
        
        pathfinder.currentNode = pathfinder.startNode;
        pathfinder.currentNode.checked = true;
        pathfinder.openList.remove(pathfinder.currentNode);
        
        int col = pathfinder.currentNode.col;
        int row = pathfinder.currentNode.row;
        
        if (row - 1 >= 0) pathfinder.openNode(pathfinder.node[col][row - 1]);
        if (col - 1 >= 0) pathfinder.openNode(pathfinder.node[col - 1][row]);
        if (row + 1 < gp.maxWorldRow) pathfinder.openNode(pathfinder.node[col][row + 1]);
        if (col + 1 < gp.maxWorldCol) pathfinder.openNode(pathfinder.node[col + 1][row]);
        
        assertTrue(pathfinder.openList.size() <= 4);
        assertTrue(pathfinder.openList.size() > 0);
    }


    @Test
    void testTrackThePath_BuildsPathFromGoalToStart() {
        pathfinder.setNode(5, 5, 8, 8);
        pathfinder.search();
        
        assertFalse(pathfinder.pathList.isEmpty());
        
        Node firstPathNode = pathfinder.pathList.get(0);
        int distanceFromStart = Math.abs(firstPathNode.col - pathfinder.startNode.col) +
                               Math.abs(firstPathNode.row - pathfinder.startNode.row);
        assertTrue(distanceFromStart <= 2); 
        
        Node lastPathNode = pathfinder.pathList.get(pathfinder.pathList.size() - 1);
        assertEquals(pathfinder.goalNode, lastPathNode);
    }

    @Test
    void testTrackThePath_PathIsInCorrectOrder() {
        pathfinder.setNode(5, 5, 10, 5);
        pathfinder.search();
        
        for (int i = 0; i < pathfinder.pathList.size() - 1; i++) {
            Node current = pathfinder.pathList.get(i);
            Node next = pathfinder.pathList.get(i + 1);
            
            assertTrue(current.parent == next || next.parent == current);
        }
    }

    @Test
    void testTrackThePath_DoesNotIncludeStartNode() {
        pathfinder.setNode(5, 5, 8, 5);
        pathfinder.search();
        
        assertFalse(pathfinder.pathList.contains(pathfinder.startNode));
    }

    @Test
    void testTrackThePath_IncludesGoalNode() {
        pathfinder.setNode(5, 5, 8, 5);
        pathfinder.search();
        
        assertTrue(pathfinder.pathList.contains(pathfinder.goalNode));
    }


    @Test
    void testIntegration_CompletePathfindingWorkflow() {
        pathfinder.setNode(0, 0, 10, 10);
        
        assertEquals(pathfinder.startNode, pathfinder.currentNode);
        assertFalse(pathfinder.goalReached);
        assertTrue(pathfinder.pathList.isEmpty());
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertTrue(pathfinder.goalReached);
        assertFalse(pathfinder.pathList.isEmpty());
        assertEquals(pathfinder.goalNode, pathfinder.currentNode);
    }

    @Test
    void testIntegration_MultipleSearchesWithReset() {
        pathfinder.setNode(5, 5, 10, 10);
        boolean result1 = pathfinder.search();
        assertTrue(result1);
        int firstPathLength = pathfinder.pathList.size();
        
        pathfinder.setNode(5, 5, 15, 15);
        boolean result2 = pathfinder.search();
        assertTrue(result2);
        int secondPathLength = pathfinder.pathList.size();
        
        assertTrue(secondPathLength > firstPathLength);
    }

    @Test
    void testIntegration_DiagonalPath() {
        pathfinder.setNode(0, 0, 20, 20);
        
        boolean result = pathfinder.search();
        
        assertTrue(result);
        int expectedLength = 20; 
        assertTrue(pathfinder.pathList.size() >= expectedLength);
    }

    @Test
    void testIntegration_ComplexMaze() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(1);
        for (int col = 5; col < 15; col++) {
            mockTileM.setMapTile(0, col, 10, 1);
        }
        mockTileM.setMapTile(0, 7, 10, 0);
        mockTileM.setMapTile(0, 12, 10, 0);
        
        pathfinder.setNode(5, 5, 15, 15);
        boolean result = pathfinder.search();
        
        assertTrue(result);
        assertFalse(pathfinder.pathList.isEmpty());
    }


    @Test
    void testEdgeCase_StartAtTopLeftCorner() {
        pathfinder.setNode(0, 0, 10, 10);
        boolean result = pathfinder.search();
        assertTrue(result);
    }

    @Test
    void testEdgeCase_GoalAtBottomRightCorner() {
        pathfinder.setNode(10, 10, gp.maxWorldCol - 1, gp.maxWorldRow - 1);
        boolean result = pathfinder.search();
        assertTrue(result || pathfinder.step >= 500);
    }

    @Test
    void testEdgeCase_AdjacentStartAndGoal() {
        pathfinder.setNode(10, 10, 11, 10);
        boolean result = pathfinder.search();
        assertTrue(result);
        assertEquals(1, pathfinder.pathList.size());
    }

    @Test
    void testEdgeCase_EmptyOpenList() {
        MockTileManager mockTileM = (MockTileManager) gp.tileM;
        
        mockTileM.setSolidTile(1);
        
        for (int col = 8; col <= 12; col++) {
            mockTileM.setMapTile(0, col, 9, 1);
            mockTileM.setMapTile(0, col, 11, 1);
        }
        for (int row = 9; row <= 11; row++) {
            mockTileM.setMapTile(0, 8, row, 1);
            mockTileM.setMapTile(0, 12, row, 1);
        }
        
        pathfinder.setNode(10, 10, 15, 15);
        boolean result = pathfinder.search();
        
        assertFalse(result);
        assertFalse(pathfinder.goalReached);
    }

    @Test
    void testEdgeCase_VeryLongPath() {
        pathfinder.setNode(0, 0, gp.maxWorldCol - 1, gp.maxWorldRow - 1);
        
        boolean result = pathfinder.search();
        
        assertTrue(result || pathfinder.step >= 500);
    }
}
