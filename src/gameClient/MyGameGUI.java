package gameClient;

import utils.Point3D;
import utils.StdDraw;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.ourFruit;
import dataStructure.ourRobots;

public class MyGameGUI implements ActionListener, Serializable{
	private final double EPSILON = 0.0001;
	private static DecimalFormat df2 = new DecimalFormat("#.###");
	ArrayList<ourFruit> fruits;
	ArrayList<ourRobots> rob;
	graph gr;
	Graph_Algo a = new Graph_Algo();
	double min_x=Integer.MAX_VALUE;
	double max_x=Integer.MIN_VALUE;
	double min_y=Integer.MAX_VALUE;
	double max_y=Integer.MIN_VALUE;
	boolean robots = true;
	double x;
	double y;
	int moveRobot;
	static int robotChoosen=0;

	public MyGameGUI() {
		this.gr = null;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI();
	}

	public MyGameGUI(DGraph gr) {
		this.gr = gr;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI();
	}


	private void initGUI(){
		StdDraw.enableDoubleBuffering();
		if(!StdDraw.getIsPaint()) {
			StdDraw.setCanvasSize(800, 600);
			StdDraw.setIsPaint();
		}
		if(gr != null){
			Collection<node_data> nodes = gr.getV();
			for (node_data node_data : nodes) {
				Point3D p = node_data.getLocation();
				if(p.x() < min_x) min_x = p.x();
				if(p.x() > max_x) max_x = p.x();
				if(p.y() > max_y) max_y = p.y();
				if(p.y() < min_y) min_y = p.y();
			}
		}
		StdDraw.setXscale(min_x , max_x);
		StdDraw.setYscale(min_y, max_y);
		StdDraw.setGraphGUI(this);
		StdDraw.show();
		//paint(game);
	}

