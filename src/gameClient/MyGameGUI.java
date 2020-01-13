package gameClient;

import utils.Point3D;
import utils.StdDraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import Server.robot;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.ourFruit;
import dataStructure.ourRobots;
import gui.graphListener;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener, Serializable,  graphListener{
	private final double EPSILON = 0.00001;
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
	int x;
	int y;

	public MyGameGUI() {
		this.gr = null;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI();
	}

	public MyGameGUI(DGraph gr) {
		gr.addListener(this);
		this.gr = gr;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI();
	}

	public MyGameGUI(DGraph gr,ArrayList<ourFruit> fruits) {
		gr.addListener(this);
		this.gr = gr;
		this.fruits = fruits;
		//this.rob = rob;
		//initGUI(gr);
	}

	private void initGUI(){
		StdDraw.setCanvasSize(900, 600);
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
		StdDraw.setXscale(min_x-0.0005, max_x+0.0005);
		StdDraw.setYscale(min_y-0.0005, max_y+0.0005);
		StdDraw.setGraphGUI(this);
		StdDraw.show();
		paint();

	}

	public void paint() {
		StdDraw.clear();
		Collection<node_data> nodes = gr.getV();
		for(node_data n: nodes) {
			Collection<edge_data> edges = gr.getE(n.getKey());
			Point3D p = n.getLocation();
			StdDraw.setPenColor(Color.blue);
			StdDraw.filledCircle(p.x(), p.y(),0.00009);
			StdDraw.text(p.x(),p.y()+0.00015, ""+ n.getKey());
			for(edge_data e: edges) {	
				StdDraw.setPenColor(Color.black);
				node_data n2 = gr.getNode(e.getDest());
				Point3D p2 = n2.getLocation();
				StdDraw.line(p.x(), p.y(), p2.x() , p2.y());
				double midx = (p.x() + p2.x())/2;
				double midy = (p.y() + p2.y())/2;
				StdDraw.text((midx+ p2.x())/2 , (midy+p2.y())/2+0.00015, ""+ ""+df2.format(e.getWeight()));
				StdDraw.setPenColor(Color.ORANGE);
				System.out.println("orange");
				StdDraw.filledCircle((midx+ p2.x())/2 , (midy+p2.y())/2, 0.00009);
			}
		}
		if(!fruits.isEmpty()){
			for(ourFruit f : fruits) {
				findFruitEdge(f);
				Point3D p = f.getPos();
				if(f.getType() == 1) StdDraw.setPenColor(Color.GREEN);
				else StdDraw.setPenColor(Color.YELLOW);
				StdDraw.filledCircle(p.x(), p.y(), 0.00009);
			}
		}
		if(!rob.isEmpty()){
			for(ourRobots r : rob) {
				Point3D p = r.getPos();
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.filledCircle(p.x(), p.y(), 0.0003);			
			}
		}
	}
	//init  robot
	private void clear(game_service game) {
		rob.clear();
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
				if((dist(p, p2)-(dist(f.getPos(),p)+dist(f.getPos(), p2)))<= EPSILON){
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
	//distance between tow given points
	private double dist(Point3D p, Point3D p2) {
		double ans = Math.sqrt(Math.pow((p.x()-p2.x()),2)+(Math.pow((p.y()-p2.y()),2)));
		return ans;

	}


	public void graphUpdate() {
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}



	public void Play_manual(String s)
	{
		try
		{
			int scenario_num = Integer.parseInt(s);
			if(scenario_num>=0 && scenario_num<=23)
			{
				game_service game = Game_Server.getServer(scenario_num);
				String g = game.getGraph();
				DGraph gg = new DGraph();
				gg.init(g);
				this.gr = gg;
				Iterator<String> fruit_iter = game.getFruits().iterator();
				//clear fruits collection if needed
				if(fruits!= null)
				{
					fruits.clear();
				}
				else
				{
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
				//initGUI(gr);
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

				//put manually the robots
				System.out.println(nomOfRobots+" NUM ROB");
				while(check < nomOfRobots)
				{
					//put the robots manually update the list and repaint using gui

					// if x y < dist 
					// for all vertex
					Point3D pos = new Point3D(x, y);
					//	System.out.println("pos " + pos.toString());
					Collection<node_data> nd = gr.getV();
					for (node_data node_data : nd) {
						//System.out.println(node_data.getLocation());
						Point3D ndPos = node_data.getLocation();
						//System.out.println("p " + p.toString());
						//System.out.println(pos.distance2D(ndPos));
						if(pos.distance2D(ndPos) <= EPSILON)
						{
							System.out.println("Found");
							game.addRobot(node_data.getKey());
							//System.out.println("ok");
							ourRobots r = new ourRobots(check, node_data.getLocation(), 1 , node_data, null , gr);
							check++;
							//System.out.println("ok2");
							rob.add(r);
							//System.out.println("ok3"+ rob.size());
							//System.out.println(check + " " + node_data.getLocation().toString() + node_data.toString());
							//	rob.add(new ourRobots(++check, node_data.getLocation(), 1 , node_data, null , gr));
							initGUI();
							this.x = 0;
							this.y =0;
							break;
						}
					}
				}
				//System.err.println("End while");
				game.startGame();
				ourRobots r = null;
				//				while(game.isRunning())
				//				{	
				//System.out.println(pos.distance2D(ndPos));
				moveManualRobots(game);	
				//					game.move();
				//clear(game);
				//repaint();
				//clear and build
				//				}
			}
			//the scenario is not exist throw error
			else
			{
				throw new RuntimeException("Error! , the scenario does not exist");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void moveManualRobots(game_service game) {

		ourRobots r = null;
		while(game.isRunning())
		{

			Point3D robPos = new Point3D(x, y);
			//System.out.println(robPos);
			//Point3D p =	(robPos);
			//System.out.println(x +" " + y);
			//ourRobots r = null;
			if(robots)
			{
				for (ourRobots roby : rob) {
					//System.out.println(roby.getPos());
					//System.out.println(robPos);
					Point3D p2= roby.getPos();
					//click on the robot
					System.out.println(p2.distance2D(robPos));
					if(p2.distance2D(robPos)<= EPSILON) {
						//search for edge for the robot
						System.err.println("TEST");
						//	roby.getEdge();
						//if(roby.getEdge() == null) {
						//nextNodeManual(game , roby);
						r= roby;
						robots = false;
						this.x =0;
						this.y =0;
						System.out.println("BOT CHOOSEN");
						//}
					}
				}
			}
			else
			{
				System.out.println(r.getId());
				Collection<node_data> nd = gr.getV();
				for (node_data node_data : nd) {
					Point3D p2= node_data.getLocation();
					//click on the robot
					if(p2.distance2D(robPos)<= EPSILON) {
						//search for edge for the robot
						//System.out.println("TEST");
						//	roby.getEdge();
						//if(roby.getEdge() == null) {
						//nextNodeManual(game , roby);
						//}
						r.setPos(node_data.getLocation());
						System.out.println("PLSEASe");
						paint();
						robots=true;
					}
				}
			}
			//paint(getGraphics());
		}
	}

	private void nextNodeManual(game_service game, ourRobots roby){
		Point3D destt = new Point3D(x, y);
		Collection<edge_data> eg = gr.getE(roby.getNode().getKey());
		//check if the pressed  p is one of the edges of the current node
		for (edge_data e : eg) {
			Point3D temp = gr.getNode(e.getDest()).getLocation();
			if(destt.distance2D(temp)<=EPSILON) {
				System.out.println("FOUND EDGE");
				roby.setEdge(e);
				break;
			}
		}
		//paint(getGraphics());
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		x  = e.getX();
		y = e.getY();
	}

}

