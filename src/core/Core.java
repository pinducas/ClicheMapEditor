package core;

import javax.swing.JFrame;

public class Core {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Map maker");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setContentPane(new Panel());
		frame.setTitle("PenisNo Pau map editor");
		frame.pack();
		
		
	}
	
}
