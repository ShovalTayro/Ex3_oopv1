package dataStructure;

import java.util.List;
import utils.Point3D;
/**
 * This interface represents the fruit in the game
 */
public interface Robot {
	/**
	 * returns the I.D. of the robot , every  robot have different id
	 * @return int representing the I.D of the robot
	 */
	public int getId();
	/**
	 *  set the I.D of the robot to the given I.D
	 * @param id
	 */
	public void setId(int id);
	/**
	 * get the position of the  robot  on the graph
	 * @return Point3D that represent the location of the robot at this moment on the graph
	 */
	public Point3D getPos();
	/**
	 *  set the position of the robot on the graph to the given point
	 * @param pos
	 */
	public void setPos(Point3D pos);
	/**
	 * this method return the speed of the robot.
	 * @return
	 */
	public double getSpeed();
	/**
	 *  set the speed of the robot to the given speed
	 * @param speed
	 */
	public void setSpeed(double speed);
	/**
	 * this method tells us on which node on the graph the robot is on now.
	 * @return the node the robot is on.
	 */
	public node_data getNode();
	/**
	 *  set the  robot node(and position) to be this node
	 * @param node
	 */
	public void setNode(node_data node);
	/**
	 *  get the edge the robot is in , help to tell us the way of the robot
	 * @return
	 */
	public edge_data getEdge();
	/**
	 *  set the robot edge to be the given edge.
	 * @param edge
	 */
	public void setEdge(edge_data edge);
	/**
	 * list on nodes represent the way of the robot to the fruit he want to collect.
	 * @return
	 */
	public List<node_data> getPath();
	/**
	 *  set the path of the robot to the given path.
	 * @param path
	 */
	public void setPath(List<node_data> path);
	/**
	 * set the graph the robot is on to be the given graph.
	 * @param gr
	 */
	public void setGraph(graph gr);
	/**
	 * this method init robots reading the information about any robot from the server and set them on our graph , using jsonObject.
	 * @param g
	 */
	public void initRobots(String g);
}
