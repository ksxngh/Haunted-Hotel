package com.group5.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Node} class.
 * Tests node creation, field initialization, and state management
 * for A* pathfinding algorithm.
 * 
 * Coverage Target: >90% instruction and branch coverage
 */
public class NodeTest {

    private Node node;

    @BeforeEach
    void setUp() {
        node = new Node(5, 10);
    }


    @Test
    void testConstructor_InitializesColumnAndRow() {
        Node testNode = new Node(3, 7);
        
        assertEquals(3, testNode.col);
        assertEquals(7, testNode.row);
    }

    @Test
    void testConstructor_WithZeroCoordinates() {
        Node testNode = new Node(0, 0);
        
        assertEquals(0, testNode.col);
        assertEquals(0, testNode.row);
    }

    @Test
    void testConstructor_WithLargeCoordinates() {
        Node testNode = new Node(999, 888);
        
        assertEquals(999, testNode.col);
        assertEquals(888, testNode.row);
    }

    @Test
    void testConstructor_WithNegativeCoordinates() {
        Node testNode = new Node(-5, -10);
        
        assertEquals(-5, testNode.col);
        assertEquals(-10, testNode.row);
    }

    @Test
    void testConstructor_DefaultFieldsAreZeroOrFalse() {
        Node testNode = new Node(5, 10);
        
        assertEquals(0, testNode.gCost);
        assertEquals(0, testNode.hCost);
        assertEquals(0, testNode.fCost);
        
        assertFalse(testNode.solid);
        assertFalse(testNode.open);
        assertFalse(testNode.checked);
        
        assertNull(testNode.parent);
    }


    @Test
    void testColField_CanBeAccessed() {
        assertEquals(5, node.col);
    }

    @Test
    void testRowField_CanBeAccessed() {
        assertEquals(10, node.row);
    }

    @Test
    void testColField_CanBeModified() {
        node.col = 15;
        assertEquals(15, node.col);
    }

    @Test
    void testRowField_CanBeModified() {
        node.row = 20;
        assertEquals(20, node.row);
    }


    @Test
    void testGCost_DefaultValue() {
        assertEquals(0, node.gCost);
    }

    @Test
    void testGCost_CanBeSet() {
        node.gCost = 15;
        assertEquals(15, node.gCost);
    }

    @Test
    void testGCost_CanBeSetToNegative() {
        node.gCost = -5;
        assertEquals(-5, node.gCost);
    }

    @Test
    void testHCost_DefaultValue() {
        assertEquals(0, node.hCost);
    }

    @Test
    void testHCost_CanBeSet() {
        node.hCost = 20;
        assertEquals(20, node.hCost);
    }

    @Test
    void testHCost_CanBeSetToNegative() {
        node.hCost = -10;
        assertEquals(-10, node.hCost);
    }

    @Test
    void testFCost_DefaultValue() {
        assertEquals(0, node.fCost);
    }

    @Test
    void testFCost_CanBeSet() {
        node.fCost = 35;
        assertEquals(35, node.fCost);
    }

    @Test
    void testFCost_IndependentOfGAndHCosts() {
        node.gCost = 10;
        node.hCost = 15;
        
        assertEquals(0, node.fCost);
        
        node.fCost = node.gCost + node.hCost;
        assertEquals(25, node.fCost);
    }

    @Test
    void testCosts_CanBeSetToLargeValues() {
        node.gCost = 999999;
        node.hCost = 888888;
        node.fCost = 1888887;
        
        assertEquals(999999, node.gCost);
        assertEquals(888888, node.hCost);
        assertEquals(1888887, node.fCost);
    }

    @Test
    void testCosts_MultipleSetsAndGets() {
        node.gCost = 5;
        assertEquals(5, node.gCost);
        
        node.gCost = 10;
        assertEquals(10, node.gCost);
        
        node.hCost = 15;
        assertEquals(15, node.hCost);
        
        node.fCost = node.gCost + node.hCost;
        assertEquals(25, node.fCost);
    }


