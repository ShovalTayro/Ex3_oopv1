package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import dataStructure.edgeData;
import dataStructure.nodeData;
import dataStructure.node_data;
import dataStructure.ourRobots;
import utils.Point3D;

public class RobotTest {

	@Test
	public void getAndSetId() {
		ourRobots robot1 = new ourRobots();
		ourRobots robot2 = new ourRobots();
		ourRobots robot3 = new ourRobots();
		robot1.setId(1);
		robot2.setId(2);
		robot3.setId(0);
		assertEquals(1, robot1.getId());
		assertEquals(2, robot2.getId());
		assertEquals(0, robot3.getId());
	}

	@Test
	public void getAndSetPos() {
		ourRobots robot1 = new ourRobots();
		ourRobots robot2 = new ourRobots();
		ourRobots robot3 = new ourRobots();
		Point3D p1 = new Point3D(0,1,2);
		Point3D p2 = new Point3D(1,2,3);
		Point3D p3 = new Point3D(3,4,5);
		robot1.setPos(p1);
		robot2.setPos(p2);
		robot3.setPos(p3);
		assertEquals(p1, robot1.getPos());
		assertEquals(p2, robot2.getPos());
		assertEquals(p3, robot3.getPos());
	}
	@Test
	public void getAndSetSpeed() {
		ourRobots robot1 = new ourRobots();
		ourRobots robot2 = new ourRobots();
		ourRobots robot3 = new ourRobots();
		robot1.setSpeed(1);
		robot2.setSpeed(5.17);
		robot3.setSpeed(0);
		assertEquals(1, robot1.getSpeed());
		assertEquals(5.17, robot2.getSpeed());
		assertEquals(0, robot3.getSpeed());
	}

	@Test
	public void getAndSetNode() {
		ourRobots robot1 = new ourRobots();
		ourRobots robot2 = new ourRobots();
		ourRobots robot3 = new ourRobots();
		nodeData node1 = new nodeData(0, 9, new Point3D(5.3, 7, 1));
		nodeData node2 = new nodeData(5, 0, new Point3D(4, 1, 0));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
		robot1.setNode(node1);
		robot2.setNode(node2);
		robot2.setNode(node3);
		assertEquals(0, robot1.getNode().getKey());
		assertEquals(5, robot2.getNode().getKey());
		assertEquals(3, robot3.getNode().getKey());
	}

	@Test
	public void getAndSetEdge() {
		ourRobots robot1 = new ourRobots();
		ourRobots robot2 = new ourRobots();
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
		edgeData edge1 = new edgeData(node1, node2, 4);
		edgeData edge2 = new edgeData(node2, node3, 12.567);

		robot1.setEdge(edge1);
		robot2.setEdge(edge2);
		assertEquals(edge1.getSrc(), robot1.getEdge().getSrc());
		assertEquals(edge1.getDest(), robot1.getEdge().getDest());
		assertEquals(edge2.getSrc(), robot2.getEdge().getSrc());
		assertEquals(edge2.getDest(), robot2.getEdge().getDest());
	}

	@Test
	public void getAndSetPath() {
		ourRobots robot1 = new ourRobots();
		nodeData node1 = new nodeData(1, 1, new Point3D(0, 1, 2));
		nodeData node2 = new nodeData(2, 2, new Point3D(1, 2, 3));
		nodeData node3 = new nodeData(3, 3, new Point3D(2, 3, 4));
		List<node_data> path1 = new ArrayList<node_data>();
		path1.add(node1);
		path1.add(node2);
		path1.add(node3);
		robot1.setPath(path1);
		for (int i = 0; i < path1.size(); i++) {
			assertEquals(1, robot1.getPath().get(i));
		}
	}
}