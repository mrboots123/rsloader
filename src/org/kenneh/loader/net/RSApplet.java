package org.kenneh.loader.net;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JPanel;

import org.kenneh.loader.utils.Logger;


@SuppressWarnings("serial")
public class RSApplet extends JPanel implements AppletStub {
	
	private Parameters params;
	
	private Applet applet;
	
	private Logger log;
	
	public RSApplet(Parameters params, Logger log) {
		this.params = params;
		this.log = log;
		this.setLayout(new BorderLayout());
	}
	
	@SuppressWarnings("resource")
	public void start() throws Exception {
		final String jar = params.get("codebase") + params.get("initial_jar");
		log.debug("Downloading jar from " + jar);
		final URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL( jar) });
		log.debug("Classloader created");
		final String mainClass = params.get("initial_class").replaceAll(".class", "");
		log.debug("Identified main class as " + mainClass);
		applet = (Applet) classLoader.loadClass(mainClass).newInstance();
		log.debug("Created applet instance");
		applet.setStub(this);
		applet.init();
		log.debug("Starting applet");
		applet.start();
		add(applet, BorderLayout.CENTER);
	}

	@Override
	public void appletResize(int arg0, int arg1) {

	}

	@Override
	public AppletContext getAppletContext() {
		return null;
	}

	public final String getParameter(String name) {
		return params.get(name);
	}

	public final URL getDocumentBase() {
		try {
			return new URL(params.get("codebase"));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public final URL getCodeBase() {
		try {
			return new URL(params.get("codebase"));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public boolean isActive() {
		return false;
	}

}
