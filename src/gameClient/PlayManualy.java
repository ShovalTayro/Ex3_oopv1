package gameClient;

import Server.game_service;
/**
 *this interface represent the method we use to play the game in manually way.
 */
public interface PlayManualy {
	/**
	 * the main method of the class , in this method we crate the graph to the scenario you want to play and play the game ,
	 * we read from the server the robots and fruits to set them on the graph and let you play the  game using mouse listener
	 */
	public void playManually();
	/**
	 * function that choose next edge for robots of the game
	 * @param game
	 */
	public void moveManualRobots(game_service game);
	/**
	 * method that helps the method above to choose the robot way
	 * @param game
	 * @return
	 */
	public int nextNodeManual(game_service game);
}
