package algorithms;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	graph ourGraph;
	@Override
	public void init(graph g) {
		this.ourGraph =g;

	}

	@Override
	public void init(String file_name) {
		try
		{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file);         
			this.ourGraph = (graph)in.readObject(); 

			in.close(); 
			file.close(); 
		} 

		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 
		catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException is caught"); 
		} 
	} 

	@Override
	public void save(String file_name) {  
		try
		{    
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 
			out.writeObject(this.ourGraph); 

			out.close(); 
			file.close(); 
		}   
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 
	}
	private static void DFS( graph ourGraph2, int v){
		// mark current node as visited
		ourGraph2.getNode(v).setTag(1); 
		Collection<edge_data> edges = ourGraph2.getE(v);
		// do for every edge (v -> u)
		for (edge_data u : edges){
			// u is not visited
			if (ourGraph2.getNode(u.getDest()).getTag() != new Color(1).getRGB())
				DFS(ourGraph2, u.getDest());
		}
	}


	@Override
	public boolean isConnected() {

		Collection<node_data> v = ourGraph.getV();
		Iterator<node_data> it = v.iterator();
		while(it.hasNext()) {
			node_data n = it.next();
			clear();
			// start DFS from first vertex
			DFS(ourGraph, n.getKey());

			// If DFS traversal doesn’t visit all vertices,
			// then graph is not strongly connected
			for (node_data node: ourGraph.getV())
				if (node.getTag()!= new Color(1).getRGB())
					return false;
		}
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		clear();
		int countVisit = 0;
		node_data source = ourGraph.getNode(src);
		source.setWeight(0);
		while(source != null) {
			//mark visit
			source.setTag(1);
			node_data minNeighbor=null;
			//array list of edges getting out from source
			Collection<edge_data> eSrc = ourGraph.getE(source.getKey());
			int size = eSrc.size();
			//checking every node weight and change it if needed , save min weight and make it source node
			Iterator<edge_data> it = eSrc.iterator();
			while(it.hasNext()) {
				edge_data e = it.next();
				node_data neighbor = ourGraph.getNode(e.getDest());
				if(neighbor.getWeight() > source.getWeight()+ e.getWeight()) {
					neighbor.setWeight( source.getWeight()+ e.getWeight());
					neighbor.setInfo(""+source.getKey());
					if(minNeighbor==null&& neighbor.getTag()== new Color(0).getRGB()) {
						minNeighbor= neighbor;
					}
					else {
						if((minNeighbor.getWeight()>neighbor.getWeight())&& neighbor.getTag()== new Color(0).getRGB() ) {
							minNeighbor=neighbor;
						}
					}

				}
				else if(countVisit != size) {
					minNeighbor = neighbor;
				}
			}
			source=minNeighbor;
		}
		return ourGraph.getNode(dest).getWeight();
	}

	private void clear() {
		Collection<node_data> v = ourGraph.getV();
		Iterator<node_data> it = v.iterator();
		while(it.hasNext()) {
			node_data n = it.next();
			n.setWeight(Double.MAX_VALUE);
			n.setTag(0);
			n.setInfo(null);
		}
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		double a = shortestPathDist(src, dest);
		ArrayList<node_data> ans = new ArrayList<node_data>();
		node_data source = ourGraph.getNode(dest);
		ans.add(source);
		while(source.getInfo()!= null) {
			source=ourGraph.getNode(Integer.parseInt(source.getInfo()));
			ans.add(source);
		}
		ArrayList<node_data> answer = new ArrayList<node_data>();
		for (int i = ans.size()-1; i >= 0 ; i--) {
			answer.add(ans.get(i));
		}
		return answer;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public graph copy() {
		DGraph temp = new DGraph();
		Collection<node_data> nodes= this.ourGraph.getV();
		Iterator<node_data> it2 = nodes.iterator();
		while(it2.hasNext()) {
			temp.addNode(it2.next());
			Collection<edge_data> edges=this.ourGraph.getE(it2.next().getKey());
			Iterator<edge_data> it = edges.iterator();
			while(it.hasNext()) {
				edge_data e = it.next();
				temp.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return temp;
	}

}


