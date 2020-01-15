package gui;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Server.game_service;

//a class for a Count down till the end of the game
public class Clock extends JFrame{
	JLabel text;

	public Clock(game_service game) {
		JFrame f = new JFrame("TIMER");
		f.setSize(220, 100);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		text = new JLabel();
		text.setFont(new Font("Timenewroman", Font.BOLD, 50));
		text.setText("00:" + game.timeToEnd()/1000);
		f.add(text);
		
		// a thred that update the time on the timer
		Thread timer;
		timer = new Thread(new Runnable() {
			@Override
			public void run() {
				while(game.isRunning()) {
					text.setText("00:" + game.timeToEnd()/1000);
					try{
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
