package gameClient;

import Server.game_service;

/**
 * this interface represent the function of the automatic option to play our game
 * @author 
 *
 */
public interface PlayAutomatic {
	/**
	 *  the main method of the class using the methods below,
	 *  we read in every moment the fruits and robots from the server and choose for the robots the best way they need to go to collect fruits.
	 */
	public void playAutomat();
	/**
	 * function that choose the robot path for collecting fruits
	 * @param game
	 */
	public void moveAutomaticallyRobots(game_service game);
	/**
	 * if the edge has a fruit , put the robot on the src of the edge
	 * @param game
	 * @param check
	 */
	public void putRobot(game_service game, int check);
}
