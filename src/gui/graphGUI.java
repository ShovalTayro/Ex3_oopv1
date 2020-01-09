package gui;

import utils.Point3D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.nodeData;
import dataStructure.node_data;

public class graphGUI extends JFrame implements ActionListener , Serializable,  graphListener{
	graph gr;
	Graph_Algo a = new Graph_Algo();
	double min_x=Integer.MAX_VALUE;
	double max_x=Integer.MIN_VALUE;
	double min_y=Integer.MAX_VALUE;
	double max_y=Integer.MIN_VALUE;

	//	public graphGUI(){
	//		gr = null;
	//		initGUI(g);
	//	}

	public graphGUI(DGraph g){
		g.addListener(this);
		this.gr = g;
		initGUI(g);

	}

	private void initGUI(DGraph g){
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
			int x = (int) scale(p.x(), min_x, max_x , 0 , this.getHeight());
			int y = (int) scale(p.y(), min_y, max_y , 0 , this.getWidth());
			g.fillOval(x, y, 8, 8);	
			g.drawString("" + n.getKey(), x, y);
			for(edge_data e: edges) {	
				g.setColor(Color.black);
				if(e.getTag() == -16777116) g.setColor(Color.BLUE);
				node_data p2 = gr.getNode(e.getDest());
				int x2 = (int) scale(p2.getLocation().x(), min_x, max_x , 0 , this.getWidth());
				int y2 = (int) scale(p2.getLocation().y(), min_y, max_y , 0 , this.getHeight());
				g.drawLine(x, y, x2, y2);
				int midx = (x+x2)/2;
				int midy = (y+y2)/2;

				g.drawString(""+ e.getWeight(), (midx+x2)/2, (midy+y2)/2);
				g.setColor(Color.ORANGE);
				g.fillOval((midx+x2)/2, (midy+y2)/2, 8, 8);
			}
		}
	}


	private void clear() {
		Collection<node_data> v = gr.getV();
		Iterator<node_data> it = v.iterator();
		while(it.hasNext()) {
			node_data n = it.next();
			Collection<edge_data> e = gr.getE(n.getKey());
			Iterator<edge_data> it2 = e.iterator();
			while(it2.hasNext()) {
				edge_data ed = it2.next();
				gr.getEdge(ed.getSrc(), ed.getDest()).setTag(0);
			}
		}

	}

	public void graphUpdate() {
		repaint();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}