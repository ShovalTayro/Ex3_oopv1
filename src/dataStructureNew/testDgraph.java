package dataStructureNew;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dataStructureNew.edge_data;
import dataStructureNew.graph;
import dataStructureNew.node_data;
import utils.Point3D;

import java.util.Hashtable;
import javax.swing.JFrame;



public class testDgraph extends JFrame  implements graph{
	Hashtable <Integer, node_data> nodes = new Hashtable<Integer, node_data>();	
	Hashtable<node_data , Hashtable<Integer,edge_data>> edges = new Hashtable<node_data, Hashtable<Integer, edge_data>>();
	int countMC=0;
	int countE = 0;
	public node_data getNode(int key) {
		return nodes.get(key);

	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(src==dest) return null;
		if(nodes.get(src)==null || nodes.get(dest)==null) return null;
		else
		{
			node_data key= nodes.get(src);
			if(edges.get(key)!= null) return edges.get(key).get(dest);
			else {		
				return null;
			}
		}
	}

	@Override
	public void addNode(node_data n) {
		if(nodes.contains(n)) System.out.println("the node already exist");
		else {
			nodes.put(n.getKey(), n); 
			edges.put(n, new Hashtable<Integer, edge_data>());
			countMC++;
		}
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(w < 0) System.out.println("the weight can not be negative");
		else {
			if(src!=dest) {

				node_data key = nodes.get(src);
				node_data desti = nodes.get(dest);
				if (key!= null&& desti!= null && edges.get(key).get(dest)== null) {
					edgeData e= new edgeData(nodes.get(src), nodes.get(dest), w);
					edges.get(key).put(dest, e);
					countMC++;
					countE++;
				}
				else {
					System.out.println("error , src/dest does not exist");
				}
			}
			else {
				System.out.println("error , same nodes");

			}
		}
	}
	@Override
	public Collection<node_data> getV() {

		return nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		node_data key = nodes.get(node_id);
		return  edges.get(key).values();
	}

	@Override
	public node_data removeNode(int key) {
		node_data temp = nodes.get(key);
		if(temp== null) return null;
		countE -= edges.get(temp).size();
		edges.get(temp).clear();
		//remove edge when dest is key
		for(int i = 0; i < nodes.size(); i++) {
			node_data tempR = nodes.get(i);
			edges.get(tempR).remove(key);
		}
		nodes.remove(key);
		countMC++;
		return temp;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		node_data temp = nodes.get(src);
		node_data temp2 = nodes.get(dest);
		edge_data ans = null;
		if(temp != null&& temp2!= null) {
			ans=  edges.get(temp).remove(dest);
			if (ans != null)
			{
				countMC++ ;
				countE--;
			}
			else {
				System.out.println("the edge does not exist");
			}
		}
		return ans;
	}

	@Override
	public int nodeSize() {
		if(nodes.isEmpty()) return 0;
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		return countE;
	}

	@Override
	public int getMC() {
		return countMC;

	}

	
	public void adasdasd(String g) {
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(g));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray Jnodes = (JSONArray) jsonObject.get("Nodes");
			JSONArray Jedges = (JSONArray) jsonObject.get("Edges");
			for(int i=0;i<Jnodes.size(); i++) {
				String temp  = (String) Jnodes.get(i);
				int x = temp.indexOf("\"pos\":\"");
				int y = temp.indexOf("\",\"id\":");
				String point = temp.substring(x, y);
				String key =  temp.substring(y);
				String [] point3d = point.split(",");
				double p_x= Double.parseDouble(point3d[0]);
				double p_y= Double.parseDouble(point3d[1]);
				double p_z= Double.parseDouble(point3d[2]);
				Point3D p = new Point3D(p_x,p_y, p_z);
				int key2 = Integer.parseInt(key);
				nodeData n = new nodeData(key2, 0, p);
				nodes.put(key2, n);
			}
			for(int i=0;i<Jedges.size(); i++) {
				String temp  = (String) Jnodes.get(i);
				int sSrc= temp.indexOf("\"src\":");
				int sWeight = temp.indexOf(",\"w\":");
				int sDest = temp.indexOf(",\"dest\":");
				int src = Integer.parseInt(temp.substring(sSrc, sWeight));
				int weight = Integer.parseInt(temp.substring(sWeight, sDest));
				int dest = Integer.parseInt(temp.substring(sDest));
				this.connect(src, dest, weight);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
