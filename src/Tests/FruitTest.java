package Tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import dataStructure.edgeData;
import dataStructure.nodeData;
import dataStructure.ourFruit;
import utils.Point3D;

public class FruitTest {

	@Test
    public void getAndSetVisited() {
		ourFruit fruit1 = new ourFruit();
        ourFruit fruit2 = new ourFruit();
        fruit1.setVisited(true);
        fruit2.setVisited(false);
        assertTrue(fruit1.getVisited());
        assertFalse(fruit2.getVisited());
	}
	
	@Test
    public void getAndSetValue() {
		ourFruit fruit1 = new ourFruit();
        ourFruit fruit2 = new ourFruit();
        fruit1.setValue(23);
        fruit2.setValue(12);
        assertEquals(23, fruit1.getValue());
        assertEquals(12, fruit2.getValue());
	}
	
	@Test
    public void getAndSetType() {
		ourFruit fruit1 = new ourFruit();
        ourFruit fruit2 = new ourFruit();
        fruit1.setType(1);
        fruit2.setType(-1);
        assertEquals(1, fruit1.getType());
        assertEquals(-1, fruit2.getType());
	}
	
    @Test
    public void getAndSetPos() {
        ourFruit fruit1 = new ourFruit();
        ourFruit fruit2 = new ourFruit();
        ourFruit fruit3 = new ourFruit();
        Point3D p1 = new Point3D(0,1,2);
        Point3D p2 = new Point3D(1,2,3);
        Point3D p3 = new Point3D(3,4,5);
        fruit1.setPos(p1);
        fruit2.setPos(p2);
        fruit3.setPos(p3);
      
        assertEquals(p1, fruit1.getPos());
        assertEquals(p2, fruit2.getPos());
        assertEquals(p3, fruit3.getPos());
    }

    @Test
    public void getAndSetEdge() {
        ourFruit fruit1 = new ourFruit();
        ourFruit fruit2 = new ourFruit();
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
        edgeData edge1 = new edgeData(node1, node2, 4);
        edgeData edge2 = new edgeData(node2, node3, 12.567);
        
        fruit1.setEdge(edge1);
        fruit2.setEdge(edge2);
        assertEquals(edge1.getSrc(), fruit1.getEdge().getSrc());
        assertEquals(edge1.getDest(), fruit1.getEdge().getDest());
        assertEquals(edge2.getSrc(), fruit2.getEdge().getSrc());
        assertEquals(edge2.getDest(), fruit2.getEdge().getDest());
    }
}

