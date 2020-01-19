package gameClient;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;
import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import utils.Point3D;


public class KML_Logger {
	graph gr;
	game_service game;
	Kml ourKml;
	Document ourDoc;

	public KML_Logger(graph g) {
		this.gr = g;
	}

	public void setGame(game_service game) {
		this.game = game;
	}

	public void kmlGraph() {
		ourKml = new Kml();
		ourDoc= ourKml.createAndSetDocument().withName("temp").withOpen(true);
		//create a folder
		Folder ourFolder = ourDoc.createAndAddFolder();
		ourFolder.withName("KML Check").withOpen(true);
		//create a icons
		Icon icon = new Icon();
		icon.withHref("http://maps.google.com/mapfiles/kml/shapes/rail.png");
		// set the stylename to use this style from the placemark
		Style ourStyle = ourDoc.createAndAddStyle();
		ourStyle.withId("placeIt").createAndSetIconStyle().withIcon(icon).withScale(1);

		//go through all nodes of the graph and set placemark , the name is the id of the node
		Collection<node_data> nodes = gr.getV();
		for (node_data n : nodes) {
			Placemark placemarkNode= ourDoc.createAndAddPlacemark();
			placemarkNode.withName(""+ n.getKey());
			placemarkNode.withStyleUrl("#placeIt");
			placemarkNode.createAndSetPoint().addToCoordinates(n.getLocation().x(), n.getLocation().y());
			//create style for edges
			Style edgeStyle= ourDoc.createAndAddStyle();
			edgeStyle.withId("edgeStyle").createAndSetLineStyle().withColor("ff43b3ff").setWidth(2.5);;
			Collection<edge_data> edges= gr.getE(n.getKey());
			for (edge_data e : edges) {
				Placemark placemarkEdge= ourDoc.createAndAddPlacemark();
				placemarkEdge.withStyleUrl("#edgeStyle");
				Point3D srcLoc= gr.getNode(e.getSrc()).getLocation();
				Point3D destLoc= gr.getNode(e.getDest()).getLocation();
				placemarkEdge.createAndSetLineString().withTessellate(true).addToCoordinates(srcLoc.x(), srcLoc.y()).addToCoordinates(destLoc.x(),destLoc.y());
			}
		}
	}

	// print and save
	public void saveToFile(String fileName) {
		try {
			ourKml.marshal(new File(fileName));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setFruit(String name){
		//icon for fruits
		Icon appleIcon = new Icon();
		appleIcon.withHref("http://maps.google.com/mapfiles/kml/shapes/snack_bar.png");
		Style appleStyle = ourDoc.createAndAddStyle();
		appleStyle.withId("Apple").createAndSetIconStyle().withIcon(appleIcon).withScale(1);
		Icon bananaIcon = new Icon();
		bananaIcon.withHref("http://maps.google.com/mapfiles/kml/shapes/coffee.png");
		Style bananaStyle = ourDoc.createAndAddStyle();
		bananaStyle.withId("Banana").createAndSetIconStyle().withIcon(bananaIcon).withScale(1);
		List<String> fruits = game.getFruits();
		//init fruit
		for (String fruitString : fruits) {
			try {
				JSONObject object = new JSONObject(fruitString);
				JSONObject fruit = (JSONObject) object.get("Fruit");
				int type = fruit.getInt("type");
				String pos = fruit.getString("pos");
				String[] point = pos.split(",");
				double x = Double.parseDouble(point[0]);
				double y = Double.parseDouble(point[1]);
				double z = Double.parseDouble(point[2]);
				Point3D p = new Point3D(x, y, z);

				Placemark placeFruit = ourDoc.createAndAddPlacemark();
				//matching each fruit to its own style
				if(type == 1) placeFruit.setStyleUrl("#Apple");
				else placeFruit.setStyleUrl("#Banana");

				placeFruit.createAndSetPoint().addToCoordinates(x, y);
				placeFruit.createAndSetTimeStamp().withWhen(name);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setRobot(String name) {
		//icon for robot
		Icon robotIcon = new Icon();
		robotIcon.withHref("http://maps.google.com/mapfiles/kml/shapes/man.png");
		Style robotStyle = ourDoc.createAndAddStyle();
		robotStyle.withId("Robot").createAndSetIconStyle().withIcon(robotIcon).withScale(1);
		List<String> robots = game.getRobots();
		//init robot
		for (String robotString : robots) {
			try {
				JSONObject object = new JSONObject(robotString);
				JSONObject robot = (JSONObject) object.get("Robot");
				int id = robot.getInt("id");
				String pos = robot.getString("pos");
				String[] point = pos.split(",");
				double x = Double.parseDouble(point[0]);
				double y = Double.parseDouble(point[1]);
				double z = Double.parseDouble(point[2]);
				Point3D p = new Point3D(x, y, z);

				Placemark placeRobot = ourDoc.createAndAddPlacemark();
				placeRobot.setStyleUrl("#Robot");
				placeRobot.createAndSetPoint().addToCoordinates(x, y);
				placeRobot.createAndSetTimeStamp().withWhen(name);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}