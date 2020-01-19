package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import dataStructure.ourRobots;
import gui.Clock;
import utils.Point3D;
import utils.StdDraw;

public class playManual implements PlayManualy {
	MyGameGUI MyGame;

	private final double EPSILON = 0.0001;
	boolean robots = true;

	public playManual(MyGameGUI MyGame) {
		this.MyGame = MyGame;
	}

	public void playManually(){
		try{
			String num = JOptionPane.showInputDialog(null, "Enter a scenario you want to play : ");
			int scenario_num = Integer.parseInt(num);
			if(scenario_num>=0 && scenario_num<=23) {
				game_service game = Game_Server.getServer(scenario_num);
				String g = game.getGraph();
				DGraph gg = new DGraph();
				gg.init(g);
				this.MyGame.gr = gg;
				//init the graph and paint him
				this.MyGame.initGUI(this.MyGame.gr);
				this.MyGame.paint(game);
				String gameString = game.toString();
				JSONObject obj = new JSONObject(gameString);
				JSONObject CurrGame = (JSONObject) obj.get("GameServer");
				int nomOfRobots = CurrGame.getInt("robots");
				int check = 0;
				//if the robots arrayList not empty clear
				if(this.MyGame.rob != null)this.MyGame.rob.clear();
				else{
					this.MyGame.rob = new ArrayList<ourRobots>();
				}

				//put the robots manually update the list and show the robot
				JOptionPane.showMessageDialog(null, "add " + nomOfRobots + " robots by click on the nodes");
				while(check < nomOfRobots)	{
					//check if your click is on node , put robot if it is , add to server and our list
					Point3D pos = new Point3D(this.MyGame.x, this.MyGame.y);
					Collection<node_data> nd = this.MyGame.gr.getV();
					for (node_data node_data : nd) {
						Point3D ndPos = node_data.getLocation();
						if(pos.distance2D(ndPos) <= EPSILON){
							game.addRobot(node_data.getKey());
							ourRobots r = new ourRobots(check, node_data.getLocation(), 1 , node_data, null , this.MyGame.gr);
							check++;
							r.setGraph(gg);
							this.MyGame.rob.add(r);
							this.MyGame.paint(game);
							this.MyGame.x = 0;
							this.MyGame.y = 0;
							break;
						}
					}
				}
				//little pause before starting the game
				StdDraw.pause(50);
				game.startGame();
				//Timer 
				Clock k = new Clock(game);
				//thread the move the game
				this.MyGame.movements(game);
				while(game.isRunning()) {
					moveManualRobots(game);	
				}
				//return result
				String res = game.toString();
				JSONObject object = new JSONObject(res);
				JSONObject cgame = (JSONObject) object.get("GameServer");
				int points = cgame.getInt("grade");
				int moves = cgame.getInt("moves");

				JOptionPane.showMessageDialog(null, "GAME OVER" + "\nYour points: " + points +"\nYour move: " + moves);			
			}
			//the scenario is not exist open a window
			else{
				JOptionPane.showMessageDialog(null, "Error , the scenario you choose does not exist. ");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//function that choose next edge for robots of the game
	public void moveManualRobots(game_service game) {
		List<String> log= game.move();
		if(log!= null) {
			int destMove = nextNodeManual(game);
			if(destMove!= -1) {
				ourRobots r = this.MyGame.rob.get(MyGameGUI.robotChoosen);
				if(r!= null) {
					game.chooseNextEdge(r.getId(), destMove);
					//game.move();
					this.MyGame.paint(game);
				}
			}
		}
		this.MyGame.paint(game);
	}
	//method that helps the method above to choose the robot way
	public int nextNodeManual(game_service game){
		ourRobots r = null;
		Point3D robPos = new Point3D(this.MyGame.x, this.MyGame.y);
		if(robots)	{
			for (ourRobots roby : this.MyGame.rob) {
				Point3D p2= roby.getPos();
				//click on the robot?
				if(p2.distance2D(robPos)<= EPSILON) {
					//search for edge for the robot
					r= roby;
					MyGameGUI.robotChoosen = roby.getId();
					robots = false;
					this.MyGame.x =0;
					this.MyGame.y =0;
					System.out.println("robot choosen");
					break;
				}
			}
		}
		//search next node for movement from the node the robot is placed on
		else {
			Collection<edge_data> eg = this.MyGame.gr.getE(this.MyGame.rob.get(MyGameGUI.robotChoosen).getNode().getKey());
			//check if the pressed dest is one of the edges of the current node
			Point3D dest = new Point3D(this.MyGame.x, this.MyGame.y);
			for (edge_data e : eg) {
				Point3D temp = this.MyGame.gr.getNode(e.getDest()).getLocation();
				if(dest.distance2D(temp)<=0.0003) {
					r= this.MyGame.rob.get(MyGameGUI.robotChoosen);
					r.setEdge(e);
					this.MyGame.rob.get(MyGameGUI.robotChoosen).setNode(this.MyGame.gr.getNode(e.getDest()));
					robots= true;
					this.MyGame.x = 0;
					this.MyGame.y =0;
					return e.getDest();
				}
			}
		}
		this.MyGame.x = 0;
		this.MyGame.y =0;
		return -1;
	}


}
