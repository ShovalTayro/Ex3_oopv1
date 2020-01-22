package gameClient;

import utils.Point3D;
import utils.StdDraw;
import java.awt.Color;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.ourFruit;
import dataStructure.ourRobots;

public class MyGameGUI implements Serializable{
	private static DecimalFormat df2 = new DecimalFormat("#.###");
	ArrayList<ourFruit> fruits;
	ArrayList<ourRobots> rob;
	graph gr;
	Graph_Algo a = new Graph_Algo();
	boolean robots = true;
	double x;
	double y;
	int moveRobot;
	static int robotChoosen=0;
	KML_Logger kmlLogger;
	Thread movement;
	Thread kml;
	static long sleepy =90;

	//Default contractor
	public MyGameGUI() {
		this.gr = null;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI(this.gr);
	}

	public MyGameGUI(graph gr) {
		this.gr = gr;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI(gr);
	}


	void initGUI(graph gr){
		StdDraw.enableDoubleBuffering();
		if(!StdDraw.getIsPaint()) {
			StdDraw.setCanvasSize(800, 600);
			StdDraw.setIsPaint();
		}
		double min_x=Integer.MAX_VALUE;
		double max_x=Integer.MIN_VALUE;
		double min_y=Integer.MAX_VALUE;
		double max_y=Integer.MIN_VALUE;
		if(gr != null){	
			kmlLogger = new KML_Logger(gr);
			kmlLogger.kmlGraph();
			//change the pixel of the window
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
	}

	public void paint(game_service game) {
		StdDraw.clear();
		StdDraw.setPenRadius(0.0005);
		//draw nodes
		Collection<node_data> nodes = gr.getV();
		for(node_data n: nodes) {
			Collection<edge_data> edges = gr.getE(n.getKey());
			Point3D p = n.getLocation();
			StdDraw.setPenColor(Color.blue);
			StdDraw.filledCircle(p.x(), p.y(),0.00007);
			StdDraw.text(p.x(),p.y()+0.00015, ""+ n.getKey());
			for(edge_data e: edges) {	
				//draw edges
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
			findFruitEdge(f, this.gr);
		}
		//draw a banana
		if(!fruits.isEmpty()){
			for(ourFruit f : fruits) {
				findFruitEdge(f, this.gr);
				Point3D p = f.getPos();
				if(f.getType() == 1) StdDraw.picture(p.x(), p.y(), "apple.jpg", 0.0004, 0.0004);
				else {
					StdDraw.picture(p.x(), p.y(), "banana.jpg", 0.0004, 0.0004);
				}
			}
		}
		//clear robots collection if needed
		if(rob!= null){
			rob.clear();
		}
		else{
			rob = new ArrayList<ourRobots>();
		}
		initRob(game);
		//draw robots
		if(!rob.isEmpty()){
			for(ourRobots r : rob) {
				Point3D p = r.getPos();
				StdDraw.picture(p.x(), p.y(), "robot.jpg", 0.0007, 0.0007);		
			}
		}
		StdDraw.show();
	}

	//init robot
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
	public static void findFruitEdge(ourFruit f, graph gr) {
		Collection<node_data> v = gr.getV();
		for(node_data n : v) {
			Collection<edge_data> e = gr.getE(n.getKey());
			for(edge_data ed: e) {
				Point3D p =gr.getNode(ed.getSrc()).getLocation();
				Point3D p2 =gr.getNode(ed.getDest()).getLocation();
				//check if the fruit is on the edge
				if(Math.abs((p.distance2D(p2)-(Math.abs((f.getPos().distance2D(p)))+(Math.abs((f.getPos().distance2D(p2))))))) <= 0.0000001){
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
	//thread that make a move in the game
	public void movements(game_service game) {
		movement = new Thread(new Runnable() {
			@Override
			public void run() {
				while(game.isRunning()) {
					//game.move();
					try{
						game.move();
						Thread.sleep(sleepy);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		movement.start();
	}
	public void createKML(game_service game){
		kml = new Thread(new Runnable() {

			@Override
			public void run() {
				while(game.isRunning()){
					if(gr !=null){
						try {
							Thread.sleep(1000);
							String name  = java.time.LocalDate.now()+"T"+java.time.LocalTime.now();
							LocalTime end = java.time.LocalTime.now();
							end= end.plusNanos(100*1000000);
							String endOfTime = java.time.LocalDate.now()+ "T"+end;
							kmlLogger.setFruit(name ,endOfTime );
							kmlLogger.setRobot(name, endOfTime);
						}

						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		kml.start();
	}

	public void setPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
}