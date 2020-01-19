package Tests;

import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dataStructure.edgeData;
import dataStructure.nodeData;
import utils.Point3D;

class edge_dataTest {

    @Test
    void getSrcDest() {
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
		nodeData node4 = new nodeData(4, 4, new Point3D(3, 4, 5));
        edgeData edge1 = new edgeData(node1, node2, 1.001);
        edgeData edge2 = new edgeData(node2, node3, 2.002);
        edgeData edge3 = new edgeData(node3, node4, 3.003);
        edgeData edge4 = new edgeData(node4, node1, 4.004);
  
        assertEquals(edge1.getSrc(), 1);
        assertEquals(edge1.getDest(), 2);
        assertEquals(edge2.getDest(), edge3.getSrc());
        assertEquals(edge1.getSrc(), edge4.getDest());
    }

    @Test
    void getWeight() {
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
		nodeData node4 = new nodeData(4, 4, new Point3D(3, 4, 5));
        edgeData edge1 = new edgeData(node1, node2, 1.001);
        edgeData edge2 = new edgeData(node2, node3, 2.002);
        edgeData edge3 = new edgeData(node3, node4, 3.003);
        edgeData edge4 = new edgeData(node4, node1, 4.004);
        assertEquals(edge1.getWeight(), 1.001);
        assertEquals(edge2.getWeight(), 2.002);
        assertEquals(edge3.getWeight(), 3.003);
        assertEquals(edge4.getWeight(), 4.004);
    }

    @Test
    void getAndSetInfo() {
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
        edgeData edge1 = new edgeData(node1, node2, 23);
        edgeData edge2 = new edgeData(node2, node3, 11.002);
        edgeData edge3 = new edgeData(node3, node1, 32);

        edge1.setInfo("hello");
        edge2.setInfo("world");
        edge3.setInfo("oop");
 
        assertEquals(edge1.getInfo(),"hello");
        assertEquals(edge2.getInfo(),"world");
        assertEquals(edge3.getInfo(),"oop");

    }

    @Test
    void getAndSetTag() {
		nodeData node1 = new nodeData(1, 1, new Point3D(6, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 19, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 68));
        edgeData edge1 = new edgeData(node1, node2, 23);
        edgeData edge2 = new edgeData(node2, node3, 11);
        edgeData edge3 = new edgeData(node3, node1, 32.98);
        edge1.setTag(1);
        edge2.setTag(2);
        edge3.setTag(3);
        assertEquals(edge1.getTag() ,new Color(1).getRGB());
        assertEquals(edge2.getTag() ,new Color(2).getRGB());
        assertEquals(edge3.getTag() ,new Color(3).getRGB());
    }
}

