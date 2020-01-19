package dataStructure;

import utils.Point3D;

/**
 * This interface represents the fruit in the game
 */
public interface Fruit {
	/**
	 * return true if there is a robot on his way to the fruit , else return false
	 * @return
	 */
	public boolean getVisited();
	/**
	 * set what explained above
	 * @param visited
	 */
	public void setVisited(boolean visited);
	/**
	 * get the value of the fruit - the points you get by eating the fruit
	 * @return
	 */
	public int getValue();
	/**
	 *  set the value of the fruit getting from the server
	 * @param value
	 */
	public void setValue(int value);
	/**
	 * set the type of the fruit - the type of the fruit tells us from which node of the edge of the fruit you can eat the fruit
	 * @param type type=1 apple ,type=-1 banana
	 */
	public void setType(int type);
	/**
	 * get the  type of thefruit to tell us if this is banana or apple
	 * @return
	 */
	public int getType();
	/**
	 * get the position of the fruit on the graph , returns Point3D of the location
	 * @return
	 */
	public Point3D getPos();
	/**
	 * set the location of the fruit on the graph getting from the server.
	 * @param pos
	 */
	public void setPos(Point3D pos);
	/**
	 *  get us on which edge the fruit is on
	 * @return
	 */
	public edge_data getEdge();
	/**
	 * set the edge the fruit is on to the given edge
	 * @param edge
	 */
	public void setEdge(edge_data edge);
	/**
	 * this method init a fruit from the server , reading information of the fruit by jsonObject.
	 * @param g
	 */
	public void initFruit(String g);

}
