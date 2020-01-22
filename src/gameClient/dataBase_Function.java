package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class dataBase_Function {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	static int[][] grade_move = {{145,450,720,570,510,1050,310,235,250,200,0,1000},
			{290,580,580,500,580,580,580,290,580,290,0,1140}};
	/**
	 * this method return the number of games you played and your current stage
	 * @param id id of the player
	 * @return
	 */
	public static int[] numberOfGames(int id){
		int count = 0;
		int stage = 0;
		int numOfGame_stage[] = new int[2];
		ResultSet result=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID="+id+";";
			result = statement.executeQuery(allCustomersQuery);
			while(result.next()){
				int IDLevel = result.getInt("levelID");
				if(IDLevel > stage) stage = IDLevel;
				count++;
			}
			result.close();
			statement.close();		
			connection.close();
		}

		catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		numOfGame_stage[0] = count;
		numOfGame_stage[1] = stage;
		return numOfGame_stage;
	}
	/**
	 * this method returns the player best moves , grades and his rank against other players
	 * @param id
	 * @return
	 */
	public static int[][] bestCase(int id){
		ResultSet result=null;
		int[][] ans = new int[3][12];
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID="+id+";";
			result = statement.executeQuery(allCustomersQuery);
			while(result.next()){
				int IDLevel = result.getInt("levelID");
				int move = result.getInt("moves");
				int stageNum = findStage(IDLevel);
				if(stageNum != 10) {
					if(move<= grade_move[1][stageNum]){
						int points = result.getInt("score");
						if(points > ans[0][stageNum]){
							ans[0][stageNum] = points;
							ans[1][stageNum] = move;
						}
					}
				}
			}
			for (int i = 0; i < 12; i++) {
				if(i != 10) {
					int level = findCase(i);
					String pos = "SELECT * FROM oop.Logs where levelID = "+level+" and score > "+ans[0][i]+" and moves <= "+grade_move[1][i]+";";
					ResultSet resForPos= statement.executeQuery(pos);
					List<Integer> saveID = new ArrayList<Integer>();
					while (resForPos.next()) {
						int tempID= resForPos.getInt("UserID");
						if(saveID.indexOf(tempID) == -1) {
							saveID.add(tempID);
						}
					}
					if(saveID.isEmpty()) ans[2][i] = 1;
					else{
						ans[2][i] = saveID.size();
					}
				}
			}
			result.close();
			statement.close();		
			connection.close();
		}

		catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
/**
 * connect between the stage number to the scenario number
 * @param num
 * @return
 */
	public static int findStage(int num) {
		switch(num) {
		case 0:
			return 0;
		case 1:
			return 1;
		case 3:
			return 2;
		case 5:
			return 3;
		case 9:
			return 4;
		case 11:
			return 5;
		case 13:
			return 6;
		case 16:
			return 7;
		case 19:
			return 8;
		case 20: 
			return 9;
		case 23:
			return 11;
		default:
			return 10;
		}
	}
	/**
	 * connect between the scenario number to the stage case
	 * @param num
	 * @return
	 */
	public static int findCase(int num) {
		switch(num) {
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
			return 3;
		case 3:
			return 5;
		case 4:
			return 9;
		case 5:
			return 11;
		case 6:
			return 13;
		case 7:
			return 16;
		case 8:
			return 19;
		case 9: 
			return 20;
		case 11:
			return 23;
		default:
			return 10;
		}
	}

}