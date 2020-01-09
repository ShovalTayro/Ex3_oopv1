package dataStructure;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONObject;
import gui.graphListener;
import utils.Point3D;
import java.util.Hashtable;
import javax.swing.JFrame;

public class DGraph extends JFrame  implements graph{
	Hashtable <Integer, node_data> nodes = new Hashtable<Integer, node_data>();	
	Hashtable<node_data , Hashtable<Integer,edge_data>> edges = new Hashtable<node_data, Hashtable<Integer, edge_data>>();
	int countMC=0;
	int countE = 0;
	private graphListener listener;
	
	public void addListener(graphListener listener) {
		this.listener = listener;
	}
	
	public void updateListener() {
		if(listener!= null)
			listener.graphUpdate();
	}
	public node_data getNode(int key) {
		return nodes.get(key);

	}
	public DGraph() {
		nodes = new Hashtable<Integer, node_data>();
		edges = new Hashtable<node_data, Hashtable<Integer,edge_data>>();
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
			updateListener();
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
					updateListener();
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
		updateListener();
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
				updateListener();
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
