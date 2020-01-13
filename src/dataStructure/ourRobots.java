package dataStructure;
import org.json.JSONObject;

import utils.Point3D;

public class ourRobots {
	private int id;
	private Point3D pos;
	private double speed;
	private node_data node;
	private edge_data edge;
	private graph g;

	public ourRobots(){
		this.id = 0;
		this.pos = null;
		this.speed = 1;
		this.edge = null;
		this.node = null;
	}

	public ourRobots(int id,Point3D pos,double speed,node_data node,edge_data edge , graph g){
		this.id = id;
		this.pos = pos;
		this.speed = speed;
		this.edge = edge;
		this.node = node;
		this.g=g;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public Point3D getPos() {
		return pos;
	}

	public void setPos(Point3D pos) {
		this.pos = pos;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public node_data getNode() {
		return node;
	}

	public void setNode(node_data node) {
		this.node = node;
	}

	public edge_data getEdge() {
		return edge;
	}

	public void setEdge(edge_data edge) {
		this.edge = edge;
	}
	//init (?)

	public void setGraph(graph gr) {
		this.g= gr;
	}
	public void initRobots(String g)
	{
		if(!g.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(g);
				JSONObject robot = (JSONObject) obj.get("Robot");
				String pos = robot.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int id = robot.getInt("id");
				this.id = id;
				int speed = robot.getInt("speed");
				this.speed = speed;

				if(this.g != null)
				{
					int src = robot.getInt("src");
					this.node  = this.g.getNode(src);
				}

			}
			catch (Exception e) {
				System.out.println("fail to init robot");
			}
		}
	}
}