    @Test
    void testSolid_DefaultValue() {
        assertFalse(node.solid);
    }

    @Test
    void testSolid_CanBeSetToTrue() {
        node.solid = true;
        assertTrue(node.solid);
    }

    @Test
    void testSolid_CanBeSetToFalse() {
        node.solid = true;
        node.solid = false;
        assertFalse(node.solid);
    }

    @Test
    void testOpen_DefaultValue() {
        assertFalse(node.open);
    }

    @Test
    void testOpen_CanBeSetToTrue() {
        node.open = true;
        assertTrue(node.open);
    }

    @Test
    void testOpen_CanBeSetToFalse() {
        node.open = true;
        node.open = false;
        assertFalse(node.open);
    }

    @Test
    void testChecked_DefaultValue() {
        assertFalse(node.checked);
    }

    @Test
    void testChecked_CanBeSetToTrue() {
        node.checked = true;
        assertTrue(node.checked);
    }

    @Test
    void testChecked_CanBeSetToFalse() {
        node.checked = true;
        node.checked = false;
        assertFalse(node.checked);
    }

    @Test
    void testBooleanFlags_AreIndependent() {
        node.solid = true;
        node.open = false;
        node.checked = true;
        
        assertTrue(node.solid);
        assertFalse(node.open);
        assertTrue(node.checked);
    }


    @Test
    void testParent_DefaultValue() {
        assertNull(node.parent);
    }

    @Test
    void testParent_CanBeSet() {
        Node parentNode = new Node(3, 8);
        node.parent = parentNode;
        
        assertNotNull(node.parent);
        assertEquals(parentNode, node.parent);
        assertEquals(3, node.parent.col);
        assertEquals(8, node.parent.row);
    }

    @Test
    void testParent_CanBeSetToNull() {
        Node parentNode = new Node(3, 8);
        node.parent = parentNode;
        node.parent = null;
        
        assertNull(node.parent);
    }

    @Test
    void testParent_CanFormChain() {
        Node grandParent = new Node(1, 1);
        Node parent = new Node(2, 2);
        Node child = new Node(3, 3);
        
        child.parent = parent;
        parent.parent = grandParent;
        
        assertEquals(parent, child.parent);
        assertEquals(grandParent, child.parent.parent);
        assertEquals(1, child.parent.parent.col);
        assertEquals(1, child.parent.parent.row);
    }

    @Test
    void testParent_CanPointToSelf() {
        node.parent = node;
        assertEquals(node, node.parent);
    }


    @Test
    void testNode_CompleteStateManagement() {
        Node testNode = new Node(10, 15);
        
        testNode.gCost = 5;
        testNode.hCost = 10;
        testNode.fCost = 15;
        testNode.solid = false;
        testNode.open = true;
        testNode.checked = false;
        testNode.parent = new Node(9, 14);
        
        assertEquals(10, testNode.col);
        assertEquals(15, testNode.row);
        assertEquals(5, testNode.gCost);
        assertEquals(10, testNode.hCost);
        assertEquals(15, testNode.fCost);
        assertFalse(testNode.solid);
        assertTrue(testNode.open);
        assertFalse(testNode.checked);
        assertNotNull(testNode.parent);
        assertEquals(9, testNode.parent.col);
        assertEquals(14, testNode.parent.row);
    }

    @Test
    void testNode_SimulatePathfindingStates() {
        Node startNode = new Node(0, 0);
        assertFalse(startNode.open);
        assertFalse(startNode.checked);
        assertFalse(startNode.solid);
        
        startNode.open = true;
        assertTrue(startNode.open);
        assertFalse(startNode.checked);
        
        startNode.open = false;
        startNode.checked = true;
        assertFalse(startNode.open);
        assertTrue(startNode.checked);
    }

