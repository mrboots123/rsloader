package org.kenneh.loader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.kenneh.loader.net.Parameters;
import org.kenneh.loader.net.RSApplet;
import org.kenneh.loader.utils.Logger;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private JLabel imageLabel;
	private JLabel textLabel;

	private Image loadImage;
	private Image logo;

	private Parameters params;
	private RSApplet rsapp;
	private Logger log;

	private boolean oldschool = false;

	public boolean chooseType() {
		Object[] options = {"RS3", "Oldschool"};
		int n = JOptionPane.showOptionDialog(this, "Would you like to load RS3 or Oldschool.",
				"Choose a game mode", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[1]);
		return n == 0 ? false : true;
	}

	public GUI() {
		try {
			logo = ImageIO.read(GUI.class.getResource("logo.png"));
			loadImage = Toolkit.getDefaultToolkit().createImage(GUI.class.getResource("loading.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		oldschool = chooseType();
		
		textLabel = new JLabel("Loading.. Please wait!");
		log = new Logger(textLabel);
		
		log.debug("Creating parameters instance");
		params = new Parameters(oldschool, log);
		log.debug("Creating RSApplet instance");
		rsapp = new RSApplet(params, log);
		createForm();
	}

	private void createForm() {
		setVisible(true);
		setIconImage(logo);
		setLayout(new BorderLayout());
		imageLabel = new JLabel(new ImageIcon(loadImage));
		getContentPane().add(imageLabel, BorderLayout.CENTER);
		getContentPane().setBackground(Color.BLACK);
		
		textLabel.setBackground(Color.BLACK);
		textLabel.setForeground(Color.LIGHT_GRAY);
		textLabel.setHorizontalTextPosition(JLabel.CENTER);
		textLabel.setFont(new Font("Calibri", Font.BOLD, 18));
		textLabel.setHorizontalAlignment(JLabel.CENTER);

		getContentPane().add(textLabel, BorderLayout.SOUTH);

		setSize(800, 600);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		log.debug("Downloading parameters");
		params.download();
		setTitle(params.get("title"));
		try {
			log.debug("Starting applet");
			rsapp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(oldschool) {
			log.debug("Oldschool chosen, setting size to 780x540");
			setSize(780, 540);
		} else {
			log.debug("RS3 chosen, setting size to 940x660");
			setSize(940, 660);
		}
		
		getContentPane().remove(imageLabel);
		getContentPane().remove(textLabel);
		log.debug("Adding applet to panel");
		getContentPane().add(rsapp, BorderLayout.CENTER);
		getContentPane().repaint();
	}

}
