package dataStructure;

import java.awt.Color;
import java.io.Serializable;

import dataStructure.edge_data;
import dataStructure.node_data;

public class edgeData implements edge_data , Serializable{
	node_data key;
	node_data src;
	node_data dest;
	double weight;
	Color tag;
	String info;

	public edgeData(node_data src, node_data dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.tag = Color.BLACK;
		this.key = src;
	}
	
	public edgeData(edgeData e) {
		this.src = e.src;
		this.dest = e.dest;
		this.weight = e.weight;
		this.tag = e.tag;
		this.key =e.key;
	}



	@Override
	public int getSrc() {
		return this.src.getKey();
	}

	@Override
	public int getDest() {
		return this.dest.getKey();
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info =s;

	}

	@Override
	public int getTag() {
		return this.tag.getRGB();
	}

	@Override
	public void setTag(int t) {
		this.tag = new Color(t);
	}

}
