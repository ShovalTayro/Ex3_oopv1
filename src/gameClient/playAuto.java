package gameClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edgeData;
import dataStructure.edge_data;
import dataStructure.nodeData;
import dataStructure.node_data;
import dataStructure.ourFruit;
import dataStructure.ourRobots;
import gui.Clock;

public class playAuto {
	MyGameGUI MyGame;
	long tempSleep = MyGameGUI.sleepy;
	static int scenario_num;
	static int id;
	public playAuto(MyGameGUI MyGame) {
		this.MyGame = MyGame;
	}

	public void playAutomat() {
		try{
			String userID = JOptionPane.showInputDialog(null, "Enter ID number : ");
			id = Integer.parseInt(userID);
			Game_Server.login(id);
			//read scenario from the player paint the graph and fruits
			String num = JOptionPane.showInputDialog(null, "Enter a scenario you want to play : ");
			int scenario_num = Integer.parseInt(num);
			if(scenario_num>=0 && scenario_num<=23) {
				game_service game = Game_Server.getServer(scenario_num);
				String g = game.getGraph();
				DGraph gg = new DGraph();
				gg.init(g);
				this.MyGame.gr = gg;
				Iterator<String> fruit_iter = game.getFruits().iterator();
				//clear fruits collection if needed
				if(this.MyGame.fruits!= null){
					this.MyGame.fruits.clear();
				}
				else{
					this.MyGame.fruits = new ArrayList<ourFruit>();
				}
				//set all fruits in their places
				while(fruit_iter.hasNext()){
					String sFruit = fruit_iter.next();
					ourFruit f = new ourFruit();
					f.initFruit(sFruit);
					this.MyGame.fruits.add(f);
					MyGameGUI.findFruitEdge(f, gg);
				}
				this.MyGame.initGUI(this.MyGame.gr);
				this.MyGame.paint(game);
				String gameString = game.toString();
				JSONObject obj = new JSONObject(gameString);
				JSONObject CurrGame = (JSONObject) obj.get("GameServer");
				int nomOfRobots = CurrGame.getInt("robots");
				int check = 0;
				//	if the robots arrayList not empty clear
				if(this.MyGame.rob != null)this.MyGame.rob.clear();
				else{
					this.MyGame.rob = new ArrayList<ourRobots>();
				}

				//put the robots automatically , update the list and repaint
				while(check < nomOfRobots)	{
					putRobot(game, check);
					check++;
				}
				this.MyGame.paint(game);
				MyGameGUI.sleepy= changSleppy(scenario_num);
				//	start the game
				game.startGame();
				//Timer
				Clock k = new Clock(game);

				//thread for move the robots
				this.MyGame.movements(game);

				//create kml file
				this.MyGame.kmlLogger.setGame(game);
				this.MyGame.createKML(game);
				while(game.isRunning()) {
					moveAutomaticallyRobots(game);	
				}
				//save the kml to file with the name of the scenario
				this.MyGame.kmlLogger.saveToFile(scenario_num+ ".kml");

				// build a string from the kml and send him to the data base
				StringBuilder contentBuilder = new StringBuilder();
				try (BufferedReader br = new BufferedReader(new FileReader(scenario_num+ ".kml"))) 
				{

					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) 
					{
						contentBuilder.append(sCurrentLine).append("\n");
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				String kmlString = contentBuilder.toString();

				game.sendKML(kmlString);


				//show result on window
				String res = game.toString();
				JSONObject object = new JSONObject(res);
				JSONObject cgame = (JSONObject) object.get("GameServer");
				int points = cgame.getInt("grade");
				int moves = cgame.getInt("moves");

				JOptionPane.showMessageDialog(null,  "GAME OVER"+ "\nComputer points: " + points +"\nComputer move: " + moves);	
			}

			//the scenario is not exist throw error
			else{
				JOptionPane.showMessageDialog(null, "Error , the scenario you choose does not exist. ");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**\
	 * this method change the thread sleep to the level we played for getting better results
	 * @param scenario_num
	 * @return
	 */
	private long changSleppy(int scenario_num) {
		switch(scenario_num) {
		case 0 :
			return 110;
		case 1 :
			return 100;
		case 3: 
			return 105;
		case 5:
			return 110;
		case 9:
			return 105;
		case 11:
			return 105;
		case 13:
			return 105;
		case 16:
			return 110;
		case 19 :
			return 120;
		case 20: 
			return 110;
		case 23:
			return 90;
		default :
			return 100;
		}
	}

	//function that choose the robot path for collecting fruits
	public void moveAutomaticallyRobots(game_service game) {
		ourFruit temp = new ourFruit();
		Graph_Algo a = new Graph_Algo();
		a.init(this.MyGame.gr);

		//find the shortest path between each robot to a fruit on the game
		for (ourRobots  roby : this.MyGame.rob) {
			double dist = Double.MAX_VALUE;
			for (ourFruit f : this.MyGame.fruits) {
				if(dist>(a.shortestPathDist(roby.getNode().getKey(), f.getEdge().getSrc())+f.getEdge().getWeight())&&(!f.getVisited())) {
					dist =a.shortestPathDist(roby.getNode().getKey(), f.getEdge().getSrc())+f.getEdge().getWeight();
					temp=f;
				}
			}
			roby.setPath(a.shortestPath(roby.getNode().getKey(), temp.getEdge().getSrc()));
			if(roby.getPath().size()>1)	roby.getPath().remove(0);
			roby.getPath().add(this.MyGame.gr.getNode(temp.getEdge().getDest()));
			for (ourFruit f : this.MyGame.fruits) {
				if(f.getPos()==temp.getPos()) {
					f.setVisited(true);
				}
			}
		}

		//move the robots by the path we find for him
		for (int i = 0; i < this.MyGame.rob.size(); i++) {
			ourRobots roby = this.MyGame.rob.get(i);
			while(!roby.getPath().isEmpty()) {
				game.chooseNextEdge(roby.getId(), roby.getPath().get(0).getKey());
				roby.getPath().remove(0);
			}
		}	
		this.MyGame.paint(game);
	}	

	private ourFruit hasFruit(edge_data e) {	
		for (ourFruit f : this.MyGame.fruits) {
			if(f.getEdge().getSrc()== e.getSrc()&& f.getEdge().getDest()== e.getDest()) return f;
		}
		return null;
	}

	//if the edge has a fruit , put the robot on the src of the edge
	public void putRobot(game_service game, int check) {
		for (ourFruit f : this.MyGame.fruits) {
			if(!f.getVisited()) {
				node_data n = this.MyGame.gr.getNode(f.getEdge().getSrc());
				game.addRobot(n.getKey());
				ourRobots r = new ourRobots(check, n.getLocation(), 1 , n, null , this.MyGame.gr);
				this.MyGame.rob.add(r);
				r.setGraph(this.MyGame.gr);
				f.setVisited(true);
				break;	
			}
		}
	}
	/**
	 * this method show in the gui the user best stats in every level
	 */
	public static void userStats() {
		int[] numberGame = dataBase_Function.numberOfGames(id);
		int[][] bestCase = dataBase_Function.bestCase(id);
		JOptionPane.showMessageDialog(null, "Current Stage: " + scenario_num +
				"\nAmount of Game: " + numberGame[0] +
				"\nYour Best Stats Are:" + 
				"\nStage: 0" + " ,Grade: " + bestCase[0][0] + " ,Moves: " + bestCase[1][0] +
				"\nStage: 1" + " ,Grade: " + bestCase[0][1] + " ,Moves: " + bestCase[1][1] +
				"\nStage: 2" + " ,Grade: " + bestCase[0][2] + " ,Moves: " + bestCase[1][2] + 
				"\nStage: 3" + " ,Grade: " + bestCase[0][3] + " ,Moves: " + bestCase[1][3] + 
				"\nStage: 4" + " ,Grade: " + bestCase[0][4] + " ,Moves: " + bestCase[1][4] + 
				"\nStage: 5" + " ,Grade: " + bestCase[0][5] + " ,Moves: " + bestCase[1][5] +
				"\nStage: 6" + " ,Grade: " + bestCase[0][6] + " ,Moves: " + bestCase[1][6] +
				"\nStage: 7" + " ,Grade: " + bestCase[0][7] + " ,Moves: " + bestCase[1][7] +
				"\nStage: 8" + " ,Grade: " + bestCase[0][8] + " ,Moves: " + bestCase[1][8] +
				"\nStage: 9" + " ,Grade: " + bestCase[0][9] + " ,Moves: " + bestCase[1][9] +
				"\nStage: 11" + " ,Grade: " + bestCase[0][11] + " ,Moves: " + bestCase[1][11]
				);

	}
	/**
	 * this method show the user rank in fron of the pther players
	 */
	public static void rank() {
		int[][] bestCase = dataBase_Function.bestCase(id);
		JOptionPane.showMessageDialog(null, "Current Stage: " + scenario_num +
				"\nYour Rank:" + 
				"\nStage: 0" + " ,Rank: " + bestCase[2][0] + 
				"\nStage: 1" + " ,Rank: " + bestCase[2][1] + 
				"\nStage: 2" + " ,Rank: " + bestCase[2][2] + 
				"\nStage: 3" + " ,Rank: " + bestCase[2][3] + 
				"\nStage: 4" + " ,Rank: " + bestCase[2][4] +  
				"\nStage: 5" + " ,Rank: " + bestCase[2][5] + 
				"\nStage: 6" + " ,Rank: " + bestCase[2][6] + 
				"\nStage: 7" + " ,Rank: " + bestCase[2][7] + 
				"\nStage: 8" + " ,Rank: " + bestCase[2][8] + 
				"\nStage: 9" + " ,Rank: " + bestCase[2][9] + 
				"\nStage: 11" + " ,Rank: " + bestCase[2][11] 
				);

	}
}


