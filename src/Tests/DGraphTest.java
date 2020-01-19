package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.nodeData;
import dataStructure.node_data;
import utils.Point3D;

class DGraphTest {
	DGraph g = new DGraph();

	@BeforeEach
	public void createGraph() {
		for (int i = 0; i < 5; i++) {
			Point3D p = new Point3D(i, i+1, i+2);
			nodeData n = new nodeData(i, i, p);
			g.addNode(n);
		}
	}

	@Test
	void testAddNode() {
		DGraph a = new DGraph();
		for (int i = 0; i < 1000000; i++) {
			Point3D p = new Point3D(i, i+1, i+2);
			nodeData n = new nodeData(i, i, p);
			a.addNode(n);
		}
		assertEquals(1000000, a.nodeSize());
		try {
			node_data n1= a.getNode(0);
			a.addNode(n1);
		}
		catch(Exception e)
		{
			System.out.println("The node already exist");
		}
	}

	@Test
	void testConnect() {
		DGraph a = new DGraph();
		for (int i = 0; i < 1000000; i++) {
			Point3D p = new Point3D(i, i+1, i+2);
			nodeData n = new nodeData(i, i, p);
			a.addNode(n);
		}
		for (int i = 0; i < 1000000-10; i++) {
			for (int j = 1; j < 11; j++) {
				a.connect(i, i+j, 0);
			}
		}
		assertEquals(9999900, a.edgeSize());
		try {
			g.connect(1, 1, 0);
		}
		catch(Exception e ) {
			System.out.println("same node can't make edge");
		}
		try {
			g.connect(1, 2, -5);
		}
		catch(Exception e ) {
			System.out.println("negative weight ,  can't make edge");
		}
		try {
			g.connect(-1, 2, 3);
		}
		catch(Exception e ) {
			System.out.println("one of the nodes doesn't exist ,  can't make edge");
		}
	}


	@Test
	void testGetV() {
		Collection<node_data> actual= g.getV();
		assertTrue(actual.equals(g.getV()));
	}

	@Test
	void testGetE() {
		for(int j=1;j<5;j++) {
			g.connect(0, j, 0);
		}
		Collection<edge_data> actual= g.getE(0);
		assertTrue(actual.equals(g.getE(0)));
	}

	@Test
	void testRemoveNode() {
		for(int j=1;j<5;j++) {
			g.connect(0, j, 0);
		}
		g.removeNode(0);
		assertEquals(0, g.edgeSize());
		assertEquals(4, g.nodeSize());

		if (g.removeNode(-1)== null) System.out.println("the node does not exist");
		if(g.removeNode(0)== null)	System.out.println("the node does not exist");
	}

	@Test
	void testRemoveEdge() {
		g.connect(0, 1, 0);
		g.connect(1, 0, 1);
		try {
			g.removeEdge(0, 1);
			g.removeEdge(2, 3);
		}
		catch(Exception E) {
		}
		assertEquals(1, g.edgeSize());
		try {
			g.removeEdge(-2, 2);
		}
		catch(Exception E) {
		}
		try {
			g.removeEdge(2, 2);
		}
		catch(Exception E) {
		}
	}

	@Test
	void testGetMC() {
		DGraph d = new DGraph();
		for (int i = 0; i < 5; i++) {
			Point3D p = new Point3D(i, i+1, i+2);
			nodeData n = new nodeData(i, i, p);
			d.addNode(n);
		}
		assertEquals(5, d.getMC());
		d.connect(0, 1, 0);
		d.removeNode(0);
		assertEquals(7, d.getMC());
	}

}
