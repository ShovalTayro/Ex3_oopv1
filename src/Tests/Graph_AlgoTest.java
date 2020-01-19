package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.nodeData;
import dataStructure.node_data;
import utils.Point3D;

class Graph_AlgoTest {

	DGraph g = new DGraph();
	Graph_Algo a = new Graph_Algo();

	@BeforeEach
	public void createGraph() {
		for (int i = 0; i < 5; i++) {
			Point3D p = new Point3D(i, i+1, i+2);
			nodeData n = new nodeData(i, i, p);
			g.addNode(n);
		}
		a.init(g);
	}
	
		@Test
	void testSave() {
		try {
			a.save("graph.txt");
			Graph_Algo b = new Graph_Algo();
			b.init("graph.txt");
		}
		catch(Exception e)
		{
			System.out.println("can't save to file");
		}
	}

	@Test
	void testIsConnected() {
		g.connect(0, 1, 5);
		g.connect(1, 2, 4);
		g.connect(2, 3, 3);
		g.connect(3, 4, 2);
		g.connect(4, 0, 1);
		assertTrue(a.isConnected());

		g.removeEdge(0, 1);
		assertFalse(a.isConnected());

		DGraph graph = new DGraph();
		Graph_Algo graphA = new Graph_Algo();
		graph.addNode(new nodeData(0, 1, new Point3D(0,0,0)));
		graphA.init(graph);
		assertTrue(graphA.isConnected());		
	}

	@Test
	void testShortestPathDist() {
		g.connect(0, 1, 1);
		g.connect(0, 4, 1);
		g.connect(1, 2, 1);
		g.connect(1, 3, 5);
		g.connect(2, 3, 2);
		g.connect(4, 3, 1);
		g.connect(4, 1, 2);
		Graph_Algo graphA = new Graph_Algo();
		graphA.init(g);
		double ans = graphA.shortestPathDist(0, 3);
		assertEquals(2, ans);
		g.removeEdge(4, 3);
		ans = graphA.shortestPathDist(4, 3);
		assertEquals(5, ans);	
		 ans = graphA.shortestPathDist(0, 3);
		 assertEquals(4, ans);
	}

	@Test
	void testShortestPath() {
		g.connect(0,1,2);
		g.connect(1,4,4);
		g.connect(4,3,1);
		g.connect(1,2,1);
		g.connect(2,4,2);
		g.connect(3,2,5);
		g.connect(3,0,7);
		g.connect(0,4,10);
		g.connect(4,1,3);
		g.connect(2,1,2);

		Graph_Algo graphA = new Graph_Algo();
		graphA.init(g);
		List<node_data> ans = graphA.shortestPath(0, 3);
		nodeData a = new nodeData(0, 0, new Point3D(0,1,2));
		nodeData b = new nodeData(1, 0, new Point3D(1,2,3));
		nodeData c = new nodeData(2, 0, new Point3D(2,3,4));
		nodeData d = new nodeData(4, 0, new Point3D(4,5,6));
		nodeData e = new nodeData(3, 0, new Point3D(3,4,5));

		List<node_data> exp = new ArrayList<node_data>();
		exp.add(a);
		exp.add(b);
		exp.add(c);
		exp.add(d);
		exp.add(e);
		String actual = "";
		String expected = "";
		for (int i = 0; i < ans.size(); i++) {
			actual += ""+ans.get(i).getKey();
		}
		for (int i = 0; i < exp.size(); i++) {
			expected += ""+exp.get(i).getKey();
		}
		assertTrue(expected.equals(actual));
		
		g.removeEdge(0, 1);
		g.connect(0, 1 , 50);
		graphA.init(g);
		List<node_data> answ = graphA.shortestPath(0, 1);
		List<node_data> exp2 = new ArrayList<node_data>();
		nodeData a1 = new nodeData(0, 0, new Point3D(0,1,2));
		nodeData b1 = new nodeData(4, 0, new Point3D(4,5,6));
		nodeData c1 = new nodeData(1, 0, new Point3D(1,2,3));
		exp2.add(a1);
		exp2.add(b1);
		exp2.add(c1);
		
		String actual1 = "";
		String expected1 = "";
		for (int i = 0; i < answ.size(); i++) {
			actual1 += ""+answ.get(i).getKey();
		}
		for (int i = 0; i < exp2.size(); i++) {
			expected1 += ""+exp2.get(i).getKey();
		}
		assertTrue(expected1.equals(actual1));
	}

	@Test
	void testTSP() {	
		DGraph g = new DGraph();
		g.addNode(new nodeData(100,0, new Point3D(100,200,300)));
		g.addNode(new nodeData(200,0, new Point3D(200,300,400)));
		g.connect(100, 200, 5);
		g.connect(200, 100, 10);
		List<Integer> targets = new ArrayList<Integer>();
		targets.add(100);
		targets.add(200);
		targets.add(100);
		Graph_Algo a = new Graph_Algo();
		a.init(g);
		List<node_data> ans =a.TSP(targets);
		if(ans.isEmpty()) System.out.println("null");
		else {
			for (int i = 0; i < ans.size(); i++) {
				System.out.print(ans.get(i).getKey() + " ");
			}
		}
	}

	@Test
	void testCopy() {
		graph d = new DGraph();
		d = a.copy();
		nodeData n = new nodeData(5,5, new Point3D(5,6,7));
		d.addNode(n);
		assertEquals(5, g.nodeSize());
		assertEquals(6, d.nodeSize());
	}
}
