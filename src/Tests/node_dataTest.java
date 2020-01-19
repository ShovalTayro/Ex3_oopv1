package Tests;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import org.junit.jupiter.api.Test;
import dataStructure.nodeData;
import utils.Point3D;

public class node_dataTest{

	@Test
	void getKeyTest(){
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
		nodeData node4 = new nodeData(4, 4, new Point3D(3, 4, 5));

		assertEquals(1, node1.getWeight());
		assertEquals(2, node2.getWeight());
		assertEquals(3, node3.getWeight());
		assertEquals(4, node4.getWeight());
	}

	@Test
	void getLocation() {
		nodeData node1 = new nodeData(1, 1, new Point3D(6, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 19, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 68));
		Point3D p1 = new Point3D(6, 1, 2);
		Point3D p2 = new Point3D(1, 19, 3);
		Point3D p3 = new Point3D(2, 3, 68);

		assertEquals(p1, node1.getLocation());
		assertEquals(p2, node2.getLocation());
		assertEquals(p3, node3.getLocation());
	}

	@Test
	void getWeight() {
		nodeData node1 = new nodeData(0, 9, new Point3D(5.3, 7, 1));
		nodeData node2 = new nodeData(5, 0, new Point3D(4, 1, 0));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));

		assertEquals(9, node1.getWeight());
		assertEquals(0, node2.getWeight());
		assertEquals(3, node3.getWeight());

	}

	@Test
	void getAndSetInfo() {
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
        node1.setInfo("hello");
        node2.setInfo("world");
        node3.setInfo("oop");
 
        assertEquals("hello", node1.getInfo());
        assertEquals("world", node2.getInfo());
        assertEquals("oop", node3.getInfo());
	}

	@Test
	void getAndSetTag() {
		nodeData node1 = new nodeData(1, 1, new Point3D(6, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 19, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 68));
        node1.setTag(1);
        node2.setTag(2);
        node3.setTag(3);
        assertEquals(node1.getTag() ,new Color(1).getRGB());
        assertEquals(node2.getTag() ,new Color(2).getRGB());
        assertEquals(node3.getTag() ,new Color(3).getRGB());
	}


}