package org.kenneh.loader;
import java.awt.EventQueue;

public class Loader {
	
	public static void main(String[] args) {
		final GUI gui = new GUI();
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				gui.setVisible(true);
			}
			
		});
	}
	
}