	public void paint(game_service game) {
		StdDraw.clear();
		StdDraw.setPenRadius(0.0005);
		Collection<node_data> nodes = gr.getV();
		for(node_data n: nodes) {
			Collection<edge_data> edges = gr.getE(n.getKey());
			Point3D p = n.getLocation();
			StdDraw.setPenColor(Color.blue);
			StdDraw.filledCircle(p.x(), p.y(),0.00007);
			StdDraw.text(p.x(),p.y()+0.00015, ""+ n.getKey());
			for(edge_data e: edges) {	
				StdDraw.setPenColor(Color.GRAY);
				node_data n2 = gr.getNode(e.getDest());
				Point3D p2 = n2.getLocation();
				StdDraw.line(p.x(), p.y(), p2.x() , p2.y());
				double midx = (p.x() + p2.x())/2;
				double midy = (p.y() + p2.y())/2;
				StdDraw.text((midx+ p2.x())/2 , (midy+p2.y())/2+0.00015, ""+ ""+df2.format(e.getWeight()));
				StdDraw.setPenColor(Color.green);
				StdDraw.filledCircle((midx+ p2.x())/2 , (midy+p2.y())/2, 0.00005);
			}
		}
		//get from the server again fruits and robots
		Iterator<String> fruit_iter = game.getFruits().iterator();
		//clear fruits collection if needed
		if(fruits!= null){
			fruits.clear();
		}
		else{
			fruits = new ArrayList<ourFruit>();
		}
		//set all fruits in their places
		while(fruit_iter.hasNext())
		{
			String sFruit = fruit_iter.next();
			ourFruit f = new ourFruit();
			f.initFruit(sFruit);
			fruits.add(f);
			findFruitEdge(f);
		}
		if(!fruits.isEmpty()){
			for(ourFruit f : fruits) {
				findFruitEdge(f);
				Point3D p = f.getPos();
				if(f.getType() == 1) StdDraw.picture(p.x(), p.y(), "apple.jpg", 0.0004, 0.0004);
				else {
					StdDraw.picture(p.x(), p.y(), "banana.jpg", 0.0004, 0.0004);
				}
			}
		}
		if(rob!= null){
			rob.clear();
		}
		else{
			rob = new ArrayList<ourRobots>();
		}
		initRob(game);
		if(!rob.isEmpty()){
			for(ourRobots r : rob) {
				Point3D p = r.getPos();
				StdDraw.picture(p.x(), p.y(), "robot.jpg", 0.0007, 0.0007);		
			}
		}
		StdDraw.show();
	}
	//init  robot
	private void initRob(game_service game) {
		List<String> robServer = game.getRobots();
		for (String string : robServer) {
			ourRobots roby = new ourRobots();
			roby.setGraph(gr);
			roby.initRobots(string);
			rob.add(roby);
		}
	}
	// find the edge the fruit is on
	private void findFruitEdge(ourFruit f) {
		Collection<node_data> v = gr.getV();
		for(node_data n : v) {
			Collection<edge_data> e = gr.getE(n.getKey());
			for(edge_data ed: e) {
				Point3D p =gr.getNode(ed.getSrc()).getLocation();
				Point3D p2 =gr.getNode(ed.getDest()).getLocation();
				//check if the fruit is on the edge
				if(Math.abs((p.distance2D(p2)-(Math.abs((f.getPos().distance2D(p)))+(Math.abs((f.getPos().distance2D(p2))))))) <= EPSILON){
					int low=n.getKey();
					int high=ed.getDest();
					if(n.getKey()>ed.getDest()) {
						low= ed.getDest();
						high= n.getKey();
					}
					if(f.getType()==1) {
						edge_data edF = gr.getEdge(low, high);
						if(edF!= null) f.setEdge(edF);
					}
					//the reverse edge is the way to eat the fruit
					if(f.getType()==-1) {
						edge_data edF = gr.getEdge(high,low);
						if(edF!= null)f.setEdge(edF);
					}
				}

			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void playManual(){
		try{
			JFrame input = new JFrame();
			String num = JOptionPane.showInputDialog(null, "Enter a scenario you want to play : ");
			int scenario_num = Integer.parseInt(num);
			if(scenario_num>=0 && scenario_num<=23) {
				game_service game = Game_Server.getServer(scenario_num);
				String g = game.getGraph();
				DGraph gg = new DGraph();
				gg.init(g);
				this.gr = gg;
				Iterator<String> fruit_iter = game.getFruits().iterator();
				//clear fruits collection if needed
				if(fruits!= null){
					fruits.clear();
				}
				else{
					fruits = new ArrayList<ourFruit>();
				}
				//set all fruits in their places
				while(fruit_iter.hasNext())
				{
					String sFruit = fruit_iter.next();
					ourFruit f = new ourFruit();
					f.initFruit(sFruit);
					fruits.add(f);
					findFruitEdge(f);
				}
				min_x=Integer.MAX_VALUE;
				max_x=Integer.MIN_VALUE;
				min_y=Integer.MAX_VALUE;
				max_y=Integer.MIN_VALUE;
				initGUI();
				paint(game);
				String gameString = game.toString();
				JSONObject obj = new JSONObject(gameString);
				JSONObject CurrGame = (JSONObject) obj.get("GameServer");
				int nomOfRobots = CurrGame.getInt("robots");
				int check = 0;
				//	if the robots arrayList not empty clear
				if(rob != null)rob.clear();
				else{
					rob = new ArrayList<ourRobots>();
				}

				//put the robots manually update the list and repaint using gui
				JOptionPane.showMessageDialog(null, "add " + nomOfRobots + " robots by click on the nodes");
				while(check < nomOfRobots)	{
					Point3D pos = new Point3D(x, y);
					Collection<node_data> nd = gr.getV();
					for (node_data node_data : nd) {
						Point3D ndPos = node_data.getLocation();
						if(pos.distance2D(ndPos) <= EPSILON){
							game.addRobot(node_data.getKey());
							ourRobots r = new ourRobots(check, node_data.getLocation(), 1 , node_data, null , gr);
							check++;
							r.setGraph(gg);
							rob.add(r);
							paint(game);
							this.x = 0;
							this.y = 0;
							break;
						}
					}
				}
				StdDraw.pause(50);
				game.startGame();
				while(game.isRunning()) {
					moveManualRobots(game);	
				}
				//return result
				String res = game.toString();
				JSONObject object = new JSONObject(res);
				JSONObject cgame = (JSONObject) object.get("GameServer");
				int points = cgame.getInt("grade");
				int moves = cgame.getInt("moves");
				
				JOptionPane.showMessageDialog(null, "Your points: " + points +"\nYour move: " + moves);			
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

	private void moveManualRobots(game_service game) {
		List<String> log= game.move();
		if(log!= null) {
			int destMove = nextNodeauto(game);
			if(destMove!= -1) {
				ourRobots r = rob.get(robotChoosen);
				if(r!= null) {
					game.chooseNextEdge(r.getId(), destMove);
					System.out.println("r+ " + r.getId()+ " , dest "+ destMove);
					game.move();
					System.out.println("MOVE");
					System.out.println(game.getFruits());
					//System.out.println(game.getRobots());
					paint(game);
				}
			}
		}
		paint(game);
	}
	private int nextNodeauto(game_service game){
		ourRobots r = null;
		Point3D robPos = new Point3D(x, y);
		if(robots)	{
			for (ourRobots roby : rob) {
				Point3D p2= roby.getPos();
				//click on the robot?
				if(p2.distance2D(robPos)<= EPSILON) {
					//search for edge for the robot
					r= roby;
					robotChoosen = roby.getId();
					robots = false;
					this.x =0;
					this.y =0;
					System.out.println("BOT CHOOSEN");
					break;
				}
			}
		}
		//search next node for movement from the node the robot is placed on
		else {
			Collection<edge_data> eg = gr.getE(rob.get(robotChoosen).getNode().getKey());
			//check if the pressed  dest is one of the edges of the current node
			Point3D dest = new Point3D(x, y);
			for (edge_data e : eg) {
				Point3D temp = gr.getNode(e.getDest()).getLocation();
				if(dest.distance2D(temp)<=0.0003) {

					System.out.println("FOUND EDGE");
					r= rob.get(robotChoosen);
					System.out.println("dest "+ e.getDest());
					r.setEdge(e);
					rob.get(robotChoosen).setNode(gr.getNode(e.getDest()));
					robots= true;
					this.x = 0;
					this.y =0;
					return e.getDest();
				}
			}
		}
		this.x = 0;
		this.y =0;
		return -1;
	}
	private  int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	public void setPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void playAuto() {
		try{
			JFrame input = new JFrame();
			String num = JOptionPane.showInputDialog(null, "Enter a scenario you want to play : ");
			int scenario_num = Integer.parseInt(num);
			if(scenario_num>=0 && scenario_num<=23) {
				game_service game = Game_Server.getServer(scenario_num);
				String g = game.getGraph();
				DGraph gg = new DGraph();
				gg.init(g);
				this.gr = gg;
				Iterator<String> fruit_iter = game.getFruits().iterator();
				//clear fruits collection if needed
				if(fruits!= null){
					fruits.clear();
				}
				else{
					fruits = new ArrayList<ourFruit>();
				}
				//set all fruits in their places
				while(fruit_iter.hasNext()){
					String sFruit = fruit_iter.next();
					ourFruit f = new ourFruit();
					f.initFruit(sFruit);
					fruits.add(f);
					findFruitEdge(f);
			//		System.out.println(f.getEdge().getSrc() + " " + f.getEdge().getDest());
				}
				min_x=Integer.MAX_VALUE;
				max_x=Integer.MIN_VALUE;
				min_y=Integer.MAX_VALUE;
				max_y=Integer.MIN_VALUE;
				initGUI();
				paint(game);
				String gameString = game.toString();
				JSONObject obj = new JSONObject(gameString);
				JSONObject CurrGame = (JSONObject) obj.get("GameServer");
				int nomOfRobots = CurrGame.getInt("robots");
				int check = 0;
				//	if the robots arrayList not empty clear
				if(rob != null)rob.clear();
				else{
					rob = new ArrayList<ourRobots>();
				}

				//put the robots automatically , update the list and repaint using gui
				while(check < nomOfRobots)	{
					putRobot(game, check);
					check++;
				}
				paint(game);
				StdDraw.pause(50);
				game.startGame();
				while(game.isRunning()) {
					moveAutomaticallyRobots(game);	
				}
				//return result
				//return result
				String res = game.toString();
				JSONObject object = new JSONObject(res);
				JSONObject cgame = (JSONObject) object.get("GameServer");
				int points = cgame.getInt("grade");
				int moves = cgame.getInt("moves");
				
				JOptionPane.showMessageDialog(null, "Computer points: " + points +"\nComputer move: " + moves);	
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

	private void moveAutomaticallyRobots(game_service game) {
		ourFruit temp = new ourFruit();
		Graph_Algo a = new Graph_Algo();
		a.init(gr);
		for (ourRobots  roby : rob) {
			double dist = Double.MAX_VALUE;
			for (ourFruit f : fruits) {
				if(dist>(a.shortestPathDist(roby.getNode().getKey(), f.getEdge().getSrc())+f.getEdge().getWeight())&&(!f.getVisited())) {
					dist =a.shortestPathDist(roby.getNode().getKey(), f.getEdge().getSrc())+f.getEdge().getWeight();
					temp=f;
				}
			}
			robotChoosen = roby.getId();
		//	System.out.println("temp"+game.getFruits());
			System.out.println(roby.getNode().getKey() + " " + temp.getEdge().getSrc());
			roby.setPath(a.shortestPath(roby.getNode().getKey(), temp.getEdge().getSrc()));
			roby.getPath().remove(0);
			roby.getPath().add(gr.getNode(temp.getEdge().getDest()));
		//	System.out.println("value "+ temp.getValue());
			for (ourFruit f : fruits) {
				if(f.getPos()==temp.getPos()) {
					f.setVisited(true);
				}
			}
			//this.fruits.get(temp.getValue()).setVisited(true);
		}

		List<String> log= game.move();
		if(log!= null) {
			for (int i = 0; i < rob.size(); i++) {
				ourRobots roby = rob.get(i);
				while(roby.getPath().size() != 0) {
					//System.out.println(roby.getId()+"roby id");
				//	System.out.println(rob.size());
					//System.out.println("path"+roby.getPath().get(0).getKey());
					game.chooseNextEdge(roby.getId(), roby.getPath().get(0).getKey());
					//paint(game);
					game.move();
					roby.getPath().remove(0);
				}
			}	
		}
		paint(game);
	}



	private void putRobot(game_service game, int check) {
		for (ourFruit f : fruits) {
			if(!f.getVisited()) {
				node_data n = gr.getNode(f.getEdge().getSrc());
				game.addRobot(n.getKey());
				ourRobots r = new ourRobots(check, n.getLocation(), 1 , n, null , gr);
				rob.add(r);
				r.setGraph(gr);
				f.setVisited(true);
				break;	
			}
		}

	}
}