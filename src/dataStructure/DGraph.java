package dataStructure;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;

public class DGraph extends JFrame  implements graph{
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
		if(nodes.get(n.getKey()) != null) System.out.println("the node already exist");
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
				//check that the nodes exist witout edge between them
				if (key!= null&& desti!= null && edges.get(key).get(dest)== null) {
					edgeData e= new edgeData(nodes.get(src), nodes.get(dest), w);
					edges.get(key).put(dest, e);
					countMC++;
					countE++;
				}
				else {
					throw new RuntimeException("error , src/dest does not exist");
				}
			}
			else {
				throw new RuntimeException("error , same nodes");

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
		//update the number of edges in the graph
		countE -= edges.get(temp).size();
		Set<node_data> sets = edges.keySet();
		//remove edge when dest is the key
		for(node_data tempR : sets) {
			edges.get(tempR).remove(key);
		}
		edges.remove(temp);
		nodes.remove(key);
		countMC++;
		return temp;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		edge_data ans = null;
		if(src!= dest) {
			node_data temp = nodes.get(src);
			node_data temp2 = nodes.get(dest);	
			if(temp != null&& temp2!= null) {
				ans=  edges.get(temp).remove(dest);
				if (ans != null){
					countMC++ ;
					countE--;
				}
				else {
					throw new RuntimeException("the edge does not exist");
				}
			}
			else {
				throw new RuntimeException("one of the nodes doesn't exist");
			}
		}
		else {
			throw new RuntimeException("same nodes");
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

	public void init(String g) {
		//JSONParser parser = new JSONParser();
		try {
			JSONObject jo = new JSONObject(g);
			JSONArray Jnodes = jo.getJSONArray("Nodes");
			JSONArray Jedges = jo.getJSONArray("Edges");

			for (int i = 0; i < Jnodes.length(); i++) {
				JSONObject nody= (JSONObject) Jnodes.get(i);
				String location = (String) nody.getString("pos");
				String[] points = location.split(",");
				double x = Double.parseDouble(points[0]);
				double y = Double.parseDouble(points[1]);	
				double z = Double.parseDouble(points[2]);
				int id = nody.getInt("id");
				Point3D p = new Point3D(x,y, z);
				nodeData n = new nodeData(id, 0, p);
				this.addNode(n);
			}
			for (int i = 0; i < Jedges.length(); i++) {
				JSONObject edgeE= (JSONObject) Jedges.get(i);
				int src = edgeE.getInt("src");
				int dest = edgeE.getInt("dest");
				double weight = edgeE.getDouble("w");
				this.connect(src, dest, weight);
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
			System.out.println("catch");
		} 
	}
}
