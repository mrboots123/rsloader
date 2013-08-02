package org.kenneh.loader.utils;
import java.awt.EventQueue;
import java.util.Calendar;

import javax.swing.JLabel;

public class Logger {
	
	private JLabel label;
	
	public Logger(JLabel label) {
		this.label = label;
	}

	public void debug(final String s) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				label.setText("[" + Calendar.getInstance().getTime() + "] " + s);
			}
			
		});
	}
	
}