    @Test
    void testNode_SimulatePathReconstruction() {
        Node start = new Node(0, 0);
        Node mid = new Node(1, 1);
        Node goal = new Node(2, 2);
        
        goal.parent = mid;
        mid.parent = start;
        
        Node current = goal;
        assertNotNull(current);
        assertEquals(2, current.col);
        
        current = current.parent;
        assertNotNull(current);
        assertEquals(1, current.col);
        
        current = current.parent;
        assertNotNull(current);
        assertEquals(0, current.col);
        
        assertNull(current.parent);
    }

    @Test
    void testNode_MultipleNodesWithDifferentStates() {
        Node node1 = new Node(0, 0);
        Node node2 = new Node(1, 1);
        Node node3 = new Node(2, 2);
        
        node1.solid = true;
        node2.open = true;
        node3.checked = true;
        
        assertTrue(node1.solid);
        assertFalse(node1.open);
        assertFalse(node1.checked);
        
        assertFalse(node2.solid);
        assertTrue(node2.open);
        assertFalse(node2.checked);
        
        assertFalse(node3.solid);
        assertFalse(node3.open);
        assertTrue(node3.checked);
    }

    @Test
    void testNode_ResetState() {
        node.gCost = 10;
        node.hCost = 15;
        node.fCost = 25;
        node.solid = true;
        node.open = true;
        node.checked = true;
        node.parent = new Node(4, 9);
        
        node.open = false;
        node.checked = false;
        node.solid = false;
        node.parent = null;
        
        assertFalse(node.open);
        assertFalse(node.checked);
        assertFalse(node.solid);
        assertNull(node.parent);
        
        assertEquals(10, node.gCost); 
        assertEquals(15, node.hCost); 
        assertEquals(25, node.fCost); 
    }


    @Test
    void testNode_WithMaxIntegerCoordinates() {
        Node testNode = new Node(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, testNode.col);
        assertEquals(Integer.MAX_VALUE, testNode.row);
    }

    @Test
    void testNode_WithMinIntegerCoordinates() {
        Node testNode = new Node(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, testNode.col);
        assertEquals(Integer.MIN_VALUE, testNode.row);
    }

    @Test
    void testCosts_WithMaxIntegerValues() {
        node.gCost = Integer.MAX_VALUE;
        node.hCost = Integer.MAX_VALUE;
        node.fCost = Integer.MAX_VALUE;
        
        assertEquals(Integer.MAX_VALUE, node.gCost);
        assertEquals(Integer.MAX_VALUE, node.hCost);
        assertEquals(Integer.MAX_VALUE, node.fCost);
    }

    @Test
    void testNode_SameCoordinates() {
        Node testNode = new Node(5, 5);
        assertEquals(5, testNode.col);
        assertEquals(5, testNode.row);
    }

    @Test
    void testNode_DifferentInstancesWithSameCoordinates() {
        Node node1 = new Node(5, 10);
        Node node2 = new Node(5, 10);
        
        assertEquals(node1.col, node2.col);
        assertEquals(node1.row, node2.row);
        
        assertNotSame(node1, node2);
        
        node1.solid = true;
        node2.solid = false;
        assertTrue(node1.solid);
        assertFalse(node2.solid);
    }

    @Test
    void testNode_CoordinateModification() {
        Node testNode = new Node(5, 10);
        assertEquals(5, testNode.col);
        assertEquals(10, testNode.row);
        
        testNode.col = 20;
        testNode.row = 30;
        
        assertEquals(20, testNode.col);
        assertEquals(30, testNode.row);
    }

    @Test
    void testNode_AllFieldsIndependent() {
        Node testNode = new Node(1, 2);
        
        testNode.col = 10;
        assertEquals(2, testNode.row);
        assertEquals(0, testNode.gCost);
        
        testNode.gCost = 5;
        assertEquals(10, testNode.col);
        assertEquals(0, testNode.hCost);
        
        testNode.solid = true;
        assertFalse(testNode.open);
        assertFalse(testNode.checked);
        
        testNode.parent = new Node(0, 0);
        assertTrue(testNode.solid);
        assertEquals(5, testNode.gCost);
    }
}
