package gui;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONObject;

import Server.game_service;

//a class for a Count down till the end of the game
public class Clock extends JFrame{
	JLabel text;

	public Clock(game_service game) {
		JFrame f = new JFrame("TIMER");
		f.setSize(400, 100);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		text = new JLabel();
		text.setFont(new Font("Timenewroman", Font.BOLD, 50));
		text.setText("00:" + game.timeToEnd()/1000);
		f.add(text);

		// a thread that update the time on the timer
		Thread timer;
		timer = new Thread(new Runnable() {
			@Override
			public void run() {
				while(game.isRunning()) {
					try {
						String res = game.toString();
						JSONObject object = new JSONObject(res);
						JSONObject cgame = (JSONObject) object.get("GameServer");
						int points = cgame.getInt("grade");
						text.setText("00:" + game.timeToEnd()/1000 +" ,Point: "+ points );
						Thread.sleep(1000);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		f.setVisible(true);
		timer.start();
	}
}
