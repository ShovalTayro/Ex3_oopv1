package gameClient;

import utils.Point3D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.ourFruit;
import dataStructure.ourRobots;
import gui.graphListener;

public class MyGameGUI extends JFrame implements ActionListener , Serializable,  graphListener{
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

	public MyGameGUI() {
		this.gr = null;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI(gr);
	}

	public MyGameGUI(DGraph gr) {
		gr.addListener(this);
		this.gr = gr;
		this.fruits = new ArrayList<ourFruit>();
		this.rob = new ArrayList<ourRobots>();
		initGUI(gr);
	}

		public MyGameGUI(DGraph gr,ArrayList<ourFruit> fruits) {
			gr.addListener(this);
			this.gr = gr;
			this.fruits = fruits;
			//this.rob = rob;
			initGUI(gr);
		}

	private void initGUI(graph gr){
		this.setSize(700, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		if(gr != null){
			//find min and max for drawing
			Collection<node_data> nodes = gr.getV();
			for (node_data node_data : nodes) {
				Point3D p = node_data.getLocation();
				if(p.x() < min_x)min_x = p.x();
				if(p.x() > max_x)max_x = p.x();
				if(p.y() > max_y)max_y = p.y();
				if(p.y() < min_y)min_y = p.y();
			}
		}
	}

	//set the point to the canvas structure
	private double scale(double data, double r_min, double r_max, double t_min, double t_max){
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	public void paint(Graphics g) {
		super.paint(g);
		Collection<node_data> nodes = gr.getV();
		for(node_data n: nodes) {
			Collection<edge_data> edges = gr.getE(n.getKey());
			Point3D p = n.getLocation();
			g.setColor(Color.RED);
			int x = (int) scale(p.x(), min_x, max_x , 50 , this.getHeight()-50);
			int y = (int) scale(p.y(), min_y, max_y , 70 , this.getWidth()-70);
			g.fillOval(x, y, 8, 8);	
			g.drawString("" + n.getKey(), x, y);
			for(edge_data e: edges) {	
				g.setColor(Color.black);
				if(e.getTag() == -16777116) g.setColor(Color.BLUE);
				node_data p2 = gr.getNode(e.getDest());
				int x2 = (int) scale(p2.getLocation().x(), min_x, max_x , 50 , this.getWidth()-50);
				int y2 = (int) scale(p2.getLocation().y(), min_y, max_y , 70 , this.getHeight()-70);
				g.drawLine(x, y, x2, y2);
				int midx = (x+x2)/2;
				int midy = (y+y2)/2;

				g.drawString(""+df2.format(e.getWeight()), (midx+x2)/2, (midy+y2)/2);
				g.setColor(Color.ORANGE);
				g.fillOval((midx+x2)/2, (midy+y2)/2, 8, 8);
			}
		}
		if(!fruits.isEmpty()){
			for(ourFruit f : fruits) {
				findFruitEdge(f);
				Point3D p = f.getPos();
				if(f.getType() == 1) g.setColor(Color.GREEN);
				else g.setColor(Color.YELLOW);
				int x = (int) scale(p.x(), min_x, max_x , 50, this.getWidth()-50);
				int y = (int) scale(p.y(), min_y, max_y ,70 , this.getHeight()-70);
				g.fillOval(x, y, 8	, 8);
			}
		}
		if(rob!= null){
			for(ourRobots r : rob) {
				Point3D p = r.getPos();
				g.setColor(Color.PINK);
				int x = (int) scale(p.x(), min_x, max_x , 50 , this.getWidth()-50);
				int y = (int) scale(p.y(), min_y, max_y , 70 , this.getHeight()-70);
				g.fillOval(x, y, 3	, 3);			
			}
		}
	}

	private void clear() {
		Collection<node_data> v = gr.getV();
		for(node_data n : v) {
			Collection<edge_data> e = gr.getE(n.getKey());
			for(edge_data ed: e) {
				gr.getEdge(ed.getSrc(), ed.getDest()).setTag(0);
			}
		}
	}
	
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
}

