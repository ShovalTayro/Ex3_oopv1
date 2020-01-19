package gameClient;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;
import dataStructure.ourFruit;
import dataStructure.ourRobots;
import gui.Clock;

public class playAuto {
	MyGameGUI MyGame;

	public playAuto(MyGameGUI MyGame) {
		this.MyGame = MyGame;
	}

	public void playAutomat() {
		try{
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
			roby.getPath().remove(0);
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
			while(roby.getPath().size() != 0) {
				game.chooseNextEdge(roby.getId(), roby.getPath().get(0).getKey());
				roby.getPath().remove(0);
			}
		}	
		this.MyGame.paint(game);
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
}
